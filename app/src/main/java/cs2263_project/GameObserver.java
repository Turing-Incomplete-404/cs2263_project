package cs2263_project;

import java.util.Map;

interface GameObserver {
    void notifyChangeTurn(Player player);
    void notifyStockDecision(Player player, String fromCorp, String toCorp);
    void notifyMergeDecision(String option1, String option2);
    void notifyGameEnd(String[] names, Integer[] dollars);
    void notifyChangeStocks(Map<String, Integer> param);
}
