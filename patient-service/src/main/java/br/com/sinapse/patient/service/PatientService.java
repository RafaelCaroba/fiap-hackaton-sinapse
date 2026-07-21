package br.com.sinapse.patient.service;

import br.com.sinapse.patient.domain.Patient;
import br.com.sinapse.patient.dto.CreatePatientRequest;
import br.com.sinapse.patient.dto.PatientResponse;
import br.com.sinapse.patient.exception.PatientAlreadyExistsException;
import br.com.sinapse.patient.exception.PatientNotFoundException;
import br.com.sinapse.patient.repository.PatientRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Transactional
    public PatientResponse create(CreatePatientRequest request) {
        if (patientRepository.existsByCpf(request.cpf())) {
            throw new PatientAlreadyExistsException(request.cpf());
        }

        Patient patient = patientMapper.toEntity(request);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toResponse(savedPatient);
    }

    @Transactional(readOnly = true)
    public PatientResponse findById(UUID id) {
        Patient patient = patientRepository.findById(id)
            .orElseThrow(() -> new PatientNotFoundException(id));
        return patientMapper.toResponse(patient);
    }

    @Transactional(readOnly = true)
    public PatientResponse findByCpf(String cpf) {
        Patient patient = patientRepository.findByCpf(cpf)
            .orElseThrow(() -> new PatientNotFoundException(cpf));
        return patientMapper.toResponse(patient);
    }
}
