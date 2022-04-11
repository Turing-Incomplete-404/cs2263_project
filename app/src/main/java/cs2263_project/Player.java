package cs2263_project;

import lombok.Getter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * This class stores the data and holds the interactive methods for each player
 * @author Eric Hill
 */
public class Player implements Comparable<Player> {
    @Getter private String name;
    @Getter private Integer dollars = 6000;
    private TreeMap<String, Integer> stocks = new TreeMap<String,Integer>();
    private LinkedList<Tile> hand = new LinkedList<Tile>();

    public Player(){}

    public Player(String n, String[] corporations){
        name = n;
        for (var corp:corporations){
            stocks.put(corp,0);
        }
    }

    /**
     * This method adds a given tile to a player's hand
     * @param tile - the tile to add to the player's hand
     */
    public void addTile(Tile tile){
        hand.add(tile);
    }

    /**
     * This method removes a given tile from a player's hand
     * @param tile - the tile to remove from the player's hand
     */
    public void removeTile(Tile tile){
        hand.remove(tile);
    }

    /**
     * This method adds to a player's dollar amount
     * @param money - the amount to add to the player's dollar amount
     */
    public void addDollars(int money){
        dollars += money;
    }

    /**
     * This method subtracts from a player's dollar amount
     * @param money - the amount to subtract from a player's dollar amount
     */
    public void subtractDollars(int money){
        dollars -= money;
    }

    /**
     * This method adds a certain amount of stocks to the player's stocks
     * @param stock - which stock to increment
     * @param value - how many to increment by
     */
    public void addStock(String stock, int value){
        stocks.put(stock,(int)(stocks.get(stock))+value);
    }

    /**
     * This method removes a certain amount of stocks from the player's stocks
     * @param stock - which stock to decrement
     * @param value - how much to decrement by
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
        return (int) stocks.get(stock);
    }

    public List<Tile> getTiles() {
        return new ArrayList<>(hand);
    }

    @Override
    public int compareTo(Player player) {
        return player.getDollars().compareTo(dollars);
    }
}