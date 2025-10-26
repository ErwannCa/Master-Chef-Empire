package main.data.personnel;

import java.util.Random;
import main.data.restaurant.Table;
import main.data.inventaire.Plat;

public class Client {
    // États possibles d'un client
    public static final int ETAT_ENTREE = 0;
    public static final int ETAT_ASSIS = 1;
    public static final int ETAT_COMMANDE_PASSEE = 2;
    public static final int ETAT_SERVI = 3;
    public static final int ETAT_PAIEMENT = 4;
    public static final int ETAT_SORTIE = 5;
    
    private Table table;
    private int ligne, colonne;
    private int etat;
    private Plat platChoisi;
    private int compteurAttente; 
    private double satisfaction;
    private Random random;
    private String nomPlatAffiche;
    
    public Client(Table table) {
        this.table = table;
        this.etat = ETAT_ENTREE;
        this.ligne = table.getLigne();
        this.colonne = table.getColonne();
        this.satisfaction = 100.0;
        this.compteurAttente = 0; // Initialiser à 0
        this.random = new Random();
        
        // Marquer la table comme occupée
        table.setOccupee(true);
    }
    
    public void mettreAJour() {
        // Si le client vient de s'asseoir
        if (etat == ETAT_ENTREE) {
            etat = ETAT_ASSIS;
        }
        
        // Incrémenter le compteur d'attente
        compteurAttente++;
        
        // Réduire la satisfaction si l'attente est longue (50 cycles ≈ 10 secondes)
        if (compteurAttente > 50) {
            satisfaction -= 0.1;
        }
    }
    
    public boolean estAssis() {
        return etat == ETAT_ASSIS;
    }
    
    public boolean aCommande() {
        return etat >= ETAT_COMMANDE_PASSEE;
    }
    
    public boolean estServi() {
        return etat >= ETAT_SERVI;
    }
    
    public void commander(Plat plat) {
        this.platChoisi = plat;
        this.etat = ETAT_COMMANDE_PASSEE;
        this.compteurAttente = 0; // Réinitialiser le compteur d'attente
        
        // Sauvegarder le nom du plat pour l'affichage
        this.nomPlatAffiche = plat.getNom();
    }
    
    public String getNomPlatAffiche() {
        return nomPlatAffiche;
    }
    
    public void etre_servi() {
        this.etat = ETAT_SERVI;
        
        // Calculer la satisfaction basée sur le temps d'attente
        double satisfactionService = Math.max(0, 100 - (compteurAttente * 0.2));
        this.satisfaction = (this.satisfaction + satisfactionService) / 2;
    }
    
    public void payer() {
        this.etat = ETAT_PAIEMENT;
        
        this.etat = ETAT_SORTIE;
    }
    
    public void partir() {
        this.table.setOccupee(false);
    }
    
    // Méthode pour choisir un plat au hasard dans le menu
    public Plat choisirPlatDansMenu(java.util.List<Plat> platsDisponibles) {
        if (platsDisponibles == null || platsDisponibles.isEmpty()) {
            return null;
        }
        
        int index = random.nextInt(platsDisponibles.size());
        return platsDisponibles.get(index);
    }
    
    // Getters et setters
    public Table getTable() {
        return table;
    }
    
    public Plat getPlatChoisi() {
        return platChoisi;
    }
    
    public int getEtat() {
        return etat;
    }
    
    public double getSatisfaction() {
        return satisfaction;
    }
    
    public int getLigne() {
        return ligne;
    }
    
    public int getColonne() {
        return colonne;
    }
    
    public double getPrix() {
        return platChoisi != null ? platChoisi.getPrix() : 0;
    }
    
    public int getCompteurAttente() {
        return compteurAttente;
    }
}