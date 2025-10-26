package main.gestion;

import main.data.personnel.Client;
import main.data.restaurant.Table;
import main.gestion.RestaurantGestion;

import java.util.ArrayList;
import java.util.List;

public class ClientGestion {
    private List<Client> clients;
    private RestaurantGestion restaurantController;
    
    public ClientGestion(RestaurantGestion restaurantController) {
        this.restaurantController = restaurantController;
        this.clients = new ArrayList<>();
    }
    
    public void ajouterClient(Table table) {
        if (table != null && !table.estOccupee()) {
            clients.add(new Client(table));
        }
    }
    
    public void mettreAJourClients() {
        for (Client client : new ArrayList<>(clients)) {
            client.mettreAJour();
        }
    }
    
    public void retirerClient(Client client) {
        clients.remove(client);
        if (client.getTable() != null) {
            client.partir();
        }
    }
    
    public Client getClientPourTable(Table table) {
        for (Client client : clients) {
            if (client.getTable() == table) {
                return client;
            }
        }
        return null;
    }
    
    public List<Client> getClients() {
        return clients;
    }
    
    public boolean peutAccueillirClient() {
        return clients.size() < 10 && restaurantController.trouverTableLibre() != null;
    }
    
    public void genererClientAleatoire() {
        if (Math.random() < 0.10 && peutAccueillirClient()) {
            Table table = restaurantController.trouverTableLibre();
            if (table != null) {
                ajouterClient(table);
            }
        }
    }
}