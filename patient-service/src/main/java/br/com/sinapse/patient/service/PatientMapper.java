package br.com.sinapse.patient.service;

import br.com.sinapse.patient.domain.Patient;
import br.com.sinapse.patient.dto.CreatePatientRequest;
import br.com.sinapse.patient.dto.PatientResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    PatientResponse toResponse(Patient patient);

    @Mapping(target = "id", ignore = true)
    Patient toEntity(CreatePatientRequest request);
}
