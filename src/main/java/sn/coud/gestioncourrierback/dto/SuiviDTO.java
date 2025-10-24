package sn.coud.gestioncourrierback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.coud.gestioncourrierback.model.StatutSuivi;
import sn.coud.gestioncourrierback.model.PrioriteSuivi;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for Suivi entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuiviDTO {
    
    private Long id;
    
    private Long courrierId;
    
    private Long responsableId;
    
    private String responsableNom;
    
    private String responsablePrenom;
    
    @NotBlank(message = "L'instruction est obligatoire")
    private String instruction;
    
    private String description;
    
    @NotNull(message = "La date est obligatoire")
    private LocalDate date;
    
    @NotNull(message = "Le statut est obligatoire")
    private StatutSuivi statut = StatutSuivi.EN_COURS;
    
    @NotNull(message = "La priorité est obligatoire")
    private PrioriteSuivi priorite = PrioriteSuivi.NORMALE;
    
    private LocalDate dateEcheance;
    
    // Champs d'audit (lecture seule)
    private LocalDateTime dateCreation;
    
    private LocalDateTime dateModification;
    
    private String creePar;
    
    private String modifiePar;
}
