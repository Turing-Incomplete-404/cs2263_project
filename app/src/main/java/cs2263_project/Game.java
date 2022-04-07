package cs2263_project;

import lombok.NonNull;
import lombok.Getter;

import javax.naming.InsufficientResourcesException;
import java.util.List;

/**
 *
 * @author Tyson Cox
 */
class Game {
    private Player[] players;
    private TileDeque tileDeque;
    private GameBoard board;
    private StockList stockList;
    private GameInfo gameInfo;
    private GameObserver observer;

    private int turnPlayer;
    private int activePlayer;

    private Game() { }

    private static class SingletonHolder {
        private static final Game INSTANCE = new Game();
    }

    public static Game getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * Starts a new game
     * @param playerNames A list of 2-6 names of players participating
     */
    public void start(@NonNull List<String> playerNames) {
        if (playerNames.size() < 2 || playerNames.size() > 6) {
            throw new RuntimeException("A game must have 2-6 players");
        }

        tileDeque = new TileDeque();
        board = new GameBoard();
        stockList = new StockList(GameInfo.Corporations, GameInfo.STARTING_STOCKS);
        turnPlayer = 0;
        activePlayer = 0;

        players = new Player[playerNames.size()];
        for(int i = 0; i < playerNames.size(); i++) {
            players[i] = new Player(playerNames.get(i), GameInfo.Corporations);
            for(int j = 0; j < 6; j++) {
                players[i].addTile(tileDeque.drawTile());
            }
        }

        Tile[] startTiles = new Tile[players.length];
        int smallestDist = 0;
        int smallestIndex = 0;

        for(int i = 0; i < startTiles.length; i++) {
            startTiles[i] = tileDeque.drawTile();
            int dist = startTiles[i].getX() + startTiles[i].getY();

            if (dist < smallestDist) {
                smallestDist = dist;
                smallestIndex = i;
            }
            else if (dist == smallestDist && startTiles[i].getX() < startTiles[smallestIndex].getX()) {
                smallestIndex = i;
            }

            board.placeTile(startTiles[i]);
        }

        turnPlayer = activePlayer = smallestIndex;
        observer.notifyChangeTurn(players[turnPlayer]);
    }

    /**
     * Draws a tile from the tile deque
     * @return the drawn tile
     */
    public Tile drawTile() {
        return tileDeque.drawTile();
    }

    public boolean placeTile(Tile tile) {
        if (!board.isTilePlaceable(tile))
            return false;


    }

    public boolean buyStock(@NonNull String stock) {
        int price = gameInfo.getCost(stock,board.countCorporation(stock));
        if (players[activePlayer].getdollars() < price){
            throw new RuntimeException("Insufficient money to make purchase");
        }
        players[activePlayer].addStock(stock,1);
        players[activePlayer].subtractDollars(price);
        return true;
    }

    public boolean isTilePlaceable(@NonNull Tile tile) {
        return board.isTilePlaceable(tile);
    }

    public boolean isTileUnplayable(@NonNull Tile tile) {
        return board.isTileUnplayable(tile);
    }

    public void sellStock(@NonNull String stock) {
        int price = gameInfo.getCost(stock,board.countCorporation(stock));
        players[activePlayer].subtractStocks(stock,1);
        players[activePlayer].addDollars(price);
    }

    public void tradeStock(@NonNull String string) {

    }

    public void save(@NonNull String path) {

    }

    public void load(@NonNull String path) {

    }

    public void registerObserver(@NonNull GameObserver observer) {
        this.observer = observer;
    }
}
