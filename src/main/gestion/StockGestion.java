package main.gestion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.data.inventaire.Article;
import main.data.inventaire.Ingredient;
import main.data.inventaire.Stock;

/**
 * Contrôleur pour la gestion des stocks du restaurant
 */
public class StockGestion {
    private Map<String, Stock> stocks;
    private RestaurantGestion restaurantController;
    
    /**
     * Constructeur qui initialise les stocks de base
     */
    public StockGestion() {
        this.stocks = new HashMap<>();
        initialiserStocksDeBase();
    }
    
    /**
     * Constructeur avec une référence au contrôleur principal
     */
    public StockGestion(RestaurantGestion restaurantController) {
        this.restaurantController = restaurantController;
        this.stocks = new HashMap<>();
        initialiserStocksDeBase();
    }
    
    /**
     * Initialise les stocks de base du restaurant avec les ingrédients du menu
     */
    private void initialiserStocksDeBase() {
        // Créer les catégories de base
        creerCategorieStock("Ingrédients");
        creerCategorieStock("Boissons");
        
        // Ajouter les ingrédients du menu
        // Ingrédients pour les plats
        ajouterArticle("Ingrédients", "Laitue", 15, 5, 0.9, "pièce");
        ajouterArticle("Ingrédients", "Tomates", 20, 5, 1.5, "kg");
        ajouterArticle("Ingrédients", "Mozzarella", 10, 3, 3.5, "kg");
        ajouterArticle("Ingrédients", "Croutons", 20, 5, 2.0, "sachet");
        ajouterArticle("Ingrédients", "Sauce césar", 8, 2, 3.5, "bouteille");
        ajouterArticle("Ingrédients", "Viande hachée", 12, 3, 10.0, "kg");
        ajouterArticle("Ingrédients", "Steak de boeuf", 10, 2, 15.0, "kg");
        ajouterArticle("Ingrédients", "Pommes de terre", 30, 10, 0.8, "kg");
        ajouterArticle("Ingrédients", "Fruits de saison", 15, 5, 2.5, "kg");
        ajouterArticle("Ingrédients", "Chocolat noir", 5, 2, 4.0, "tablette");
        ajouterArticle("Ingrédients", "Poulet", 15, 3, 8.5, "kg");
        ajouterArticle("Ingrédients", "Fromage", 15, 3, 4.5, "kg");
        
        // Boissons
        ajouterArticle("Boissons", "Coca-Cola", 25, 10, 1.5, "bouteille");
        ajouterArticle("Boissons", "IceTea", 20, 8, 1.2, "bouteille");
        ajouterArticle("Boissons", "Eau minérale", 30, 10, 0.9, "bouteille");
        ajouterArticle("Boissons", "Vin rouge", 15, 3, 8.0, "bouteille");
    }
    
    /**
     * Crée une nouvelle catégorie de stock
     */
    public boolean creerCategorieStock(String categorie) {
        if (!stocks.containsKey(categorie)) {
            stocks.put(categorie, new Stock(categorie));
            return true;
        }
        return false;
    }
    
    /**
     * Ajoute un nouvel article dans une catégorie
     */
    public boolean ajouterArticle(String categorie, String nom, int quantite, 
                                int seuilAlerte, double prixUnitaire, String unite) {
        Stock stock = stocks.get(categorie);
        if (stock == null) {
            return false;
        }
        
        // Vérifier si l'article existe déjà
        Article existant = stock.getArticle(nom);
        if (existant != null) {
            // Si l'article existe, on ajoute juste la quantité
            existant.ajouterQuantite(quantite);
            return true;
        }
        
        // Sinon, créer un nouvel article
        Article nouvelArticle = new Article(nom, quantite, seuilAlerte, prixUnitaire, unite);
        stock.ajouterArticle(nouvelArticle);
        return true;
    }
    
    /**
     * Retire une quantité d'un article spécifique
     */
    public boolean retirerQuantite(String categorie, String nomArticle, int quantite) {
        Stock stock = stocks.get(categorie);
        if (stock == null) {
            return false;
        }
        
        return stock.retirerQuantite(nomArticle, quantite);
    }
    
    /**
     * Ajoute une quantité à un article spécifique
     */
    public boolean ajouterQuantite(String categorie, String nomArticle, int quantite) {
        Stock stock = stocks.get(categorie);
        if (stock == null) {
            return false;
        }
        
        return stock.ajouterQuantite(nomArticle, quantite);
    }
    
    /**
     * Vérifie si un article est disponible en quantité suffisante
     */
    public boolean estArticleDisponible(String categorie, String nomArticle, int quantiteRequise) {
        Stock stock = stocks.get(categorie);
        if (stock == null) {
            return false;
        }
        
        Article article = stock.getArticle(nomArticle);
        if (article == null) {
            return false;
        }
        
        return article.estDisponible(quantiteRequise);
    }
    
    /**
     * Vérifie si un ingrédient est disponible en quantité suffisante
     */
    public boolean verifierStock(String nomIngredient, int quantiteRequise) {
        // Déterminer la catégorie de l'ingrédient
        String categorie = trouverCategorieIngredient(nomIngredient);
        
        // Vérifier la disponibilité
        return estArticleDisponible(categorie, nomIngredient, quantiteRequise);
    }

    /**
     * Réduit la quantité d'un ingrédient dans le stock
     */
    public boolean reduireStock(String nomIngredient, int quantite) {
        // Déterminer la catégorie de l'ingrédient
        String categorie = trouverCategorieIngredient(nomIngredient);
        
        // Retirer la quantité spécifiée
        return retirerQuantite(categorie, nomIngredient, quantite);
    }
    
    /**
     * Récupère tous les articles sous le seuil d'alerte
     */
    public Map<String, List<Article>> getArticlesSousSeuil() {
        Map<String, List<Article>> alertes = new HashMap<>();
        
        for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
            String categorie = entry.getKey();
            Stock stock = entry.getValue();
            
            List<Article> articlesAlerte = stock.getArticlesSousSeuil();
            if (!articlesAlerte.isEmpty()) {
                alertes.put(categorie, articlesAlerte);
            }
        }
        
        return alertes;
    }
    
    /**
     * Retourne tous les stocks du restaurant
     */
    public Map<String, Stock> getTousLesStocks() {
        return stocks;
    }
    
    /**
     * Récupère un stock par sa catégorie
   
     */
    public Stock getStock(String categorie) {
        return stocks.get(categorie);
    }
    
    /**
     * Calcule la valeur totale de tous les stocks
   
     */
    public double calculerValeurTotaleStocks() {
        double total = 0.0;
        
        for (Stock stock : stocks.values()) {
            for (Article article : stock.getArticles()) {
                total += article.calculerPrixTotal();
            }
        }
        
        return total;
    }
    
    /**
     * Obtient la liste de toutes les catégories de stock
     */
    public List<String> getCategories() {
        return new ArrayList<>(stocks.keySet());
    }
    
    /**
     * Récupère tous les ingrédients du stock pour l'affichage
     */
    public List<Ingredient> getStockItems() {
        List<Ingredient> ingredients = new ArrayList<>();
        
        for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
            String categorie = entry.getKey();
            Stock stock = entry.getValue();
            
            for (Article article : stock.getArticles()) {
                ingredients.add(new Ingredient(article, categorie));
            }
        }
        
        return ingredients;
    }
    
    /**
     * Commander un ingrédient et l'ajouter au stock
     */
    public boolean commander(String nomIngredient, int quantite, double coutTotal) {
        if (restaurantController == null || !restaurantController.peutPayer(coutTotal)) {
            return false;
        }
        
        // Déterminer la catégorie de l'ingrédient
        String categorie = trouverCategorieIngredient(nomIngredient);
        
        // Effectuer le paiement
        if (restaurantController.effectuerPaiement(coutTotal)) {
            // Enregistrer l'achat dans les statistiques
            if (restaurantController.getStatsController() != null) {
                restaurantController.getStatsController().enregistrerAchatStock(coutTotal);
            }
            
            // Ajouter l'ingrédient au stock
            boolean resultat = false;
            if (ajouterQuantite(categorie, nomIngredient, quantite)) {
                resultat = true;
            } else {
                // Si l'ingrédient n'existe pas encore, le créer
                double prixUnitaire = coutTotal / quantite;
                resultat = ajouterArticle(categorie, nomIngredient, quantite, 5, prixUnitaire, "unité");
            }
            
            // Forcer la mise à jour de l'interface
            if (resultat) {
                restaurantController.forceUIUpdate();
                // Afficher un message de confirmation (optionnel)
                System.out.println("Achat réussi: " + nomIngredient + " (" + quantite + " unités) pour " + coutTotal + "€");
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Trouve la catégorie d'un ingrédient par son nom
     */
    private String trouverCategorieIngredient(String nomIngredient) {
        for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
            String categorie = entry.getKey();
            Stock stock = entry.getValue();
            
            if (stock.getArticle(nomIngredient) != null) {
                return categorie;
            }
        }
        
        // Déterminer la catégorie par défaut en fonction du nom
        if (nomIngredient.contains("Coca") || nomIngredient.contains("Tea") || 
            nomIngredient.contains("Vin") || nomIngredient.contains("Eau")) {
            return "Boissons";
        } else {
            return "Ingrédients";
        }
    }
    
    /**
     * Définit le contrôleur principal du restaurant
     */
    public void setRestaurantController(RestaurantGestion restaurantController) {
        this.restaurantController = restaurantController;
    }
    
    /**
     * Obtient le contrôleur principal du restaurant
     */
    public RestaurantGestion getRestaurantController() {
        return restaurantController;
    }
}