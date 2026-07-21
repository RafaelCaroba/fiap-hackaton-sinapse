package br.com.sinapse.patient.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PatientAlreadyExistsException extends RuntimeException {

    public PatientAlreadyExistsException(String cpf) {
        super("Patient already exists with cpf: " + cpf);
    }
}
