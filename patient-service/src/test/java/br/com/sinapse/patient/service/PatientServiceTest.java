package br.com.sinapse.patient.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.sinapse.patient.domain.BloodType;
import br.com.sinapse.patient.domain.Gender;
import br.com.sinapse.patient.domain.Patient;
import br.com.sinapse.patient.dto.CreatePatientRequest;
import br.com.sinapse.patient.dto.PatientResponse;
import br.com.sinapse.patient.exception.PatientAlreadyExistsException;
import br.com.sinapse.patient.exception.PatientNotFoundException;
import br.com.sinapse.patient.repository.PatientRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientService patientService;

    private static final UUID ID = UUID.randomUUID();
    private static final String CPF = "12345678901";

    // ── helpers ──────────────────────────────────────────────────────────────

    private CreatePatientRequest buildRequest() {
        return new CreatePatientRequest(CPF, "John Doe", LocalDate.of(1990, 1, 1), Gender.MALE, BloodType.O_POSITIVE);
    }

    private Patient buildPatient() {
        return Patient.builder()
            .id(ID)
            .cpf(CPF)
            .fullName("John Doe")
            .birthDate(LocalDate.of(1990, 1, 1))
            .gender(Gender.MALE)
            .bloodType(BloodType.O_POSITIVE)
            .createdAt(LocalDateTime.now())
            .build();
    }

    private PatientResponse buildResponse(Patient patient) {
        return new PatientResponse(
            patient.getId(),
            patient.getCpf(),
            patient.getFullName(),
            patient.getBirthDate(),
            patient.getGender(),
            patient.getBloodType(),
            patient.getCreatedAt()
        );
    }

    // ── create ───────────────────────────────────────────────────────────────

    @Test
    void create_shouldPersistAndReturnResponse_whenCpfIsNew() {
        CreatePatientRequest request = buildRequest();
        Patient patient = buildPatient();
        PatientResponse expected = buildResponse(patient);

        when(patientRepository.existsByCpf(CPF)).thenReturn(false);
        when(patientMapper.toEntity(request)).thenReturn(patient);
        when(patientRepository.save(patient)).thenReturn(patient);
        when(patientMapper.toResponse(patient)).thenReturn(expected);

        PatientResponse actual = patientService.create(request);

        assertThat(actual).isEqualTo(expected);
        verify(patientRepository).existsByCpf(CPF);
        verify(patientMapper).toEntity(request);
        verify(patientRepository).save(patient);
        verify(patientMapper).toResponse(patient);
    }

    @Test
    void create_shouldThrowPatientAlreadyExistsException_whenCpfAlreadyExists() {
        CreatePatientRequest request = buildRequest();

        when(patientRepository.existsByCpf(CPF)).thenReturn(true);

        assertThatThrownBy(() -> patientService.create(request))
            .isInstanceOf(PatientAlreadyExistsException.class)
            .hasMessageContaining(CPF);

        verify(patientRepository).existsByCpf(CPF);
        verify(patientRepository, never()).save(any());
        verify(patientMapper, never()).toEntity(any());
    }

    // ── findById ─────────────────────────────────────────────────────────────

    @Test
    void findById_shouldReturnResponse_whenPatientExists() {
        Patient patient = buildPatient();
        PatientResponse expected = buildResponse(patient);

        when(patientRepository.findById(ID)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(expected);

        PatientResponse actual = patientService.findById(ID);

        assertThat(actual).isEqualTo(expected);
        verify(patientRepository).findById(ID);
        verify(patientMapper).toResponse(patient);
    }

    @Test
    void findById_shouldThrowPatientNotFoundException_whenPatientDoesNotExist() {
        when(patientRepository.findById(ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findById(ID))
            .isInstanceOf(PatientNotFoundException.class)
            .hasMessageContaining(ID.toString());

        verify(patientRepository).findById(ID);
        verify(patientMapper, never()).toResponse(any());
    }

    // ── findByCpf ────────────────────────────────────────────────────────────

    @Test
    void findByCpf_shouldReturnResponse_whenPatientExists() {
        Patient patient = buildPatient();
        PatientResponse expected = buildResponse(patient);

        when(patientRepository.findByCpf(CPF)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(expected);

        PatientResponse actual = patientService.findByCpf(CPF);

        assertThat(actual).isEqualTo(expected);
        verify(patientRepository).findByCpf(CPF);
        verify(patientMapper).toResponse(patient);
    }

    @Test
    void findByCpf_shouldThrowPatientNotFoundException_whenPatientDoesNotExist() {
        when(patientRepository.findByCpf(CPF)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patientService.findByCpf(CPF))
            .isInstanceOf(PatientNotFoundException.class)
            .hasMessageContaining(CPF);

        verify(patientRepository).findByCpf(CPF);
        verify(patientMapper, never()).toResponse(any());
    }
}
