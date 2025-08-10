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
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity representing a follow-up for a mail/courier in the system.
 */
@Entity
@Table(name = "suivis", indexes = {
    @Index(name = "idx_suivi_statut", columnList = "statut"),
    @Index(name = "idx_suivi_priorite", columnList = "priorite"),
    @Index(name = "idx_suivi_date", columnList = "date"),
    @Index(name = "idx_suivi_courrier_id", columnList = "id_courrier"),
    @Index(name = "idx_suivi_responsable_id", columnList = "id_responsable")
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString(exclude = {"courrier", "responsable"})
@EqualsAndHashCode(exclude = {"courrier", "responsable"})
@NoArgsConstructor
@AllArgsConstructor
public class Suivi implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_courrier", nullable = false)
    private Courrier courrier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_responsable")
    private User responsable;

    @NotBlank(message = "L'instruction est obligatoire")
    @Column(nullable = false)
    private String instruction;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutSuivi statut = StatutSuivi.EN_COURS;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioriteSuivi priorite = PrioriteSuivi.NORMALE;

    @Column(name = "date_echeance")
    private LocalDate dateEcheance;

    // Champs d'audit automatique
    @CreatedDate
    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    @LastModifiedDate
    @Column(name = "date_modification")
    private LocalDateTime dateModification;

    @CreatedBy
    @Column(name = "cree_par", nullable = false, updatable = false)
    private String creePar;

    @LastModifiedBy
    @Column(name = "modifie_par")
    private String modifiePar;
}
