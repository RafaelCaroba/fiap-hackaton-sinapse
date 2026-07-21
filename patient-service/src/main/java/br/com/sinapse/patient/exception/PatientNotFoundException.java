package br.com.sinapse.patient.exception;

import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(UUID id) {
        super("Patient not found with id: " + id);
    }

    public PatientNotFoundException(String cpf) {
        super("Patient not found with cpf: " + cpf);
    }
}
