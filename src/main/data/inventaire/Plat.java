package main.data.inventaire;

public class Plat {
    private String nom;
    private double prix;
    private String description;
    private String categorie;
    private boolean disponible;
    
    public Plat(String nom, double prix, String description, String categorie) {
        this.nom = nom;
        this.prix = prix;
        this.description = description;
        this.categorie = categorie;
        this.disponible = true;
    }
    
    // Getters et setters
    public String getNom() {
        return nom;
    }
    
    public double getPrix() {
        return prix;
    }
    
    public void setPrix(double prix) {
        this.prix = prix;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getCategorie() {
        return categorie;
    }
    
    public boolean isDisponible() {
        return disponible;
    }
    
    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
    
    @Override
    public String toString() {
        return nom + " - " + prix + "€";
    }
}