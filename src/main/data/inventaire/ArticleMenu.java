package main.data.inventaire;

import java.util.ArrayList;
import java.util.List;

public class ArticleMenu {
    private String nom;
    private double prix;
    private String categorie;  // Entrée, Plat, Dessert, Boisson
    private List<String> ingredients;
    
    public ArticleMenu(String nom, double prix, String categorie) {
        this.nom = nom;
        this.prix = prix;
        this.categorie = categorie;
        this.ingredients = new ArrayList<>();
    }
    
    public String getNom() {
        return nom;
    }
    
    public double getPrix() {
        return prix;
    }
    
    public void setPrix(double prix) {
        this.prix = prix;
    }
    
    public String getCategorie() {
        return categorie;
    }
    
    public List<String> getIngredients() {
        return ingredients;
    }
    
    public void ajouterIngredient(String ingredient) {
        ingredients.add(ingredient);
    }
    
    @Override
    public String toString() {
        return nom + " " + prix + "€";
    }
}