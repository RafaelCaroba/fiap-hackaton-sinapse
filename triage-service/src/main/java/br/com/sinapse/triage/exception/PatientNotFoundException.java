package br.com.sinapse.triage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends RuntimeException {

    public PatientNotFoundException(String cpf) {
        super("Patient not found with cpf: " + cpf);
    }
}
