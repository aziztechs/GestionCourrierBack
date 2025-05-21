package sn.coud.gestioncourrierback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sn.coud.gestioncourrierback.dto.SuiviDTO;
import sn.coud.gestioncourrierback.service.SuiviService;

import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing suivis.
 */
@RestController
@RequestMapping("/api/suivis")
@RequiredArgsConstructor
@Tag(name = "Suivi", description = "API for managing suivis")
@CrossOrigin(origins = "*")
public class SuiviController {
    
    private final SuiviService suiviService;
    
    @PostMapping
    @Operation(summary = "Create a new suivi")
    public ResponseEntity<SuiviDTO> createSuivi(@Valid @RequestBody SuiviDTO suiviDTO) {
        SuiviDTO createdSuivi = suiviService.createSuivi(suiviDTO);
        return new ResponseEntity<>(createdSuivi, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing suivi")
    public ResponseEntity<SuiviDTO> updateSuivi(@PathVariable Long id, @Valid @RequestBody SuiviDTO suiviDTO) {
        SuiviDTO updatedSuivi = suiviService.updateSuivi(id, suiviDTO);
        return ResponseEntity.ok(updatedSuivi);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get a suivi by id")
    public ResponseEntity<SuiviDTO> getSuiviById(@PathVariable Long id) {
        SuiviDTO suivi = suiviService.getSuiviById(id);
        return ResponseEntity.ok(suivi);
    }
    
    @GetMapping
    @Operation(summary = "Get all suivis")
    public ResponseEntity<List<SuiviDTO>> getAllSuivis() {
        List<SuiviDTO> suivis = suiviService.getAllSuivis();
        return ResponseEntity.ok(suivis);
    }
    
    @GetMapping("/courrier/{courrierId}")
    @Operation(summary = "Get all suivis for a courrier")
    public ResponseEntity<List<SuiviDTO>> getSuivisByCourrierId(@PathVariable Long courrierId) {
        List<SuiviDTO> suivis = suiviService.getSuivisByCourrierId(courrierId);
        return ResponseEntity.ok(suivis);
    }
    
    @GetMapping("/date/{date}")
    @Operation(summary = "Get all suivis by date")
    public ResponseEntity<List<SuiviDTO>> getSuivisByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<SuiviDTO> suivis = suiviService.getSuivisByDate(date);
        return ResponseEntity.ok(suivis);
    }
    
    @GetMapping("/date-between")
    @Operation(summary = "Get all suivis between two dates")
    public ResponseEntity<List<SuiviDTO>> getSuivisBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<SuiviDTO> suivis = suiviService.getSuivisBetweenDates(startDate, endDate);
        return ResponseEntity.ok(suivis);
    }
    
    @GetMapping("/instruction/{instruction}")
    @Operation(summary = "Get all suivis containing the given instruction")
    public ResponseEntity<List<SuiviDTO>> getSuivisByInstructionContaining(@PathVariable String instruction) {
        List<SuiviDTO> suivis = suiviService.getSuivisByInstructionContaining(instruction);
        return ResponseEntity.ok(suivis);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a suivi")
    public ResponseEntity<Void> deleteSuivi(@PathVariable Long id) {
        suiviService.deleteSuivi(id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/courrier/{courrierId}")
    @Operation(summary = "Delete all suivis for a courrier")
    public ResponseEntity<Void> deleteSuivisByCourrierId(@PathVariable Long courrierId) {
        suiviService.deleteSuivisByCourrierId(courrierId);
        return ResponseEntity.noContent().build();
    }
}