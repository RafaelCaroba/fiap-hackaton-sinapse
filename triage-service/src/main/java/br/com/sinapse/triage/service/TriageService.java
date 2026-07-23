package br.com.sinapse.triage.service;

import br.com.sinapse.triage.client.PatientClient;
import br.com.sinapse.triage.dto.integration.PatientIntegrationResponse;
import br.com.sinapse.triage.dto.request.CreateTriageRequest;
import br.com.sinapse.triage.dto.response.TriageResponse;
import br.com.sinapse.triage.entity.Triage;
import br.com.sinapse.triage.enums.Priority;
import br.com.sinapse.triage.event.TriageCompletedEvent;
import br.com.sinapse.triage.mapper.TriageMapper;
import br.com.sinapse.triage.repository.TriageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@RequiredArgsConstructor
public class TriageService {

    private final PatientClient patientClient;
    private final PriorityEngine priorityEngine;
    private final TriageRepository triageRepository;
    private final TriageMapper triageMapper;
    private final TriageEventPublisher triageEventPublisher;

    @Transactional
    public TriageResponse create(CreateTriageRequest request) {
        PatientIntegrationResponse patient = patientClient.findByCpf(request.cpf());
        Priority priority = priorityEngine.calculate(request);

        Triage triage = triageMapper.toEntity(request);
        triage.setPatientId(patient.id());
        triage.setPriority(priority);

        Triage savedTriage = triageRepository.saveAndFlush(triage);
        if (savedTriage.getCreatedAt() == null) {
            savedTriage = triageRepository.findById(savedTriage.getId()).orElse(savedTriage);
        }

        TriageCompletedEvent event = new TriageCompletedEvent(
            savedTriage.getId(),
            savedTriage.getPatientId(),
            savedTriage.getCpf(),
            savedTriage.getPriority(),
            savedTriage.getCreatedAt()
        );
        publishAfterCommit(event);

        return triageMapper.toResponse(savedTriage);
    }

    private void publishAfterCommit(TriageCompletedEvent event) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    triageEventPublisher.publish(event);
                }
            });
            return;
        }
        triageEventPublisher.publish(event);
    }
}
