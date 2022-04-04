package cs2263_project;

class GameInfo {
    private int[] corpTiers = {1,1,2,2,2,3,3};
    public String[] Corporations = {"Lux","Imperial","Tower","Eagle", "FINISH THIS LATER", "exc."};
    private int[] Brackets = {2,3,4,5,6,11,21,31,41};
    private int[] tier1Pbonus = {0,1,2,3,4,5,6,7,8,9};
    private int[] tier2Pbonus = {0,1,2,3,4,5,6,7,8,9};
    private int[] tier3Pbonus = {0,1,2,3,4,5,6,7,8,9};
    private int[] tier1Sbonus = {0,1,2,3,4,5,6,7,8,9};
    private int[] tier2Sbonus = {0,1,2,3,4,5,6,7,8,9};
    private int[] tier3Sbonus = {0,1,2,3,4,5,6,7,8,9};
    private int[] tier1Cost = {0,1,2,3,4,5,6,7,8,9};
    private int[] tier2Cost = {0,1,2,3,4,5,6,7,8,9};
    private int[] tier3Cost = {0,1,2,3,4,5,6,7,8,9};

    public GameInfo(){
    }

    private int getBracket(int size){
        for (int i = 0; i <= 8; i++){
            if (size < Brackets[i]){ return i; }
        }
        return 9;
    }

    private int getStockIndex(String stock){
        for (int i = 0; i < Corporations.length; i++){
            if (stock.equals(Corporations[i]))return i;
        }
        return -1;
    }

    public int getPBonus(String stock, int size){
        int bracket = getBracket(size);
        int index = getStockIndex(stock);
        if (corpTiers[index] == 1)return tier1Pbonus[bracket];
        if (corpTiers[index] == 2)return tier2Pbonus[bracket];
        if (corpTiers[index] == 3)return tier3Pbonus[bracket];
        return -1;
    }

    public int getSBonus(String stock, int size){
        int bracket = getBracket(size);
        int index = getStockIndex(stock);
        if (corpTiers[index] == 1){return tier1Sbonus[bracket];}
        if (corpTiers[index] == 2){return tier2Sbonus[bracket];}
        if (corpTiers[index] == 3){return tier3Sbonus[bracket];}
        return -1;
    }

    public int getCost(String stock, int size){
        int bracket = getBracket(size);
        int index = getStockIndex(stock);
        if (corpTiers[index] == 1){return tier1Cost[bracket];}
        if (corpTiers[index] == 2){return tier2Cost[bracket];}
        if (corpTiers[index] == 3){return tier3Cost[bracket];}
        return -1;
    }

}
