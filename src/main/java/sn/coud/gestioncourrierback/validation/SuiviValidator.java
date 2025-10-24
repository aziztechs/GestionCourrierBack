package sn.coud.gestioncourrierback.validation;

import org.springframework.stereotype.Component;
import sn.coud.gestioncourrierback.dto.SuiviDTO;
import sn.coud.gestioncourrierback.model.StatutSuivi;
import sn.coud.gestioncourrierback.model.Suivi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Validateur métier pour les règles de validation avancées des suivis.
 */
@Component
public class SuiviValidator {

    /**
     * Transitions de statut autorisées.
     * Définit quels changements de statut sont permis.
     */
    private static final Set<StatutSuivi> STATUTS_FINAUX = Set.of(StatutSuivi.TERMINE, StatutSuivi.ANNULE);
    
    /**
     * Valide un SuiviDTO selon les règles métier.
     * 
     * @param suiviDTO le DTO à valider
     * @return la liste des erreurs de validation (vide si valide)
     */
    public List<String> validateSuiviDTO(SuiviDTO suiviDTO) {
        List<String> errors = new ArrayList<>();
        
        // Validation des dates
        validateDates(suiviDTO, errors);
        
        // Validation de la cohérence des données
        validateDataConsistency(suiviDTO, errors);
        
        return errors;
    }
    
    /**
     * Valide une transition de statut.
     * 
     * @param currentStatut le statut actuel
     * @param newStatut le nouveau statut souhaité
     * @return true si la transition est autorisée
     */
    public boolean isValidStatusTransition(StatutSuivi currentStatut, StatutSuivi newStatut) {
        if (currentStatut == null || newStatut == null) {
            return false;
        }
        
        // Si le statut ne change pas, c'est toujours valide
        if (currentStatut == newStatut) {
            return true;
        }
        
        // Une fois terminé ou annulé, on ne peut plus changer le statut
        if (STATUTS_FINAUX.contains(currentStatut)) {
            return false;
        }
        
        // Toutes les autres transitions sont autorisées
        return true;
    }
    
    /**
     * Valide qu'un suivi peut être modifié.
     * 
     * @param suivi le suivi à vérifier
     * @return true si le suivi peut être modifié
     */
    public boolean canModifySuivi(Suivi suivi) {
        if (suivi == null) {
            return false;
        }
        
        // Les suivis terminés ou annulés ne peuvent pas être modifiés
        return !STATUTS_FINAUX.contains(suivi.getStatut());
    }
    
    /**
     * Valide qu'un suivi peut être supprimé.
     * 
     * @param suivi le suivi à vérifier
     * @return true si le suivi peut être supprimé
     */
    public boolean canDeleteSuivi(Suivi suivi) {
        if (suivi == null) {
            return false;
        }
        
        // On peut supprimer un suivi seulement s'il n'est pas terminé
        return suivi.getStatut() != StatutSuivi.TERMINE;
    }
    
    /**
     * Valide les dates du suivi.
     */
    private void validateDates(SuiviDTO suiviDTO, List<String> errors) {
        LocalDate today = LocalDate.now();
        
        // La date du suivi ne peut pas être dans le futur
        if (suiviDTO.getDate() != null && suiviDTO.getDate().isAfter(today)) {
            errors.add("La date du suivi ne peut pas être dans le futur");
        }
        
        // La date d'échéance ne peut pas être dans le passé (sauf si le suivi est terminé)
        if (suiviDTO.getDateEcheance() != null && 
            suiviDTO.getDateEcheance().isBefore(today) && 
            suiviDTO.getStatut() != StatutSuivi.TERMINE &&
            suiviDTO.getStatut() != StatutSuivi.ANNULE) {
            errors.add("La date d'échéance ne peut pas être dans le passé pour un suivi actif");
        }
        
        // La date d'échéance doit être postérieure à la date du suivi
        if (suiviDTO.getDate() != null && suiviDTO.getDateEcheance() != null &&
            suiviDTO.getDateEcheance().isBefore(suiviDTO.getDate())) {
            errors.add("La date d'échéance doit être postérieure à la date du suivi");
        }
    }
    
    /**
     * Valide la cohérence des données.
     */
    private void validateDataConsistency(SuiviDTO suiviDTO, List<String> errors) {
        // Un suivi terminé doit avoir une description
        if (suiviDTO.getStatut() == StatutSuivi.TERMINE && 
            (suiviDTO.getDescription() == null || suiviDTO.getDescription().trim().isEmpty())) {
            errors.add("Un suivi terminé doit avoir une description");
        }
        
        // Un suivi avec une priorité urgente devrait avoir une date d'échéance
        if (suiviDTO.getPriorite() != null && 
            suiviDTO.getPriorite().getNiveau() >= 3 && // HAUTE ou URGENTE
            suiviDTO.getDateEcheance() == null &&
            suiviDTO.getStatut() != StatutSuivi.TERMINE &&
            suiviDTO.getStatut() != StatutSuivi.ANNULE) {
            errors.add("Un suivi de priorité haute ou urgente devrait avoir une date d'échéance");
        }
    }
    
    /**
     * Valide qu'un utilisateur peut être assigné comme responsable.
     * 
     * @param userId l'ID de l'utilisateur
     * @return true si l'utilisateur peut être assigné
     */
    public boolean canAssignUser(Long userId) {
        // Pour l'instant, tous les utilisateurs peuvent être assignés
        // Cette méthode peut être étendue avec des règles plus complexes
        // (ex: vérifier les rôles, la disponibilité, etc.)
        return userId != null;
    }
    
    /**
     * Obtient les statuts suivants possibles pour un statut donné.
     * 
     * @param currentStatut le statut actuel
     * @return la liste des statuts suivants possibles
     */
    public List<StatutSuivi> getPossibleNextStatuses(StatutSuivi currentStatut) {
        List<StatutSuivi> possibleStatuses = new ArrayList<>();
        
        if (currentStatut == null) {
            possibleStatuses.add(StatutSuivi.EN_COURS);
            return possibleStatuses;
        }
        
        // Si le statut est final, aucune transition n'est possible
        if (STATUTS_FINAUX.contains(currentStatut)) {
            return possibleStatuses;
        }
        
        // Ajouter tous les statuts possibles sauf le statut actuel
        for (StatutSuivi statut : StatutSuivi.values()) {
            if (statut != currentStatut) {
                possibleStatuses.add(statut);
            }
        }
        
        return possibleStatuses;
    }
}
