package main.data.restaurant;

public class Decoration {
    private String type;
    private int row;
    private int col;
    
    public Decoration(String type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }
    
    public String getType() {
        return type;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
}