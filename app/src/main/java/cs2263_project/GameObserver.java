package cs2263_project;

import java.util.Map;

interface GameObserver {
    void notifyPlayerUpdate(Player player);
    void notifyStockDecision(Player player, String fromCorp, String toCorp);
    void notifyMergeDecision(String option1, String option2, Tile tile);
    void notifyGameEnd(String[] names, Integer[] dollars);
    void notifyChangeStocks(Map<String, Integer> param);
    void notifyFormOption(String[] options, Tile tile);
    void notifyTilePlaced(Tile tile);
}
