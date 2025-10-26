package main.data.personnel;

import main.data.restaurant.*;
import main.data.restaurant.*;
import main.gestion.StockGestion;
import java.util.List;

public class Chef extends Employe {
    // Limites de déplacement pour les chefs
    private static final int MIN_LIGNE = 1;
    private static final int MAX_LIGNE = 5;
    private static final int MIN_COLONNE = 12;
    private static final int MAX_COLONNE = 14;
    private boolean occupe;
    private CommandeEnAttente commandeEnCours;
    private long tempsPrepDebut;
    private StockGestion stockController;
    
    // Définir les temps de préparation pour chaque niveau (en millisecondes)
    private static final long TEMPS_NIVEAU_1 = 30000; // 30 secondes
    private static final long TEMPS_NIVEAU_2 = 25000; // 25 secondes
    private static final long TEMPS_NIVEAU_3 = 20000; // 20 secondes
    private static final long TEMPS_NIVEAU_4 = 15000; // 15 secondes
    private static final long TEMPS_NIVEAU_5 = 10000; // 10 secondes
    
    public Chef() {
        super("CUISINIER", "Paul", 2000, 2, 3, 11);
        this.estChef = true;
        this.occupe = false;
    }
    
    public Chef(String nom, String prenom, double salaire, int niveau) {
        super(nom, prenom, salaire, niveau, 3, 11);
        this.estChef = true;
        this.occupe = false;
    }
    
    /**
     * Calcule le temps de préparation en fonction du niveau du chef
     */
    private long getTempsPreparation() {
        switch (niveau) {
            case 1:
                return TEMPS_NIVEAU_1;
            case 2:
                return TEMPS_NIVEAU_2;
            case 3:
                return TEMPS_NIVEAU_3;
            case 4:
                return TEMPS_NIVEAU_4;
            case 5:
                return TEMPS_NIVEAU_5;
            default:
                // Si le niveau est invalide, utiliser le temps du niveau 3
                return TEMPS_NIVEAU_3;
        }
    }
    
    /**
     * Mise à jour de l'état du chef
     * Vérifie si la préparation en cours est terminée
     */
    public void mettreAJour() {
        deplacerAleatoirement();
        // Si le chef est en train de préparer une commande
        if (occupe && commandeEnCours != null) {
            long tempsEcoule = System.currentTimeMillis() - tempsPrepDebut;
            
            // Si le temps de préparation est écoulé
            if (tempsEcoule >= getTempsPreparation()) {
                terminerPreparation();
            }
        }
    }
    
    public boolean isOccupe() {
        return occupe;
    }
    
    public void deplacerAleatoirement() {
        int direction = (int)(Math.random() * 4);
        switch(direction) {
            case 0: deplacerHaut(); break;
            case 1: deplacerBas(); break;
            case 2: deplacerGauche(); break;
            case 3: deplacerDroite(); break;
        }        
        // Garder le chef dans les limites d'une zone définie
        position.setLigne(Math.max(MIN_LIGNE, Math.min(MAX_LIGNE, position.getLigne())));
        position.setColonne(Math.max(MIN_COLONNE, Math.min(MAX_COLONNE, position.getColonne())));
    }
    
    public void preparerCommande(CommandeEnAttente commande) {
        // Avant de commencer la préparation, vérifier la disponibilité des ingrédients
        List<LienPlatStock.IngredientQuantite> ingredientsManquants = 
            LienPlatStock.getIngredientsManquants(commande.getPlat().getNom(), getStockController());
        
        if (!ingredientsManquants.isEmpty()) {
            // Message console
            System.out.println("Impossible de préparer " + commande.getPlat().getNom() + ": ingrédients manquants");
            
            // Alerte graphique avec option d'achat
            if (getStockController() != null && 
                getStockController().getRestaurantController() != null) {
                getStockController().getRestaurantController().afficherAlerteStockInsuffisant(
                    commande.getPlat().getNom(), ingredientsManquants);
            }
            return;
        }
        
        if (!occupe) {
            this.commandeEnCours = commande;
            this.occupe = true;
            this.tempsPrepDebut = System.currentTimeMillis();
            commande.setEnPreparation(true);
            
            System.out.println(prenom + " " + nom + " commence à préparer: " + commande.getPlat().getNom() + 
                             " (Temps estimé: " + (getTempsPreparation() / 1000) + " secondes)");
        }
    }
    private void terminerPreparation() {
        if (commandeEnCours != null) {
            System.out.println(prenom + " " + nom + " a terminé de préparer: " + commandeEnCours.getPlat().getNom());
            commandeEnCours.setPrete(true);
            occupe = false;
            commandeEnCours = null;
        }
    }
    
    private StockGestion getStockController() {
        return stockController;
    }
    
    public CommandeEnAttente getCommandeEnCours() {
        return commandeEnCours;
    }
    
    public void deplacerHaut() {
        position.setLigne(position.getLigne() - 1);
    }
    
    public void deplacerBas() {
        position.setLigne(position.getLigne() + 1);
    }
    
    public void deplacerGauche() {
        position.setColonne(position.getColonne() - 1);
    }
    
    public void deplacerDroite() {
        position.setColonne(position.getColonne() + 1);
    }
    
    public void setStockController(StockGestion stockController) {
        this.stockController = stockController;
    }
    
    
}