package sn.coud.gestioncourrierback.service;

import sn.coud.gestioncourrierback.dto.CourrierDTO;
import sn.coud.gestioncourrierback.model.Courrier.TypeCourrier;
import sn.coud.gestioncourrierback.model.Courrier.NatureCourrier;

import java.time.LocalDate;
import java.util.List;

/**
 * Service interface for managing courriers.
 */
public interface CourrierService {
    
    /**
     * Create a new courrier.
     * 
     * @param courrierDTO the courrier to create
     * @return the created courrier
     */
    CourrierDTO createCourrier(CourrierDTO courrierDTO);
    
    /**
     * Update an existing courrier.
     * 
     * @param id the id of the courrier to update
     * @param courrierDTO the updated courrier data
     * @return the updated courrier
     */
    CourrierDTO updateCourrier(Long id, CourrierDTO courrierDTO);
    
    /**
     * Get a courrier by id.
     * 
     * @param id the id of the courrier to get
     * @return the courrier
     */
    CourrierDTO getCourrierById(Long id);
    
    /**
     * Get a courrier by number.
     * 
     * @param numCourrier the number of the courrier to get
     * @return the courrier
     */
    CourrierDTO getCourrierByNumCourrier(String numCourrier);
    
    /**
     * Get all courriers.
     * 
     * @return a list of all courriers
     */
    List<CourrierDTO> getAllCourriers();
    
    /**
     * Get all courriers by type.
     * 
     * @param type the type of courrier (INTERNE or EXTERNE)
     * @return a list of courriers matching the type
     */
    List<CourrierDTO> getCourriersByType(TypeCourrier type);
    
    /**
     * Get all courriers by nature.
     * 
     * @param nature the nature of courrier (ARRIVE or DEPART)
     * @return a list of courriers matching the nature
     */
    List<CourrierDTO> getCourriersByNature(NatureCourrier nature);
    
    /**
     * Get all courriers by date.
     * 
     * @param date the date to search for
     * @return a list of courriers matching the date
     */
    List<CourrierDTO> getCourriersByDate(LocalDate date);
    
    /**
     * Get all courriers between two dates.
     * 
     * @param startDate the start date
     * @param endDate the end date
     * @return a list of courriers between the two dates
     */
    List<CourrierDTO> getCourriersBetweenDates(LocalDate startDate, LocalDate endDate);
    
    /**
     * Get all courriers by destinataire.
     * 
     * @param destinataire the destinataire to search for
     * @return a list of courriers matching the destinataire
     */
    List<CourrierDTO> getCourriersByDestinataire(String destinataire);
    
    /**
     * Get all courriers by expediteur.
     * 
     * @param expediteur the expediteur to search for
     * @return a list of courriers matching the expediteur
     */
    List<CourrierDTO> getCourriersByExpediteur(String expediteur);
    
    /**
     * Get all courriers containing the given objet.
     * 
     * @param objet the objet to search for
     * @return a list of courriers containing the objet
     */
    List<CourrierDTO> getCourriersByObjetContaining(String objet);
    
    /**
     * Delete a courrier by id.
     * 
     * @param id the id of the courrier to delete
     */
    void deleteCourrier(Long id);
    
    /**
     * Check if a courrier number exists.
     * 
     * @param numCourrier the courrier number to check
     * @return true if the courrier number exists, false otherwise
     */
    boolean existsByNumCourrier(String numCourrier);
    
    /**
     * Upload a PDF file for a courrier.
     * 
     * @param id the id of the courrier
     * @param pdfFile the PDF file as a byte array
     * @return the updated courrier
     */
    CourrierDTO uploadPdfFile(Long id, byte[] pdfFile, String fileName);
}