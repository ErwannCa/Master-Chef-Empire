// Dialogue de gestion des stocks
package main.affichage.dialogue;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import main.data.inventaire.Ingredient;
import main.gestion.StockGestion;
import main.affichage.ui.PanneauDeJeu;

/**
 * Dialogue pour la gestion des stocks du restaurant
 */
public class StockDialogue extends JFrame {
    private DefaultListModel<String> stockListModel;
    private JList<String> stockList;
    private StockGestion stockController;
    private String ingredientAPreselectionner = null;
    
    // Couleurs du thème, similaires à EmployeeDialogue
    private final Color COULEUR_FOND = new Color(255, 245, 225); // Beige clair
    private final Color COULEUR_ENTETE = new Color(204, 102, 0); // Orange foncé
    private final Color COULEUR_BOUTON = new Color(255, 153, 51); // Orange moyen

    /**
     * Constructeur pour le dialogue de gestion des stocks
     */
    public StockDialogue(StockGestion stockController) {
        this.stockController = stockController;
        
        setTitle("Gestion des Stocks");
        setSize(700, 600);
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
        
        JLabel etiquetteTitre = new JLabel("Inventaire du Restaurant");
        etiquetteTitre.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetteTitre.setForeground(Color.WHITE);
        etiquetteTitre.setHorizontalAlignment(SwingConstants.CENTER);
        panneauEntete.add(etiquetteTitre, BorderLayout.CENTER);
        
        // Liste des stocks
        stockListModel = new DefaultListModel<>();
        stockList = new JList<>(stockListModel);
        updateStockList();
        
        stockList.setFont(new Font("Arial", Font.PLAIN, 14));
        stockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        
        JScrollPane scrollPane = new JScrollPane(stockList);
        scrollPane.setBorder(BorderFactory.createLineBorder(COULEUR_ENTETE));
        
        // Panneau d'information
        JPanel panneauInfo = new JPanel();
        panneauInfo.setBackground(new Color(255, 240, 210));
        panneauInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JLabel infoText = new JLabel(
            "<html>Gérez vos stocks en commandant de nouveaux ingrédients.<br>" +
            "Les ingrédients en quantité trop faible sont affichés en rouge.</html>"
        );
        infoText.setFont(new Font("Arial", Font.PLAIN, 12));
        panneauInfo.add(infoText);
        
        // Panneau des boutons
        JPanel panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new GridLayout(3, 1, 0, 10));
        panneauBoutons.setBackground(COULEUR_FOND);
        panneauBoutons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        JButton boutonCommander = new JButton("Commander");
        JButton boutonActualiser = new JButton("Actualiser");
        JButton boutonRetour = new JButton("Retour");
        
        // Style des boutons
        styliserBouton(boutonCommander, COULEUR_BOUTON);
        styliserBouton(boutonActualiser, COULEUR_BOUTON);
        styliserBouton(boutonRetour, COULEUR_BOUTON);
        
        // Actions des boutons
        boutonCommander.addActionListener(e -> openOrderDialog());
        boutonActualiser.addActionListener(e -> {
            updateStockList();
            rafraichirInterface();
        });
        boutonRetour.addActionListener(e -> dispose());
        
        panneauBoutons.add(boutonCommander);
        panneauBoutons.add(boutonActualiser);
        panneauBoutons.add(boutonRetour);
        
        // Assemblage du tout
        panneauPrincipal.add(panneauEntete, BorderLayout.NORTH);
        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);
        panneauPrincipal.add(panneauBoutons, BorderLayout.EAST);
        panneauPrincipal.add(panneauInfo, BorderLayout.SOUTH);
        
        add(panneauPrincipal);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public StockDialogue(StockGestion stockController, String ingredientAPreselectionner) {
        this(stockController); // Appel au constructeur principal
        this.ingredientAPreselectionner = ingredientAPreselectionner;
        openOrderDialog(); // Ouvrir directement la fenêtre de commande
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
     * Met à jour la liste des stocks affichée
     */
    public void updateStockList() {
        stockListModel.clear();
        List<Ingredient> stockItems = stockController.getStockItems();
        
        // Organiser par catégorie
        String currentCategory = "";
        
        for (Ingredient item : stockItems) {
            String category = item.getCategorie();
            
            // Si nouvelle catégorie, l'ajouter comme en-tête
            if (!category.equals(currentCategory)) {
                if (!currentCategory.isEmpty()) {
                    // Ajouter une ligne vide entre les catégories
                    stockListModel.addElement("");
                }
                stockListModel.addElement("-- " + category.toUpperCase() + " --");
                currentCategory = category;
            }
            
            stockListModel.addElement("  • " + item.getNom() + " - " + item.getQuantite() + " unités");
        }
    }
    
    /**
     * Ouvre le dialogue de commande d'ingrédients
     */
    private void openOrderDialog() {
        new OrderDialog(this);
    }
    
    /**
     * Demande au contrôleur de mettre à jour l'interface (le budget)
     */
    private void rafraichirInterface() {
        // Utiliser la méthode forceUIUpdate du contrôleur
        if (stockController.getRestaurantController() != null) {
            stockController.getRestaurantController().forceUIUpdate();
        }
    }
    
    /**
     * Classe interne pour la commande d'ingrédients
     */
    private class OrderDialog extends JFrame {
        private JList<String> ingredientList;
        private JTextField quantityField;
        private JLabel prixTotalLabel;
        private String[] ingredients = {
            "Laitue (0.9€/pièce)",
            "Mozzarella (3.5€/kg)",
            "Tomate (1.5€/kg)",
            "Sauce César (3.5€/bouteille)",
            "Viande hachée (10.0€/kg)",
            "Steak de boeuf (15.0€/kg)",
            "Pomme de Terre (0.8€/kg)",
            "Fruit de saisons (2.5€/kg)",
            "Chocolat (4.0€/tablette)",
            "Poulet (8.5€/kg)",
            "Fromage (4.5€/kg)",
            "Croutons (2.0€/sachet)",
            "Oignon (1.0€/kg)",
            "Gruyère (4.0€/kg)",
            "Jambon (6.0€/kg)",
            "Pâtes (1.2€/kg)",
            "Riz (1.5€/kg)",
            "Champignon (2.8€/kg)",
            "Parmesan (5.0€/kg)",
            "Saumon (12.0€/kg)",
            "Beurre (2.5€/plaquette)",
            "Sucre (1.0€/kg)",
            "Farine (0.8€/kg)",
            "Oeuf (0.3€/pièce)",
            "Vanille (3.0€/gousse)",
            "Pomme (1.2€/kg)",
            "Coca-Cola (1.5€/bouteille)",
            "IceTea (1.2€/bouteille)",
            "Vin rouge (8.0€/bouteille)",
            "Café (2.0€/paquet)"
        };
        
        /**
         * Constructeur pour le dialogue de commande
         */
        public OrderDialog(StockDialogue parent) {
            setTitle("Commander des Ingrédients");
            setSize(700, 600);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Panneau principal
            JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
            panneauPrincipal.setBackground(COULEUR_FOND);
            panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            // Panneau d'en-tête
            JPanel panneauEntete = new JPanel();
            panneauEntete.setBackground(COULEUR_ENTETE);
            panneauEntete.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JLabel etiquetteTitre = new JLabel("Catalogue de produits");
            etiquetteTitre.setFont(new Font("Arial", Font.BOLD, 16));
            etiquetteTitre.setForeground(Color.WHITE);
            etiquetteTitre.setHorizontalAlignment(SwingConstants.CENTER);
            panneauEntete.add(etiquetteTitre);
            
            // Initialiser la liste des ingrédients
            ingredientList = new JList<>(ingredients);
            ingredientList.setFont(new Font("Arial", Font.PLAIN, 14));
            
            // Présélectionner un ingrédient si nécessaire
            preselectIngredient();
            
            JScrollPane scrollPane = new JScrollPane(ingredientList);
            scrollPane.setBorder(BorderFactory.createLineBorder(COULEUR_ENTETE));
            
            // Panel pour les contrôles
            JPanel controlPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            controlPanel.setBackground(COULEUR_FOND);
            controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JLabel quantiteLabel = new JLabel("Quantité à commander:");
            quantiteLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            // Champ texte simple pour la quantité
            quantityField = new JTextField("10");
            quantityField.setFont(new Font("Arial", Font.PLAIN, 14));
            
            // Label pour le prix total
            prixTotalLabel = new JLabel("Prix total: 0.00€");
            prixTotalLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            buttonPanel.setBackground(COULEUR_FOND);
            
            JButton commanderButton = new JButton("Commander");
            JButton annulerButton = new JButton("Annuler");
            
            styliserBouton(commanderButton, COULEUR_BOUTON);
            styliserBouton(annulerButton, new Color(150, 150, 150));
            
            commanderButton.addActionListener(e -> addIngredient());
            annulerButton.addActionListener(e -> dispose());
            
            buttonPanel.add(commanderButton);
            buttonPanel.add(annulerButton);
            
            // Ajouter les composants au panel de contrôles
            controlPanel.add(quantiteLabel);
            controlPanel.add(quantityField);
            controlPanel.add(prixTotalLabel);
            controlPanel.add(buttonPanel);
            
            // Assemblage du tout
            panneauPrincipal.add(panneauEntete, BorderLayout.NORTH);
            panneauPrincipal.add(scrollPane, BorderLayout.CENTER);
            panneauPrincipal.add(controlPanel, BorderLayout.SOUTH);
            
            add(panneauPrincipal);
            setLocationRelativeTo(parent);
            setVisible(true);
        }
        
        /**
         * Présélectionne un ingrédient dans la liste si spécifié
         */
        private void preselectIngredient() {
            if (StockDialogue.this.ingredientAPreselectionner != null) {
                // Rechercher l'ingrédient dans la liste
                for (int i = 0; i < ingredients.length; i++) {
                    if (ingredients[i].startsWith(StockDialogue.this.ingredientAPreselectionner)) {
                        ingredientList.setSelectedIndex(i);
                        // Faire défiler jusqu'à l'élément sélectionné
                        ingredientList.ensureIndexIsVisible(i);
                        break;
                    }
                }
            }
        }
        
        /**
         * Ajoute un ingrédient sélectionné au stock
         */
        private void addIngredient() {
            // 1. Vérifier qu'un ingrédient est sélectionné
            String selectedItem = ingredientList.getSelectedValue();
            if (selectedItem == null) {
                JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un ingrédient.",
                    "Aucune sélection",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            // 2. Récupérer le nom de l'ingrédient (texte avant la parenthèse)
            String ingredientName = selectedItem.split(" \\(")[0];
            
            // 3. Récupérer la quantité depuis le champ texte
            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    quantity = 1;
                }
            } catch (NumberFormatException e) {
                quantity = 10;
                quantityField.setText("10");
            }
            
            // 4. Fixer un prix unitaire simple (1€ par unité)
            double prixUnitaire = 1.0;
            double coutTotal = prixUnitaire * quantity;
            
            // 5. Confirmer la commande avec le client
            int reponse = JOptionPane.showConfirmDialog(this,
                "Commander " + quantity + " " + ingredientName + " pour " + coutTotal + "€ ?",
                "Confirmation",
                JOptionPane.YES_NO_OPTION);
                
            if (reponse != JOptionPane.YES_OPTION) {
                return; // L'utilisateur a annulé
            }
            
            // 6. Passer la commande
            boolean commandeReussie = stockController.commander(ingredientName, quantity, coutTotal);
            
            // 7. Traiter le résultat
            if (commandeReussie) {
                JOptionPane.showMessageDialog(this,
                    "Commande réussie !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                    
                // Mettre à jour l'affichage des stocks
                StockDialogue.this.updateStockList();
                StockDialogue.this.rafraichirInterface();
                
                // Fermer la fenêtre
                dispose();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Fonds insuffisants pour cette commande.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}