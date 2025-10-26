package main.affichage.dialogue;


import javax.swing.*;
import javax.swing.border.EmptyBorder;

import main.gestion.StatisticsGestion;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

/**
 * Dialogue affichant les statistiques du restaurant
 */
public class StatsDialogue extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private StatisticsGestion statsController;
    
    /**
     * Constructeur - Initialise la fenêtre des statistique
     */
    public StatsDialogue(StatisticsGestion statsController) {
        this.statsController = statsController;
        
        setTitle("Statistiques du Restaurant");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Couleurs personnalisées
        Color bgColor = new Color(252, 246, 225); // Beige clair
        Color headerColor = new Color(255, 154, 0); // Orange
        
        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // En-tête
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(headerColor);
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel titleLabel = new JLabel("Tableau de Bord");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Panneau des statistiques
        JPanel statsPanel = new JPanel(new GridLayout(8, 2, 10, 15));
        statsPanel.setBackground(bgColor);
        
        // Formatter pour les nombres décimaux
        DecimalFormat df = new DecimalFormat("#,##0.00");
        
        // Ajouter les lignes de statistiques
        addStatRow(statsPanel, "Clients servis:", statsController.getClientsServis() + "");
        addStatRow(statsPanel, "Satisfaction moyenne:", df.format(statsController.getSatisfactionMoyenne()) + "%");
        addStatRow(statsPanel, "Revenu total:", df.format(statsController.getRevenuTotal()) + " €");
        addStatRow(statsPanel, "Dépenses (salaires):", df.format(statsController.getDepensesSalaires()) + " €");
        addStatRow(statsPanel, "Dépenses (stocks):", df.format(statsController.getDepensesStocks()) + " €");
        addStatRow(statsPanel, "Bénéfice net:", df.format(statsController.getBeneficeNet()) + " €");
        addStatRow(statsPanel, "Popularité:", statsController.getPopularite() + "%");
        
        // Panneau des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(bgColor);
        
        JButton refreshButton = new JButton("Actualiser");
        JButton closeButton = new JButton("Fermer");
        
        // Style des boutons
        styleButton(refreshButton, headerColor);
        styleButton(closeButton, headerColor);
        
        // Actions des boutons
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Recharger la page avec les statistiques mises à jour
                dispose();
                new StatsDialogue(statsController);
            }
        });
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(closeButton);
        
        // Assemblage des panneaux
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(statsPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Centrer la fenêtre
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Ajoute une ligne de statistique au panneau
     */
    private void addStatRow(JPanel panel, String label, String value) {
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
        
        panel.add(labelComponent);
        panel.add(valueComponent);
    }
    
    /**
     * Applique un style à un bouton
     */
    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 100, 0), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
    }
}