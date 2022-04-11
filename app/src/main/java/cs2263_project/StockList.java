/**
 * MIT License
 *
 * Copyright (c) 2022 Turing-Incomplete-404
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
