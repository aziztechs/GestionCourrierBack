package sn.coud.gestioncourrierback.model;

/**
 * Enumération représentant les différents niveaux de priorité d'un suivi.
 */
public enum PrioriteSuivi {
    /**
     * Priorité basse
     */
    BASSE("Basse", 1),
    
    /**
     * Priorité normale (par défaut)
     */
    NORMALE("Normale", 2),
    
    /**
     * Priorité haute
     */
    HAUTE("Haute", 3),
    
    /**
     * Priorité urgente
     */
    URGENTE("Urgente", 4);
    
    private final String libelle;
    private final int niveau;
    
    PrioriteSuivi(String libelle, int niveau) {
        this.libelle = libelle;
        this.niveau = niveau;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public int getNiveau() {
        return niveau;
    }
    
    @Override
    public String toString() {
        return libelle;
    }
    
    /**
     * Compare deux priorités selon leur niveau
     * @param autre la priorité à comparer
     * @return true si cette priorité est plus élevée que l'autre
     */
    public boolean estPlusEleveeQue(PrioriteSuivi autre) {
        return this.niveau > autre.niveau;
    }
}
