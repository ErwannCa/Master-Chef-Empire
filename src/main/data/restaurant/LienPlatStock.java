package main.data.restaurant;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import main.gestion.StockGestion;

/**
 * Classe gérant le lien entre les plats du menu et les ingrédients du stock
 */
public class LienPlatStock {
    
    // Structure pour représenter un ingrédient et sa quantité
    public static class IngredientQuantite {
        private String nomIngredient;
        private int quantite;
        
        public IngredientQuantite(String nomIngredient, int quantite) {
            this.nomIngredient = nomIngredient;
            this.quantite = quantite;
        }
        
        public String getNomIngredient() {
            return nomIngredient;
        }
        
        public int getQuantite() {
            return quantite;
        }
    }
    
    // Map liant chaque plat à ses ingrédients
    private static final Map<String, List<IngredientQuantite>> recettesPlats = new HashMap<>();
    
    // Initialisation des recettes
    static {
        // Entrées
        List<IngredientQuantite> saladeCesar = new ArrayList<>();
        saladeCesar.add(new IngredientQuantite("Laitue", 1));
        saladeCesar.add(new IngredientQuantite("Sauce césar", 1));
        saladeCesar.add(new IngredientQuantite("Fromage", 1));
        recettesPlats.put("Salade César", saladeCesar);
        
        List<IngredientQuantite> tomateMozza = new ArrayList<>();
        tomateMozza.add(new IngredientQuantite("Tomates", 1));
        tomateMozza.add(new IngredientQuantite("Mozzarella", 1));
        recettesPlats.put("Tomate mozzarella", tomateMozza);
        
        List<IngredientQuantite> soupeOignon = new ArrayList<>();
        soupeOignon.add(new IngredientQuantite("Oignon", 3));
        soupeOignon.add(new IngredientQuantite("Fromage", 1));
        recettesPlats.put("Soupe à l'oignon", soupeOignon);
        
        List<IngredientQuantite> assietteCharcut = new ArrayList<>();
        assietteCharcut.add(new IngredientQuantite("Jambon", 4));
        recettesPlats.put("Assiette de charcuterie", assietteCharcut);
        
        // Plats
        List<IngredientQuantite> bolo = new ArrayList<>();
        bolo.add(new IngredientQuantite("Pâtes", 1));
        bolo.add(new IngredientQuantite("Tomates", 1));
        bolo.add(new IngredientQuantite("Viande hachée", 1));
        recettesPlats.put("Spaghettis bolognaise", bolo);
        
        List<IngredientQuantite> steakFrite = new ArrayList<>();
        steakFrite.add(new IngredientQuantite("Pommes de terre", 2));
        steakFrite.add(new IngredientQuantite("Steak de boeuf", 1));
        recettesPlats.put("Steak frites", steakFrite);
        
        List<IngredientQuantite> rissotoChampi = new ArrayList<>();
        rissotoChampi.add(new IngredientQuantite("Riz", 1));
        rissotoChampi.add(new IngredientQuantite("Champignon", 2));
        rissotoChampi.add(new IngredientQuantite("Fromage", 1));
        recettesPlats.put("Risotto aux champignons", rissotoChampi);
        
        List<IngredientQuantite> pouletLegume = new ArrayList<>();
        pouletLegume.add(new IngredientQuantite("Poulet", 1));
        pouletLegume.add(new IngredientQuantite("Légume de saisons", 1));
        recettesPlats.put("Poulet rôti et légumes", pouletLegume);
        
        List<IngredientQuantite> saumonPatate = new ArrayList<>();
        saumonPatate.add(new IngredientQuantite("Saumon", 1));
        saumonPatate.add(new IngredientQuantite("Pommes de terre", 1));
        recettesPlats.put("Filet de saumon et patates sautés", saumonPatate);
        
        // Desserts
        List<IngredientQuantite> fondantChoco = new ArrayList<>();
        fondantChoco.add(new IngredientQuantite("Chocolat", 1));
        fondantChoco.add(new IngredientQuantite("Beurre", 1));
        fondantChoco.add(new IngredientQuantite("Sucre", 1));
        fondantChoco.add(new IngredientQuantite("Farine", 1));
        recettesPlats.put("Fondant au chocolat", fondantChoco);
        
        List<IngredientQuantite> saladeFruit = new ArrayList<>();
        saladeFruit.add(new IngredientQuantite("Fruits de saison", 3));
        recettesPlats.put("Salade de fruits", saladeFruit);
        
        List<IngredientQuantite> cremeBrule = new ArrayList<>();
        cremeBrule.add(new IngredientQuantite("Oeuf", 2));
        cremeBrule.add(new IngredientQuantite("Beurre", 1));
        cremeBrule.add(new IngredientQuantite("Sucre", 1));
        cremeBrule.add(new IngredientQuantite("Vanille", 1));
        recettesPlats.put("Crème brûlée", cremeBrule);
        
        List<IngredientQuantite> mousseChoco = new ArrayList<>();
        mousseChoco.add(new IngredientQuantite("Oeuf", 2));
        mousseChoco.add(new IngredientQuantite("Chocolat noir", 1));
        mousseChoco.add(new IngredientQuantite("Sucre", 1));
        recettesPlats.put("Mousse au chocolat", mousseChoco);
        
        List<IngredientQuantite> tarteTatin = new ArrayList<>();
        tarteTatin.add(new IngredientQuantite("Farine", 1));
        tarteTatin.add(new IngredientQuantite("Beurre", 1));
        tarteTatin.add(new IngredientQuantite("Pomme", 3));
        tarteTatin.add(new IngredientQuantite("Sucre", 1));
        recettesPlats.put("Tarte Tatin", tarteTatin);
        
        // Boissons
        List<IngredientQuantite> iceTea = new ArrayList<>();
        iceTea.add(new IngredientQuantite("Ice Tea", 1));
        recettesPlats.put("IceTea", iceTea);
        
        List<IngredientQuantite> cocaCola = new ArrayList<>();
        cocaCola.add(new IngredientQuantite("Coca-Cola", 1));
        recettesPlats.put("Coca-Cola", cocaCola);
        
        List<IngredientQuantite> cafe = new ArrayList<>();
        cafe.add(new IngredientQuantite("Café", 1));
        recettesPlats.put("Café", cafe);
    }
    
    /**
     * Récupère les ingrédients pour un plat donné
     */
    public static List<IngredientQuantite> getIngredientsForPlat(String nomPlat) {
        return recettesPlats.get(nomPlat);
    }
    
    public static List<IngredientQuantite> getIngredientsManquants(String nomPlat, StockGestion stockController) {
        List<IngredientQuantite> ingredients = getIngredientsForPlat(nomPlat);
        List<IngredientQuantite> manquants = new ArrayList<>();
        
        if (ingredients == null) return manquants;
        
        for (IngredientQuantite ingredient : ingredients) {
            if (!stockController.verifierStock(ingredient.getNomIngredient(), ingredient.getQuantite())) {
                manquants.add(ingredient);
            }
        }
        
        return manquants;
    }
    
    /**
     * Vérifie si tous les ingrédients pour un plat sont disponibles en stock
     */
    public static boolean verifierDisponibilite(String nomPlat, StockGestion stockController) {
        List<IngredientQuantite> ingredients = getIngredientsForPlat(nomPlat);
        if (ingredients == null) return false;
        
        for (IngredientQuantite ingredient : ingredients) {
            if (!stockController.verifierStock(ingredient.getNomIngredient(), ingredient.getQuantite())) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Déduit les ingrédients du stock pour un plat commandé
     */
    public static boolean consommerIngredients(String nomPlat, StockGestion stockController) {
        if (!verifierDisponibilite(nomPlat, stockController)) {
            return false;
        }
        
        List<IngredientQuantite> ingredients = getIngredientsForPlat(nomPlat);
        for (IngredientQuantite ingredient : ingredients) {
            stockController.reduireStock(ingredient.getNomIngredient(), ingredient.getQuantite());
        }
        return true;
    }
    
    /**
     * Renvoie une description des ingrédients pour l'interface utilisateur
     */
    public static String getDescriptionIngredients(String nomPlat) {
        List<IngredientQuantite> ingredients = getIngredientsForPlat(nomPlat);
        if (ingredients == null) return "Aucun ingrédient défini";
        
        StringBuilder sb = new StringBuilder();
        for (IngredientQuantite ingredient : ingredients) {
            sb.append("- ").append(ingredient.getNomIngredient())
              .append(" (").append(ingredient.getQuantite()).append(" unité")
              .append(ingredient.getQuantite() > 1 ? "s" : "").append(")\n");
        }
        return sb.toString();
    }
    
    /**
     * Récupère tous les plats d'une catégorie spécifique
     */
    public static List<String> getPlatsParCategorie(String categorie) {
        List<String> plats = new ArrayList<>();
        
        for (String plat : recettesPlats.keySet()) {
            if (categorieCorrespondante(plat, categorie)) {
                plats.add(plat);
            }
        }
        
        return plats;
    }
    
    /**
     * Détermine si un plat appartient à une catégorie
     */
    private static boolean categorieCorrespondante(String plat, String categorie) {
        switch (categorie.toLowerCase()) {
            case "entrée":
                return plat.equals("Salade César") || plat.equals("Tomate mozzarella") ||
                       plat.equals("Soupe à l'oignon") || 
                       plat.equals("Assiette de charcuterie");
            case "plat":
                return plat.equals("Spaghetti bolognaise") || plat.equals("Steak frites") ||
                       plat.equals("Poulet rôti et légumes") || plat.equals("Risotto aux champignons") ||
                       plat.equals("Filet de saumon et patates sautés");
            case "dessert":
                return plat.equals("Salade de fruits") || plat.equals("Fondant au chocolat") ||
                       plat.equals("Crème brûlée") || plat.equals("Tarte Tatin") ||
                       plat.equals("Mousse au chocolat");
            case "boisson":
                return plat.equals("Coca-Cola") || plat.equals("IceTea") ||
                       plat.equals("Café");
            default:
                return false;
        }
    }
}