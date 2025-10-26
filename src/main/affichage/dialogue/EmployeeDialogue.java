package main.affichage.dialogue;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import main.data.personnel.Employe;
import main.gestion.EmployeeGestion;

/**
 * Fenêtre de gestion des employés du restaurant
 */
public class EmployeeDialogue extends JFrame {
    private DefaultListModel<String> modeleListeEmployes;
    private JList<String> listeEmployes;
    private EmployeeGestion controleurEmployes;
    
    // Couleurs du thème
    private final Color COULEUR_FOND = new Color(255, 245, 225); // Beige clair
    private final Color COULEUR_ENTETE = new Color(204, 102, 0); // Orange foncé
    private final Color COULEUR_BOUTON = new Color(255, 153, 51); // Orange moyen
    
    /**
     * Constructeur de la fenêtre de gestion des employés
     */
    public EmployeeDialogue(EmployeeGestion controleurEmployes) {
        this.controleurEmployes = controleurEmployes;
        
        setTitle("Gestion du Personnel");
        setSize(550, 400);
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
        
        JLabel etiquetteTitre = new JLabel("Équipe du Restaurant");
        etiquetteTitre.setFont(new Font("Arial", Font.BOLD, 18));
        etiquetteTitre.setForeground(Color.WHITE);
        etiquetteTitre.setHorizontalAlignment(SwingConstants.CENTER);
        panneauEntete.add(etiquetteTitre, BorderLayout.CENTER);
        
        // Liste des employés
        modeleListeEmployes = new DefaultListModel<>();
        listeEmployes = new JList<>(modeleListeEmployes);
        mettreAJourListeEmployes();
        
        listeEmployes.setFont(new Font("Arial", Font.PLAIN, 14));
        listeEmployes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
     
        
        JScrollPane scrollPane = new JScrollPane(listeEmployes);
        scrollPane.setBorder(BorderFactory.createLineBorder(COULEUR_ENTETE));
        
        // Panneau des boutons
        JPanel panneauBoutons = new JPanel();
        panneauBoutons.setLayout(new GridLayout(3, 1, 0, 10));
        panneauBoutons.setBackground(COULEUR_FOND);
        panneauBoutons.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        JButton boutonEmbaucher = new JButton("Embaucher");
        JButton boutonLicencier = new JButton("Licencier");
        JButton boutonRetour = new JButton("Retour");
        
        // Style des boutons
        Color couleurBouton = COULEUR_BOUTON;
        styliserBouton(boutonEmbaucher, couleurBouton);
        styliserBouton(boutonLicencier, couleurBouton);
        styliserBouton(boutonRetour, couleurBouton);
        
        // Actions des boutons
        boutonEmbaucher.addActionListener(e -> ouvrirDialogueEmbauche());
        boutonLicencier.addActionListener(e -> licencierEmploye());
        boutonRetour.addActionListener(e -> dispose());
        
        panneauBoutons.add(boutonEmbaucher);
        panneauBoutons.add(boutonLicencier);
        panneauBoutons.add(boutonRetour);
        
        // Assembler le tout
        panneauPrincipal.add(panneauEntete, BorderLayout.NORTH);
        panneauPrincipal.add(scrollPane, BorderLayout.CENTER);
        panneauPrincipal.add(panneauBoutons, BorderLayout.EAST);
        
        add(panneauPrincipal);
        setLocationRelativeTo(null);
        setVisible(true);
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
     * Met à jour la liste des employés
     */
    public void mettreAJourListeEmployes() {
        modeleListeEmployes.clear();
        List<Employe> employes = controleurEmployes.getEmployes();
        
        for (Employe employe : employes) {
            // Utiliser directement toString() de l'employé pour l'affichage
            modeleListeEmployes.addElement(employe.toString());
        }
    }
    
    /**
     * Licencie l'employé sélectionné
     */
    private void licencierEmploye() {
        int indexSelectionne = listeEmployes.getSelectedIndex();
        if (indexSelectionne != -1) {
            if (controleurEmployes.supprimerEmploye(indexSelectionne)) {
                mettreAJourListeEmployes();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Vous devez garder au moins un chef dans le restaurant.",
                    "Impossible de licencier",
                    JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un employé à licencier.",
                "Aucune sélection",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Ouvre le dialogue d'embauche
     */
    private void ouvrirDialogueEmbauche() {
        new DialogueEmbauche(this);
    }
    
    /**
     * Classe interne pour le dialogue d'embauche
     */
    private class DialogueEmbauche extends JFrame {
        private JList<String> listeCandidats;
        private DefaultListModel<String> modeleListeCandidats;
        
        public DialogueEmbauche(EmployeeDialogue parent) {
            setTitle("Recrutement de personnel");
            setSize(650, 500);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Panneau principal
            JPanel panneauPrincipal = new JPanel(new BorderLayout(10, 10));
            panneauPrincipal.setBackground(COULEUR_FOND);
            panneauPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            
            // Panneau d'en-tête
            JPanel panneauEntete = new JPanel();
            panneauEntete.setBackground(COULEUR_ENTETE);
            panneauEntete.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JLabel etiquetteTitre = new JLabel("Candidats disponibles");
            etiquetteTitre.setFont(new Font("Arial", Font.BOLD, 16));
            etiquetteTitre.setForeground(Color.WHITE);
            etiquetteTitre.setHorizontalAlignment(SwingConstants.CENTER);
            panneauEntete.add(etiquetteTitre);
            
            // Obtenir la liste des candidats disponibles
            List<String> candidatsDisponibles = controleurEmployes.getCandidatsDisponibles();
            
            // Liste des candidats
            modeleListeCandidats = new DefaultListModel<>();
            for (String candidat : candidatsDisponibles) {
                modeleListeCandidats.addElement(candidat);
            }
            
            // Si la liste est vide, afficher un message
            if (modeleListeCandidats.isEmpty()) {
                modeleListeCandidats.addElement("Aucun candidat disponible pour le moment");
            }
            
            listeCandidats = new JList<>(modeleListeCandidats);
            listeCandidats.setFont(new Font("Arial", Font.PLAIN, 14));
            
      
       
            
            JScrollPane scrollPane = new JScrollPane(listeCandidats);
            scrollPane.setBorder(BorderFactory.createLineBorder(COULEUR_ENTETE));
            
            // Panneau de description
            JPanel panneauDescription = new JPanel();
            panneauDescription.setBackground(new Color(255, 240, 210));
            panneauDescription.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JLabel description = new JLabel(
                "<html>Sélectionnez un candidat pour l'embaucher dans votre restaurant.<br>" +
                "Les chefs de niveau supérieur préparent les plats plus rapidement.</html>"
            );
            description.setFont(new Font("Arial", Font.PLAIN, 12));
            panneauDescription.add(description);
            
            // Panneau des boutons
            JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            panneauBoutons.setBackground(COULEUR_FOND);
            
            JButton boutonRecruter = new JButton("Recruter");
            JButton boutonAnnuler = new JButton("Annuler");
            
            styliserBouton(boutonRecruter, COULEUR_BOUTON);
            styliserBouton(boutonAnnuler, new Color(150, 150, 150));
            
            boutonRecruter.addActionListener(e -> recruterCandidat());
            boutonAnnuler.addActionListener(e -> dispose());
            
            panneauBoutons.add(boutonRecruter);
            panneauBoutons.add(boutonAnnuler);
            
            // Assembler le tout
            panneauPrincipal.add(panneauEntete, BorderLayout.NORTH);
            panneauPrincipal.add(scrollPane, BorderLayout.CENTER);
            panneauPrincipal.add(panneauDescription, BorderLayout.SOUTH);
            panneauPrincipal.add(panneauBoutons, BorderLayout.EAST);
            
            add(panneauPrincipal);
            setLocationRelativeTo(parent);
            setVisible(true);
        }
        
        /**
         * Recrute le candidat sélectionné
         */
        private void recruterCandidat() {
            String candidatSelectionne = listeCandidats.getSelectedValue();
            if (candidatSelectionne != null && !candidatSelectionne.startsWith("Aucun candidat")) {
                if (controleurEmployes.ajouterEmployeDepuisString(candidatSelectionne)) {
                    EmployeeDialogue.this.mettreAJourListeEmployes();
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Impossible d'embaucher ce candidat.",
                        "Erreur de recrutement",
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un candidat valide à recruter.",
                    "Aucune sélection",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}