package br.com.sinapse.patient.service;

import br.com.sinapse.patient.domain.BloodType;
import br.com.sinapse.patient.domain.Gender;
import br.com.sinapse.patient.domain.Patient;
import br.com.sinapse.patient.dto.CreatePatientRequest;
import br.com.sinapse.patient.dto.PatientResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-07-21T02:25:13-0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 25 (Oracle Corporation)"
)
@Component
public class PatientMapperImpl implements PatientMapper {

    @Override
    public PatientResponse toResponse(Patient patient) {
        if ( patient == null ) {
            return null;
        }

        UUID id = null;
        String cpf = null;
        String fullName = null;
        LocalDate birthDate = null;
        Gender gender = null;
        BloodType bloodType = null;
        LocalDateTime createdAt = null;

        id = patient.getId();
        cpf = patient.getCpf();
        fullName = patient.getFullName();
        birthDate = patient.getBirthDate();
        gender = patient.getGender();
        bloodType = patient.getBloodType();
        createdAt = patient.getCreatedAt();

        PatientResponse patientResponse = new PatientResponse( id, cpf, fullName, birthDate, gender, bloodType, createdAt );

        return patientResponse;
    }

    @Override
    public Patient toEntity(CreatePatientRequest request) {
        if ( request == null ) {
            return null;
        }

        Patient.PatientBuilder patient = Patient.builder();

        patient.cpf( request.cpf() );
        patient.fullName( request.fullName() );
        patient.birthDate( request.birthDate() );
        patient.gender( request.gender() );
        patient.bloodType( request.bloodType() );

        return patient.build();
    }
}
