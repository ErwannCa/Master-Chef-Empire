package main.affichage.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import main.gestion.RestaurantGestion;
import main.affichage.dialogue.*;
import main.affichage.rendue.*;

/**
 * Panneau principal du jeu qui affiche le restaurant et gère l'interface utilisateur
 */
public class PanneauDeJeu extends JPanel implements Runnable {
    private static final int TAILLE_TUILE = 55;
    private static final int NB_COLONNES = 16;  
    private static final int NB_LIGNES = 13;
    
    private static final int LARGEUR_ECRAN = (TAILLE_TUILE * NB_COLONNES) + 200; // +200 pour les boutons
    private static final int HAUTEUR_ECRAN = (TAILLE_TUILE * NB_LIGNES);
    
    private JLabel valeurRevenu;
    private JLabel valeurSatisfaction;
    private MapRendue mapRenderer;
    private ObjetRendue objectRenderer;
    private PersonnelRendue personRenderer;
    private RestaurantGestion restaurantController;
    private Thread gameThread;
    
    private JFrame mainFrame;
    private JButton boutonPause;
    
    // Panneau de jeu principal où le restaurant est dessiné
    private JPanel panneauJeu;
    
    /**
     * Constructeur - Initialise le panneau de jeu
     */
    public PanneauDeJeu(RestaurantGestion restaurantController, JFrame mainFrame) {
        this.restaurantController = restaurantController;
        this.mainFrame = mainFrame;
        
        // Configuration du panneau
        setPreferredSize(new Dimension(LARGEUR_ECRAN, HAUTEUR_ECRAN));
        setBackground(Color.WHITE);
        setLayout(new BorderLayout(5, 5));
        
        // Initialiser les rendue
        mapRenderer = new MapRendue(TAILLE_TUILE, NB_COLONNES, NB_LIGNES);
        objectRenderer = new ObjetRendue(restaurantController, TAILLE_TUILE);
        personRenderer = new PersonnelRendue(restaurantController, TAILLE_TUILE);
        
        // Initialiser l'interface
        initialiserInterface();
        
        // Démarrer la boucle de jeu
        demarrerGameThread();
    }
    
    /**
     * Initialise tous les éléments de l'interface utilisateur
     */
    private void initialiserInterface() {
        // Couleurs pour l'interface
        Color couleurFond = new Color(255, 231, 186);  // Beige clair
        Color couleurBoutons = new Color(255, 154, 0); // Orange vif
        Color couleurTexte = new Color(60, 30, 0);     // Brun foncé
        
        // ===== BARRE DE STATUT (HAUT) =====
        JPanel panneauHaut = new JPanel();
        panneauHaut.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panneauHaut.setBackground(couleurFond);
        panneauHaut.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Icônes et étiquettes
        JLabel iconeRevenu = new JLabel("💰");
        JLabel texteRevenu = new JLabel("Revenu:");
        valeurRevenu = new JLabel("0€");
        valeurRevenu.setFont(new Font("Arial", Font.BOLD, 14));
        valeurRevenu.setForeground(couleurTexte);
        
        JLabel iconeSatisfaction = new JLabel("😊");
        JLabel texteSatisfaction = new JLabel("Satisfaction:");
        valeurSatisfaction = new JLabel("0%");
        valeurSatisfaction.setFont(new Font("Arial", Font.BOLD, 14));
        valeurSatisfaction.setForeground(couleurTexte);
        
        panneauHaut.add(iconeRevenu);
        panneauHaut.add(texteRevenu);
        panneauHaut.add(valeurRevenu);
        panneauHaut.add(new JSeparator(JSeparator.VERTICAL));
        panneauHaut.add(iconeSatisfaction);
        panneauHaut.add(texteSatisfaction);
        panneauHaut.add(valeurSatisfaction);
        
        add(panneauHaut, BorderLayout.NORTH);
        
        // ===== PANNEAU DE JEU (CENTRE) =====
        panneauJeu = new JPanel() {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                
                // Active l'antialiasing pour un meilleur rendu
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dessiner le fond
                g2.setColor(new Color(255, 240, 200)); // Beige très clair
                g2.fillRect(0, 0, getWidth(), getHeight());
                
                // Dessiner tous les éléments du jeu
                mapRenderer.draw(g2);
                objectRenderer.dessiner(g2);
                personRenderer.dessiner(g2);
                
                // Afficher le statut de pause si nécessaire (méthode simplifiée)
                if (restaurantController.estEnPause()) {
                    g2.setColor(Color.RED);
                    g2.setFont(new Font("Arial", Font.BOLD, 36));
                    g2.drawString("PAUSE", 20, 50);
                }
            }
        };
        
        panneauJeu.setPreferredSize(new Dimension(TAILLE_TUILE * NB_COLONNES, HAUTEUR_ECRAN));
        add(panneauJeu, BorderLayout.CENTER);
        
        // ===== PANNEAU DES BOUTONS (DROITE) =====
        JPanel panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new GridLayout(6, 1, 0, 10));
        panneauBoutons.setBackground(couleurFond);
        panneauBoutons.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        panneauBoutons.setPreferredSize(new Dimension(200, HAUTEUR_ECRAN));
        
        // Titre du panneau
        JLabel titrePanneau = new JLabel("Menu Principal");
        titrePanneau.setFont(new Font("Arial", Font.BOLD, 16));
        titrePanneau.setHorizontalAlignment(SwingConstants.CENTER);
        titrePanneau.setForeground(couleurTexte);
        
        // Boutons
        JButton boutonEmployes = creerBouton("Gérer les employés", couleurBoutons);
        JButton boutonStocks = creerBouton("Gérer les stocks", couleurBoutons);
        JButton boutonMenu = creerBouton("Modifier le menu", couleurBoutons);
        JButton boutonAmelioration = creerBouton("Améliorer restaurant", couleurBoutons);
        boutonPause = creerBouton("Pause", couleurBoutons);
        
        // Actions des boutons
        boutonEmployes.addActionListener(e -> {
            new EmployeeDialogue(restaurantController.getEmployeeController());
        });
        
        boutonStocks.addActionListener(e -> {
            new StockDialogue(restaurantController.getStockController());
        });
        
        boutonMenu.addActionListener(e -> {
            new MenuDialogue(restaurantController.getMenuController());
        });
        
        boutonAmelioration.addActionListener(e -> {
            new AmeliorationDialogue(restaurantController);
        });
        
        // Utilisation de la méthode basculerPause du RestaurantGestion
        boutonPause.addActionListener(e -> {
            restaurantController.basculerPause();
            boutonPause.setText(restaurantController.estEnPause() ? "Reprendre" : "Pause");
            panneauJeu.repaint(); // Pour afficher l'indicateur de pause
        });
        
        panneauBoutons.add(titrePanneau);
        panneauBoutons.add(boutonEmployes);
        panneauBoutons.add(boutonStocks);
        panneauBoutons.add(boutonMenu);
        panneauBoutons.add(boutonAmelioration);
        panneauBoutons.add(boutonPause);
        
        add(panneauBoutons, BorderLayout.EAST);
        
        // ===== PANNEAU DU BAS (SUD) =====
        JPanel panneauBas = new JPanel();
        panneauBas.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        panneauBas.setBackground(couleurFond);
        
        JButton boutonRetour = creerBouton("Retour", couleurBoutons);
        JButton boutonTableauBord = creerBouton("Tableau de bord", couleurBoutons);
        
        boutonTableauBord.addActionListener(e -> {
            new StatsDialogue(restaurantController.getStatsController());
        });
        
        boutonRetour.addActionListener(e -> {
            restaurantController.arreter();
            mainFrame.dispose();
            new FenetreDeLancement();
        });
        
        panneauBas.add(boutonRetour);
        panneauBas.add(boutonTableauBord);
        add(panneauBas, BorderLayout.SOUTH);
    }
    
    /**
     * Crée un bouton stylisé
     */
    private JButton creerBouton(String texte, Color couleurFond) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Arial", Font.BOLD, 14));
        bouton.setBackground(couleurFond);
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        bouton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 100, 0), 2),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        return bouton;
    }
    
    /**
     * Démarre le thread principal du jeu
     */
    private void demarrerGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
    /**
     * Boucle principale du jeu
     */
    public void run() {
        while (gameThread != null) {
            // Mettre à jour l'affichage uniquement si le jeu n'est pas en pause
            // On utilise la méthode estEnPause du RestaurantGestion pour vérifier l'état
            if (!restaurantController.estEnPause()) {
                mettreAJourAffichage();
            }
            
            // Rafraîchir l'écran
            repaint();
            
            try {
                Thread.sleep(16); // ~60 images par seconde (1000 ms / 60 = 16 ms)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Met à jour les informations d'affichage (revenu, satisfaction)
     */
    private void mettreAJourAffichage() {
        valeurRevenu.setText(String.format("%.2f€", restaurantController.getRevenu()));
        valeurSatisfaction.setText(String.format("%.1f%%", restaurantController.getSatisfaction()));
        
        // Demande explicite de rafraîchissement du panneau de jeu
        panneauJeu.repaint();
    }
    
    /**
     * Appelle repaint sur le panneau de jeu pour le rafraîchir
     */
    @Override
    public void repaint() {
        super.repaint();
        if (panneauJeu != null) {
            panneauJeu.repaint();
        }
    }
}