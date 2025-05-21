package sn.coud.gestioncourrierback.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.coud.gestioncourrierback.dto.SuiviDTO;
import sn.coud.gestioncourrierback.model.Courrier;
import sn.coud.gestioncourrierback.model.Suivi;
import sn.coud.gestioncourrierback.repository.CourrierRepository;
import sn.coud.gestioncourrierback.repository.SuiviRepository;
import sn.coud.gestioncourrierback.service.SuiviService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the SuiviService interface.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SuiviServiceImpl implements SuiviService {
    
    private final SuiviRepository suiviRepository;
    private final CourrierRepository courrierRepository;
    
    /**
     * Convert a Suivi entity to a SuiviDTO.
     * 
     * @param suivi the Suivi entity to convert
     * @return the SuiviDTO
     */
    private SuiviDTO convertToDTO(Suivi suivi) {
        SuiviDTO suiviDTO = new SuiviDTO();
        suiviDTO.setId(suivi.getId());
        suiviDTO.setCourrierId(suivi.getCourrier().getId());
        suiviDTO.setInstruction(suivi.getInstruction());
        suiviDTO.setDescription(suivi.getDescription());
        suiviDTO.setDate(suivi.getDate());
        return suiviDTO;
    }
    
    /**
     * Convert a SuiviDTO to a Suivi entity.
     * 
     * @param suiviDTO the SuiviDTO to convert
     * @return the Suivi entity
     */
    private Suivi convertToEntity(SuiviDTO suiviDTO) {
        Suivi suivi = new Suivi();
        suivi.setId(suiviDTO.getId());
        
        // Set courrier
        if (suiviDTO.getCourrierId() != null) {
            Courrier courrier = courrierRepository.findById(suiviDTO.getCourrierId())
                    .orElseThrow(() -> new EntityNotFoundException("Courrier not found with id: " + suiviDTO.getCourrierId()));
            suivi.setCourrier(courrier);
        }
        
        suivi.setInstruction(suiviDTO.getInstruction());
        suivi.setDescription(suiviDTO.getDescription());
        suivi.setDate(suiviDTO.getDate());
        return suivi;
    }
    
    @Override
    public SuiviDTO createSuivi(SuiviDTO suiviDTO) {
        // Validate that courrier exists
        if (suiviDTO.getCourrierId() == null) {
            throw new IllegalArgumentException("Courrier ID is required");
        }
        
        if (!courrierRepository.existsById(suiviDTO.getCourrierId())) {
            throw new EntityNotFoundException("Courrier not found with id: " + suiviDTO.getCourrierId());
        }
        
        Suivi suivi = convertToEntity(suiviDTO);
        suivi = suiviRepository.save(suivi);
        return convertToDTO(suivi);
    }
    
    @Override
    public SuiviDTO updateSuivi(Long id, SuiviDTO suiviDTO) {
        Suivi existingSuivi = suiviRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suivi not found with id: " + id));
        
        // Validate that courrier exists if it's being changed
        if (suiviDTO.getCourrierId() != null && 
                !existingSuivi.getCourrier().getId().equals(suiviDTO.getCourrierId())) {
            if (!courrierRepository.existsById(suiviDTO.getCourrierId())) {
                throw new EntityNotFoundException("Courrier not found with id: " + suiviDTO.getCourrierId());
            }
            
            Courrier courrier = courrierRepository.findById(suiviDTO.getCourrierId())
                    .orElseThrow(() -> new EntityNotFoundException("Courrier not found with id: " + suiviDTO.getCourrierId()));
            existingSuivi.setCourrier(courrier);
        }
        
        // Update suivi fields
        existingSuivi.setInstruction(suiviDTO.getInstruction());
        existingSuivi.setDescription(suiviDTO.getDescription());
        existingSuivi.setDate(suiviDTO.getDate());
        
        existingSuivi = suiviRepository.save(existingSuivi);
        return convertToDTO(existingSuivi);
    }
    
    @Override
    @Transactional(readOnly = true)
    public SuiviDTO getSuiviById(Long id) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suivi not found with id: " + id));
        return convertToDTO(suivi);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SuiviDTO> getAllSuivis() {
        return suiviRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SuiviDTO> getSuivisByCourrierId(Long courrierId) {
        return suiviRepository.findByCourrierId(courrierId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SuiviDTO> getSuivisByDate(LocalDate date) {
        return suiviRepository.findByDate(date).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SuiviDTO> getSuivisBetweenDates(LocalDate startDate, LocalDate endDate) {
        return suiviRepository.findByDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<SuiviDTO> getSuivisByInstructionContaining(String instruction) {
        return suiviRepository.findByInstructionContaining(instruction).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public void deleteSuivi(Long id) {
        if (!suiviRepository.existsById(id)) {
            throw new EntityNotFoundException("Suivi not found with id: " + id);
        }
        suiviRepository.deleteById(id);
    }
    
    @Override
    public void deleteSuivisByCourrierId(Long courrierId) {
        if (!courrierRepository.existsById(courrierId)) {
            throw new EntityNotFoundException("Courrier not found with id: " + courrierId);
        }
        suiviRepository.deleteByCourrierId(courrierId);
    }
}