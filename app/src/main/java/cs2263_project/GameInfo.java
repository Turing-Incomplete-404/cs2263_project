package cs2263_project;

/**
 * This class stores the information on the different corporations, the
 * primary and secondary bonuses, and the costs, per size
 * @author Eric Hill
 */
class GameInfo {
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

}
