package main.gestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur qui gère les statistiques du restaurant
 */
public class StatisticsGestion {
    // Données statistiques
    private int clientsServis;
    private List<Long> tempsServiceClients;
    private List<Double> satisfactionClients;
    private double depensesSalaires;
    private double depensesStock;
    private double depensesAmeliorations;
    private RestaurantGestion restaurantController;
    
    /**
     * Constructeur - Initialise les données statistiques
     */
    public StatisticsGestion() {
        this.clientsServis = 0;
        this.tempsServiceClients = new ArrayList<>();
        this.satisfactionClients = new ArrayList<>();
        this.depensesSalaires = 0;
        this.depensesStock = 0;
        this.depensesAmeliorations = 0;
    }
    
    /**
     * Définit le contrôleur du restaurant associé
     */
    public void setRestaurantController(RestaurantGestion restaurantController) {
        this.restaurantController = restaurantController;
    }
    
    /**
     * Enregistre un client servi avec son temps de service
     */
    public void enregistrerClientServi() {
        clientsServis++;
        
        // Temps de service aléatoire entre 0 et 10 seconde (en ms)
        long tempsService = (long) (Math.random() * 6000);
        tempsServiceClients.add(tempsService);
        
        // Satisfaction aléatoire pour ce client (60-100%)
        double satisfaction = 60 + Math.random() * 40;
        satisfactionClients.add(satisfaction);
    }
    
    /**
     * Enregistre un salaire payé à un employé
     */
    public void enregistrerSalaireEmploye(double salaire) {
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
     * Retourne le nombre de clients servis
     */
    public int getClientsServis() {
        return clientsServis;
    }
    
    /**
     * Calcule la satisfaction moyenne des clients
     */
    public double getSatisfactionMoyenne() {
        if (satisfactionClients.isEmpty()) {
            // Si aucun client, retourner la satisfaction actuelle du restaurant
            return restaurantController != null ? restaurantController.getSatisfaction() : 50.0;
        }
        
        double total = 0;
        for (double s : satisfactionClients) {
            total += s;
        }
        return total / satisfactionClients.size();
    }
 
    
    /**
     * Retourne le revenu total du restaurant
     */
    public double getRevenuTotal() {
        if (restaurantController == null) return 0;
        return restaurantController.getRevenu();
    }
    
    /**
     * Retourne les dépenses en salaires
     */
    public double getDepensesSalaires() {
        return depensesSalaires;
    }
    
    /**
     * Retourne les dépenses en stock
     */
    public double getDepensesStocks() {
        return depensesStock;
    }
    
    /**
     * Retourne les dépenses en améliorations
     */
    public double getDepensesAmeliorations() {
        return depensesAmeliorations;
    }
    
    /**
     * Calcule les dépenses totales

     */
    public double getDepensesTotales() {
        return depensesSalaires + depensesStock + depensesAmeliorations;
    }
    
    /**
     * Calcule le bénéfice net (revenu - dépenses)
     */
    public double getBeneficeNet() {
        if (restaurantController == null) return 0;
        return restaurantController.getRevenu() - getDepensesTotales();
    }
    
    /**
     * Calcule la popularité du restaurant (0-100%)
     */
    public int getPopularite() {
        if (restaurantController == null) return 50;
        
        // Facteurs influençant la popularité
        double satisfactionFactor = getSatisfactionMoyenne();
        double decoBonus = restaurantController.getDecorations().size() * 2;
       
        
        // Calcul de la popularité (moyenne)
        return (int) Math.min(100, (satisfactionFactor * 0.5 + decoBonus * 0.3 ));
    }
    
}