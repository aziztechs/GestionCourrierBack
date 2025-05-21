package sn.coud.gestioncourrierback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a mail/courier in the system.
 */
@Entity
@Table(name = "courriers")
@Getter
@Setter
@ToString(exclude = "suivis")
@EqualsAndHashCode(exclude = "suivis")
@NoArgsConstructor
@AllArgsConstructor
public class Courrier implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le numéro de courrier est obligatoire")
    @Column(name = "num_courrier", nullable = false, unique = true)
    private String numCourrier;

    @NotBlank(message = "L'objet est obligatoire")
    @Column(nullable = false)
    private String objet;

    @NotNull(message = "Le type est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TypeCourrier type;

    @NotNull(message = "La nature est obligatoire")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NatureCourrier nature;

    @Column(name = "pdf_file")
    private String pdfFile;

    @NotBlank(message = "Le destinataire est obligatoire")
    @Column(nullable = false)
    private String destinataire;

    @NotBlank(message = "L'expéditeur est obligatoire")
    @Column(nullable = false)
    private String expediteur;

    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(mappedBy = "courrier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Suivi> suivis = new ArrayList<>();

    /**
     * Enum representing the type of mail (internal or external).
     */
    public enum TypeCourrier {
        INTERNE, EXTERNE
    }

    /**
     * Enum representing the nature of mail (incoming or outgoing).
     */
    public enum NatureCourrier {
        ARRIVE, DEPART
    }
}
