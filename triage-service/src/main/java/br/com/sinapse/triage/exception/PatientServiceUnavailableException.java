package br.com.sinapse.triage.exception;

public class PatientServiceUnavailableException extends RuntimeException {

    public PatientServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
