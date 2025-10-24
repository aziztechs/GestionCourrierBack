package sn.coud.gestioncourrierback.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import sn.coud.gestioncourrierback.dto.SuiviDTO;
import sn.coud.gestioncourrierback.model.StatutSuivi;
import sn.coud.gestioncourrierback.model.PrioriteSuivi;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Service interface for managing suivis.
 */
public interface SuiviService {
    
    /**
     * Create a new suivi.
     * 
     * @param suiviDTO the suivi to create
     * @return the created suivi
     */
    SuiviDTO createSuivi(SuiviDTO suiviDTO);
    
    /**
     * Update an existing suivi.
     * 
     * @param id the id of the suivi to update
     * @param suiviDTO the updated suivi data
     * @return the updated suivi
     */
    SuiviDTO updateSuivi(Long id, SuiviDTO suiviDTO);
    
    /**
     * Get a suivi by id.
     * 
     * @param id the id of the suivi to get
     * @return the suivi
     */
    SuiviDTO getSuiviById(Long id);
    
    /**
     * Get all suivis.
     * 
     * @return a list of all suivis
     */
    List<SuiviDTO> getAllSuivis();
    
    /**
     * Get all suivis for a courrier.
     * 
     * @param courrierId the id of the courrier
     * @return a list of suivis for the courrier
     * @deprecated Use getSuivisByCourrierId(Long, Pageable) instead
     */
    @Deprecated
    List<SuiviDTO> getSuivisByCourrierId(Long courrierId);
    
    /**
     * Get all suivis by date.
     * 
     * @param date the date to search for
     * @return a list of suivis matching the date
     * @deprecated Use getSuivisByDate(LocalDate, Pageable) instead
     */
    @Deprecated
    List<SuiviDTO> getSuivisByDate(LocalDate date);
    
    /**
     * Get all suivis between two dates.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return a list of suivis between the two dates
     * @deprecated Use getSuivisBetweenDates(LocalDate, LocalDate, Pageable) instead
     */
    @Deprecated
    List<SuiviDTO> getSuivisBetweenDates(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get all suivis containing the given instruction.
     * 
     * @param instruction the instruction to search for
     * @return a list of suivis containing the instruction
     * @deprecated Use getSuivisByInstructionContaining(String, Pageable) instead
     */
    @Deprecated
    List<SuiviDTO> getSuivisByInstructionContaining(String instruction);
    
    /**
     * Delete a suivi by id.
     * 
     * @param id the id of the suivi to delete
     */
    void deleteSuivi(Long id);
    
    /**
     * Delete all suivis for a courrier.
     * 
     * @param courrierId the id of the courrier
     */
    void deleteSuivisByCourrierId(Long courrierId);
    
    // ========== NOUVELLES MÉTHODES AVEC PAGINATION ==========
    
    /**
     * Get all suivis with pagination and default sorting.
     * 
     * @param pageable pagination and sorting parameters
     * @return a page of suivis
     */
    Page<SuiviDTO> getAllSuivisPaginated(Pageable pageable);
    
    /**
     * Get all suivis by statut with pagination.
     * 
     * @param statut the statut to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis matching the statut
     */
    Page<SuiviDTO> getSuivisByStatut(StatutSuivi statut, Pageable pageable);
    
    /**
     * Get all suivis by priorite with pagination.
     * 
     * @param priorite the priorite to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis matching the priorite
     */
    Page<SuiviDTO> getSuivisByPriorite(PrioriteSuivi priorite, Pageable pageable);
    
    /**
     * Get all suivis by responsable with pagination.
     * 
     * @param responsableId the responsable id to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis for the responsable
     */
    Page<SuiviDTO> getSuivisByResponsable(Long responsableId, Pageable pageable);
    
    /**
     * Get all suivis by courrier with pagination.
     * 
     * @param courrierId the courrier id to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis for the courrier
     */
    Page<SuiviDTO> getSuivisByCourrierId(Long courrierId, Pageable pageable);
    
    /**
     * Get all suivis by date with pagination.
     * 
     * @param date the date to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis matching the date
     */
    Page<SuiviDTO> getSuivisByDate(LocalDate date, Pageable pageable);
    
    /**
     * Get all suivis between two dates with pagination.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @param pageable pagination and sorting parameters
     * @return a page of suivis between the two dates
     */
    Page<SuiviDTO> getSuivisBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    /**
     * Get all suivis containing the given instruction with pagination.
     * 
     * @param instruction the instruction to search for
     * @param pageable pagination and sorting parameters
     * @return a page of suivis containing the instruction
     */
    Page<SuiviDTO> getSuivisByInstructionContaining(String instruction, Pageable pageable);
    
    /**
     * Search suivis by multiple criteria.
     * 
     * @param statut the statut to filter by (optional)
     * @param priorite the priorite to filter by (optional)
     * @param responsableId the responsable id to filter by (optional)
     * @param courrierId the courrier id to filter by (optional)
     * @param pageable pagination and sorting parameters
     * @return a page of suivis matching the criteria
     */
    Page<SuiviDTO> searchSuivis(StatutSuivi statut, PrioriteSuivi priorite, 
                                Long responsableId, Long courrierId, Pageable pageable);
    
    /**
     * Get suivis with approaching deadline.
     * 
     * @param days number of days from now
     * @param pageable pagination and sorting parameters
     * @return a page of suivis with approaching deadline
     */
    Page<SuiviDTO> getSuivisWithApproachingDeadline(int days, Pageable pageable);
    
    /**
     * Get overdue suivis.
     * 
     * @param pageable pagination and sorting parameters
     * @return a page of overdue suivis
     */
    Page<SuiviDTO> getOverdueSuivis(Pageable pageable);
    
    // ========== MÉTHODES DE GESTION DES STATUTS ==========
    
    /**
     * Change the status of a suivi.
     * 
     * @param id the id of the suivi
     * @param newStatut the new status
     * @return the updated suivi
     */
    SuiviDTO changeStatut(Long id, StatutSuivi newStatut);
    
    /**
     * Assign a responsable to a suivi.
     * 
     * @param id the id of the suivi
     * @param responsableId the id of the new responsable
     * @return the updated suivi
     */
    SuiviDTO assignResponsable(Long id, Long responsableId);
    
    /**
     * Remove responsable from a suivi.
     * 
     * @param id the id of the suivi
     * @return the updated suivi
     */
    SuiviDTO removeResponsable(Long id);
    
    /**
     * Change the priority of a suivi.
     * 
     * @param id the id of the suivi
     * @param newPriorite the new priority
     * @return the updated suivi
     */
    SuiviDTO changePriorite(Long id, PrioriteSuivi newPriorite);
    
    // ========== MÉTHODES DE STATISTIQUES ==========
    
    /**
     * Get statistics about suivis.
     * 
     * @return a map containing various statistics
     */
    Map<String, Object> getSuiviStatistics();
    
    /**
     * Get count of suivis by statut.
     * 
     * @return a map with statut as key and count as value
     */
    Map<StatutSuivi, Long> getCountByStatut();
    
    /**
     * Get count of suivis by priorite.
     * 
     * @return a map with priorite as key and count as value
     */
    Map<PrioriteSuivi, Long> getCountByPriorite();
    
    /**
     * Get count of suivis by responsable.
     * 
     * @param responsableId the responsable id
     * @return the count of suivis for the responsable
     */
    long getCountByResponsable(Long responsableId);
    
    // ========== MÉTHODES DE VALIDATION ==========
    
    /**
     * Get possible next statuses for a suivi.
     * 
     * @param id the id of the suivi
     * @return the list of possible next statuses
     */
    List<StatutSuivi> getPossibleNextStatuses(Long id);
    
    /**
     * Check if a suivi can be modified.
     * 
     * @param id the id of the suivi
     * @return true if the suivi can be modified
     */
    boolean canModifySuivi(Long id);
    
    /**
     * Check if a suivi can be deleted.
     * 
     * @param id the id of the suivi
     * @return true if the suivi can be deleted
     */
    boolean canDeleteSuivi(Long id);
}
