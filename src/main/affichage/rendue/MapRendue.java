package main.affichage.rendue;

import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import main.gestion.MapGestion;
import main.gestion.RestaurantGestion;

public class MapRendue {
    private MapGestion mapController;
    private int tailleTuile;
    
    public MapRendue(int tailleTuile, int maxColonnes, int maxLignes) {
        this.tailleTuile = tailleTuile;
        this.mapController = new MapGestion(tailleTuile, maxColonnes, maxLignes);
    }
    
    /**
     * Dessine la carte du restaurant
     */
    public void draw(Graphics2D g2) {
        // Dessiner le fond de carte
        mapController.dessiner(g2);
    }
    
    public MapGestion getMapController() {
        return mapController;
    }
}