package br.com.sinapse.triage.controller;

import br.com.sinapse.triage.client.PatientClient;
import br.com.sinapse.triage.dto.integration.PatientIntegrationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// TODO: REMOVE this temporary controller once /triages (Phase 2.2+) exercises PatientClient in the real flow.
@RestController
@RequestMapping("/internal/test")
@RequiredArgsConstructor
public class PatientClientTestController {

    private final PatientClient patientClient;

    @GetMapping("/patients/cpf/{cpf}")
    public ResponseEntity<PatientIntegrationResponse> testFindByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(patientClient.findByCpf(cpf));
    }
}
