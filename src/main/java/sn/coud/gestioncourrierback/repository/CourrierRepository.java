package sn.coud.gestioncourrierback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.coud.gestioncourrierback.model.Courrier;
import sn.coud.gestioncourrierback.model.Courrier.TypeCourrier;
import sn.coud.gestioncourrierback.model.Courrier.NatureCourrier;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for Courrier entity.
 */
@Repository
public interface CourrierRepository extends JpaRepository<Courrier, Long> {
    
    /**
     * Find a courrier by its number.
     * 
     * @param numCourrier the courrier number to search for
     * @return an Optional containing the courrier if found, or empty if not found
     */
    Optional<Courrier> findByNumCourrier(String numCourrier);
    
    /**
     * Check if a courrier number exists.
     * 
     * @param numCourrier the courrier number to check
     * @return true if the courrier number exists, false otherwise
     */
    boolean existsByNumCourrier(String numCourrier);
    
    /**
     * Find all courriers by type.
     * 
     * @param type the type of courrier (INTERNE or EXTERNE)
     * @return a list of courriers matching the type
     */
    List<Courrier> findByType(TypeCourrier type);
    
    /**
     * Find all courriers by nature.
     * 
     * @param nature the nature of courrier (ARRIVE or DEPART)
     * @return a list of courriers matching the nature
     */
    List<Courrier> findByNature(NatureCourrier nature);
    
    /**
     * Find all courriers by date.
     * 
     * @param date the date to search for
     * @return a list of courriers matching the date
     */
    List<Courrier> findByDate(LocalDate date);
    
    /**
     * Find all courriers between two dates.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return a list of courriers between the two dates
     */
    List<Courrier> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find all courriers by destinataire.
     * 
     * @param destinataire the destinataire to search for
     * @return a list of courriers matching the destinataire
     */
    List<Courrier> findByDestinataire(String destinataire);
    
    /**
     * Find all courriers by expediteur.
     * 
     * @param expediteur the expediteur to search for
     * @return a list of courriers matching the expediteur
     */
    List<Courrier> findByExpediteur(String expediteur);
    
    /**
     * Find all courriers containing the given objet.
     * 
     * @param objet the objet to search for
     * @return a list of courriers containing the objet
     */
    List<Courrier> findByObjetContaining(String objet);
}