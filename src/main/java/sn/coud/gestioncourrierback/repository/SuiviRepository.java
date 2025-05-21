package sn.coud.gestioncourrierback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sn.coud.gestioncourrierback.model.Courrier;
import sn.coud.gestioncourrierback.model.Suivi;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for Suivi entity.
 */
@Repository
public interface SuiviRepository extends JpaRepository<Suivi, Long> {
    
    /**
     * Find all suivis by courrier.
     * 
     * @param courrier the courrier to search for
     * @return a list of suivis for the courrier
     */
    List<Suivi> findByCourrier(Courrier courrier);
    
    /**
     * Find all suivis by courrier id.
     * 
     * @param courrierId the courrier id to search for
     * @return a list of suivis for the courrier id
     */
    List<Suivi> findByCourrierId(Long courrierId);
    
    /**
     * Find all suivis by date.
     * 
     * @param date the date to search for
     * @return a list of suivis matching the date
     */
    List<Suivi> findByDate(LocalDate date);
    
    /**
     * Find all suivis between two dates.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return a list of suivis between the two dates
     */
    List<Suivi> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    /**
     * Find all suivis containing the given instruction.
     * 
     * @param instruction the instruction to search for
     * @return a list of suivis containing the instruction
     */
    List<Suivi> findByInstructionContaining(String instruction);
    
    /**
     * Delete all suivis for a courrier.
     * 
     * @param courrier the courrier to delete suivis for
     */
    void deleteByCourrier(Courrier courrier);
    
    /**
     * Delete all suivis for a courrier id.
     * 
     * @param courrierId the courrier id to delete suivis for
     */
    void deleteByCourrierId(Long courrierId);
}