package sn.coud.gestioncourrierback.service;

import sn.coud.gestioncourrierback.dto.SuiviDTO;

import java.time.LocalDate;
import java.util.List;

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
     */
    List<SuiviDTO> getSuivisByCourrierId(Long courrierId);
    
    /**
     * Get all suivis by date.
     * 
     * @param date the date to search for
     * @return a list of suivis matching the date
     */
    List<SuiviDTO> getSuivisByDate(LocalDate date);
    
    /**
     * Get all suivis between two dates.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return a list of suivis between the two dates
     */
    List<SuiviDTO> getSuivisBetweenDates(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get all suivis containing the given instruction.
     * 
     * @param instruction the instruction to search for
     * @return a list of suivis containing the instruction
     */
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
}