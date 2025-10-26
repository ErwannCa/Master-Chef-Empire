package main.data.personnel;

import main.data.restaurant.Table;
import main.data.inventaire.Plat;
import java.util.ArrayList;
import java.util.List;

public class Serveur extends Employe {
    private Table tableServie;
    private boolean enDeplacement;
    private int cibleLigne, cibleColonne;
    private boolean attendCommande = false;
    private boolean attendService = false;
    private int positionCuisineLigne = 2;
    private int positionCuisineColonne = 10;
    
    // Liste des tables dont ce serveur est responsable
    private List<Table> tablesAttribuees;
    
    // Constantes
    private static final int CUISINE_LIGNE = 2;
    private static final int CUISINE_COLONNE = 10;
    
    /**
     * Constructeur pour la classe Serveur
     */
    public Serveur(String prenom, int ligne, int colonne) {
        super("Serveur", prenom, 1200.0, 1, ligne, colonne);
        this.enDeplacement = false;
        this.positionCuisineLigne = ligne;
        this.positionCuisineColonne = colonne;
        this.tablesAttribuees = new ArrayList<>();
    }
    
    public Serveur(String nom, String prenom, double salaire, int niveau, int ligne, int colonne) {
        super(nom, prenom, salaire, niveau, ligne, colonne);
        this.enDeplacement = false;
        this.positionCuisineLigne = ligne;
        this.positionCuisineColonne = colonne;
        this.tablesAttribuees = new ArrayList<>();
    }
    
    public Serveur(String nom, String prenom, double salaire, int niveau) {
        super(nom, prenom, salaire, niveau, 0, 0); // Position par défaut
        this.estChef = false;
        this.tablesAttribuees = new ArrayList<>();
    }
    
    /**
     * Définit les tables attribuées à ce serveur
     */
    public void setTablesAttribuees(List<Table> tables) {
        this.tablesAttribuees = tables;
        
        // Si le serveur a des tables, il doit retourner en cuisine pour commencer à travailler
        if (tables != null && !tables.isEmpty()) {
            retournerEnCuisine();
        }
    }
    
    /**
     * Ajoute une table aux responsabilités du serveur
     */
    public void ajouterTable(Table table) {
        // Initialiser la liste si elle est null
        if (tablesAttribuees == null) {
            tablesAttribuees = new ArrayList<>();
        }
        
        // Vérifier que la table n'est pas déjà attribuée à ce serveur
        if (!tablesAttribuees.contains(table)) {
            tablesAttribuees.add(table);
            System.out.println("Table ajoutée au serveur " + this.getPrenom());
        }
    }
    
    /**
     * Vérifie si le serveur est responsable d'une table spécifique
     */
    public boolean estResponsable(Table table) {
        return tablesAttribuees.contains(table);
    }
    
    /**
     * Retourne la liste des tables dont ce serveur est responsable
     */
    public List<Table> getTablesAttribuees() {
        return tablesAttribuees;
    }
    
    /**
     * Définit la position de cuisine personnalisée pour ce serveur
     */
    public void setPositionCuisine(int ligne, int colonne) {
        this.positionCuisineLigne = ligne;
        this.positionCuisineColonne = colonne;
    }

    @Override
    public void mettreAJour() {
        if (enDeplacement) {
            if (getLigne() == cibleLigne && getColonne() == cibleColonne) {
                enDeplacement = false;
                
                // Une fois arrivé à destination, effectuer l'action appropriée
                if (tableServie != null) {
                    if (attendCommande) {
                        // Le serveur est arrivé à la table pour prendre commande
                        System.out.println("Serveur " + getPrenom() + " est arrivé à la table pour prendre commande");
                        // La commande sera prise dans RestaurantController
                    } else if (attendService) {
                        // Le serveur est arrivé à la table pour servir
                        System.out.println("Serveur " + getPrenom() + " est arrivé à la table pour servir");
                        // Le service sera fait dans RestaurantController
                    }
                }
            } else {
                deplacerVers(cibleLigne, cibleColonne);
            }
        }
    }
    
    // Déplacement pas à pas vers la cible
    private void deplacerVers(int cibleLigne, int cibleColonne) {
        int ligneActuelle = getLigne();
        int colonneActuelle = getColonne();
        
        // Déplacement d'un pas à la fois
        if (ligneActuelle < cibleLigne) {
            setPosition(ligneActuelle + 1, colonneActuelle);
        } else if (ligneActuelle > cibleLigne) {
            setPosition(ligneActuelle - 1, colonneActuelle);
        } else if (colonneActuelle < cibleColonne) {
            setPosition(ligneActuelle, colonneActuelle + 1);
        } else if (colonneActuelle > cibleColonne) {
            setPosition(ligneActuelle, colonneActuelle - 1);
        }
    }
    
    // Service d'une table pour prise de commande
    public void allerPrendreCommande(Table table) {
        // Vérifier si le serveur est responsable de cette table
        if (!estResponsable(table)) {
            System.out.println("Serveur " + getPrenom() + " n'est pas responsable de cette table");
            return;
        }
        
        this.tableServie = table;
        this.cibleLigne = table.getLigne();
        this.cibleColonne = table.getColonne();
        this.enDeplacement = true;
        this.attendCommande = true;
        this.attendService = false;
        System.out.println("Serveur " + getPrenom() + " va prendre commande à la table " + 
                         tablesAttribuees.indexOf(table) + 1);
    }
    
    // Service d'une table pour livrer le plat
    public void allerServir(Table table) {
        // Vérifier si le serveur est responsable de cette table
        if (!estResponsable(table)) {
            System.out.println("Serveur " + getPrenom() + " n'est pas responsable de cette table");
            return;
        }
        
        this.tableServie = table;
        this.cibleLigne = table.getLigne();
        this.cibleColonne = table.getColonne();
        this.enDeplacement = true;
        this.attendCommande = false;
        this.attendService = true;
        System.out.println("Serveur " + getPrenom() + " va servir la table " + 
                         (tablesAttribuees.indexOf(table) + 1));
    }
    
    // Retourner en cuisine à sa position personnalisée
    public void retournerEnCuisine() {
        this.cibleLigne = positionCuisineLigne;
        this.cibleColonne = positionCuisineColonne;
        this.enDeplacement = true;
        this.attendCommande = false;
        this.attendService = false;
        System.out.println("Serveur " + getPrenom() + " retourne en cuisine " );
    }
    
    // Autres getters et méthodes existantes...
    public boolean estEnAttentePourCommande() {
        return attendCommande && !enDeplacement;
    }
    
    public boolean estEnAttentePourService() {
        return attendService && !enDeplacement;
    }
    
    public void commandePrise() {
        this.attendCommande = false;
        this.tableServie = null;
        System.out.println("Serveur " + getPrenom() + " a pris la commande");
    }
    
    public void platServi() {
        this.attendService = false;
        this.tableServie = null;
        System.out.println("Serveur " + getPrenom() + " a servi le plat");
    }
    
    public Table getTableServie() {
        return tableServie;
    }

    public void setTableServie(Table tableServie) {
        this.tableServie = tableServie;
    }

    public boolean estEnDeplacement() {
        return enDeplacement;
    }
}