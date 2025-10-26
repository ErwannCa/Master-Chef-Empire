// Contrôleur pour la gestion des employés
package main.gestion;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

import main.data.personnel.Chef;
import main.data.personnel.Employe;
import main.data.personnel.Serveur;
import main.data.restaurant.Table;

public class EmployeeGestion {
    private List<Employe> employes;
    private List<String> candidatsDisponibles;
    private RestaurantGestion restaurantGestion;
    private static final int MAX_CUISINIERS = 4;
    private static final int MIN_CUISINIERS = 1;
    private static final int CUISINE_LIGNE = 2;
    private static final int CUISINE_COLONNE = 10;
    private static final int MAX_TABLES_PAR_SERVEUR = 4;
    
    // Positions possibles pour les cuisiniers
    private static final int[][] POSITIONS_CUISINE = {
        {14, 6}, // Ligne, Colonne
        {14, 7},
        {13, 6},
        {13, 7}
    };
    
    // Positions possibles pour les serveurs
    private static final int[][] POSITIONS_SERVEUR = {
        {5, 5},
        {6, 6},
        {7, 7},
        {8, 8},
        {5, 9},
        {6, 10},
        {7, 10},
        {8, 11}
    };
    
    public EmployeeGestion() {
        this.employes = new ArrayList<>();
        this.candidatsDisponibles = new ArrayList<>();
        
        // Initialiser la liste des candidats disponibles
        initCandidatsDisponibles();
        
        // Ajouter Chef Paul par défaut
        employes.add(new Chef());
        
        // Créer le serveur par défaut et lui assigner une position cuisine
        Serveur serveurDefault = new Serveur("SERVEUR", "Jean", 1200.0, 1, CUISINE_LIGNE + 1, CUISINE_COLONNE);
        serveurDefault.setPositionCuisine(CUISINE_LIGNE + 1, CUISINE_COLONNE);
        employes.add(serveurDefault);
        
        // Retirer les employés déjà embauchés de la liste des candidats
        retirerCandidatsEmbauchesDeListeDisponibles();
    }
    
    // Setter pour établir la référence à RestaurantGestion
    public void setRestaurantGestion(RestaurantGestion restaurantGestion) {
        this.restaurantGestion = restaurantGestion;
    }
    
    private void initCandidatsDisponibles() {
        // Liste initiale de tous les candidats
        candidatsDisponibles.add("Pierre CUISINIER - Niveau 1 - 1500€");
        candidatsDisponibles.add("Erwann CUISINIER - Niveau 2 - 2000€");
        candidatsDisponibles.add("Sarah CUISINIERE - Niveau 3 - 2500€");
        candidatsDisponibles.add("Kamilia CUISINIERE - Niveau 4 - 3000€");
        candidatsDisponibles.add("Lucas CUISINIER - Niveau 5 - 3500€");
        candidatsDisponibles.add("Marc SERVEUR - Niveau 1 - 1500€");
        candidatsDisponibles.add("Jacques SERVEUR - Niveau 1 - 1500€");
        candidatsDisponibles.add("Rosa SERVEUSE - Niveau 1 - 1500€");
        candidatsDisponibles.add("Stéphane SERVEUR - Niveau 1 - 1500€");
        candidatsDisponibles.add("Morgan SERVEUSE - Niveau 1 - 1500€");
    }
    
    private void retirerCandidatsEmbauchesDeListeDisponibles() {
        // Pour chaque employé, retirer le candidat correspondant de la liste
        for (Employe employe : employes) {
            String prenom = employe.getPrenom();
            
            // Parcourir la liste des candidats et retirer ceux avec le même prénom
            List<String> aRetirer = new ArrayList<>();
            for (String candidat : candidatsDisponibles) {
                if (candidat.startsWith(prenom + " ")) {
                    aRetirer.add(candidat);
                }
            }
            candidatsDisponibles.removeAll(aRetirer);
        }
    }
    
    public List<Employe> getEmployes() {
        return employes;
    }
    
    public List<String> getCandidatsDisponibles() {
        return candidatsDisponibles;
    }
    
    // Obtient la liste des tables depuis RestaurantGestion
    private List<Table> getTables() {
        if (restaurantGestion != null) {
            return restaurantGestion.getTables();
        }
        return new ArrayList<>();
    }
    
    public boolean ajouterEmploye(String nom, String prenom, double salaire, int niveau, boolean estChef) {
        // Vérifier si c'est un cuisinier et s'il y a de la place
        if (estChef && compteCuisiniers() >= MAX_CUISINIERS) {
            JOptionPane.showMessageDialog(null, 
                                         "Vous avez déjà atteint le nombre maximum de cuisiniers (4).",
                                         "Impossible d'embaucher", 
                                         JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        // Vérifier s'il y a assez de tables pour embaucher un nouveau serveur
        if (!estChef && restaurantGestion != null && !restaurantGestion.peutEmbaucherServeur()) {
            JOptionPane.showMessageDialog(null, 
                                         "Vous n'avez pas assez de tables pour embaucher un nouveau serveur.\n" +
                                         "Chaque serveur peut s'occuper de " + MAX_TABLES_PAR_SERVEUR + " tables.",
                                         "Impossible d'embaucher", 
                                         JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        Employe employe;
        
        if (estChef) {
            // Créer le chef avec le constructeur disponible
            employe = new Chef(nom, prenom, salaire, niveau);
            
            // Puis définir sa position séparément
            int posIndex = compteCuisiniers() % POSITIONS_CUISINE.length;
            employe.setPosition(POSITIONS_CUISINE[posIndex][0], POSITIONS_CUISINE[posIndex][1]);
        } else {
            // Pour les serveurs, calculer leur position en se basant sur le serveur précédent
            int posLigne = CUISINE_LIGNE + 1; // Position de base (comme Jean)
            int posColonne = CUISINE_COLONNE; // Position de base
            
            // Si on a déjà des serveurs, placer le nouveau une ligne plus bas que le dernier
            int maxLigne = CUISINE_LIGNE + 1; // Position de base de Jean
            
            // Parcourir tous les employés pour trouver les serveurs et leur position
            for (Employe emp : employes) {
                if (!emp.isEstChef()) { // C'est un serveur
                    if (emp.getLigne() > maxLigne) {
                        maxLigne = emp.getLigne();
                    }
                }
            }
            
            // Placer le nouveau serveur une ligne en dessous
            posLigne = maxLigne + 1;
            
            // Créer le serveur avec sa position calculée
            Serveur nouveauServeur = new Serveur(nom, prenom, salaire, niveau, posLigne, posColonne);
            nouveauServeur.setPositionCuisine(posLigne, posColonne); // Important: définir la position de cuisine
            
            employe = nouveauServeur;
            
            // Déboguer: afficher la position du nouveau serveur
            System.out.println("Nouveau serveur " + prenom + " placé à la position (" + posLigne + "," + posColonne + ")");
            
            // Attribuer les tables au nouveau serveur si possible
            if (restaurantGestion != null) {
                employes.add(employe); // Ajouter l'employé à la liste avant d'appeler emploiServeur
                restaurantGestion.emploiServeur((Serveur)employe);
                
                // Important: nous avons déjà ajouté l'employé à la liste, donc ne pas le faire une seconde fois
                return true;
            }
        }
        
        employes.add(employe);
        
        // Retirer le candidat correspondant de la liste des disponibles
        List<String> aRetirer = new ArrayList<>();
        for (String candidat : candidatsDisponibles) {
            if (candidat.startsWith(prenom + " ")) {
                aRetirer.add(candidat);
            }
        }
        candidatsDisponibles.removeAll(aRetirer);
        
        return true;
    }
    
    
    public boolean ajouterEmployeDepuisString(String employeString) {
        try {
            String[] parts = employeString.split(" - ");
            String[] nomComplet = parts[0].split(" ");
            String prenom = nomComplet[0];
            String nom = nomComplet[1];
            int niveau = Integer.parseInt(parts[1].replace("Niveau ", ""));
            double salaire = Double.parseDouble(parts[2].replace("€", "").trim());
            
            boolean estChef = nom.contains("CUISINIER") || nom.contains("CUISINIERE") || nom.contains("CHEF");
            
            boolean resultat = ajouterEmploye(nom, prenom, salaire, niveau, estChef);
            
            return resultat;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                                        "Format de données employé incorrect: " + employeString,
                                        "Erreur de format", 
                                        JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean supprimerEmploye(int index) {
        if (index >= 0 && index < employes.size()) {
            Employe employe = employes.get(index);
            
            // Vérifier si c'est un cuisinier et si c'est le dernier
            if (employe.isEstChef() && compteCuisiniers() <= MIN_CUISINIERS) {
                JOptionPane.showMessageDialog(null, 
                                             "Vous devez avoir au moins un cuisinier dans le restaurant.",
                                             "Impossible de licencier", 
                                             JOptionPane.WARNING_MESSAGE);
                return false;
            }
            
            employes.remove(index);
            
            // Si c'était un serveur, réorganiser l'attribution des tables
            if (!employe.isEstChef() && restaurantGestion != null) {
                restaurantGestion.licenciementServeur();
            }
            
            return true;
        }
        return false;
    }

    public void mettreAJourEmployes() {
        for (Employe employe : employes) {
            employe.mettreAJour();
        }
    }

    public Employe getEmployeAtPosition(int ligne, int colonne) {
        for (Employe employe : employes) {
            if (employe.getLigne() == ligne && employe.getColonne() == colonne) {
                return employe;
            }
        }
        return null;
    }
    
    /**
     * Trouve le serveur responsable d'une table spécifique
     */
    public Serveur getServeurPourTable(Table table) {
        for (Employe employe : employes) {
            if (!employe.isEstChef()) {
                Serveur serveur = (Serveur) employe;
                if (serveur.estResponsable(table)) {
                    return serveur;
                }
            }
        }
        return null;
    }

    public int compteCuisiniers() {
        int count = 0;
        for (Employe emp : employes) {
            if (emp.isEstChef()) {
                count++;
            }
        }
        return count;
    }

    public int compteServeurs() {
        int count = 0;
        for (Employe emp : employes) {
            if (!emp.isEstChef()) {
                count++;
            }
        }
        return count;
    }

    public double getMasseSalariale() {
        double total = 0;
        for (Employe emp : employes) {
            total += emp.getSalaire();
        }
        return total;
    }
}