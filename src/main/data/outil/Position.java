// Classe utilitaire pour gérer les positions
package main.data.outil;

public class Position {
    private int ligne;
    private int colonne;
    
    public Position(int ligne, int colonne) {
        this.ligne = ligne;
        this.colonne = colonne;
    }
    
    public int getLigne() {
        return ligne;
    }
    
    public void setLigne(int ligne) {
        this.ligne = ligne;
    }
    
    public int getColonne() {
        return colonne;
    }
    
    public void setColonne(int colonne) {
        this.colonne = colonne;
    }
    
 
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return ligne == position.ligne && colonne == position.colonne;
    }
    
   
    @Override
    public String toString() {
        return "(" + ligne + ", " + colonne + ")";
    }
}