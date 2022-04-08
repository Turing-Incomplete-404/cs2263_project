package cs2263_project;

import java.util.Map;

class GameUI implements GameObserver {

    @Override
    public void notifyPlayerUpdate(Player player) {

    }

    @Override
    public void notifyStockDecision(Player player, String fromCorp, String toCorp) {

    }

    @Override
    public void notifyMergeDecision(String option1, String option2, Tile tile) {

    }

    @Override
    public void notifyGameEnd(String[] names, Integer[] dollars) {

    }

    @Override
    public void notifyChangeStocks(Map<String, Integer> param) {

    }

    @Override
    public void notifyFormOption(String[] options, Tile tile) {

    }
}
