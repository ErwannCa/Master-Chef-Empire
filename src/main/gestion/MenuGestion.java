package main.gestion;

import java.util.List;
import java.util.ArrayList;

import main.data.inventaire.ArticleMenu;
import main.data.inventaire.*;

public class MenuGestion {
    private Menu menu;
    
    public MenuGestion() {
        this.menu = new Menu();
    }
    
    public List<ArticleMenu> getAllArticles() {
        return menu.getArticles();
    }
    
    public List<ArticleMenu> getArticlesByCategory(String category) {
        return menu.getArticlesParCategorie(category);
    }
    
    public List<String> getCategories() {
        return menu.getCategories();
    }
    
    public List<Plat> getPlatsDisponibles() {
        List<Plat> disponibles = new ArrayList<>();
        
        // Convertir les ArticleMenu en Plat si nécessaire
        for (ArticleMenu article : menu.getArticles()) {
            // Supposons que chaque ArticleMenu peut être transformé en Plat
            Plat plat = new Plat(article.getNom(), article.getPrix(), 
                               "Description du plat", article.getCategorie());
            
            disponibles.add(plat);
        }
        
        return disponibles;
    }
    
    public void addArticle(String nom, double prix, String categorie) {
        ArticleMenu article = new ArticleMenu(nom, prix, categorie);
        menu.ajouterArticle(article);
    }
    
    public void removeArticle(ArticleMenu article) {
        menu.retirerArticle(article);
    }
    
    public void removeArticleByIndex(int index, String category) {
        List<ArticleMenu> articles;
        
        if ("Toutes".equals(category)) {
            articles = menu.getArticles();
        } else {
            articles = menu.getArticlesParCategorie(category);
        }
        
        if (index >= 0 && index < articles.size()) {
            menu.retirerArticle(articles.get(index));
        }
    }
    
    public void updateArticle(int index, String nom, double prix, String category) {
        List<ArticleMenu> articles;
        
        if ("Toutes".equals(category)) {
            articles = menu.getArticles();
        } else {
            articles = menu.getArticlesParCategorie(category);
        }
        
        if (index >= 0 && index < articles.size()) {
            ArticleMenu article = articles.get(index);
            // Comme on ne peut pas modifier directement l'article, on le supprime et on en ajoute un nouveau
            menu.retirerArticle(article);
            addArticle(nom, prix, article.getCategorie());
        }
    }
}