package main.data.inventaire;

/**
 * Représente un ingrédient dans l'inventaire du restaurant
 */
public class Ingredient {
    private String nom;
    private int quantite;
    private double prixUnitaire;
    private String categorie;
    
    /**
     * Constructeur d'un ingrédient
     */
    public Ingredient(String nom, int quantite, double prixUnitaire, String categorie) {
        this.nom = nom;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.categorie = categorie;
    }
    
    /**
     * Constructeur à partir d'un Article
     */
    public Ingredient(Article article, String categorie) {
        this.nom = article.getNom();
        this.quantite = article.getQuantite();
        this.prixUnitaire = article.getPrixUnitaire();
        this.categorie = categorie;
    }
    
    /**
     * Retire une quantité de l'ingrédient
     */
    public boolean utiliser(int quantite) {
        if (this.quantite >= quantite) {
            this.quantite -= quantite;
            return true;
        }
        return false;
    }
    
    /**
     * Ajoute une quantité à l'ingrédient
     */
    public void ajouter(int quantite) {
        this.quantite += quantite;
    }
    
    // Getters et setters
    
    public String getNom() {
        return nom;
    }
    
    public int getQuantite() {
        return quantite;
    }
    
    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }
    
    public double getPrixUnitaire() {
        return prixUnitaire;
    }
    
    public String getCategorie() {
        return categorie;
    }
    
    @Override
    public String toString() {
        return nom + " - " + categorie + " : " + quantite + " unités (Prix: " + prixUnitaire + "€)";
    }
}