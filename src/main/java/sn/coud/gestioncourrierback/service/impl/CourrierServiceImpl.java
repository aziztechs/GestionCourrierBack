package sn.coud.gestioncourrierback.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.coud.gestioncourrierback.dto.CourrierDTO;
import sn.coud.gestioncourrierback.dto.SuiviDTO;
import sn.coud.gestioncourrierback.model.Courrier;
import sn.coud.gestioncourrierback.model.Courrier.TypeCourrier;
import sn.coud.gestioncourrierback.model.Courrier.NatureCourrier;
import sn.coud.gestioncourrierback.model.Suivi;
import sn.coud.gestioncourrierback.repository.CourrierRepository;
import sn.coud.gestioncourrierback.repository.SuiviRepository;
import sn.coud.gestioncourrierback.service.CourrierService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the CourrierService interface.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CourrierServiceImpl implements CourrierService {
    
    private final CourrierRepository courrierRepository;
    private final SuiviRepository suiviRepository;
    
    // Directory to store PDF files
    private static final String UPLOAD_DIR = "uploads/courriers";
    
    /**
     * Convert a Suivi entity to a SuiviDTO.
     * 
     * @param suivi the Suivi entity to convert
     * @return the SuiviDTO
     */
    private SuiviDTO convertToSuiviDTO(Suivi suivi) {
        SuiviDTO suiviDTO = new SuiviDTO();
        suiviDTO.setId(suivi.getId());
        suiviDTO.setCourrierId(suivi.getCourrier().getId());
        suiviDTO.setInstruction(suivi.getInstruction());
        suiviDTO.setDescription(suivi.getDescription());
        suiviDTO.setDate(suivi.getDate());
        return suiviDTO;
    }
    
    /**
     * Convert a Courrier entity to a CourrierDTO.
     * 
     * @param courrier the Courrier entity to convert
     * @return the CourrierDTO
     */
    private CourrierDTO convertToDTO(Courrier courrier) {
        CourrierDTO courrierDTO = new CourrierDTO();
        courrierDTO.setId(courrier.getId());
        courrierDTO.setNumCourrier(courrier.getNumCourrier());
        courrierDTO.setObjet(courrier.getObjet());
        courrierDTO.setType(courrier.getType());
        courrierDTO.setNature(courrier.getNature());
        courrierDTO.setPdfFile(courrier.getPdfFile());
        courrierDTO.setDestinataire(courrier.getDestinataire());
        courrierDTO.setExpediteur(courrier.getExpediteur());
        courrierDTO.setDate(courrier.getDate());
        
        // Convert and set suivis
        List<SuiviDTO> suiviDTOs = courrier.getSuivis().stream()
                .map(this::convertToSuiviDTO)
                .collect(Collectors.toList());
        courrierDTO.setSuivis(suiviDTOs);
        
        return courrierDTO;
    }
    
    /**
     * Convert a CourrierDTO to a Courrier entity.
     * 
     * @param courrierDTO the CourrierDTO to convert
     * @return the Courrier entity
     */
    private Courrier convertToEntity(CourrierDTO courrierDTO) {
        Courrier courrier = new Courrier();
        courrier.setId(courrierDTO.getId());
        courrier.setNumCourrier(courrierDTO.getNumCourrier());
        courrier.setObjet(courrierDTO.getObjet());
        courrier.setType(courrierDTO.getType());
        courrier.setNature(courrierDTO.getNature());
        courrier.setPdfFile(courrierDTO.getPdfFile());
        courrier.setDestinataire(courrierDTO.getDestinataire());
        courrier.setExpediteur(courrierDTO.getExpediteur());
        courrier.setDate(courrierDTO.getDate());
        
        // We don't set suivis here because they are managed separately
        
        return courrier;
    }
    
    @Override
    public CourrierDTO createCourrier(CourrierDTO courrierDTO) {
        // Check if courrier number already exists
        if (courrierRepository.existsByNumCourrier(courrierDTO.getNumCourrier())) {
            throw new IllegalArgumentException("Courrier number already exists: " + courrierDTO.getNumCourrier());
        }
        
        Courrier courrier = convertToEntity(courrierDTO);
        courrier = courrierRepository.save(courrier);
        return convertToDTO(courrier);
    }
    
    @Override
    public CourrierDTO updateCourrier(Long id, CourrierDTO courrierDTO) {
        Courrier existingCourrier = courrierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Courrier not found with id: " + id));
        
        // Check if courrier number already exists for another courrier
        if (!existingCourrier.getNumCourrier().equals(courrierDTO.getNumCourrier()) && 
                courrierRepository.existsByNumCourrier(courrierDTO.getNumCourrier())) {
            throw new IllegalArgumentException("Courrier number already exists: " + courrierDTO.getNumCourrier());
        }
        
        // Update courrier fields
        existingCourrier.setNumCourrier(courrierDTO.getNumCourrier());
        existingCourrier.setObjet(courrierDTO.getObjet());
        existingCourrier.setType(courrierDTO.getType());
        existingCourrier.setNature(courrierDTO.getNature());
        existingCourrier.setDestinataire(courrierDTO.getDestinataire());
        existingCourrier.setExpediteur(courrierDTO.getExpediteur());
        existingCourrier.setDate(courrierDTO.getDate());
        
        // Don't update PDF file here, it's handled separately
        
        existingCourrier = courrierRepository.save(existingCourrier);
        return convertToDTO(existingCourrier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CourrierDTO getCourrierById(Long id) {
        Courrier courrier = courrierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Courrier not found with id: " + id));
        return convertToDTO(courrier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public CourrierDTO getCourrierByNumCourrier(String numCourrier) {
        Courrier courrier = courrierRepository.findByNumCourrier(numCourrier)
                .orElseThrow(() -> new EntityNotFoundException("Courrier not found with number: " + numCourrier));
        return convertToDTO(courrier);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CourrierDTO> getAllCourriers() {
        return courrierRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CourrierDTO> getCourriersByType(TypeCourrier type) {
        return courrierRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CourrierDTO> getCourriersByNature(NatureCourrier nature) {
        return courrierRepository.findByNature(nature).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CourrierDTO> getCourriersByDate(LocalDate date) {
        return courrierRepository.findByDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CourrierDTO> getCourriersBetweenDates(LocalDate startDate, LocalDate endDate) {
        return courrierRepository.findByDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CourrierDTO> getCourriersByDestinataire(String destinataire) {
        return courrierRepository.findByDestinataire(destinataire).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CourrierDTO> getCourriersByExpediteur(String expediteur) {
        return courrierRepository.findByExpediteur(expediteur).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<CourrierDTO> getCourriersByObjetContaining(String objet) {
        return courrierRepository.findByObjetContaining(objet).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteCourrier(Long id) {
        if (!courrierRepository.existsById(id)) {
            throw new EntityNotFoundException("Courrier not found with id: " + id);
        }
        
        // Delete associated suivis first
        suiviRepository.deleteByCourrierId(id);
        
        // Delete the courrier
        courrierRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public boolean existsByNumCourrier(String numCourrier) {
        return courrierRepository.existsByNumCourrier(numCourrier);
    }
    
    @Override
    public CourrierDTO uploadPdfFile(Long id, byte[] pdfFile, String fileName) {
        Courrier courrier = courrierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Courrier not found with id: " + id));
        
        try {
            // Create directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate a unique file name
            String uniqueFileName = courrier.getNumCourrier() + "_" + System.currentTimeMillis() + "_" + fileName;
            Path filePath = uploadPath.resolve(uniqueFileName);
            
            // Save the file
            Files.write(filePath, pdfFile);
            
            // Update the courrier with the file path
            courrier.setPdfFile(uniqueFileName);
            courrier = courrierRepository.save(courrier);
            
            return convertToDTO(courrier);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }
    }
}