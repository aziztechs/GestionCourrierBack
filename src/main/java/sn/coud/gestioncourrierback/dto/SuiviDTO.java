package sn.coud.gestioncourrierback.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Suivi entity.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuiviDTO {
    
    private Long id;
    
    private Long courrierId;
    
    @NotBlank(message = "L'instruction est obligatoire")
    private String instruction;
    
    private String description;
    
    @NotNull(message = "La date est obligatoire")
    private LocalDate date;
}