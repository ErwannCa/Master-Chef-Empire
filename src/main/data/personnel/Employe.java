package main.data.personnel;

public abstract class Employe extends Personne {
    protected String prenom;
    protected double salaire;
    protected int niveau;
    protected boolean estChef;
    
    public Employe(String nom, String prenom, double salaire, int niveau, int ligne, int colonne) {
        super(nom + " " + prenom, ligne, colonne);
        this.prenom = prenom;
        this.salaire = salaire;
        this.niveau = niveau;
        this.estChef = false;
    }
    
    public String getPrenom() {
        return prenom;
    }
    
    public double getSalaire() {
        return salaire;
    }
    
    public int getNiveau() {
        return niveau;
    }
    
    public boolean isEstChef() {
        return estChef;
    }
    
    public void setEstChef(boolean estChef) {
        this.estChef = estChef;
    }
    
    @Override
    public String toString() {
        return nom + " - Niveau " + niveau + " - " + salaire + "€";
    }
}