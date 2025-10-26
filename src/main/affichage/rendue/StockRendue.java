package main.affichage.rendue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import java.util.Map;

import main.data.inventaire.Article;
import main.data.inventaire.Stock;
import main.gestion.RestaurantGestion;
import main.gestion.StockGestion;

/**
 * Classe responsable du rendu graphique des stocks du restaurant
 */
public class StockRendue {
    private RestaurantGestion restaurantController;
    private Rectangle zoneStock; // Zone dédiée à l'affichage des stocks
    
    // Paramètres d'affichage
    private static final Font FONT_TITRE = new Font("Arial", Font.BOLD, 14);
    private static final Font FONT_ITEM = new Font("Arial", Font.PLAIN, 12);
    private static final Font FONT_QUANTITE = new Font("Arial", Font.BOLD, 12);
    private static final Color COULEUR_FOND = new Color(240, 240, 240);
    private static final Color COULEUR_BORDURE = Color.DARK_GRAY;
    private static final Color COULEUR_TEXTE = Color.BLACK;
    private static final Color COULEUR_FAIBLE_STOCK = Color.RED;
    private static final Color COULEUR_STOCK_OK = new Color(0, 128, 0); // Vert foncé
    private static final int MARGE = 10;
    private static final int LIGNE_HAUTEUR = 20;
    
    /**
     * Constructeur du rendeur de stocks
     */
    public StockRendue(RestaurantGestion restaurantController, Rectangle zoneStock) {
        this.restaurantController = restaurantController;
        this.zoneStock = zoneStock;
    }
    
    /**
     * Dessine les stocks dans la zone dédiée
     */
    public void dessiner(Graphics2D g2) {
        // Récupérer le contrôleur de stock
        StockGestion stockController = restaurantController.getStockController();
        if (stockController == null) {
            return;
        }
        
        // Dessiner le fond et la bordure de la zone de stock
        g2.setColor(COULEUR_FOND);
        g2.fill(zoneStock);
        g2.setColor(COULEUR_BORDURE);
        g2.draw(zoneStock);
        
        // Dessiner le titre
        g2.setColor(COULEUR_TEXTE);
        g2.setFont(FONT_TITRE);
        g2.drawString("INVENTAIRE", zoneStock.x + MARGE, zoneStock.y + LIGNE_HAUTEUR);
        
        // Récupérer les stocks
        Map<String, Stock> stocks = stockController.getTousLesStocks();
        if (stocks == null || stocks.isEmpty()) {
            g2.setFont(FONT_ITEM);
            g2.drawString("Aucun stock disponible", zoneStock.x + MARGE, zoneStock.y + LIGNE_HAUTEUR * 2);
            return;
        }
        
        // Position de départ pour les éléments de stock
        int y = zoneStock.y + LIGNE_HAUTEUR * 2;
        
        // Parcourir et afficher chaque catégorie de stock
        for (Map.Entry<String, Stock> entry : stocks.entrySet()) {
            String categorie = entry.getKey();
            Stock stock = entry.getValue();
            
            if (y + LIGNE_HAUTEUR > zoneStock.y + zoneStock.height - MARGE) {
                // Si on dépasse la zone, on s'arrête
                g2.setFont(FONT_ITEM);
                g2.drawString("...", zoneStock.x + MARGE, y);
                break;
            }
            
            // Afficher la catégorie
            g2.setColor(COULEUR_TEXTE);
            g2.setFont(FONT_ITEM);
            g2.drawString(categorie, zoneStock.x + MARGE, y);
            y += LIGNE_HAUTEUR;
            
            // Afficher les articles de cette catégorie
            List<Article> articles = stock.getArticles();
            for (Article article : articles) {
                if (y + LIGNE_HAUTEUR > zoneStock.y + zoneStock.height - MARGE) {
                    // Si on dépasse la zone, on s'arrête
                    g2.setFont(FONT_ITEM);
                    g2.drawString("...", zoneStock.x + MARGE, y);
                    break;
                }
                
                // Nom de l'article
                g2.setColor(COULEUR_TEXTE);
                g2.setFont(FONT_ITEM);
                g2.drawString("  • " + article.getNom(), zoneStock.x + MARGE, y);
                
                // Quantité avec couleur selon le niveau de stock
                int quantite = article.getQuantite();
                g2.setFont(FONT_QUANTITE);
                if (quantite <= article.getSeuilAlerte()) {
                    g2.setColor(COULEUR_FAIBLE_STOCK);
                } else {
                    g2.setColor(COULEUR_STOCK_OK);
                }
                
                String quantiteStr = quantite + " " + article.getUnite();
                int largeurQuantite = g2.getFontMetrics().stringWidth(quantiteStr);
                g2.drawString(quantiteStr, 
                        zoneStock.x + zoneStock.width - MARGE - largeurQuantite, 
                        y);
                
                y += LIGNE_HAUTEUR;
            }
            
            // Ajouter un espace entre les catégories
            y += LIGNE_HAUTEUR / 2;
        }
    }
    
    /**
     * Définit la zone d'affichage des stocks
     */
    public void setZoneStock(Rectangle zoneStock) {
        this.zoneStock = zoneStock;
    }
}