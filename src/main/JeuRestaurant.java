package main;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import main.affichage.ui.PanneauDeJeu;
import main.affichage.ui.FenetreDeLancement;
import main.gestion.RestaurantGestion;

public class JeuRestaurant {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            FenetreDeLancement welcomeScreen = new FenetreDeLancement();
            
            // Attendre que l'utilisateur démarre la partie
            new Thread(() -> {
                while (!welcomeScreen.isGameStarted()) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                
                // Créer la simulation
                RestaurantGestion restaurantController = new RestaurantGestion();
                
                // Créer la fenêtre principale
                JFrame frame = new JFrame("Restaurant Simulator");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                
                // Créer le panneau de jeu
                PanneauDeJeu gamePanel = new PanneauDeJeu(restaurantController, frame);
                frame.setContentPane(gamePanel);
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }).start();
        });
    }
}