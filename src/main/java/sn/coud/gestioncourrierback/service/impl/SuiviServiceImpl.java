package sn.coud.gestioncourrierback.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.coud.gestioncourrierback.dto.SuiviDTO;
import sn.coud.gestioncourrierback.model.Courrier;
import sn.coud.gestioncourrierback.model.Suivi;
import sn.coud.gestioncourrierback.model.User;
import sn.coud.gestioncourrierback.model.StatutSuivi;
import sn.coud.gestioncourrierback.model.PrioriteSuivi;
import sn.coud.gestioncourrierback.repository.CourrierRepository;
import sn.coud.gestioncourrierback.repository.SuiviRepository;
import sn.coud.gestioncourrierback.repository.UserRepository;
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
    private final UserRepository userRepository;
    
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
        
        // Informations du responsable
        if (suivi.getResponsable() != null) {
            suiviDTO.setResponsableId(suivi.getResponsable().getId());
            suiviDTO.setResponsableNom(suivi.getResponsable().getNom());
            suiviDTO.setResponsablePrenom(suivi.getResponsable().getPrenom());
        }
        
        suiviDTO.setInstruction(suivi.getInstruction());
        suiviDTO.setDescription(suivi.getDescription());
        suiviDTO.setDate(suivi.getDate());
        suiviDTO.setStatut(suivi.getStatut());
        suiviDTO.setPriorite(suivi.getPriorite());
        suiviDTO.setDateEcheance(suivi.getDateEcheance());
        
        // Champs d'audit
        suiviDTO.setDateCreation(suivi.getDateCreation());
        suiviDTO.setDateModification(suivi.getDateModification());
        suiviDTO.setCreePar(suivi.getCreePar());
        suiviDTO.setModifiePar(suivi.getModifiePar());
        
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
        
        // Set responsable
        if (suiviDTO.getResponsableId() != null) {
            User responsable = userRepository.findById(suiviDTO.getResponsableId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + suiviDTO.getResponsableId()));
            suivi.setResponsable(responsable);
        }
        
        suivi.setInstruction(suiviDTO.getInstruction());
        suivi.setDescription(suiviDTO.getDescription());
        suivi.setDate(suiviDTO.getDate());
        suivi.setStatut(suiviDTO.getStatut() != null ? suiviDTO.getStatut() : StatutSuivi.EN_COURS);
        suivi.setPriorite(suiviDTO.getPriorite() != null ? suiviDTO.getPriorite() : PrioriteSuivi.NORMALE);
        suivi.setDateEcheance(suiviDTO.getDateEcheance());
        
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
        
        // Validate and update responsable if it's being changed
        if (suiviDTO.getResponsableId() != null) {
            if (existingSuivi.getResponsable() == null || 
                !existingSuivi.getResponsable().getId().equals(suiviDTO.getResponsableId())) {
                if (!userRepository.existsById(suiviDTO.getResponsableId())) {
                    throw new EntityNotFoundException("User not found with id: " + suiviDTO.getResponsableId());
                }
                
                User responsable = userRepository.findById(suiviDTO.getResponsableId())
                        .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + suiviDTO.getResponsableId()));
                existingSuivi.setResponsable(responsable);
            }
        } else {
            existingSuivi.setResponsable(null);
        }
        
        // Update suivi fields
        existingSuivi.setInstruction(suiviDTO.getInstruction());
        existingSuivi.setDescription(suiviDTO.getDescription());
        existingSuivi.setDate(suiviDTO.getDate());
        existingSuivi.setStatut(suiviDTO.getStatut() != null ? suiviDTO.getStatut() : existingSuivi.getStatut());
        existingSuivi.setPriorite(suiviDTO.getPriorite() != null ? suiviDTO.getPriorite() : existingSuivi.getPriorite());
        existingSuivi.setDateEcheance(suiviDTO.getDateEcheance());
        
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
