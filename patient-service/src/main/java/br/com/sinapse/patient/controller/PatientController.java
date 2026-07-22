package br.com.sinapse.patient.controller;

import br.com.sinapse.patient.dto.CreatePatientRequest;
import br.com.sinapse.patient.dto.ErrorResponse;
import br.com.sinapse.patient.dto.PatientResponse;
import br.com.sinapse.patient.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import java.net.URI;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Patients", description = "Patient registration and lookup")
@Validated
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @Operation(summary = "Register a new patient")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Patient created successfully",
            content = @Content(schema = @Schema(implementation = PatientResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "CPF already registered",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PatientResponse> create(@Valid @RequestBody CreatePatientRequest request) {
        PatientResponse response = patientService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Find patient by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient found",
            content = @Content(schema = @Schema(implementation = PatientResponse.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> findById(
            @Parameter(description = "Patient UUID") @PathVariable UUID id) {
        return ResponseEntity.ok(patientService.findById(id));
    }

    @Operation(summary = "Find patient by CPF")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Patient found",
            content = @Content(schema = @Schema(implementation = PatientResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid CPF format",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<PatientResponse> findByCpf(
            @Parameter(description = "11-digit CPF (numbers only)")
            @Pattern(regexp = "\\d{11}", message = "CPF must contain exactly 11 digits")
            @PathVariable String cpf) {
        return ResponseEntity.ok(patientService.findByCpf(cpf));
    }
}
