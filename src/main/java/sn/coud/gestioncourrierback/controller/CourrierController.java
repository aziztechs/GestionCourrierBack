package sn.coud.gestioncourrierback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sn.coud.gestioncourrierback.dto.CourrierDTO;
import sn.coud.gestioncourrierback.model.Courrier.TypeCourrier;
import sn.coud.gestioncourrierback.model.Courrier.NatureCourrier;
import sn.coud.gestioncourrierback.service.CourrierService;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * REST controller for managing courriers.
 */
@RestController
@RequestMapping("/api/courriers")
@RequiredArgsConstructor
@Tag(name = "Courrier", description = "API for managing courriers")
@CrossOrigin(origins = "*")
public class CourrierController {
    
    private final CourrierService courrierService;
    
    @PostMapping
    @Operation(summary = "Create a new courrier")
    public ResponseEntity<CourrierDTO> createCourrier(@Valid @RequestBody CourrierDTO courrierDTO) {
        CourrierDTO createdCourrier = courrierService.createCourrier(courrierDTO);
        return new ResponseEntity<>(createdCourrier, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update an existing courrier")
    public ResponseEntity<CourrierDTO> updateCourrier(@PathVariable Long id, @Valid @RequestBody CourrierDTO courrierDTO) {
        CourrierDTO updatedCourrier = courrierService.updateCourrier(id, courrierDTO);
        return ResponseEntity.ok(updatedCourrier);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get a courrier by id")
    public ResponseEntity<CourrierDTO> getCourrierById(@PathVariable Long id) {
        CourrierDTO courrier = courrierService.getCourrierById(id);
        return ResponseEntity.ok(courrier);
    }
    
    @GetMapping("/numero/{numCourrier}")
    @Operation(summary = "Get a courrier by number")
    public ResponseEntity<CourrierDTO> getCourrierByNumCourrier(@PathVariable String numCourrier) {
        CourrierDTO courrier = courrierService.getCourrierByNumCourrier(numCourrier);
        return ResponseEntity.ok(courrier);
    }
    
    @GetMapping
    @Operation(summary = "Get all courriers")
    public ResponseEntity<List<CourrierDTO>> getAllCourriers() {
        List<CourrierDTO> courriers = courrierService.getAllCourriers();
        return ResponseEntity.ok(courriers);
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Get all courriers by type")
    public ResponseEntity<List<CourrierDTO>> getCourriersByType(@PathVariable TypeCourrier type) {
        List<CourrierDTO> courriers = courrierService.getCourriersByType(type);
        return ResponseEntity.ok(courriers);
    }
    
    @GetMapping("/nature/{nature}")
    @Operation(summary = "Get all courriers by nature")
    public ResponseEntity<List<CourrierDTO>> getCourriersByNature(@PathVariable NatureCourrier nature) {
        List<CourrierDTO> courriers = courrierService.getCourriersByNature(nature);
        return ResponseEntity.ok(courriers);
    }
    
    @GetMapping("/date/{date}")
    @Operation(summary = "Get all courriers by date")
    public ResponseEntity<List<CourrierDTO>> getCourriersByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<CourrierDTO> courriers = courrierService.getCourriersByDate(date);
        return ResponseEntity.ok(courriers);
    }
    
    @GetMapping("/date-between")
    @Operation(summary = "Get all courriers between two dates")
    public ResponseEntity<List<CourrierDTO>> getCourriersBetweenDates(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<CourrierDTO> courriers = courrierService.getCourriersBetweenDates(startDate, endDate);
        return ResponseEntity.ok(courriers);
    }
    
    @GetMapping("/destinataire/{destinataire}")
    @Operation(summary = "Get all courriers by destinataire")
    public ResponseEntity<List<CourrierDTO>> getCourriersByDestinataire(@PathVariable String destinataire) {
        List<CourrierDTO> courriers = courrierService.getCourriersByDestinataire(destinataire);
        return ResponseEntity.ok(courriers);
    }
    
    @GetMapping("/expediteur/{expediteur}")
    @Operation(summary = "Get all courriers by expediteur")
    public ResponseEntity<List<CourrierDTO>> getCourriersByExpediteur(@PathVariable String expediteur) {
        List<CourrierDTO> courriers = courrierService.getCourriersByExpediteur(expediteur);
        return ResponseEntity.ok(courriers);
    }
    
    @GetMapping("/objet/{objet}")
    @Operation(summary = "Get all courriers containing the given objet")
    public ResponseEntity<List<CourrierDTO>> getCourriersByObjetContaining(@PathVariable String objet) {
        List<CourrierDTO> courriers = courrierService.getCourriersByObjetContaining(objet);
        return ResponseEntity.ok(courriers);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a courrier")
    public ResponseEntity<Void> deleteCourrier(@PathVariable Long id) {
        courrierService.deleteCourrier(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/check/numero/{numCourrier}")
    @Operation(summary = "Check if a courrier number exists")
    public ResponseEntity<Boolean> existsByNumCourrier(@PathVariable String numCourrier) {
        boolean exists = courrierService.existsByNumCourrier(numCourrier);
        return ResponseEntity.ok(exists);
    }
    
    @PostMapping(value = "/{id}/upload-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a PDF file for a courrier")
    public ResponseEntity<CourrierDTO> uploadPdfFile(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) throws IOException {
        
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        CourrierDTO courrier = courrierService.uploadPdfFile(id, file.getBytes(), file.getOriginalFilename());
        return ResponseEntity.ok(courrier);
    }
}