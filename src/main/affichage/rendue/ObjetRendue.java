package main.affichage.rendue;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import main.gestion.RestaurantGestion;
import main.data.restaurant.Decoration;
import main.data.restaurant.Table;

public class ObjetRendue {
    private RestaurantGestion restaurantController;
    private Image tableImage, planteImage;
    private int tailleTuile;
    
    public ObjetRendue(RestaurantGestion restaurantController, int tailleTuile) {
        this.restaurantController = restaurantController;
        this.tailleTuile = tailleTuile;
        chargerImages();
    }
    
    /**
     * Charge les images des objets
     */
    private void chargerImages() {
        try {
            // Utiliser le chemin direct comme dans votre exemple
            tableImage = ImageIO.read(new File("src/main/images/table.png"));
            planteImage = ImageIO.read(new File("src/main/images/plante.png"));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Dessine tous les objets du restaurant (tables, décorations)
     */
    public void dessiner(Graphics2D g2) {
        // Copie sécurisée des listes
        List<Table> tablesCopie;
        synchronized (restaurantController.getTables()) {
            tablesCopie = new ArrayList<>(restaurantController.getTables());
        }
        
        // Dessiner les tables
        for (Table table : tablesCopie) {
            g2.drawImage(tableImage, table.getX(), table.getY(), tailleTuile - 7, tailleTuile - 7, null);
        }
        
        // Copie sécurisée des décorations
        List<Decoration> decorationsCopie = new ArrayList<>(restaurantController.getDecorations());
        
        
        // Dessiner les décorations
        for (Decoration decor : decorationsCopie) {
            int x = decor.getCol() * tailleTuile;
            int y = decor.getRow() * tailleTuile;
            
            Image image = null;
            
            switch (decor.getType()) {
                case "plante":
                    image = planteImage;
                    break;
                default:
                    image = null;
                    break;
            }
            
            if (image != null) {
                g2.drawImage(image, x, y, tailleTuile - 7, tailleTuile - 7, null);
            }
        }
    }
}