// Dialogue de gestion du menu
package main.affichage.dialogue;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import main.data.inventaire.ArticleMenu;
import main.gestion.MenuGestion;

/**
 * Dialogue pour la gestion du menu du restaurant
 */
public class MenuDialogue extends JFrame {
 
    
    // Modèles pour les listes d'articles par catégorie
    private DefaultListModel<String> entreesModel;
    private DefaultListModel<String> platsModel;
    private DefaultListModel<String> dessertsModel;
    private DefaultListModel<String> boissonsModel;
    
    // Listes d'affichage
    private JList<String> entreesList;
    private JList<String> platsList;
    private JList<String> dessertsList;
    private JList<String> boissonsList;
    
    // Référence au contrôleur de menu
    private MenuGestion menuController;
    
    // Couleurs du thème, similaires aux autres dialogues
    private final Color COULEUR_FOND = new Color(255, 245, 225); // Beige clair
    private final Color COULEUR_ENTETE = new Color(204, 102, 0); // Orange foncé
    private final Color COULEUR_BOUTON = new Color(255, 153, 51); // Orange moyen
    private final Color COULEUR_LISTE = new Color(255, 250, 240); // Beige très clair

    /**
     * Constructeur pour le dialogue de gestion du menu
     */
    public MenuDialogue(MenuGestion menuController) {
        this.menuController = menuController;
        
        setTitle("Gestion du Menu");
        setSize(800, 500);
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
        
        JLabel etiquetteTitre = new JLabel("MENU DU RESTAURANT", SwingConstants.CENTER);
        etiquetteTitre.setFont(new Font("Arial", Font.BOLD, 22));
        etiquetteTitre.setForeground(Color.WHITE);
        panneauEntete.add(etiquetteTitre, BorderLayout.CENTER);
        
        // Initialiser les modèles et listes
        entreesModel = new DefaultListModel<>();
        platsModel = new DefaultListModel<>();
        dessertsModel = new DefaultListModel<>();
        boissonsModel = new DefaultListModel<>();
        
        entreesList = new JList<>(entreesModel);
        platsList = new JList<>(platsModel);
        dessertsList = new JList<>(dessertsModel);
        boissonsList = new JList<>(boissonsModel);
        
        // Styliser les listes
        styliserListe(entreesList);
        styliserListe(platsList);
        styliserListe(dessertsList);
        styliserListe(boissonsList);
        
        // Créer les quatre colonnes pour les catégories
        JPanel panneauColonnes = new JPanel(new GridLayout(1, 4, 10, 0));
        panneauColonnes.setBackground(COULEUR_FOND);
        
        panneauColonnes.add(creerPanneauCategorie("ENTRÉES", entreesList, "Entrée"));
        panneauColonnes.add(creerPanneauCategorie("PLATS", platsList, "Plat"));
        panneauColonnes.add(creerPanneauCategorie("DESSERTS", dessertsList, "Dessert"));
        panneauColonnes.add(creerPanneauCategorie("BOISSONS", boissonsList, "Boisson"));
        
        // Panneau des boutons en bas
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panneauBoutons.setBackground(COULEUR_FOND);
        
        JButton boutonAjouter = creerBouton("Ajouter un article", COULEUR_BOUTON);
        JButton boutonSupprimer = creerBouton("Supprimer", COULEUR_BOUTON);
        JButton boutonFermer = creerBouton("Fermer", COULEUR_BOUTON);
        JButton boutonInformation = creerBouton("Information", COULEUR_BOUTON);
        
        boutonAjouter.addActionListener(e -> ouvrirDialogueAjout());
        boutonSupprimer.addActionListener(e -> supprimerSelectionne());
        boutonFermer.addActionListener(e -> dispose());
        boutonInformation.addActionListener(e -> afficherInformationIngredients());
        
        panneauBoutons.add(boutonAjouter);
        panneauBoutons.add(boutonSupprimer);
        panneauBoutons.add(boutonFermer);
        panneauBoutons.add(boutonInformation);
        
        // Assembler le tout
        panneauPrincipal.add(panneauEntete, BorderLayout.NORTH);
        panneauPrincipal.add(panneauColonnes, BorderLayout.CENTER);
        panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);
        
        add(panneauPrincipal);
        
        // Charger les données du menu
        chargerMenu();
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Applique un style à une liste
     */
    private void styliserListe(JList<String> liste) {
        liste.setFont(new Font("Arial", Font.PLAIN, 14));
        liste.setBackground(COULEUR_LISTE);
        liste.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
      
    }
    
    /**
     * Crée un bouton stylisé
     */
    private JButton creerBouton(String texte, Color couleur) {
        JButton bouton = new JButton(texte);
        bouton.setFont(new Font("Arial", Font.BOLD, 14));
        bouton.setBackground(couleur);
        bouton.setForeground(Color.WHITE);
        bouton.setFocusPainted(false);
        bouton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
      
        
        return bouton;
    }
    
    /**
     * Crée un panneau pour une catégorie avec titre et liste
     */
    private JPanel creerPanneauCategorie(String titre, JList<String> liste, final String categorie) {
        JPanel panneau = new JPanel(new BorderLayout(0, 5));
        panneau.setBackground(COULEUR_FOND);
        
        // Titre de la catégorie
        JPanel panneauTitre = new JPanel();
        panneauTitre.setBackground(COULEUR_ENTETE);
        panneauTitre.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JLabel etiquetteTitre = new JLabel(titre, SwingConstants.CENTER);
        etiquetteTitre.setFont(new Font("Arial", Font.BOLD, 16));
        etiquetteTitre.setForeground(Color.WHITE);
        panneauTitre.add(etiquetteTitre);
        
        // Bouton d'ajout spécifique à la catégorie
        JButton boutonAjouter = new JButton("+");
        boutonAjouter.setFont(new Font("Arial", Font.BOLD, 14));
        boutonAjouter.setBackground(COULEUR_BOUTON);
        boutonAjouter.setForeground(Color.WHITE);
        boutonAjouter.setMargin(new Insets(2, 6, 2, 6));
        boutonAjouter.addActionListener(e -> ouvrirDialogueAjoutPourCategorie(categorie));
        
        JPanel panneauTitreComplet = new JPanel(new BorderLayout());
        panneauTitreComplet.setBackground(COULEUR_ENTETE);
        panneauTitreComplet.add(panneauTitre, BorderLayout.CENTER);
        panneauTitreComplet.add(boutonAjouter, BorderLayout.EAST);
        
        // Liste avec défilement
        JScrollPane scrollPane = new JScrollPane(liste);
        scrollPane.setBorder(BorderFactory.createLineBorder(COULEUR_ENTETE));
        
        panneau.add(panneauTitreComplet, BorderLayout.NORTH);
        panneau.add(scrollPane, BorderLayout.CENTER);
        
        return panneau;
    }
    
    /**
     * Charge les articles du menu depuis le contrôleur
     */
    private void chargerMenu() {
        // Vider toutes les listes
        entreesModel.clear();
        platsModel.clear();
        dessertsModel.clear();
        boissonsModel.clear();
        
        // Récupérer tous les articles
        List<ArticleMenu> articles = menuController.getAllArticles();
        
        // Trier par catégorie
        for (ArticleMenu article : articles) {
            String categorie = article.getCategorie().toLowerCase();
            String articleTexte = article.toString();
            
            switch (categorie) {
                case "entrée":
                    entreesModel.addElement(articleTexte);
                    break;
                case "plat":
                    platsModel.addElement(articleTexte);
                    break;
                case "dessert":
                    dessertsModel.addElement(articleTexte);
                    break;
                case "boisson":
                    boissonsModel.addElement(articleTexte);
                    break;
                default:
                    // Pour les autres catégories, on peut les ajouter aux plats par défaut
                    platsModel.addElement(articleTexte);
                    break;
            }
        }
    }
    
    /**
     * Ouvre le dialogue d'ajout d'article
     */
    private void ouvrirDialogueAjout() {
        new DialogueAjoutArticle(this);
    }
    
    /**
     * Ouvre le dialogue d'ajout d'article pour une catégorie spécifique
     */
    private void ouvrirDialogueAjoutPourCategorie(String categorie) {
        new DialogueAjoutArticle(this, categorie);
    }
    
    /**
     * Supprime l'article sélectionné
     */
    private void supprimerSelectionne() {
        if (entreesList.getSelectedIndex() != -1) {
            supprimerArticle(entreesList, entreesModel, "Entrée");
        } else if (platsList.getSelectedIndex() != -1) {
            supprimerArticle(platsList, platsModel, "Plat");
        } else if (dessertsList.getSelectedIndex() != -1) {
            supprimerArticle(dessertsList, dessertsModel, "Dessert");
        } else if (boissonsList.getSelectedIndex() != -1) {
            supprimerArticle(boissonsList, boissonsModel, "Boisson");
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Veuillez sélectionner un article à supprimer.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Supprime un article d'une liste spécifique
     */
    private void supprimerArticle(JList<String> liste, DefaultListModel<String> modele, String categorie) {
        int index = liste.getSelectedIndex();
        String article = liste.getSelectedValue();
        
        int option = JOptionPane.showConfirmDialog(
            this,
            "Êtes-vous sûr de vouloir supprimer " + article + " du menu?",
            "Confirmer la suppression",
            JOptionPane.YES_NO_OPTION);
            
        if (option == JOptionPane.YES_OPTION) {
            // Rechercher l'index dans la liste complète des articles de cette catégorie
            List<ArticleMenu> articlesCategorie = menuController.getArticlesByCategory(categorie);
            menuController.removeArticleByIndex(index, categorie);
            
            // Rafraîchir le menu
            chargerMenu();
        }
    }
    
   
    
    
    /**
     * Classe interne pour l'ajout d'un nouvel article au menu
     */
    private class DialogueAjoutArticle extends JFrame {
        private static final long serialVersionUID = 1L;
        private JList<String> listeArticles;
        private JComboBox<String> categorieCombo;
        
        // Articles prédéfinis par catégorie
        private final String[] ENTREES_PREDEFINIES = {
            "Salade César (5.00€)",
            "Tomate mozzarella (4.50€)",
            "Soupe à l'oignon (6.00€)",
            "Assiette de charcuterie (7.00€)"
        };
        
        private final String[] PLATS_PREDEFINIS = {
            "Spaghetti bolognaise (10.00€)",
            "Steak frites (12.00€)",
            "Poulet rôti et légumes (11.00€)",
            "Risotto aux champignons (13.00€)",
            "Filet de saumon et patates sautés (14.00€)"
        };
        
        private final String[] DESSERTS_PREDEFINIS = {
            "Salade de fruits (6.00€)",
            "Fondant au chocolat (7.00€)",
            "Crème brûlée (5.00€)",
            "Tarte Tatin (6.00€)",
            "Mousse au chocolat (5.00€)"
        };
        
        private final String[] BOISSONS_PREDEFINIES = {
            "Coca-Cola (3.00€)",
            "IceTea (3.00€)",
            "Café (2.00€)"
        };
        
        /**
         * Constructeur pour l'ajout d'un article (catégorie non spécifiée)
         */
        public DialogueAjoutArticle(MenuDialogue parent) {
            this(parent, null);
        }
        
        /**
         * Constructeur pour l'ajout d'un article avec catégorie spécifiée
         */
        public DialogueAjoutArticle(MenuDialogue parent, String categoriePredefinie) {
            setTitle("Ajouter un article au menu");
            setSize(450, 400);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Panel principal
            JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
            panneauPrincipal.setBackground(COULEUR_FOND);
            panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            // Panneau d'en-tête
            JPanel panneauEntete = new JPanel();
            panneauEntete.setBackground(COULEUR_ENTETE);
            panneauEntete.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JLabel titre = new JLabel("Sélectionner un article", SwingConstants.CENTER);
            titre.setFont(new Font("Arial", Font.BOLD, 16));
            titre.setForeground(Color.WHITE);
            panneauEntete.add(titre);
            
            // Panneau de sélection de catégorie
            JPanel panneauCategorie = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panneauCategorie.setBackground(COULEUR_FOND);
            
            JLabel categorieLabel = new JLabel("Catégorie:");
            String[] categories = {"Entrée", "Plat", "Dessert", "Boisson"};
            categorieCombo = new JComboBox<>(categories);
            
            // Si une catégorie est prédéfinie, la sélectionner
            if (categoriePredefinie != null) {
                for (int i = 0; i < categories.length; i++) {
                    if (categories[i].equalsIgnoreCase(categoriePredefinie)) {
                        categorieCombo.setSelectedIndex(i);
                        break;
                    }
                }
            }
            
            // Mettre à jour la liste des articles quand la catégorie change
            categorieCombo.addActionListener(e -> mettreAJourListeArticles());
            
            panneauCategorie.add(categorieLabel);
            panneauCategorie.add(categorieCombo);
            
            // Liste des articles
            DefaultListModel<String> modeleArticles = new DefaultListModel<>();
            listeArticles = new JList<>(modeleArticles);
            listeArticles.setFont(new Font("Arial", Font.PLAIN, 14));
            listeArticles.setBackground(COULEUR_LISTE);
            
         
            
            JScrollPane scrollPane = new JScrollPane(listeArticles);
            scrollPane.setBorder(BorderFactory.createLineBorder(COULEUR_ENTETE));
            
            // Boutons
            JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            panneauBoutons.setBackground(COULEUR_FOND);
            
            JButton boutonAjouter = creerBouton("Ajouter au menu", COULEUR_BOUTON);
            JButton boutonAnnuler = creerBouton("Annuler", new Color(150, 150, 150));
            
            boutonAjouter.addActionListener(e -> ajouterArticle());
            boutonAnnuler.addActionListener(e -> dispose());
            
            panneauBoutons.add(boutonAjouter);
            panneauBoutons.add(boutonAnnuler);
            
            // Assemblage
            panneauPrincipal.add(panneauEntete, BorderLayout.NORTH);
            panneauPrincipal.add(panneauCategorie, BorderLayout.NORTH);
            panneauEntete.setLayout(new BorderLayout());
            panneauEntete.add(titre, BorderLayout.CENTER);
            panneauEntete.add(panneauCategorie, BorderLayout.SOUTH);
            
            panneauPrincipal.add(scrollPane, BorderLayout.CENTER);
            panneauPrincipal.add(panneauBoutons, BorderLayout.SOUTH);
            
            add(panneauPrincipal);
            
            // Initialiser la liste d'articles
            mettreAJourListeArticles();
            
            setLocationRelativeTo(parent);
            setVisible(true);
        }
        
        /**
         * Met à jour la liste des articles selon la catégorie sélectionnée
         */
        private void mettreAJourListeArticles() {
            DefaultListModel<String> modele = (DefaultListModel<String>) listeArticles.getModel();
            modele.clear();
            
            String categorieSelectionnee = (String) categorieCombo.getSelectedItem();
            String[] articlesAAjouter;
            
            // Sélectionner la liste d'articles selon la catégorie
            switch (categorieSelectionnee) {
                case "Entrée":
                    articlesAAjouter = ENTREES_PREDEFINIES;
                    break;
                case "Plat":
                    articlesAAjouter = PLATS_PREDEFINIS;
                    break;
                case "Dessert":
                    articlesAAjouter = DESSERTS_PREDEFINIS;
                    break;
                case "Boisson":
                    articlesAAjouter = BOISSONS_PREDEFINIES;
                    break;
                default:
                    articlesAAjouter = new String[0];
            }
            
            // Ajouter les articles à la liste
            for (String article : articlesAAjouter) {
                modele.addElement(article);
            }
        }
        
        /**
         * Ajoute l'article sélectionné au menu
         */
        private void ajouterArticle() {
            String articleTexte = listeArticles.getSelectedValue();
            if (articleTexte == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un article à ajouter.",
                    "Aucune sélection",
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            
            try {
                // Extraire le nom et le prix
                // Format attendu: "Nom de l'article (XX.XX€)"
                int indexParentheseOuvrante = articleTexte.lastIndexOf('(');
                int indexParentheseFermante = articleTexte.lastIndexOf(')');
                
                if (indexParentheseOuvrante != -1 && indexParentheseFermante != -1) {
                    String nom = articleTexte.substring(0, indexParentheseOuvrante).trim();
                    String prixTexte = articleTexte.substring(indexParentheseOuvrante + 1, indexParentheseFermante);
                    prixTexte = prixTexte.replace("€", "").trim();
                    double prix = Double.parseDouble(prixTexte);
                    
                    String categorie = (String) categorieCombo.getSelectedItem();
                    
                    // Vérifier si cet article existe déjà dans la catégorie sélectionnée
                    List<ArticleMenu> articlesExistants = menuController.getArticlesByCategory(categorie);
                    boolean articleExiste = false;
                    
                    for (ArticleMenu article : articlesExistants) {
                        if (article.getNom().equals(nom)) {
                            articleExiste = true;
                            break;
                        }
                    }
                    
                    if (articleExiste) {
                        JOptionPane.showMessageDialog(
                            this,
                            "Cet article existe déjà dans le menu.",
                            "Article existant",
                            JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    
                    menuController.addArticle(nom, prix, categorie);
                    chargerMenu();
                    dispose();
                } else {
                    throw new Exception("Format d'article invalide");
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(
                    this,
                    "Erreur lors de l'ajout de l'article: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Méthode pour afficher les informations sur les ingrédients
     */
    private void afficherInformationIngredients() {
        new DialogueIngredientsSimple(this);
    }

    /**
     * Dialogue pour afficher les ingrédients des plats
     */
    private class DialogueIngredientsSimple extends JFrame {
        
        public DialogueIngredientsSimple(MenuDialogue parent) {
            setTitle("Ingrédients des Plats");
            setSize(600, 450);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
            panneauPrincipal.setBackground(COULEUR_FOND);
            panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            // En-tête
            JLabel titre = new JLabel("Ingrédients requis par plat", SwingConstants.CENTER);
            titre.setFont(new Font("Arial", Font.BOLD, 18));
            titre.setForeground(Color.WHITE);
            titre.setBackground(COULEUR_ENTETE);
            titre.setOpaque(true);
            titre.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            // Zone de texte pour afficher les ingrédients
            JTextArea zoneTexte = new JTextArea();
            zoneTexte.setFont(new Font("Arial", Font.PLAIN, 14));
            zoneTexte.setEditable(false);
            zoneTexte.setBackground(COULEUR_LISTE);
            zoneTexte.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            // Ajouter tous les plats et leurs ingrédients
            StringBuilder texte = new StringBuilder();
            
            // Ajouter les entrées
            texte.append("=== ENTRÉES ===\n\n");
            for (String plat : main.data.restaurant.LienPlatStock.getPlatsParCategorie("Entrée")) {
                texte.append(plat).append(" :\n");
                texte.append(main.data.restaurant.LienPlatStock.getDescriptionIngredients(plat));
                texte.append("\n\n");
            }
            
            // Ajouter les plats
            texte.append("=== PLATS ===\n\n");
            for (String plat : main.data.restaurant.LienPlatStock.getPlatsParCategorie("Plat")) {
                texte.append(plat).append(" :\n");
                texte.append(main.data.restaurant.LienPlatStock.getDescriptionIngredients(plat));
                texte.append("\n\n");
            }
            
            // Ajouter les desserts
            texte.append("=== DESSERTS ===\n\n");
            for (String plat : main.data.restaurant.LienPlatStock.getPlatsParCategorie("Dessert")) {
                texte.append(plat).append(" :\n");
                texte.append(main.data.restaurant.LienPlatStock.getDescriptionIngredients(plat));
                texte.append("\n\n");
            }
            
            // Ajouter les boissons
            texte.append("=== BOISSONS ===\n\n");
            for (String plat : main.data.restaurant.LienPlatStock.getPlatsParCategorie("Boisson")) {
                texte.append(plat).append(" :\n");
                texte.append(main.data.restaurant.LienPlatStock.getDescriptionIngredients(plat));
                texte.append("\n\n");
            }
            
            zoneTexte.setText(texte.toString());
            
            JScrollPane scrollPane = new JScrollPane(zoneTexte);
            scrollPane.setBorder(BorderFactory.createLineBorder(COULEUR_ENTETE));
            
            // Bouton de fermeture
            JButton boutonFermer = creerBouton("Fermer", COULEUR_BOUTON);
            boutonFermer.addActionListener(e -> dispose());
            
            JPanel panneauBouton = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panneauBouton.setBackground(COULEUR_FOND);
            panneauBouton.add(boutonFermer);
            
            // Assembler l'interface
            panneauPrincipal.add(titre, BorderLayout.NORTH);
            panneauPrincipal.add(scrollPane, BorderLayout.CENTER);
            panneauPrincipal.add(panneauBouton, BorderLayout.SOUTH);
            
            add(panneauPrincipal);
            
            setLocationRelativeTo(parent);
            setVisible(true);
        }
    }
  
}