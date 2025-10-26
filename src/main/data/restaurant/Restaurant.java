package main.data.restaurant;

import java.util.ArrayList;

import java.util.List;

import main.data.inventaire.Menu;
import main.data.inventaire.Stock;
import main.data.personnel.Client;
import main.data.personnel.Employe;
import main.data.personnel.Serveur;
import main.gestion.*;

/**
 * Classe principale qui représente le restaurant dans son ensemble.
 * Elle contient toutes les données du modèle.
 */
public class Restaurant {
    // Éléments physiques du restaurant
    private List<Table> tables;
    private List<Decoration> decorations;
    private Cuisine cuisine;
    
    // Personnes dans le restaurant
    private List<Employe> employes;
    private List<Client> clients;
    private List<Serveur> serveurs;
    
    // Inventaire et finances
    private Stock stock;
    private StockGestion stockController;
    private Menu menu;
    private double revenu;
    private float satisfaction;
    
    // Statistiques
    private int clientsServis;
    private List<Long> tempsServiceClients;
    private double depensesSalaires;
    private double depensesStock;
    private double depensesAmeliorations;
    
    // État du restaurant
    private boolean enFonctionnement;
    
    /**
     * Constructeur - Initialise le restaurant avec ses éléments de base
     */
    public Restaurant() {
        // Initialiser les listes
        this.tables = new ArrayList<>();
        this.decorations = new ArrayList<>();
        this.employes = new ArrayList<>();
        this.clients = new ArrayList<>();
        this.serveurs = new ArrayList<>();
        this.tempsServiceClients = new ArrayList<>();
        
        // Créer les composants de base
        this.cuisine = new Cuisine();
        this.stockController = new StockGestion();
        this.menu = new Menu();
        
        // Initialiser les valeurs
        this.revenu = 10000.0;  // Capital de départ
        this.satisfaction = 50.0f;
        this.clientsServis = 0;
        this.depensesSalaires = 0.0;
        this.depensesStock = 0.0;
        this.depensesAmeliorations = 0.0;
        this.enFonctionnement = true;
        
        // Ajouter le chef issu de la cuisine aux employés
        this.employes.add(cuisine.getChef());
    }
    
    /**
     * Trouve une table libre pour placer un cliente
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
     * Ajoute une table au restaurant
     */
    public void ajouterTable(int colonne, int ligne) {
        this.tables.add(new Table(colonne, ligne));
    }
    
    /**
     * Ajoute une décoration au restaurant
     */
    public void ajouterDecoration(Decoration decoration) {
        this.decorations.add(decoration);
    }
    
    /**
     * Ajoute un employé au restaurant
     */
    public void ajouterEmploye(Employe employe) {
        this.employes.add(employe);
        
        // Si c'est un serveur, l'ajouter aussi à la liste des serveurs
        if (!employe.isEstChef() && employe instanceof Serveur) {
            this.serveurs.add((Serveur) employe);
        }
    }
    
    /**
     * Ajoute un client au restaurant
     */
    public void ajouterClient(Client client) {
        this.clients.add(client);
    }
    
    /**
     * Effectue un paiement (réduit le revenu)

     */
    public boolean effectuerPaiement(double montant) {
        if (revenu >= montant) {
            revenu -= montant;
            return true;
        }
        return false;
    }
    
    /**
     * Enregistre un client servi
     */
    public void enregistrerClientServi(long tempsService) {
        clientsServis++;
        tempsServiceClients.add(tempsService);
        revenu += 10;  // Revenu standard par client
    }
    
    /**
     * Enregistre un salaire payé
     */
    public void enregistrerSalaire(double salaire) {
        depensesSalaires += salaire;
    }
    
    /**
     * Enregistre un achat de stock
     */
    public void enregistrerAchatStock(double cout) {
        depensesStock += cout;
    }
    
    /**
     * Enregistre une dépense pour amélioration
     */
    public void enregistrerAmelioration(double cout) {
        depensesAmeliorations += cout;
    }
    
    /**
     * Calcule le temps moyen de service
     */
    public double getTempsMoyenService() {
        if (tempsServiceClients.isEmpty()) return 0;
        
        long total = 0;
        for (long t : tempsServiceClients) {
            total += t;
        }
        return (total / tempsServiceClients.size()) / 60000.0; // ms → minutes
    }
    
    /**
     * Calcule le bénéfice net
     */
    public double getBeneficeNet() {
        return revenu - (depensesSalaires + depensesStock + depensesAmeliorations);
    }
    
    /**
     * Calcule la popularité du restaurant (sur 100)
     */
    public int getPopularite() {
        double decoBonus = decorations.size() * 1.2; // Bonus pour décorations
        double serviceBonus = Math.max(0, 100 - getTempsMoyenService()); // Bonus pour service rapide
        return (int) Math.min(100, (satisfaction + decoBonus + serviceBonus) / 3);
    }
    
    // Getters et setters
    
    public List<Table> getTables() {
        return tables;
    }
    
    public List<Decoration> getDecorations() {
        return decorations;
    }
    
    public Cuisine getCuisine() {
        return cuisine;
    }
    
    public List<Employe> getEmployes() {
        return employes;
    }
    
    public List<Client> getClients() {
        return clients;
    }
    
    public List<Serveur> getServeurs() {
        return serveurs;
    }
    
    public Stock getStock() {
        return stock;
    }
    
    public Menu getMenu() {
        return menu;
    }
    
    public double getRevenu() {
        return revenu;
    }
    
    public void setRevenu(double revenu) {
        this.revenu = revenu;
    }
    
    public float getSatisfaction() {
        return satisfaction;
    }
    
    public void setSatisfaction(float satisfaction) {
        this.satisfaction = Math.max(0, Math.min(100, satisfaction));
    }
    
    public int getClientsServis() {
        return clientsServis;
    }
    
    public double getDepensesSalaires() {
        return depensesSalaires;
    }
    
    public double getDepensesStock() {
        return depensesStock;
    }
    
    public double getDepensesAmeliorations() {
        return depensesAmeliorations;
    }
    
    public double getDepensesTotales() {
        return depensesSalaires + depensesStock + depensesAmeliorations;
    }
    
    public boolean isEnFonctionnement() {
        return enFonctionnement;
    }
    
    public void setEnFonctionnement(boolean enFonctionnement) {
        this.enFonctionnement = enFonctionnement;
    }
}