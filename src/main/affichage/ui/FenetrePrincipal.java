package main.affichage.ui;

import javax.swing.*;

import main.gestion.RestaurantGestion;

public class FenetrePrincipal extends JFrame {
    
    public FenetrePrincipal(RestaurantGestion restaurantController) {
        setTitle("Restaurant Simulator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Ajouter le panneau principal
        PanneauDeJeu gamePanel = new PanneauDeJeu(restaurantController, this);
        setContentPane(gamePanel);
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}