package main.affichage.dialogue;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import main.data.restaurant.Decoration;
import main.gestion.RestaurantGestion;
import main.data.restaurant.Table;

/**
 * Dialogue pour améliorer le restaurant
 */
public class AmeliorationDialogue extends JFrame {
    private RestaurantGestion restaurantGestion;
    
    // Prix des améliorations
    private final double PRIX_TABLE = 300.0;
    private final double PRIX_PLANTE = 200.0;
    
    // Couleurs du thème, similaires à StockDialogue
    private final Color COULEUR_FOND = new Color(255, 245, 225); // Beige clair
    private final Color COULEUR_ENTETE = new Color(204, 102, 0); // Orange foncé
    private final Color COULEUR_BOUTON = new Color(255, 153, 51); // Orange moyen
    
    /**
     * Constructeur pour le dialogue d'amélioration
     */
    public AmeliorationDialogue(RestaurantGestion restaurantGestion) {
        this.restaurantGestion = restaurantGestion;
        
        setTitle("Améliorer le Restaurant");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Configurer l'apparence générale
        JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
        panneauPrincipal.setBackground(COULEUR_FOND);
        panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Panneau d'en-tête
        JPanel panneauEntete = new JPanel();
        panneauEntete.setBackground(COULEUR_ENTETE);
        panneauEntete.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panneauEntete.setLayout(new BorderLayout());
        
        JLabel etiquetteTitre = new JLabel("Catalogue des Améliorations");
        etiquetteTitre.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetteTitre.setForeground(Color.WHITE);
        etiquetteTitre.setHorizontalAlignment(SwingConstants.CENTER);
        panneauEntete.add(etiquetteTitre, BorderLayout.CENTER);
        
        // Panneaux pour les différentes améliorations
        JPanel panneauAmeliorations = new JPanel(new GridLayout(2, 1, 10, 10));
        panneauAmeliorations.setBackground(COULEUR_FOND);
        
        panneauAmeliorations.add(creerPanneauAmelioration(
            "Table", 
            "Ajouter une table pour accueillir plus de clients.",
            "table",
            PRIX_TABLE
        ));
        
        panneauAmeliorations.add(creerPanneauAmelioration(
            "Plante", 
            "Ajouter une plante décorative pour améliorer l'ambiance.",
            "plante",
            PRIX_PLANTE
        ));
        
        // Panneau des boutons à droite
        JPanel panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new GridLayout(1, 1, 0, 10));
        panneauBoutons.setBackground(COULEUR_FOND);
        panneauBoutons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        JButton boutonFermer = new JButton("Fermer");
        
        // Style des boutons
        styliserBouton(boutonFermer, COULEUR_BOUTON);
        
        // Actions des boutons
        boutonFermer.addActionListener(e -> dispose());
        
        panneauBoutons.add(boutonFermer);
        
        // Panneau d'état en bas
        JPanel panneauStatut = new JPanel();
        panneauStatut.setBackground(new Color(255, 240, 210));
        panneauStatut.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JLabel statutLabel = new JLabel("Budget disponible: " + String.format("%.2f", restaurantGestion.getRevenu()) + "€");
        statutLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panneauStatut.add(statutLabel);
        
        // Assemblage du tout
        panneauPrincipal.add(panneauEntete, BorderLayout.NORTH);
        panneauPrincipal.add(panneauAmeliorations, BorderLayout.CENTER);
        panneauPrincipal.add(boutonFermer, BorderLayout.SOUTH);
        panneauPrincipal.add(panneauStatut, BorderLayout.SOUTH);
        
        // Reconfigurer la disposition
        panneauPrincipal.remove(boutonFermer);
        panneauPrincipal.add(panneauBoutons, BorderLayout.EAST);
        
        add(panneauPrincipal);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Crée un panneau pour un type d'amélioration
     */
    private JPanel creerPanneauAmelioration(String titre, String description, String type, double prix) {
        JPanel panneau = new JPanel(new BorderLayout(10, 0));
        panneau.setBackground(new Color(255, 250, 240));
        panneau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COULEUR_ENTETE),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Titre et description au centre
        JPanel panneauTexte = new JPanel();
        panneauTexte.setLayout(new BoxLayout(panneauTexte, BoxLayout.Y_AXIS));
        panneauTexte.setBackground(new Color(255, 250, 240));
        
        JLabel titreLabel = new JLabel(titre);
        titreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel prixLabel = new JLabel("Prix: " + String.format("%.2f", prix) + "€");
        prixLabel.setFont(new Font("Arial", Font.BOLD, 14));
        prixLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panneauTexte.add(Box.createVerticalGlue());
        panneauTexte.add(titreLabel);
        panneauTexte.add(Box.createRigidArea(new Dimension(0, 5)));
        panneauTexte.add(descriptionLabel);
        panneauTexte.add(Box.createRigidArea(new Dimension(0, 5)));
        panneauTexte.add(prixLabel);
        panneauTexte.add(Box.createVerticalGlue());
        
        // Bouton d'achat
        JButton acheterButton = new JButton("Acheter");
        acheterButton.setFont(new Font("Arial", Font.BOLD, 14));
        acheterButton.setBackground(COULEUR_BOUTON);
        acheterButton.setForeground(Color.WHITE);
        acheterButton.addActionListener(e -> acheter(type, prix));
        
        // Centrer le bouton
        JPanel panneauBouton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panneauBouton.setBackground(new Color(255, 250, 240));
        panneauBouton.add(acheterButton);
        
       
        
        // Assembler le tout
        JPanel panneauGlobal = new JPanel(new BorderLayout());
        panneauGlobal.setBackground(new Color(255, 250, 240));
        panneauGlobal.add(panneauTexte, BorderLayout.CENTER);
        panneauGlobal.add(panneauBouton, BorderLayout.SOUTH);
        
        panneau.add(panneauGlobal, BorderLayout.CENTER);
        
        return panneau;
    }
    
    /**
     * Applique un style au bouton
     */
    private void styliserBouton(JButton bouton, Color couleur) {
        bouton.setFont(new Font("Arial", Font.BOLD, 14));
        bouton.setBackground(couleur);
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        bouton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
    }
    
    /**
     * Achat d'une amélioration
     */
    private void acheter(String nom, double prix) {
        double revenuActuel = restaurantGestion.getRevenu();
        
        if (revenuActuel >= prix) {
            restaurantGestion.setRevenu(revenuActuel - prix);
            
            if (nom.equals("table")) {
                // Liste fixe des emplacements autorisés pour les tables
                int[][] positionsDisponibles = {
                    {3, 6}, {4, 6}, {3, 8}, {4, 8}, {6, 6}, {6, 8},
                    {6, 4}, {6, 2}, {7, 6}, {7, 8}, {7, 4}, {7, 2},
                };
                
                boolean placeTrouvee = false;
                
                for (int[] pos : positionsDisponibles) {
                    int col = pos[0];
                    int row = pos[1];
                    
                    // Vérifie si une table existe déjà à cet emplacement
                    boolean dejaOccupee = false;
                    for (Table table : restaurantGestion.getTables()) {
                        if (table.getColonne() == col && table.getLigne() == row) {
                            dejaOccupee = true;
                            break;
                        }
                    }
                    
                    if (!dejaOccupee) {
                        restaurantGestion.ajouterTable(col, row);
                        JOptionPane.showMessageDialog(this,
                            "Table ajoutée!",
                            "Achat réussi",
                            JOptionPane.INFORMATION_MESSAGE);
                        placeTrouvee = true;
                        break;
                    }
                }
                
                if (!placeTrouvee) {
                    JOptionPane.showMessageDialog(this,
                        "Plus d'emplacements disponibles pour une nouvelle table !",
                        "Impossible d'ajouter",
                        JOptionPane.WARNING_MESSAGE);
                    // Rembourser le montant
                    restaurantGestion.setRevenu(revenuActuel);
                }
                
            } else if (nom.equals("plante")) {
                // Emplacements fixes autorisés pour les plantes
                int[][] emplacementsPlantes = {
                    {1, 1}, {8, 1}, {1, 7}, {8, 7},
                };
                
                boolean placePlanteTrouvee = false;
                
                for (int[] pos : emplacementsPlantes) {
                    int col = pos[0];
                    int row = pos[1];
                    
                    boolean dejaOccupee = false;
                    for (Decoration decor : restaurantGestion.getDecorations()) {
                        if (decor.getType().equals("plante") && decor.getCol() == col && decor.getRow() == row) {
                            dejaOccupee = true;
                            break;
                        }
                    }
                    
                    if (!dejaOccupee) {
                        restaurantGestion.ajouterDecoration(new Decoration("plante", row, col));
                        JOptionPane.showMessageDialog(this,
                            "Plante ajoutée !",
                            "Achat réussi",
                            JOptionPane.INFORMATION_MESSAGE);
                        placePlanteTrouvee = true;
                        break;
                    }
                }
                
                if (!placePlanteTrouvee) {
                    JOptionPane.showMessageDialog(this,
                        "Plus de place disponible pour ajouter une plante !",
                        "Impossible d'ajouter",
                        JOptionPane.WARNING_MESSAGE);
                    // Rembourser le montant
                    restaurantGestion.setRevenu(revenuActuel);
                }
            }
            
        } else {
            JOptionPane.showMessageDialog(this,
                "Budget insuffisant pour une " + nom + " ! Il manque " + 
                String.format("%.2f", (prix - revenuActuel)) + "€",
                "Fonds insuffisants",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}