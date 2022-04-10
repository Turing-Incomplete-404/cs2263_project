package cs2263_project;

import com.google.gson.Gson;
import lombok.NonNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tyson Cox
 * @author Eric Hill
 */
public class Game {
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
     * Resets the game to the opening state with an empty board
     * @param playerNames A list of 2-6 names of players participating
     */
    private void reset(List<String> playerNames) {
        gameInfo = new GameInfo();
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
    }

    /**
     * Starts a new game
     * @param playerNames A list of 2-6 names of players participating
     */
    public void start(@NonNull List<String> playerNames) {
        if (playerNames.size() < 2 || playerNames.size() > 6) {
            throw new RuntimeException("A game must have 2-6 players");
        }

        reset(playerNames);

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

        if (observer != null)
            observer.notifyPlayerUpdate(players[turnPlayer]);

    }

    /**
     * Draws a tile from the tile deque and ends the turn
     * @return the drawn tile
     */
    public Tile drawTile() {
        turnPlayer = (turnPlayer + 1) % players.length;
        activePlayer = turnPlayer;

        if (observer != null)
            observer.notifyPlayerUpdate(players[activePlayer]);

        return tileDeque.drawTile();
    }

    /**
     * Plays a tile to the game board
     * @param tile The tile to place
     * @return Whether the placement was successful
     */
    public boolean placeTile(Tile tile) {
        if (!board.isTilePlaceable(tile))
            return false;

        if (board.wouldTriggerFormation(tile)) {
            List<String> currentCorps = board.getCurrentCorporationList();
            List<String> validCorps = new ArrayList<>();

            for(String corp : GameInfo.Corporations) {
                if (!currentCorps.contains(corp))
                    validCorps.add(corp);
            }

            if (observer != null) {
                String[] array = new String[validCorps.size()];
                validCorps.toArray(array);
                observer.notifyFormOption(array, tile);
            }
        }

        if (board.wouldTriggerMerge(tile)) {
            List<String> options = board.getMergeOptions(tile);
            assert options.size() >= 2;

            if (observer != null)
                observer.notifyMergeDecision(options.get(0), options.get(1), tile);

            assert tile.getCorporation() != null;

            String from = tile.getCorporation().equals(options.get(0)) ? options.get(1) : options.get(0);
            String to = tile.getCorporation().equals(options.get(1)) ? options.get(0) : options.get(1);

            int start = activePlayer;
            for(int id = activePlayer; id < players.length + start; id++) {
                if (observer != null)
                    observer.notifyStockDecision(players[id % players.length], from, to);
                activePlayer = (activePlayer + 1) % players.length;
            }

            activePlayer = start;
        }

        board.placeTile(tile);
        return true;
    }

    public boolean buyStock(@NonNull String stock) {
        int corpSize = board.countCorporation(stock);

        if (corpSize == 0)
            return false;

        int price = gameInfo.getCost(stock, corpSize);

        if (players[activePlayer].getDollars() < price || !stockList.isInStock(stock))
            return false;

        players[activePlayer].addStock(stock, 1);
        players[activePlayer].subtractDollars(price);
        stockList.subtractStock(stock, 1);

        if (observer != null)
            observer.notifyPlayerUpdate(players[activePlayer]);

        return true;
    }

    public boolean isTilePlaceable(@NonNull Tile tile) {
        return board.isTilePlaceable(tile);
    }

    public boolean isTileUnplayable(@NonNull Tile tile) {
        return board.isTileUnplayable(tile);
    }

    public void sellStock(@NonNull String stock) {
        if (players[activePlayer].stockAmount(stock) <= 0)
            throw new RuntimeException("Player has insufficient stock to sell");

        int price = gameInfo.getCost(stock,board.countCorporation(stock));

        if (stockList.isInStock(stock)) {
            players[activePlayer].subtractStocks(stock,1);
            players[activePlayer].addDollars(price);
            stockList.addStock(stock, 1);

            if (observer != null)
                observer.notifyPlayerUpdate(players[activePlayer]);
        }
    }

    /**
     * Trades two of a stock from one corporation for one of another
     * @param from The corporation to trade from
     * @param to The corporation to trade to
     */
    public void tradeStock(@NonNull String from, @NonNull String to) {
        if (players[activePlayer].stockAmount(from) < 2)
            throw new RuntimeException("Player has insufficient stock to trade");

        if (stockList.isInStock(to)) {
            players[activePlayer].subtractStocks(from, 2);
            stockList.addStock(from, 2);

            players[activePlayer].addStock(to, 1);
            stockList.subtractStock(to, 1);
        }
    }

    private static class GameState {
        Player[] players;
        TileDeque tileDeque;
        GameBoard board;
        StockList stockList;
        GameInfo gameInfo;
        int turnPlayer;
        int activePlayer;
    }

    /**
     * Save the current game state to a file
     * @param path the path to write the saved state file to
     */
    public void save(@NonNull String path) {
        try {
            FileWriter writer = new FileWriter(path);
            GameState state = new GameState();

            state.players = players;
            state.tileDeque = tileDeque;
            state.board = board;
            state.stockList = stockList;
            state.gameInfo = gameInfo;
            state.turnPlayer = turnPlayer;
            state.activePlayer = activePlayer;
            new Gson().toJson(state, writer);
            writer.close();
        }
        catch (IOException ex) {
            throw new RuntimeException("Unable to serialize file with reason: " + ex.getMessage());
        }
    }

    /**
     * Load the game state from a file
      * @param path The path to the file containing the game state to load
     */
    public void load(@NonNull String path) {
        try {
            FileReader reader = new FileReader(path);
            GameState state = new Gson().fromJson(reader, GameState.class);

            players = state.players;
            tileDeque = state.tileDeque;
            board = state.board;
            stockList = state.stockList;
            gameInfo = state.gameInfo;
            turnPlayer = state.turnPlayer;
            activePlayer = state.activePlayer;

            if (observer != null)
                observer.notifyPlayerUpdate(players[turnPlayer]);
        }
        catch (FileNotFoundException ex) {
            throw new RuntimeException("Can't find save game file to load");
        }
    }

    public void registerObserver(@NonNull GameObserver observer) {
        this.observer = observer;
        if (players != null && players.length != 0)
            observer.notifyPlayerUpdate(players[turnPlayer]);
    }
}
