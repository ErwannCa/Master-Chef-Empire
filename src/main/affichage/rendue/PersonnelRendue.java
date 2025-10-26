package main.affichage.rendue;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

import main.gestion.RestaurantGestion;
import main.data.personnel.Chef;
import main.data.personnel.Client;
import main.data.personnel.Employe;
import main.data.personnel.Serveur;
import main.data.restaurant.CommandeEnAttente;

/**
 * Classe responsable du rendu graphique de tous les personnages du restaurant
 */
public class PersonnelRendue {
    private RestaurantGestion restaurantController;
    private Image chefImage, serveurImage, clientImage;
    private int tailleTuile;
    
    // Polices et couleurs pour le rendu
    private static final Font FONT_NOM = new Font("Arial", Font.BOLD, 12);
    private static final Font FONT_ETAT = new Font("Arial", Font.BOLD, 10);
    
    // Constantes pour les couleurs d'état des clients
    private static final Color COULEUR_ARRIVE = Color.BLUE;
    private static final Color COULEUR_ATTEND = Color.ORANGE;
    private static final Color COULEUR_COMMANDE = Color.GREEN;
    private static final Color COULEUR_MANGE = Color.MAGENTA;
    private static final Color COULEUR_PAIE = Color.RED;
    private static final Color COULEUR_CHEF_OCCUPE = new Color(255, 100, 100); // Rouge clair
    
    /**
     * Constructeur du rendue du personnages
     */
    public PersonnelRendue(RestaurantGestion restaurantController, int tailleTuile) {
        this.restaurantController = restaurantController;
        this.tailleTuile = tailleTuile;
        chargerImages();
    }
    
    /**
     * Charge les images des personnages
     */
    private void chargerImages() {
        try {
            chefImage = ImageIO.read(new File("src/main/images/chef_pixel.png"));
            serveurImage = ImageIO.read(new File("src/main/images/serveur_pixel.png"));
            clientImage = ImageIO.read(new File("src/main/images/client.png"));
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement des images: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Dessine tous les personnages (clients, serveurs, chefs)
     */
    public void dessiner(Graphics2D g2) {
        dessinerClients(g2);
        dessinerServeurs(g2);
        dessinerChefs(g2);
    }
    
    /**
     * Dessine tous les clients avec leur état actuel
     */
    private void dessinerClients(Graphics2D g2) {
    
    	// Récupérer la liste des clients
        List<Client> clientsCopie = new ArrayList<>(restaurantController.getClients());

        for (int i = 0; i < clientsCopie.size(); i++) {
            Client client = clientsCopie.get(i);
            
            // Dessiner l'image du client
            g2.drawImage(clientImage,
                    client.getColonne() * tailleTuile,
                    client.getLigne() * tailleTuile,
                    tailleTuile, tailleTuile, null);

            // Déterminer l'état et la couleur à afficher
            String etatTexte = getEtatClientTexte(client);
            Color couleurEtat = getEtatClientCouleur(client);

            // Afficher l'état s'il existe
            if (!etatTexte.isEmpty()) {
                g2.setColor(couleurEtat);
                g2.setFont(FONT_ETAT);
                
                // Alterner entre haut et bas selon l'indice du client
                if (i % 2 == 0) {
                    // Position au-dessus pour les clients d'indice pair
                    g2.drawString(etatTexte,
                            client.getColonne() * tailleTuile,
                            client.getLigne() * tailleTuile - 5);
                } else {
                    // Position en dessous pour les clients d'indice impair
                    g2.drawString(etatTexte,
                            client.getColonne() * tailleTuile,
                            client.getLigne() * tailleTuile + tailleTuile + 15);
                }
            }
        }
    }
    /**
     * Obtient le texte d'état pour un client
     */
    private String getEtatClientTexte(Client client) {
        switch (client.getEtat()) {
            case Client.ETAT_ENTREE:
                return "Arrivé";
            case Client.ETAT_ASSIS:
                return "Attend";
            case Client.ETAT_COMMANDE_PASSEE:
                return "Commande: " + client.getNomPlatAffiche();
            case Client.ETAT_SERVI:
                return "Mange: " + client.getNomPlatAffiche();
            case Client.ETAT_PAIEMENT:
                return "Paie";
            default:
                return "";
        }
    }
    
    /**
     * Obtient la couleur correspondant à l'état d'un client
     */
    private Color getEtatClientCouleur(Client client) {
        switch (client.getEtat()) {
            case Client.ETAT_ENTREE:
                return COULEUR_ARRIVE;
            case Client.ETAT_ASSIS:
                return COULEUR_ATTEND;
            case Client.ETAT_COMMANDE_PASSEE:
                return COULEUR_COMMANDE;
            case Client.ETAT_SERVI:
                return COULEUR_MANGE;
            case Client.ETAT_PAIEMENT:
                return COULEUR_PAIE;
            default:
                return Color.BLACK;
        }
    }
    
    /**
     * Dessine tous les serveurs
     */
    private void dessinerServeurs(Graphics2D g2) {
        // Copie sécurisée des serveurs
        List<Serveur> serveursCopie = new ArrayList<>(restaurantController.getServeurs());
        
      
        
        
        for (Serveur serveur : serveursCopie) {
            // Dessiner l'image du serveur
            g2.drawImage(serveurImage, 
                    serveur.getColonne() * tailleTuile, 
                    serveur.getLigne() * tailleTuile, 
                    tailleTuile, tailleTuile, null);
            
            // Afficher le nom du serveur
            g2.setColor(Color.BLACK);
            g2.setFont(FONT_NOM);
            g2.drawString(serveur.getPrenom(), 
                    serveur.getColonne() * tailleTuile, 
                    serveur.getLigne() * tailleTuile - 5);
            
            // Afficher l'état du serveur si nécessaire
            if (serveur.estEnAttentePourCommande()) {
                g2.setColor(COULEUR_COMMANDE);
                g2.setFont(FONT_ETAT);
                g2.drawString("Prend commande", 
                        serveur.getColonne() * tailleTuile, 
                        serveur.getLigne() * tailleTuile - 18);
            } else if (serveur.estEnAttentePourService()) {
                g2.setColor(COULEUR_MANGE);
                g2.setFont(FONT_ETAT);
                g2.drawString("Sert client", 
                        serveur.getColonne() * tailleTuile, 
                        serveur.getLigne() * tailleTuile - 18);
            }
        }
    }
    
    /**
     * Dessine tous les chefs/cuisiniers
     */
    private void dessinerChefs(Graphics2D g2) {
        List<Employe> employes = restaurantController.getEmployeeController().getEmployes();
        if (employes == null || employes.isEmpty()) {
            return;
        }
        
        // Liste des commandes en attente pour vérifier quels plats sont en préparation
        List<CommandeEnAttente> commandes = restaurantController.getCommandesEnAttente();
        
        for (Employe emp : employes) {
            if (emp.isEstChef()) {
                Chef chef = (Chef) emp;
                
                // Dessiner l'image du chef
                g2.drawImage(chefImage, 
                        chef.getColonne() * tailleTuile, 
                        chef.getLigne() * tailleTuile, 
                        tailleTuile, tailleTuile, null);
                
                // Choisir la couleur selon que le chef est occupé ou non
                Color couleurNom = chef.isOccupe() ? COULEUR_CHEF_OCCUPE : Color.BLACK;
                g2.setColor(couleurNom);
                g2.setFont(FONT_NOM);
                
                // Afficher le nom du chef
                g2.drawString(chef.getPrenom(), 
                        chef.getColonne() * tailleTuile, 
                        chef.getLigne() * tailleTuile - 5);
                
                // Si le chef prépare quelque chose, afficher le plat
                if (chef.isOccupe() && chef.getCommandeEnCours() != null) {
                    String platEnPrep = chef.getCommandeEnCours().getPlat().getNom();
                    g2.setColor(COULEUR_CHEF_OCCUPE);
                    g2.setFont(FONT_ETAT);
                    g2.drawString("Prépare: " + platEnPrep, 
                            chef.getColonne() * tailleTuile, 
                            chef.getLigne() * tailleTuile - 18);
                }
            }
        }
    }
}