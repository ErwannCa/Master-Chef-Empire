// Classe de base pour toutes les personnes
package main.data.personnel;

import main.data.outil.Position;

public abstract class Personne {
    protected Position position;
    protected String nom;
    
    public Personne(String nom, int ligne, int colonne) {
        this.nom = nom;
        this.position = new Position(ligne, colonne);
    }
    
    public String getNom() {
        return nom;
    }
    
    public Position getPosition() {
        return position;
    }
    
    public void setPosition(int ligne, int colonne) {
        position.setLigne(ligne);
        position.setColonne(colonne);
    }
    
    public int getLigne() {
        return position.getLigne();
    }
    
    public int getColonne() {
        return position.getColonne();
    }
    
    // Chaque personne aura une stratégie différente de déplacement
    public abstract void mettreAJour();
}

