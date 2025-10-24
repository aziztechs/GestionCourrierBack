package sn.coud.gestioncourrierback.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sn.coud.gestioncourrierback.dto.SuiviDTO;
import sn.coud.gestioncourrierback.exception.SuiviBusinessException;
import sn.coud.gestioncourrierback.model.Courrier;
import sn.coud.gestioncourrierback.model.Suivi;
import sn.coud.gestioncourrierback.model.User;
import sn.coud.gestioncourrierback.model.StatutSuivi;
import sn.coud.gestioncourrierback.model.PrioriteSuivi;
import sn.coud.gestioncourrierback.repository.CourrierRepository;
import sn.coud.gestioncourrierback.repository.SuiviRepository;
import sn.coud.gestioncourrierback.repository.UserRepository;
import sn.coud.gestioncourrierback.service.SuiviService;
import sn.coud.gestioncourrierback.validation.SuiviValidator;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final SuiviValidator suiviValidator;
    
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
        // Validation métier
        List<String> validationErrors = suiviValidator.validateSuiviDTO(suiviDTO);
        if (!validationErrors.isEmpty()) {
            throw new SuiviBusinessException("Erreurs de validation lors de la création du suivi", validationErrors);
        }
        
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
    
    // ========== NOUVELLES MÉTHODES AVEC PAGINATION ==========
    
    @Override
    public Page<SuiviDTO> getAllSuivisPaginated(Pageable pageable) {
        return suiviRepository.findAllWithDefaultSort(pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> getSuivisByStatut(StatutSuivi statut, Pageable pageable) {
        return suiviRepository.findByStatut(statut, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> getSuivisByPriorite(PrioriteSuivi priorite, Pageable pageable) {
        return suiviRepository.findByPriorite(priorite, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> getSuivisByResponsable(Long responsableId, Pageable pageable) {
        return suiviRepository.findByResponsableId(responsableId, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> getSuivisByCourrierId(Long courrierId, Pageable pageable) {
        return suiviRepository.findByCourrierId(courrierId, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> getSuivisByDate(LocalDate date, Pageable pageable) {
        return suiviRepository.findByDate(date, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> getSuivisBetweenDates(LocalDate startDate, LocalDate endDate, Pageable pageable) {
        return suiviRepository.findByDateBetween(startDate, endDate, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> getSuivisByInstructionContaining(String instruction, Pageable pageable) {
        return suiviRepository.findByInstructionContaining(instruction, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> searchSuivis(StatutSuivi statut, PrioriteSuivi priorite, 
                                       Long responsableId, Long courrierId, Pageable pageable) {
        return suiviRepository.findByCriteria(statut, priorite, responsableId, courrierId, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> getSuivisWithApproachingDeadline(int days, Pageable pageable) {
        LocalDate deadline = LocalDate.now().plusDays(days);
        return suiviRepository.findSuivisWithApproachingDeadline(deadline, pageable)
                .map(this::convertToDTO);
    }
    
    @Override
    public Page<SuiviDTO> getOverdueSuivis(Pageable pageable) {
        LocalDate currentDate = LocalDate.now();
        return suiviRepository.findOverdueSuivis(currentDate, pageable)
                .map(this::convertToDTO);
    }
    
    // ========== MÉTHODES DE GESTION DES STATUTS ==========
    
    @Override
    @Transactional
    public SuiviDTO changeStatut(Long id, StatutSuivi newStatut) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suivi not found with id: " + id));
        
        // Vérifier si le suivi peut être modifié
        if (!suiviValidator.canModifySuivi(suivi)) {
            throw new SuiviBusinessException("Ce suivi ne peut pas être modifié car il est dans un état final");
        }
        
        // Vérifier si la transition de statut est valide
        if (!suiviValidator.isValidStatusTransition(suivi.getStatut(), newStatut)) {
            throw new SuiviBusinessException("Transition de statut non autorisée de " + 
                    suivi.getStatut() + " vers " + newStatut);
        }
        
        suivi.setStatut(newStatut);
        suivi = suiviRepository.save(suivi);
        return convertToDTO(suivi);
    }
    
    @Override
    @Transactional
    public SuiviDTO assignResponsable(Long id, Long responsableId) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suivi not found with id: " + id));
        
        // Vérifier si le suivi peut être modifié
        if (!suiviValidator.canModifySuivi(suivi)) {
            throw new SuiviBusinessException("Ce suivi ne peut pas être modifié car il est dans un état final");
        }
        
        // Vérifier si l'utilisateur peut être assigné
        if (!suiviValidator.canAssignUser(responsableId)) {
            throw new SuiviBusinessException("Cet utilisateur ne peut pas être assigné comme responsable");
        }
        
        // Vérifier que l'utilisateur existe
        User responsable = userRepository.findById(responsableId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + responsableId));
        
        suivi.setResponsable(responsable);
        suivi = suiviRepository.save(suivi);
        return convertToDTO(suivi);
    }
    
    @Override
    @Transactional
    public SuiviDTO removeResponsable(Long id) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suivi not found with id: " + id));
        
        // Vérifier si le suivi peut être modifié
        if (!suiviValidator.canModifySuivi(suivi)) {
            throw new SuiviBusinessException("Ce suivi ne peut pas être modifié car il est dans un état final");
        }
        
        suivi.setResponsable(null);
        suivi = suiviRepository.save(suivi);
        return convertToDTO(suivi);
    }
    
    @Override
    @Transactional
    public SuiviDTO changePriorite(Long id, PrioriteSuivi newPriorite) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suivi not found with id: " + id));
        
        // Vérifier si le suivi peut être modifié
        if (!suiviValidator.canModifySuivi(suivi)) {
            throw new SuiviBusinessException("Ce suivi ne peut pas être modifié car il est dans un état final");
        }
        
        suivi.setPriorite(newPriorite);
        suivi = suiviRepository.save(suivi);
        return convertToDTO(suivi);
    }
    
    // ========== MÉTHODES DE STATISTIQUES ==========
    
    @Override
    public Map<String, Object> getSuiviStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        // Statistiques générales
        stats.put("totalSuivis", suiviRepository.count());
        
        // Statistiques par statut
        Map<StatutSuivi, Long> statutStats = new HashMap<>();
        for (StatutSuivi statut : StatutSuivi.values()) {
            statutStats.put(statut, suiviRepository.countByStatut(statut));
        }
        stats.put("parStatut", statutStats);
        
        // Statistiques par priorité
        Map<PrioriteSuivi, Long> prioriteStats = new HashMap<>();
        for (PrioriteSuivi priorite : PrioriteSuivi.values()) {
            prioriteStats.put(priorite, suiviRepository.countByPriorite(priorite));
        }
        stats.put("parPriorite", prioriteStats);
        
        // Suivis en retard
        LocalDate today = LocalDate.now();
        long overdueSuivis = suiviRepository.findOverdueSuivis(today, Pageable.unpaged()).getTotalElements();
        stats.put("enRetard", overdueSuivis);
        
        // Suivis avec échéance proche (7 jours)
        LocalDate nextWeek = today.plusDays(7);
        long approachingDeadline = suiviRepository.findSuivisWithApproachingDeadline(nextWeek, Pageable.unpaged()).getTotalElements();
        stats.put("echeanceProche", approachingDeadline);
        
        return stats;
    }
    
    @Override
    public Map<StatutSuivi, Long> getCountByStatut() {
        Map<StatutSuivi, Long> counts = new HashMap<>();
        for (StatutSuivi statut : StatutSuivi.values()) {
            counts.put(statut, suiviRepository.countByStatut(statut));
        }
        return counts;
    }
    
    @Override
    public Map<PrioriteSuivi, Long> getCountByPriorite() {
        Map<PrioriteSuivi, Long> counts = new HashMap<>();
        for (PrioriteSuivi priorite : PrioriteSuivi.values()) {
            counts.put(priorite, suiviRepository.countByPriorite(priorite));
        }
        return counts;
    }
    
    @Override
    public long getCountByResponsable(Long responsableId) {
        return suiviRepository.countByResponsableId(responsableId);
    }
    
    // ========== MÉTHODES DE VALIDATION ==========
    
    @Override
    public List<StatutSuivi> getPossibleNextStatuses(Long id) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suivi not found with id: " + id));
        
        return suiviValidator.getPossibleNextStatuses(suivi.getStatut());
    }
    
    @Override
    public boolean canModifySuivi(Long id) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suivi not found with id: " + id));
        
        return suiviValidator.canModifySuivi(suivi);
    }
    
    @Override
    public boolean canDeleteSuivi(Long id) {
        Suivi suivi = suiviRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Suivi not found with id: " + id));
        
        return suiviValidator.canDeleteSuivi(suivi);
    }
}
