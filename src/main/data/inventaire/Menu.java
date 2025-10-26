package main.data.inventaire;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private List<ArticleMenu> articles;
    private Map<String, List<ArticleMenu>> categoriesArticles;
    
    public Menu() {
        this.articles = new ArrayList<>();
        this.categoriesArticles = new HashMap<>();
        initialiserMenu();
    }
    
    private void initialiserMenu() {
        ajouterArticle(new ArticleMenu("Salade César", 5.0, "Entrée"));
        ajouterArticle(new ArticleMenu("Tomate mozzarella", 4.5, "Entrée"));
        ajouterArticle(new ArticleMenu("Spaghettis bolognaise", 10.0, "Plat"));
        ajouterArticle(new ArticleMenu("Steak frites", 12.0, "Plat"));
        ajouterArticle(new ArticleMenu("Salade de fruits", 6.0, "Dessert"));
        ajouterArticle(new ArticleMenu("Fondant au chocolat", 7.0, "Dessert"));
        ajouterArticle(new ArticleMenu("Coca-Cola", 3.0, "Boisson"));
        ajouterArticle(new ArticleMenu("Ice Tea", 3.0, "Boisson"));
    }
    
    public void ajouterArticle(ArticleMenu article) {
        articles.add(article);
        
        // Ajouter à la catégorie correspondante
        String categorie = article.getCategorie();
        if (!categoriesArticles.containsKey(categorie)) {
            categoriesArticles.put(categorie, new ArrayList<>());
        }
        categoriesArticles.get(categorie).add(article);
    }
    
    public void retirerArticle(ArticleMenu article) {
        articles.remove(article);
        
        // Retirer de la catégorie
        String categorie = article.getCategorie();
        if (categoriesArticles.containsKey(categorie)) {
            categoriesArticles.get(categorie).remove(article);
        }
    }
    
    public List<ArticleMenu> getArticles() {
        return articles;
    }
    
    public List<ArticleMenu> getArticlesParCategorie(String categorie) {
        return categoriesArticles.getOrDefault(categorie, new ArrayList<>());
    }
    
    public List<String> getCategories() {
        return new ArrayList<>(categoriesArticles.keySet());
    }
}