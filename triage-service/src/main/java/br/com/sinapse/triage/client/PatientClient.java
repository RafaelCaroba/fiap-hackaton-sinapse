package br.com.sinapse.triage.client;

import br.com.sinapse.triage.dto.integration.PatientIntegrationResponse;
import br.com.sinapse.triage.exception.PatientNotFoundException;
import br.com.sinapse.triage.exception.PatientServiceUnavailableException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class PatientClient {

    private final RestClient patientRestClient;

    public PatientClient(RestClient patientRestClient) {
        this.patientRestClient = patientRestClient;
    }

    public PatientIntegrationResponse findByCpf(String cpf) {
        try {
            return patientRestClient.get()
                .uri("/patients/cpf/{cpf}", cpf)
                .retrieve()
                .body(PatientIntegrationResponse.class);

        } catch (HttpClientErrorException.NotFound ex) {
            throw new PatientNotFoundException(cpf);

        } catch (ResourceAccessException ex) {
            throw new PatientServiceUnavailableException(
                "Patient Service is unreachable while looking up cpf: " + cpf, ex);

        } catch (HttpClientErrorException | org.springframework.web.client.HttpServerErrorException ex) {
            throw new PatientServiceUnavailableException(
                "Patient Service returned an error (%s) while looking up cpf: %s"
                    .formatted(ex.getStatusCode(), cpf), ex);

        } catch (RestClientException ex) {
            throw new PatientServiceUnavailableException(
                "Unexpected error while communicating with Patient Service for cpf: " + cpf, ex);
        }
    }
}
