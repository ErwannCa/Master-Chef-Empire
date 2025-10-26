package main.affichage.ui;

import javax.swing.*;

import main.gestion.RestaurantGestion;

import java.awt.*;

public class StatusBar extends JPanel {
    private static final long serialVersionUID = 1L;
    
    private JLabel revenuLabel;
    private JLabel satisfactionLabel;
    private RestaurantGestion restaurantController;
    
    public StatusBar(RestaurantGestion restaurantController) {
        this.restaurantController = restaurantController;
        
        setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JLabel revenuIcon = new JLabel("💰");
        JLabel revenuText = new JLabel("Revenu:");
        revenuLabel = new JLabel("0");
        
        JLabel satisfactionIcon = new JLabel("😊");
        JLabel satisfactionText = new JLabel("Satisfaction:");
        satisfactionLabel = new JLabel("0");
        
        add(revenuIcon);
        add(revenuText);
        add(revenuLabel);
        add(satisfactionIcon);
        add(satisfactionText);
        add(satisfactionLabel);
    }
    
    public void mettreAJour() {
        revenuLabel.setText(String.format("%.2f", restaurantController.getRevenu()));
        satisfactionLabel.setText(String.format("%.1f", restaurantController.getSatisfaction()));
    }
}