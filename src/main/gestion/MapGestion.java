package main.gestion;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import main.data.restaurant.Zone;


public class MapGestion {
    private Zone[] zones;
    private int[][] mapZoneNum;
    private int tailleTuile;
    private int maxColonnes;
    private int maxLignes;
    
    public MapGestion(int tailleTuile, int maxColonnes, int maxLignes) {
        this.tailleTuile = tailleTuile;
        this.maxColonnes = maxColonnes;
        this.maxLignes = maxLignes;
        
        // Initialiser le tableau des zones
        this.zones = new Zone[20];
        
        // Initialiser la matrice de la carte
        this.mapZoneNum = new int[maxColonnes][maxLignes];
        
        // Charger les images et la carte
        chargerImages();
        chargerCarte("src/main/images/map.txt");
    }
    
    private void chargerImages() {
        try {
            // Zone 0 : plan général
            zones[0] = new Zone(ImageIO.read(new File("src/main/images/plan.jpg")), 0);
            
            // Zone 1 : image blanche
            zones[1] = new Zone(ImageIO.read(new File("src/main/images/blanc.jpg")), 1);
            
            // Zone 2 : orange
            zones[2] = new Zone(ImageIO.read(new File("src/main/images/orange.jpg")), 2);
            
            // Zone 3 : beige
            zones[3] = new Zone(ImageIO.read(new File("src/main/images/beige.jpg")), 3);
            
            // Zone 4 : lavabo
            zones[4] = new Zone(ImageIO.read(new File("src/main/images/lavabo.png")), 4);
            
            // Zone 5 : four
            zones[5] = new Zone(ImageIO.read(new File("src/main/images/four.jpg")), 5);
            
            // Zone 6 : frigo
            zones[6] = new Zone(ImageIO.read(new File("src/main/images/frigo.png")), 6);
            
            // Zone 7 : menu
            zones[7] = new Zone(ImageIO.read(new File("src/main/images/menuu.png")), 7);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void chargerCarte(String cheminFichier) {
        try {
            InputStream is = Files.newInputStream(Paths.get(cheminFichier));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            
            int ligne = 0;
            while (ligne < maxLignes) {
                String ligneFichier = br.readLine();
                if (ligneFichier == null) break;
                
                String[] nombres = ligneFichier.split(" ");
                
                for (int col = 0; col < maxColonnes; col++) {
                    if (col < nombres.length) {
                        mapZoneNum[col][ligne] = Integer.parseInt(nombres[col]);
                    } else {
                        mapZoneNum[col][ligne] = 0;
                    }
                }
                ligne++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void dessiner(Graphics2D g2) {
        for (int ligne = 0; ligne < maxLignes; ligne++) {
            for (int col = 0; col < maxColonnes; col++) {
                int zoneNum = mapZoneNum[col][ligne];
                int x = col * tailleTuile;
                int y = ligne * tailleTuile;
                
                if (zoneNum >= 0 && zoneNum < zones.length && zones[zoneNum] != null) {
                    g2.drawImage(zones[zoneNum].getImage(), x, y, tailleTuile, tailleTuile, null);
                }
            }
        }
    }
    
    public int getTailleTuile() {
        return tailleTuile;
    }
    
    public int getMaxColonnes() {
        return maxColonnes;
    }
    
    public int getMaxLignes() {
        return maxLignes;
    }
}