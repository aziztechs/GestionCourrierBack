package sn.coud.gestioncourrierback.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Entity representing a user in the system.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(nullable = false)
    private String prenom;

    @NotBlank(message = "Le nom d'utilisateur est obligatoire")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Le matricule est obligatoire")
    @Column(nullable = false, unique = true)
    private String matricule;

    @Column(nullable = false)
    private boolean active = true;

    @NotBlank(message = "Le rôle/fonction est obligatoire")
    @Column(name = "role_fonction", nullable = false)
    private String roleFonction;

    @Email(message = "Format d'email invalide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String telephone;
}