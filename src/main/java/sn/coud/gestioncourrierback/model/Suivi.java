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

/**
 * Entity representing a follow-up for a mail/courier in the system.
 */
@Entity
@Table(name = "suivis")
@Getter
@Setter
@ToString(exclude = "courrier")
@EqualsAndHashCode(exclude = "courrier")
@NoArgsConstructor
@AllArgsConstructor
public class Suivi implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_courrier", nullable = false)
    private Courrier courrier;

    @NotBlank(message = "L'instruction est obligatoire")
    @Column(nullable = false)
    private String instruction;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull(message = "La date est obligatoire")
    @Column(nullable = false)
    private LocalDate date;
}
