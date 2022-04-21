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

/**
 * This class stores the information on the different corporations, the
 * primary and secondary bonuses, and the costs, per size
 * @author Eric Hill
 */
public class GameInfo {
    private static int[] corpTiers = {1,1,2,2,2,3,3};
    public static String[] Corporations = {"Sackson","Zeta","Hydra","Fusion", "America", "Phoenix","Quantum"};
    private static int[] Brackets = {2,3,4,5,6,11,21,31,41};
    private static int[] tier1Cost = {0,200,300,400,500,600,700,800,900,1000};
    private static int[] tier2Cost = {0,300,400,500,600,700,800,900,1000,1100};
    private static int[] tier3Cost = {0,400,500,600,700,800,900,1000,1100,1200};
    public static final int STARTING_STOCKS = 25;

    public GameInfo(){
    }

    /**
     * This method gets the size bracket for a set size of corporation (number of tiles)
     * @param size
     * @return bracket
     */
    private int getBracket(int size){
        for (int i = 0; i <= 8; i++){
            if (size < Brackets[i]){ return i; }
        }
        return 9;
    }

    /**
     * This class just gets the index of the corporation within Corporations,
     * which is used in getting the "tier" of the corporation
     * @param stock
     * @return int - index
     */
    private int getStockIndex(String stock){
        for (int i = 0; i < Corporations.length; i++){
            if (stock.equals(Corporations[i]))return i;
        }
        return -1;
    }

    /**
     * This method uses the private data to find the primary bonuses of a given stock and size
     * @param stock The name of the stock to get the value of
     * @param size The number of tiles the stock has on the board
     * @return The current bonus for the secondary stock holder
     */
    public int getPrimaryBonus(String stock, int size){
        int bracket = getBracket(size);
        int index = getStockIndex(stock);
        if (corpTiers[index] == 1){return tier1Cost[bracket]*10;}
        if (corpTiers[index] == 2){return tier2Cost[bracket]*10;}
        if (corpTiers[index] == 3){return tier3Cost[bracket]*10;}
        return -1;
    }

    /**
     * This method uses the private data to find the secondary bonuses of a given stock and size
     * @param stock The name of the stock to get the value of
     * @param size The number of tiles the stock has on the board
     * @return The current bonus to be paid to the primary stock holder
     */
    public int getSecondaryBonus(String stock, int size){
        int bracket = getBracket(size);
        int index = getStockIndex(stock);
        if (corpTiers[index] == 1){return tier1Cost[bracket]*5;}
        if (corpTiers[index] == 2){return tier2Cost[bracket]*5;}
        if (corpTiers[index] == 3){return tier3Cost[bracket]*5;}
        return -1;
    }

    /**
     * This method uses the private data to find the cost of a given stock and size
     * @param stock The name of the stock to get the value of
     * @param size The number of tiles the stock has on the board
     * @return The current value of the stock
     */
    public int getCost(String stock, int size){
        int bracket = getBracket(size);
        int index = getStockIndex(stock);
        if (corpTiers[index] == 1){return tier1Cost[bracket];}
        if (corpTiers[index] == 2){return tier2Cost[bracket];}
        if (corpTiers[index] == 3){return tier3Cost[bracket];}
        return -1;
    }

    /**
     * Get the numeric id of a corporation name
     * @param corporation the corporation to get the id for
     * @return the corporation's id, or 0 if not a valid corporation
     */
    public static int getCorporationID(String corporation) {
        for(int i = 0; i < Corporations.length; i++) {
            if (Corporations[i].equals(corporation))
                return i;
        }

        return 0;
    }

}
