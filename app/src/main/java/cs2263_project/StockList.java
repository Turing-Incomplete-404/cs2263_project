package cs2263_project;

import java.util.TreeMap;

class StockList {
    private TreeMap Stocks = new TreeMap<String, Integer>();

    private StockList() {
    }

    public StockList(String[] stocks, Integer baseAmount) {
        for (var stock : stocks) {
            Stocks.put(stock, baseAmount);
        }
    }

    public boolean isInStock(String stock) {
        return !Stocks.get(stock).equals(0);
    }

    public void addStock(String stock, int number) {
        Stocks.put(stock, (int) Stocks.get(stock) + number);
    }

    public void subtractStock(String stock, int number) {
        Stocks.put(stock, (int) Stocks.get(stock) - number);
    }
}
