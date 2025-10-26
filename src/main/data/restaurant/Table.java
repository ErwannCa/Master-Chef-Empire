package main.data.restaurant;

public class Table {
    private int ligne, colonne;
    private boolean occupee;
    
    public Table(int colonne, int ligne) {
        this.colonne = colonne;
        this.ligne = ligne;
        this.occupee = false;
    }
    
    public int getX() {
        return colonne * 55; // position x en pixels
    }
    
    public int getY() {
        return ligne * 55;   // position y en pixels
    }
    
    public int getLigne() {
        return ligne;
    }
    
    public int getColonne() {
        return colonne;
    }
    
    public boolean estOccupee() {
        return occupee;
    }
    
    public void setOccupee(boolean occupee) {
        this.occupee = occupee;
    }
}