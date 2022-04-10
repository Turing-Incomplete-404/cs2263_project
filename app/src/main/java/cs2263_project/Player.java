package cs2263_project;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * This class stores the data and holds the interactive methods for each player
 * @author Eric Hill
 */
public class Player {
    @Getter private String name;
    @Getter private Integer dollars = 0;
    private TreeMap stocks = new TreeMap<String,Integer>();
    private LinkedList hand = new LinkedList<Tile>();

    public Player(){}

    public Player(String n, String[] corporations){
        name = n;
        for (var corp:corporations){
            stocks.put(corp,0);
        }
    }

    /**
     * This method adds a given tile to a player's hand
     * @param tile
     */
    public void addTile(Tile tile){
        hand.add(tile);
    }

    /**
     * This method removes a given tile from a player's hand
     * @param tile
     */
    public void removeTile(Tile tile){
        hand.remove(tile);
    }

    /**
     * This method adds to a player's dollar amount
     * @param money
     */
    public void addDollars(int money){
        dollars += money;
    }

    /**
     * This method subtracts from a player's dollar amount
     * @param money
     */
    public void subtractDollars(int money){
        dollars -= money;
    }

    /**
     * This method adds a certain amount of stocks to the player's stocks
     * @param stock
     * @param value
     */
    public void addStock(String stock, int value){
        stocks.put(stock,(int)(stocks.get(stock))+value);
    }

    /**
     * This method removes a certain amount of stocks from the player's stocks
     * @param stock
     * @param value
     */
    public void subtractStocks(String stock, int value) {
        assert (int)stocks.get(stock) - value >= 0;
        stocks.put(stock, (Integer)(stocks.get(stock)) - value);
    }

    /**
     * Get the number of stocks a player has
     * @param stock The stock to check
     * @return The number of stocks the player has
     */
    public int stockAmount(String stock) {
        return (Integer) stocks.get(stock);
    }
}