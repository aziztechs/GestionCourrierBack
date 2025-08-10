package sn.coud.gestioncourrierback.model;

/**
 * Enumération représentant les différents statuts d'un suivi.
 */
public enum StatutSuivi {
    /**
     * Le suivi est en cours de traitement
     */
    EN_COURS("En cours"),
    
    /**
     * Le suivi est terminé
     */
    TERMINE("Terminé"),
    
    /**
     * Le suivi est en attente
     */
    EN_ATTENTE("En attente"),
    
    /**
     * Le suivi a été annulé
     */
    ANNULE("Annulé");
    
    private final String libelle;
    
    StatutSuivi(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
}
