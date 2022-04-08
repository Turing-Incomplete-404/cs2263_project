package cs2263_project;

import java.util.Map;

class GameUI implements GameObserver {

    @Override
    public void notifyChangeTurn(Player player) {

    }

    @Override
    public void notifyStockDecision(Player player, String fromCorp, String toCorp) {

    }

    @Override
    public void notifyMergeDecision(String option1, String option2) {

    }

    @Override
    public void notifyGameEnd(String[] names, Integer[] dollars) {

    }

    @Override
    public void notifyChangeStocks(Map<String, Integer> param) {

    }
}
