// Classe de base pour tous les éléments d'inventaire
package main.data.inventaire;

public abstract class InventaireItem {
    protected String nom;
    protected int quantite;
    
    public InventaireItem(String nom, int quantite) {
        this.nom = nom;
        this.quantite = quantite;
    }
    
    public String getNom() {
        return nom;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public void incrementer(int montant) {
        this.quantite += montant;
    }
    
    public boolean decrementer(int montant) {
        if (this.quantite >= montant) {
            this.quantite -= montant;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return nom + " - " + quantite;
    }
}
