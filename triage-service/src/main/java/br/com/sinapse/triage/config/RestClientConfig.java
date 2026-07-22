package br.com.sinapse.triage.config;

import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient patientRestClient(
            @Value("${patient-service.url}") String patientServiceUrl,
            @Value("${patient-service.connect-timeout-ms:3000}") long connectTimeoutMs,
            @Value("${patient-service.read-timeout-ms:5000}") long readTimeoutMs) {

        ClientHttpRequestFactorySettings settings = ClientHttpRequestFactorySettings.defaults()
            .withConnectTimeout(Duration.ofMillis(connectTimeoutMs))
            .withReadTimeout(Duration.ofMillis(readTimeoutMs));

        ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder.detect().build(settings);

        return RestClient.builder()
            .baseUrl(patientServiceUrl)
            .requestFactory(requestFactory)
            .build();
    }
}
