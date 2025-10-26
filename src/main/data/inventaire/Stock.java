package main.data.inventaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un stock d'articles dans le restaurant
 */
public class Stock {
    private String categorie;
    private List<Article> articles;
    
    /**
     * Constructeur du stock
     */
    public Stock(String categorie) {
        this.categorie = categorie;
        this.articles = new ArrayList<>();
    }
    
    /**
     * Ajoute un nouvel article au stock
     */
    public void ajouterArticle(Article article) {
        articles.add(article);
    }
    
    /**
     * Retire un article du stock
     */
    public boolean retirerArticle(Article article) {
        return articles.remove(article);
    }
    
    /**
     * Retire une quantité d'un article spécifique
     */
    public boolean retirerQuantite(String nomArticle, int quantite) {
        for (Article article : articles) {
            if (article.getNom().equals(nomArticle)) {
                return article.retirerQuantite(quantite);
            }
        }
        return false;
    }
    
    /**
     * Ajoute une quantité à un article spécifique
     */
    public boolean ajouterQuantite(String nomArticle, int quantite) {
        for (Article article : articles) {
            if (article.getNom().equals(nomArticle)) {
                article.ajouterQuantite(quantite);
                return true;
            }
        }
        return false;
    }
    
    /**
     * Récupère un article par son nom
     */
    public Article getArticle(String nomArticle) {
        for (Article article : articles) {
            if (article.getNom().equals(nomArticle)) {
                return article;
            }
        }
        return null;
    }
    
    /**
     * Récupère la liste de tous les articles du stock
     */
    public List<Article> getArticles() {
        return articles;
    }
    
    /**
     * Retourne la catégorie du stock
     */
    public String getCategorie() {
        return categorie;
    }
    
    /**
     * Vérifie si des articles sont sous leur seuil d'alerte
     */
    public List<Article> getArticlesSousSeuil() {
        List<Article> articlesAlerte = new ArrayList<>();
        for (Article article : articles) {
            if (article.getQuantite() <= article.getSeuilAlerte()) {
                articlesAlerte.add(article);
            }
        }
        return articlesAlerte;
    }
}