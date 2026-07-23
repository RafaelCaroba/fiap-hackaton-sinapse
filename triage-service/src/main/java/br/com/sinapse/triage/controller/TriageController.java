package br.com.sinapse.triage.controller;

import br.com.sinapse.triage.dto.ErrorResponse;
import br.com.sinapse.triage.dto.request.CreateTriageRequest;
import br.com.sinapse.triage.dto.response.TriageResponse;
import br.com.sinapse.triage.service.TriageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Tag(name = "Triages", description = "Triage creation and risk classification")
@RestController
@RequestMapping("/triages")
@RequiredArgsConstructor
public class TriageController {

    private final TriageService triageService;

    @Operation(summary = "Create a triage")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Triage created successfully",
            content = @Content(schema = @Schema(implementation = TriageResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request body",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Patient not found",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "503", description = "Patient service unavailable",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<TriageResponse> create(@Valid @RequestBody CreateTriageRequest request) {
        TriageResponse response = triageService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(response.id())
            .toUri();
        return ResponseEntity.created(location).body(response);
    }
}
