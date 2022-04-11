package cs2263_project;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class keeps track of the stocks "on the board", that have
 * not been bought/awarded yet.
 * @author Eric Hill
 * @author Tyson Cox
 */
class StockList {
    private final TreeMap<String, Integer> stocks = new TreeMap<String, Integer>();

    private StockList() {
    }

    public StockList(String[] stocks, Integer baseAmount) {
        for (var stock : stocks) {
            this.stocks.put(stock, baseAmount);
        }
    }

    /**
     * Check if a corporation has any stock available
     * @param stock the corporation to check
     * @return if the corporation has >1 stock
     */
    public boolean isInStock(String stock) {
        return !stocks.get(stock).equals(0);
    }

    /**
     * Add stock back into the stocklist
     * @param stock the corporation to add to
     * @param number the amount of stock to add
     */
    public void addStock(String stock, int number) {
        stocks.put(stock, (int) stocks.get(stock) + number);
    }

    /**
     * Subtract stock out of the stocklist
     * @param stock the corporation to subtract from
     * @param number the amount to subtract
     */
    public void subtractStock(String stock, int number) {
        stocks.put(stock, (int) stocks.get(stock) - number);
    }

    /**
     * Get all stocks and their quantities as a map
     * @return the stock map
     */
    public Map<String, Integer> getAllStocks() {
        return new HashMap<>(stocks);
    }
}
