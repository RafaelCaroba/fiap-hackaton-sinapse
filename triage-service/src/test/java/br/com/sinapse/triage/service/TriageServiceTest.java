package br.com.sinapse.triage.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sinapse.triage.client.PatientClient;
import br.com.sinapse.triage.dto.VitalSigns;
import br.com.sinapse.triage.dto.integration.PatientIntegrationResponse;
import br.com.sinapse.triage.dto.request.CreateTriageRequest;
import br.com.sinapse.triage.dto.response.TriageResponse;
import br.com.sinapse.triage.entity.Triage;
import br.com.sinapse.triage.enums.Priority;
import br.com.sinapse.triage.exception.PatientNotFoundException;
import br.com.sinapse.triage.exception.PatientServiceUnavailableException;
import br.com.sinapse.triage.mapper.TriageMapper;
import br.com.sinapse.triage.repository.TriageRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TriageServiceTest {

    @Mock
    private PatientClient patientClient;

    @Mock
    private PriorityEngine priorityEngine;

    @Mock
    private TriageRepository triageRepository;

    @Mock
    private TriageMapper triageMapper;

    @InjectMocks
    private TriageService triageService;

    @Test
    void create_shouldPersistAndReturnResponse_withCalculatedPriority() {
        CreateTriageRequest request = request();
        UUID patientId = UUID.randomUUID();
        Priority priority = Priority.URGENT;
        PatientIntegrationResponse patient = patient(patientId);
        Triage triage = triage();
        Triage saved = triage();
        saved.setId(UUID.randomUUID());
        saved.setPatientId(patientId);
        saved.setPriority(priority);
        saved.setCreatedAt(LocalDateTime.now());
        TriageResponse expected = response(saved);

        when(patientClient.findByCpf(request.cpf())).thenReturn(patient);
        when(priorityEngine.calculate(request)).thenReturn(priority);
        when(triageMapper.toEntity(request)).thenReturn(triage);
        when(triageRepository.saveAndFlush(triage)).thenReturn(saved);
        when(triageMapper.toResponse(saved)).thenReturn(expected);

        TriageResponse actual = triageService.create(request);

        assertThat(actual).isEqualTo(expected);
        assertThat(triage.getPatientId()).isEqualTo(patientId);
        assertThat(triage.getPriority()).isEqualTo(priority);
        verify(patientClient).findByCpf(request.cpf());
        verify(priorityEngine).calculate(request);
        verify(triageMapper).toEntity(request);
        verify(triageRepository).saveAndFlush(triage);
        verify(triageMapper).toResponse(saved);
    }

    @Test
    void create_shouldPropagatePatientNotFoundException() {
        CreateTriageRequest request = request();

        when(patientClient.findByCpf(request.cpf())).thenThrow(new PatientNotFoundException(request.cpf()));

        assertThatThrownBy(() -> triageService.create(request))
            .isInstanceOf(PatientNotFoundException.class);

        verify(patientClient).findByCpf(request.cpf());
        verify(priorityEngine, never()).calculate(any());
        verify(triageRepository, never()).save(any());
    }

    @Test
    void create_shouldPropagatePatientServiceUnavailableException() {
        CreateTriageRequest request = request();

        when(patientClient.findByCpf(request.cpf())).thenThrow(
            new PatientServiceUnavailableException("unavailable", new RuntimeException("boom")));

        assertThatThrownBy(() -> triageService.create(request))
            .isInstanceOf(PatientServiceUnavailableException.class);

        verify(patientClient).findByCpf(request.cpf());
        verify(priorityEngine, never()).calculate(any());
        verify(triageRepository, never()).save(any());
    }

    private CreateTriageRequest request() {
        return new CreateTriageRequest(
            "12345678901",
            List.of("dor abdominal"),
            new VitalSigns(120, 80, 95, 18, 37.2, 97)
        );
    }

    private PatientIntegrationResponse patient(UUID patientId) {
        return new PatientIntegrationResponse(
            patientId,
            "12345678901",
            "Maria Teste",
            LocalDate.of(1990, 5, 10),
            "FEMALE",
            "O_POSITIVE",
            LocalDateTime.now()
        );
    }

    private Triage triage() {
        return Triage.builder()
            .cpf("12345678901")
            .symptoms(List.of("dor abdominal"))
            .systolicPressure(120)
            .diastolicPressure(80)
            .heartRate(95)
            .respiratoryRate(18)
            .temperature(37.2)
            .oxygenSaturation(97)
            .build();
    }

    private TriageResponse response(Triage triage) {
        return new TriageResponse(
            triage.getId(),
            triage.getPatientId(),
            triage.getCpf(),
            triage.getSymptoms(),
            new VitalSigns(
                triage.getSystolicPressure(),
                triage.getDiastolicPressure(),
                triage.getHeartRate(),
                triage.getRespiratoryRate(),
                triage.getTemperature(),
                triage.getOxygenSaturation()
            ),
            triage.getPriority(),
            triage.getCreatedAt()
        );
    }
}
