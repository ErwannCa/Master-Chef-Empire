// Contrôleur principal pour la simulation du restaurant
package main.gestion;

import main.data.inventaire.Menu;

import main.data.inventaire.*;
import main.data.personnel.*;
import main.data.restaurant.*;
import main.gestion.*;
import main.affichage.dialogue.StockDialogue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import javax.swing.JOptionPane;

/**
 * Contrôleur principal qui gère l'ensemble de la simulation du restaurant
 */
public class RestaurantGestion {
    // Contrôleurs associés
    private EmployeeGestion employeeController;
    private MenuGestion menuController;
    private StockGestion stockController;
    private StatisticsGestion statsController;
    
    // Éléments du restaurant
    private List<Table> tables;
    private List<Client> clients;
    private List<Decoration> decorations;
    private Cuisine cuisine;
    private List<CommandeEnAttente> commandesEnAttente;
    
    // Données de simulation
    private double revenu;
    private double satisfaction;
    private boolean enFonctionnement;
    private int compteurClients = 0;
    private boolean enPause = false;
    
    // Gestion des alertes
    private Map<String, Long> derniereAlerte = new HashMap<>();
    private boolean fenetreStockOuverte = false;
    
    // Constantes
    private static final double REVENU_INITIAL = 10000.0;
    private static final double SATISFACTION_INITIALE = 50.0;
    private static final int SEUIL_PAIEMENT_SALAIRES = 50;
    private static final int MAX_TABLES_PAR_SERVEUR = 4;
    private static final long DELAI_ENTRE_ALERTES = 6000; // 60 secondes en millisecondes
    private Map<String, Integer> compteurDepuisDerniereAlerte = new HashMap<>();
    private Map<String, List<LienPlatStock.IngredientQuantite>> ingredientsManquantsEnAttente = new HashMap<>();
    private boolean alerteEnAttente = false;
    private int compteurAlerte = 0;
    private static final int SEUIL_DELAI_ENTRE_ALERTES = 30; // ~6 secondes à 5 mises à jour par seconde
    private static final int SEUIL_GROUPEMENT_ALERTES = 15; // ~3 secondes
    
    /**
     * Constructeur - Initialise le restaurant et démarre la simulation
     */
    public RestaurantGestion() {
        // Initialisation des éléments du restaurant
        this.tables = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.decorations = new ArrayList<>();
        this.cuisine = new Cuisine(false);
        this.commandesEnAttente = new ArrayList<>();
        
        // Initialisation des données de simulation
        this.revenu = REVENU_INITIAL;
        this.satisfaction = SATISFACTION_INITIALE;
        this.enFonctionnement = true;
        
        // Configuration initiale
        initialiserTables();
        
        // Initialisation des contrôleurs
        this.employeeController = new EmployeeGestion();
        this.menuController = new MenuGestion();
        this.stockController = new StockGestion(this);  
        this.statsController = new StatisticsGestion();
        this.statsController.setRestaurantController(this);
        
        this.employeeController.setRestaurantGestion(this);
        
        // Attribution des tables aux serveurs existants
        attribuerTablesAuxServeurs();
        
        // Démarrer la boucle de simulation
        demarrerBoucle();
    }
    
    /**
     * Initialise les tables de départ du restaurant
     */
    private void initialiserTables() {
        // Ajouter quelques tables initiales
        tables.add(new Table(3, 2));
        tables.add(new Table(4, 2));
        tables.add(new Table(3, 4));
        tables.add(new Table(4, 4));
    }
    
    /**
     * Attribue les tables aux serveurs selon le modèle: 4 tables max par serveur
     */
    private void attribuerTablesAuxServeurs() {
        List<Serveur> serveurs = getServeurs();
        
        // Réinitialiser l'attribution pour tous les serveurs
        for (Serveur serveur : serveurs) {
            serveur.setTablesAttribuees(new ArrayList<>());
        }
        
        // Attribuer les tables disponibles
        if (!serveurs.isEmpty()) {
            for (int i = 0; i < tables.size(); i++) {
                // Calculer quel serveur est responsable de cette table
                int indexServeur = i / MAX_TABLES_PAR_SERVEUR;
                
                // Si on a assez de serveurs, attribuer la table
                if (indexServeur < serveurs.size()) {
                    serveurs.get(indexServeur).ajouterTable(tables.get(i));
                    System.out.println("Table " + (i + 1) + " attribuée au serveur " + 
                                      serveurs.get(indexServeur).getPrenom());
                } else {
                    System.out.println("Table " + (i + 1) + " non attribuée (pas assez de serveurs)");
                }
            }
            
            // Initialiser tous les serveurs pour qu'ils commencent à travailler
            for (Serveur serveur : serveurs) {
                serveur.retournerEnCuisine();
            }
        } else {
            System.out.println("Aucun serveur disponible pour attribuer les tables");
        }
    }
    
    /**
     * Démarre la boucle principale de la simulation
     */
    private void demarrerBoucle() {
        new Thread(() -> {
            while (enFonctionnement) {
                if (!enPause) {  // Vérifier si le jeu est en pause
                    miseAJourJeu();
                }
                try {
                    Thread.sleep(200); // Rafraîchissement 5 fois par seconde
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    
    /**
     * Met à jour l'état du jeu à chaque pas de simulation
     */
    private void miseAJourJeu() {
        // Ajouter parfois un nouveau client
        if (Math.random() < 0.10 && clients.size() < 10) {
            Table table = trouverTableLibre();
            if (table != null) {
                clients.add(new Client(table));
            }
        }
        
        // Mise à jour des clients
        List<Client> clientsCopie = new ArrayList<>(clients);
        for (Client client : clientsCopie) {
            client.mettreAJour();
            
            // Si client est parti
            if (client.getEtat() == Client.ETAT_SORTIE) {
                // Gagner de l'argent
                if (client.getPlatChoisi() != null) {
                    double prix = client.getPrix();
                    revenu += prix;
                    statsController.enregistrerClientServi();
                    
                    // Incrémenter le compteur de clients servis pour le paiement des salaires
                    compteurClients++;
                    
                    // Vérifier si on doit payer les salaires
                    if (compteurClients >= SEUIL_PAIEMENT_SALAIRES) {
                        payerSalairesEmployes();
                        compteurClients = 0;  // Réinitialiser le compteur
                    }
                }
                clients.remove(client);
                client.partir();
            }
        }
        
        // Mise à jour des serveurs
        List<Serveur> serveurs = getServeurs();
        for (Serveur serveur : serveurs) {
            serveur.mettreAJour();
            
            // Si le serveur est arrivé à une table pour prendre commande
            if (serveur.estEnAttentePourCommande()) {
                for (Client client : clients) {
                    if (client.getTable() == serveur.getTableServie() && 
                        client.estAssis() && !client.aCommande()) {
                        // Prise de commande
                        List<Plat> platsDisponibles = menuController.getPlatsDisponibles();
                        if (platsDisponibles != null && !platsDisponibles.isEmpty()) {
                            Plat platChoisi = client.choisirPlatDansMenu(platsDisponibles);
                            if (platChoisi != null) {
                                client.commander(platChoisi);
                                serveur.commandePrise();
                                
                                // Ajouter la commande à la file d'attente
                                CommandeEnAttente nouvelleCommande = new CommandeEnAttente(client, platChoisi);
                                commandesEnAttente.add(nouvelleCommande);
                                
                                // Le serveur retourne en cuisine
                                serveur.retournerEnCuisine();
                            }
                        }
                        break;
                    }
                }
            }
            // Si le serveur est arrivé à une table pour servir
            else if (serveur.estEnAttentePourService()) {
                for (Client client : clients) {
                    if (client.getTable() == serveur.getTableServie() && 
                        client.aCommande() && !client.estServi()) {
                        
                        // Vérifier si la commande est prête
                        boolean commandePrete = false;
                        CommandeEnAttente aServir = null;
                        
                        for (CommandeEnAttente commande : commandesEnAttente) {
                            if (commande.getClient() == client && commande.isPrete()) {
                                commandePrete = true;
                                aServir = commande;
                                break;
                            }
                        }
                        
                        if (commandePrete) {
                            // Service du plat
                            client.etre_servi();
                            serveur.platServi();
                            
                            // Retirer de la liste d'attente
                            commandesEnAttente.remove(aServir);
                            
                            // Consommer les ingrédients pour ce plat
                            String nomPlatServi = client.getPlatChoisi().getNom();
                            boolean stocksConsommes = LienPlatStock.consommerIngredients(nomPlatServi, stockController);
                            if (!stocksConsommes) {
                                System.out.println("Attention: Stock insuffisant pour " + nomPlatServi);
                                // Afficher message d'alerte pour stocks insuffisants
                                List<LienPlatStock.IngredientQuantite> manquants = 
                                    LienPlatStock.getIngredientsManquants(nomPlatServi, stockController);
                                afficherAlerteStockInsuffisant(nomPlatServi, manquants);
                            }
                            
                            // Le client mangera puis paiera
                            final Client clientFinal = client;
                            new Thread(() -> {
                                try {
                                    Thread.sleep(10000); // 10 secondes pour manger
                                    clientFinal.payer();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }).start();
                        }
                        
                        // Le serveur retourne en cuisine dans tous les cas
                        serveur.retournerEnCuisine();
                        break;
                    }
                }
            }
            // Si le serveur est libre et en cuisine, chercher un client à servir
            else if (!serveur.estEnDeplacement()) {
                // D'abord chercher des clients avec commandes prêtes parmi ses tables attribuées
                boolean assignation = false;
                
                for (CommandeEnAttente commande : commandesEnAttente) {
                    if (commande.isPrete() && !commande.getClient().estServi()) {
                        // Vérifier si le serveur est responsable de cette table
                        Table tableClient = commande.getClient().getTable();
                        if (serveur.estResponsable(tableClient)) {
                            serveur.allerServir(tableClient);
                            assignation = true;
                            break;
                        }
                    }
                }
                
                // Si aucune commande prête, chercher des clients qui attendent pour commander
                if (!assignation) {
                    for (Client client : clients) {
                        if (client.estAssis() && !client.aCommande()) {
                            // Vérifier si le serveur est responsable de cette table
                            if (serveur.estResponsable(client.getTable())) {
                                serveur.allerPrendreCommande(client.getTable());
                                assignation = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        // Mise à jour des cuisiniers
        List<Chef> cuisiniers = getCuisiniers();
        
       
        for (Chef cuisinier : cuisiniers) {
            cuisinier.mettreAJour();
        }
        
        for (Chef cuisinier : cuisiniers) {
            cuisinier.setStockController(stockController);
        }
        
        // Ensuite, affecter des commandes aux cuisiniers libres
        for (Chef cuisinier : cuisiniers) {
            if (!cuisinier.isOccupe()) {
                // Chercher une commande en attente non préparée
                for (CommandeEnAttente commande : commandesEnAttente) {
                    if (!commande.isEnPreparation() && !commande.isPrete()) {
                        cuisinier.preparerCommande(commande);
                        break;
                    }
                }
            }
        }
        
        // Mise à jour des employés (autres que serveurs et cuisiniers)
        employeeController.mettreAJourEmployes();
        
        // Calcul de la satisfaction
        mettreAJourSatisfaction();
        
        verifierAlertes();
    }
    
    /**
     * Gestion améliorée des alertes de stock insuffisant
     */
    public void afficherAlerteStockInsuffisant(String nomPlat, List<LienPlatStock.IngredientQuantite> ingredientsManquants) {
        // Si aucun ingrédient manquant, ne rien faire
        if (ingredientsManquants == null || ingredientsManquants.isEmpty()) {
            return;
        }
        
        // Vérifier s'il y a eu une alerte récente globale
        Integer dernierCompteur = compteurDepuisDerniereAlerte.get("global");
        
        if (dernierCompteur != null && (compteurAlerte - dernierCompteur) < SEUIL_DELAI_ENTRE_ALERTES) {
            // Une alerte globale a été affichée récemment, ne pas en afficher une autre
            return;
        }
        
        // Ajouter ces ingrédients à la liste d'attente
        ingredientsManquantsEnAttente.put(nomPlat, ingredientsManquants);
        
        // Si une alerte n'est pas déjà en attente, démarrer un compteur
        if (!alerteEnAttente) {
            alerteEnAttente = true;
            compteurDepuisDerniereAlerte.put("compteurDeclenchement", compteurAlerte);
        }
    }

    /**
     * Méthode à appeler dans la boucle principale de mise à jour du jeu
     */
    private void verifierAlertes() {
        // Incrémenter le compteur global à chaque mise à jour
        compteurAlerte++;
        
        // S'il y a une alerte en attente et que le délai est écoulé
        if (alerteEnAttente) {
            Integer compteurDeclenchement = compteurDepuisDerniereAlerte.get("compteurDeclenchement");
            
            if (compteurDeclenchement != null && (compteurAlerte - compteurDeclenchement) >= SEUIL_GROUPEMENT_ALERTES) {
                // Le délai est écoulé, afficher l'alerte groupée
                afficherAlerteGroupee();
            }
        }
    }

  
    /**
     * Affiche une alerte groupée pour tous les ingrédients manquants en attente
     */
    private void afficherAlerteGroupee() {
        // Mettre à jour le compteur de dernière alerte globale
        compteurDepuisDerniereAlerte.put("global", compteurAlerte);
        
        // Si aucun ingrédient en attente, ne rien faire
        if (ingredientsManquantsEnAttente.isEmpty()) {
            alerteEnAttente = false;
            return;
        }
        
        // Construire le message d'alerte groupée
        StringBuilder message = new StringBuilder();
        message.append("Stock insuffisant pour plusieurs plats :\n\n");
        
        // Calculer le coût total des ingrédients manquants
        double coutTotal = 0.0;
        Map<String, Integer> ingredientsUniques = new HashMap<>();
        
        // Regrouper tous les ingrédients uniques
        for (Map.Entry<String, List<LienPlatStock.IngredientQuantite>> entry : ingredientsManquantsEnAttente.entrySet()) {
            String nomPlat = entry.getKey();
            List<LienPlatStock.IngredientQuantite> ingredients = entry.getValue();
            
            message.append("▶ ").append(nomPlat).append(" :\n");
            
            for (LienPlatStock.IngredientQuantite ing : ingredients) {
                String nomIngredient = ing.getNomIngredient();
                int quantite = ing.getQuantite();
                
                message.append("  • ").append(nomIngredient)
                       .append(" (").append(quantite).append(" unités)\n");
                
                // Additionner les quantités pour les ingrédients identiques
                if (ingredientsUniques.containsKey(nomIngredient)) {
                    ingredientsUniques.put(nomIngredient, ingredientsUniques.get(nomIngredient) + quantite);
                } else {
                    ingredientsUniques.put(nomIngredient, quantite);
                }
                
                // Calculer le coût pour cet ingrédient
                double prixUnitaire = 2.0; // Prix par défaut par unité
                coutTotal += prixUnitaire * quantite;
            }
            message.append("\n");
        }
        
        final double coutFinal = coutTotal;
        
        // Demander à l'utilisateur s'il veut acheter tous les ingrédients manquants
        int choix = JOptionPane.showConfirmDialog(
            null,
            message.toString() + "\nVoulez-vous acheter tous ces ingrédients pour environ " 
                + String.format("%.2f", coutFinal) + "€?",
            "Stock insuffisant",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        // Si l'utilisateur choisit oui, acheter tous les ingrédients automatiquement
        if (choix == JOptionPane.YES_OPTION) {
            // Vérifier si on peut payer le montant total
            if (peutPayer(coutFinal)) {
                boolean tousAchetes = true;
                double coutReel = 0.0; // Pour suivre le montant réellement dépensé
                
                // Acheter chaque ingrédient manquant
                for (Map.Entry<String, Integer> ingredient : ingredientsUniques.entrySet()) {
                    String nomIngredient = ingredient.getKey();
                    int quantite = ingredient.getValue();
                    
                    // Calculer le coût pour cet ingrédient (prix unitaire * quantité)
                    double prixUnitaire = 2.0; // Prix par défaut
                    double coutIngredient = prixUnitaire * quantite;
                    
                    // Commander l'ingrédient
                    boolean achatReussi = stockController.commander(nomIngredient, quantite, coutIngredient);
                    
                    if (achatReussi) {
                        coutReel += coutIngredient; // Ajouter le coût réel si achat réussi
                    } else {
                        tousAchetes = false;
                    }
                }
                
                // Déduire le montant total des achats réussis
                if (coutReel > 0) {
                    effectuerPaiement(coutReel);
                    System.out.println("Dépense pour ingrédients: " + coutReel + "€");
                }
                
                // Afficher message de confirmation
                if (tousAchetes) {
                    JOptionPane.showMessageDialog(
                        null,
                        "Tous les ingrédients manquants ont été achetés avec succès pour " + 
                        String.format("%.2f", coutReel) + "€!",
                        "Achat réussi",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                        null,
                        "Certains ingrédients ont été achetés pour " + 
                        String.format("%.2f", coutReel) + "€. D'autres n'ont pas pu être achetés.",
                        "Achat partiel",
                        JOptionPane.WARNING_MESSAGE
                    );
                }
                
                // Mettre à jour l'interface
                forceUIUpdate();
            } else {
                // Pas assez d'argent
                JOptionPane.showMessageDialog(
                    null,
                    "Fonds insuffisants pour acheter tous les ingrédients manquants.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
        
        // Réinitialiser les ingrédients en attente et le statut d'alerte
        ingredientsManquantsEnAttente.clear();
        alerteEnAttente = false;
    }
    /**
     * Paie les salaires de tous les employés
     */
    private void payerSalairesEmployes() {
        double masseSalariale = employeeController.getMasseSalariale();
        
        if (peutPayer(masseSalariale)) {
            // Déduire le montant du revenu
            revenu -= masseSalariale;
            
            // Enregistrer dans les statistiques
            statsController.enregistrerSalaireEmploye(masseSalariale);
            
            // Afficher un message (optionnel)
            System.out.println("Salaires payés: " + masseSalariale + "€");
            
            // Vous pouvez aussi afficher une notification à l'utilisateur
            JOptionPane.showMessageDialog(null, 
                "Les salaires ont été payés: " + masseSalariale + "€", 
                "Paiement des salaires", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            // Message d'erreur si pas assez d'argent
            System.out.println("Fonds insuffisants pour payer les salaires!");
            JOptionPane.showMessageDialog(null, 
                "Fonds insuffisants pour payer les salaires! Il manque " + 
                (masseSalariale - revenu) + "€", 
                "Problème de trésorerie", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    /**
     * Met à jour le niveau de satisfaction des clients
     */
    private void mettreAJourSatisfaction() {
        // Exemple de facteurs qui influencent la satisfaction:
        
        // Si les serveurs sont plus nombreux que les clients, baisse légère
        if (employeeController.compteServeurs() > clients.size()) {
            satisfaction -= 0.5;
        }
        
        // Les décorations augmentent la satisfaction
        double bonusDecoration = decorations.size() * 0.05;
        satisfaction += bonusDecoration;
        
        // Limiter entre 0 et 100
        satisfaction = Math.max(0, Math.min(100, satisfaction));
    }
    
    /**
     * Recherche une table libre pour un client
     */
    public Table trouverTableLibre() {
        for (Table table : tables) {
            if (!table.estOccupee()) {
                return table;
            }
        }
        return null;
    }
    
    /**
     * Vérifie s'il y a assez de tables pour embaucher un nouveau serveur
     */
    public boolean peutEmbaucherServeur() {
        int nombreTables = tables.size();
        int nombreServeurs = getServeurs().size();
        return nombreTables > nombreServeurs * MAX_TABLES_PAR_SERVEUR;
    }
    
    /**
     * Trouve le serveur responsable d'une table spécifique
     */
    public Serveur getServeurPourTable(Table table) {
        for (Serveur serveur : getServeurs()) {
            if (serveur.estResponsable(table)) {
                return serveur;
            }
        }
        return null;
    }
    
    /**
     * Obtient la liste des cuisiniers du restaurant
     */
    private List<Chef> getCuisiniers() {
        List<Chef> cuisiniers = new ArrayList<>();
        
        for (Employe employe : employeeController.getEmployes()) {
            if (employe instanceof Chef) {
                cuisiniers.add((Chef) employe);
            }
        }
        
        return cuisiniers;
    }
    
    /**
     * Méthode appelée quand un nouveau serveur est embauché
     * pour lui attribuer des tables
     */
    public void emploiServeur(Serveur nouveauServeur) {
        // D'abord réattribuer toutes les tables
        attribuerTablesAuxServeurs();
        
        // S'assurer que le serveur est initialisé correctement
        nouveauServeur.retournerEnCuisine();
        
        // Forcer la mise à jour de l'interface
        forceUIUpdate();
        
        // Imprimer un message de confirmation
        List<Table> tablesAttribuees = nouveauServeur.getTablesAttribuees();
        if (tablesAttribuees != null && !tablesAttribuees.isEmpty()) {
            System.out.println("Le serveur " + nouveauServeur.getPrenom() + 
                              " est maintenant responsable de " + tablesAttribuees.size() + " tables.");
        } else {
            System.out.println("Attention: Le serveur " + nouveauServeur.getPrenom() + 
                              " n'a pas de tables attribuées.");
        }
    }
    
    
    /**
     * Méthode appelée quand un serveur est licencié
     * pour réattribuer ses tables
     */
    public void licenciementServeur() {
        // Réattribuer toutes les tables
        attribuerTablesAuxServeurs();
    }
  
    public void forceUIUpdate() {
        // Cette méthode sera implémentée par les composants UI qui observent le modèle
        // Par exemple, elle peut être utilisée pour actualiser la barre de statut
    }
    
    // Méthodes d'accès pour d'autres contrôleurs
    public EmployeeGestion getEmployeeController() {
        return employeeController;
    }
    
    public MenuGestion getMenuController() {
        return menuController;
    }
    
    public StockGestion getStockController() {
        return stockController;
    }
    
    public StatisticsGestion getStatsController() {
        return statsController;
    }
    
    /**
     * Met en pause
     */
    public void basculerPause() {
        this.enPause = !this.enPause;
    }
    
    public boolean estEnPause() {
        return enPause;
    } 
    
    /**
     * Modifie le revenu du restaurant
     */
    public void modifierRevenu(double montant) {
        this.revenu += montant;
    }
    
    /**
     * Vérifie si le restaurant peut payer un montant donné
     */
    public boolean peutPayer(double montant) {
        return revenu >= montant;
    }
    
    /**
     * Effectue un paiement si possible
     */
    public boolean effectuerPaiement(double montant) {
        if (peutPayer(montant)) {
            revenu -= montant;
            return true;
        }
        return false;
    }
    
    /**
     * Ajoute une table à l'emplacement spécifié et réattribue les tables
     */
    public boolean ajouterTable(int colonne, int ligne) {
        // Vérifier si l'emplacement est déjà occupé
        for (Table table : tables) {
            if (table.getLigne() == ligne && table.getColonne() == colonne) {
                return false;
            }
        }
        
        tables.add(new Table(colonne, ligne));
        
        // Réattribuer les tables aux serveurs après l'ajout
        attribuerTablesAuxServeurs();
        
        return true;
    }
    
    /**
     * Ajoute une décoration au restaurant
     */
    public void ajouterDecoration(Decoration decoration) {
        decorations.add(decoration);
    }
    
    /**
     * Ajoute une décoration au restaurant en spécifiant ses propriétés
     */
    public void ajouterDecoration(String type, int row, int col) {
        decorations.add(new Decoration(type, row, col));
    }
    
    // Accesseurs et mutateurs divers
    public double getRevenu() {
        return revenu;
    }
    
    public void setRevenu(double revenu) {
        this.revenu = revenu;
    }
    
    public double getSatisfaction() {
        return satisfaction;
    }
    
    public List<Table> getTables() {
        return tables;
    }
    
    public List<Client> getClients() {
        return clients;
    }
    
    public List<Decoration> getDecorations() {
        return decorations;
    }
    
    /**
     * Obtient la liste des serveurs du restaurant
     */
    public List<Serveur> getServeurs() {
        List<Serveur> serveurs = new ArrayList<>();
        if (employeeController == null || employeeController.getEmployes() == null) {
            return serveurs; // Liste vide mais non-null
        }
        
        for (Employe employe : employeeController.getEmployes()) {
            if (employe instanceof Serveur) {
                serveurs.add((Serveur) employe);
            }
        }
        return serveurs;
    }
    
    public Cuisine getCuisine() {
        return cuisine;
    }
    
    /**
     * Récupère la liste des commandes en attente
     */
    public List<CommandeEnAttente> getCommandesEnAttente() {
        return commandesEnAttente;
    }
    
    /**
     * Arrête la simulation du restaurant
     */
    public void arreter() {
        this.enFonctionnement = false;
    }
}  