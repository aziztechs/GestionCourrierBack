package sn.coud.gestioncourrierback.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sn.coud.gestioncourrierback.model.Courrier;
import sn.coud.gestioncourrierback.model.Suivi;
import sn.coud.gestioncourrierback.model.StatutSuivi;
import sn.coud.gestioncourrierback.model.PrioriteSuivi;
import sn.coud.gestioncourrierback.model.User;

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
    
    // ========== NOUVELLES MÉTHODES DE RECHERCHE ==========
    
    /**
     * Find all suivis with pagination and default sorting.
     * 
     * @param pageable pagination and sorting parameters
     * @return a page of suivis
     */
    @Query("SELECT s FROM Suivi s ORDER BY s.priorite DESC, s.date DESC, s.dateCreation DESC")
    Page<Suivi> findAllWithDefaultSort(Pageable pageable);
    
    /**
     * Find all suivis by statut.
     * 
     * @param statut the statut to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis matching the statut
     */
    Page<Suivi> findByStatut(StatutSuivi statut, Pageable pageable);
    
    /**
     * Find all suivis by priorite.
     * 
     * @param priorite the priorite to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis matching the priorite
     */
    Page<Suivi> findByPriorite(PrioriteSuivi priorite, Pageable pageable);
    
    /**
     * Find all suivis by responsable.
     * 
     * @param responsable the responsable to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis for the responsable
     */
    Page<Suivi> findByResponsable(User responsable, Pageable pageable);
    
    /**
     * Find all suivis by responsable id.
     * 
     * @param responsableId the responsable id to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis for the responsable id
     */
    Page<Suivi> findByResponsableId(Long responsableId, Pageable pageable);
    
    /**
     * Find all suivis by courrier id with pagination.
     * 
     * @param courrierId the courrier id to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis for the courrier id
     */
    Page<Suivi> findByCourrierId(Long courrierId, Pageable pageable);
    
    /**
     * Find all suivis by date with pagination.
     * 
     * @param date the date to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis matching the date
     */
    Page<Suivi> findByDate(LocalDate date, Pageable pageable);
    
    /**
     * Find all suivis between two dates with pagination.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination and sorting parameters
     * @return a page of suivis between the two dates
     */
    Page<Suivi> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Find all suivis containing the given instruction with pagination.
     * 
     * @param instruction the instruction to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis containing the instruction
     */
    Page<Suivi> findByInstructionContaining(String instruction, Pageable pageable);
    
    /**
     * Find all suivis by multiple criteria.
     * 
     * @param statut the statut to filter by (optional)
     * @param priorite the priorite to filter by (optional)
     * @param responsableId the responsable id to filter by (optional)
     * @param courrierId the courrier id to filter by (optional)
     * @param pageable pagination and sorting parameters
     * @return a page of suivis matching the criteria
     */
    @Query("SELECT s FROM Suivi s WHERE " +
           "(:statut IS NULL OR s.statut = :statut) AND " +
           "(:priorite IS NULL OR s.priorite = :priorite) AND " +
           "(:responsableId IS NULL OR s.responsable.id = :responsableId) AND " +
           "(:courrierId IS NULL OR s.courrier.id = :courrierId) " +
           "ORDER BY s.priorite DESC, s.date DESC, s.dateCreation DESC")
    Page<Suivi> findByCriteria(@Param("statut") StatutSuivi statut,
                               @Param("priorite") PrioriteSuivi priorite,
                               @Param("responsableId") Long responsableId,
                               @Param("courrierId") Long courrierId,
                               Pageable pageable);
    
    /**
     * Find all suivis with approaching deadline (within specified days).
     * 
     * @param days number of days from now
     * @param pageable pagination and sorting parameters
     * @return a page of suivis with approaching deadline
     */
    @Query("SELECT s FROM Suivi s WHERE s.dateEcheance IS NOT NULL AND " +
           "s.dateEcheance <= :deadline AND s.statut != 'TERMINE' AND s.statut != 'ANNULE' " +
           "ORDER BY s.dateEcheance ASC, s.priorite DESC")
    Page<Suivi> findSuivisWithApproachingDeadline(@Param("deadline") LocalDate deadline, Pageable pageable);
    
    /**
     * Find all overdue suivis.
     * 
     * @param currentDate the current date
     * @param pageable pagination and sorting parameters
     * @return a page of overdue suivis
     */
    @Query("SELECT s FROM Suivi s WHERE s.dateEcheance IS NOT NULL AND " +
           "s.dateEcheance < :currentDate AND s.statut != 'TERMINE' AND s.statut != 'ANNULE' " +
           "ORDER BY s.dateEcheance ASC, s.priorite DESC")
    Page<Suivi> findOverdueSuivis(@Param("currentDate") LocalDate currentDate, Pageable pageable);
    
    /**
     * Count suivis by statut.
     * 
     * @param statut the statut to count
     * @return the number of suivis with the given statut
     */
    long countByStatut(StatutSuivi statut);
    
    /**
     * Count suivis by priorite.
     * 
     * @param priorite the priorite to count
     * @return the number of suivis with the given priorite
     */
    long countByPriorite(PrioriteSuivi priorite);
    
    /**
     * Count suivis by responsable id.
     * 
     * @param responsableId the responsable id to count
     * @return the number of suivis for the given responsable
     */
    long countByResponsableId(Long responsableId);
}
