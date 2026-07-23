package br.com.sinapse.triage.mapper;

import br.com.sinapse.triage.dto.VitalSigns;
import br.com.sinapse.triage.dto.request.CreateTriageRequest;
import br.com.sinapse.triage.dto.response.TriageResponse;
import br.com.sinapse.triage.entity.Triage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TriageMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "patientId", ignore = true)
    @Mapping(target = "priority", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "systolicPressure", source = "vitalSigns.systolicPressure")
    @Mapping(target = "diastolicPressure", source = "vitalSigns.diastolicPressure")
    @Mapping(target = "heartRate", source = "vitalSigns.heartRate")
    @Mapping(target = "respiratoryRate", source = "vitalSigns.respiratoryRate")
    @Mapping(target = "temperature", source = "vitalSigns.temperature")
    @Mapping(target = "oxygenSaturation", source = "vitalSigns.oxygenSaturation")
    Triage toEntity(CreateTriageRequest request);

    @Mapping(target = "vitalSigns", source = ".")
    TriageResponse toResponse(Triage triage);

    default VitalSigns toVitalSigns(Triage triage) {
        return new VitalSigns(
            triage.getSystolicPressure(),
            triage.getDiastolicPressure(),
            triage.getHeartRate(),
            triage.getRespiratoryRate(),
            triage.getTemperature(),
            triage.getOxygenSaturation()
        );
    }
}
