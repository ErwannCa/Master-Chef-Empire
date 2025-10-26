package main.data.restaurant;

import main.data.personnel.Chef;

public class Cuisine {
    private Chef chef;
    
    public Cuisine() {
        this(true); //par défaut,  crée un chef
    }
    
    public Cuisine(boolean creerChef) {
        if (creerChef) {
            this.chef = new Chef();
            
        }
    }
    
    public Chef getChef() {
        return chef;
    }
    
    public void setChef(Chef chef) {
        this.chef = chef;
    }
    
    
}