package sn.coud.gestioncourrierback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.coud.gestioncourrierback.model.Courrier.TypeCourrier;
import sn.coud.gestioncourrierback.model.Courrier.NatureCourrier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Transfer Object for Courrier entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourrierDTO {
    
    private Long id;
    
    @NotBlank(message = "Le numéro de courrier est obligatoire")
    private String numCourrier;
    
    @NotBlank(message = "L'objet est obligatoire")
    private String objet;
    
    @NotNull(message = "Le type est obligatoire")
    private TypeCourrier type;
    
    @NotNull(message = "La nature est obligatoire")
    private NatureCourrier nature;
    
    private String pdfFile;
    
    @NotBlank(message = "Le destinataire est obligatoire")
    private String destinataire;
    
    @NotBlank(message = "L'expéditeur est obligatoire")
    private String expediteur;
    
    @NotNull(message = "La date est obligatoire")
    private LocalDate date;
    
    private List<SuiviDTO> suivis = new ArrayList<>();
}