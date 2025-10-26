package main.data.inventaire;

/**
 * Classe représentant un article dans l'inventaire du restaurant
 */
public class Article {
    private String nom;
    private int quantite;
    private int seuilAlerte;
    private double prixUnitaire;
    private String unite; // ex: "kg", "pièce", "litre"
    
    /**
     * Constructeur d'un article
     */
    public Article(String nom, int quantite, int seuilAlerte, double prixUnitaire, String unite) {
        this.nom = nom;
        this.quantite = quantite;
        this.seuilAlerte = seuilAlerte;
        this.prixUnitaire = prixUnitaire;
        this.unite = unite;
    }
    
    /**
     * Retire une quantité de l'article
     */
    public boolean retirerQuantite(int qte) {
        if (qte <= 0) return false;
        
        if (quantite >= qte) {
            quantite -= qte;
            return true;
        }
        return false;
    }
    
    /**
     * Ajoute une quantité à l'article
     */
    public void ajouterQuantite(int qte) {
        if (qte > 0) {
            quantite += qte;
        }
    }
    
    /**
     * Vérifie si l'article est en quantité suffisante
     */
    public boolean estDisponible(int qteRequise) {
        return quantite >= qteRequise;
    }
    
    /**
     * Vérifie si l'article est sous le seuil d'alerte
     */
    public boolean estSousSeuil() {
        return quantite <= seuilAlerte;
    }
    
    /**
     * Calcule le prix total de l'article en stock
     */
    public double calculerPrixTotal() {
        return quantite * prixUnitaire;
    }
    
    // Getters et setters
    
    public String getNom() {
        return nom;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = Math.max(0, quantite);
    }
    
    public int getSeuilAlerte() {
        return seuilAlerte;
    }
    
    public void setSeuilAlerte(int seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
    }
    
    public double getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public void setPrixUnitaire(double prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }
    
    public String getUnite() {
        return unite;
    }
    
    @Override
    public String toString() {
        return nom + ": " + quantite + " " + unite + " (Seuil: " + seuilAlerte + ")";
    }
}