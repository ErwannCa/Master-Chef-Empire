package main.data.restaurant;

import main.data.inventaire.Plat;

import main.data.personnel.Client;
import main.gestion.StockGestion;
import main.data.restaurant.*;

public class CommandeEnAttente {
    private Client client;
    private Plat plat;
    private boolean enPreparation;
    private boolean prete;
    
    public CommandeEnAttente(Client client, Plat plat) {
        this.client = client;
        this.plat = plat;
        this.enPreparation = false;
        this.prete = false;
    }
    
    public Client getClient() {
        return client;
    }
    
    public Plat getPlat() {
        return plat;
    }
    
    public boolean isEnPreparation() {
        return enPreparation;
    }
    
    public void setEnPreparation(boolean enPreparation) {
        this.enPreparation = enPreparation;
    }
    
    public boolean isPrete() {
        return prete;
    }
    
    public void setPrete(boolean prete) {
        this.prete = prete;
    }
    public boolean verifierIngredientsDisponibles(StockGestion stockController) {
        if (this.plat == null) return false;
        
        return main.data.restaurant.LienPlatStock.verifierDisponibilite(plat.getNom(), stockController);
    }

}