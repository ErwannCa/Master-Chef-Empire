package main.data.restaurant;

import java.awt.image.BufferedImage;

/**
 * Représente une zone du restaurant avec son image et son type.
 * Utilisée pour le rendu de la carte du restaurant.
 */
public class Zone {
    private BufferedImage image;
    private int typeZone;
    
    /**
     * Crée une nouvelle zone
     */
    public Zone(BufferedImage image, int typeZone) {
        this.image = image;
        this.typeZone = typeZone;
    }
    
    /**
     * Obtient l'image de la zone
     */
    public BufferedImage getImage() {
        return image;
    }
    
    /**
     * Définit l'image de la zone
     */
    public void setImage(BufferedImage image) {
        this.image = image;
    }
    
    /**
     * Obtient le type de la zone

     */
    public int getTypeZone() {
        return typeZone;
    }
    
    /**
     * Définit le type de la zone
     */
    public void setTypeZone(int typeZone) {
        this.typeZone = typeZone;
    }
    
    /**
     * Vérifie si cette zone est de type spécifique

     */
    public boolean estDeType(int type) {
        return this.typeZone == type;
    }
    
    /**
     * Les types de zones connus dans le restaurant
     */
    public static class Types {
        public static final int PLAN_GENERAL = 0;
        public static final int BLANC = 1;
        public static final int ORANGE = 2;
        public static final int BEIGE = 3;
        public static final int LAVABO = 4;
        public static final int FOUR = 5;
        public static final int FRIGO = 6;
        public static final int MENU = 7;
    }
}