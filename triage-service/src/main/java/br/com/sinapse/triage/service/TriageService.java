package br.com.sinapse.triage.service;

import br.com.sinapse.triage.client.PatientClient;
import br.com.sinapse.triage.dto.integration.PatientIntegrationResponse;
import br.com.sinapse.triage.dto.request.CreateTriageRequest;
import br.com.sinapse.triage.dto.response.TriageResponse;
import br.com.sinapse.triage.entity.Triage;
import br.com.sinapse.triage.enums.Priority;
import br.com.sinapse.triage.mapper.TriageMapper;
import br.com.sinapse.triage.repository.TriageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TriageService {

    private final PatientClient patientClient;
    private final PriorityEngine priorityEngine;
    private final TriageRepository triageRepository;
    private final TriageMapper triageMapper;

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
        return triageMapper.toResponse(savedTriage);
    }
}
