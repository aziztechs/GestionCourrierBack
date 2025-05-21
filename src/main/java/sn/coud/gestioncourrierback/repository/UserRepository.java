package sn.coud.gestioncourrierback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.coud.gestioncourrierback.model.User;

import java.util.Optional;

/**
 * Repository for User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Find a user by username.
     * 
     * @param username the username to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByUsername(String username);
    
    /**
     * Find a user by email.
     * 
     * @param email the email to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Find a user by matricule.
     * 
     * @param matricule the matricule to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByMatricule(String matricule);
    
    /**
     * Check if a username exists.
     * 
     * @param username the username to check
     * @return true if the username exists, false otherwise
     */
    boolean existsByUsername(String username);
    
    /**
     * Check if an email exists.
     * 
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    boolean existsByEmail(String email);
    
    /**
     * Check if a matricule exists.
     * 
     * @param matricule the matricule to check
     * @return true if the matricule exists, false otherwise
     */
    boolean existsByMatricule(String matricule);
}