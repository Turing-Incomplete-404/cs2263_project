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

import com.google.gson.Gson;
import lombok.NonNull;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class keeps track of all of the game-logic and is responsible for
 * holding and communicating changes to all of the data classes
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

    /**
     * @return the game class's singleton instance
     */
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
            if (observer != null)
                observer.notifyTilePlaced(startTiles[i]);
        }

        turnPlayer = activePlayer = smallestIndex;

        if (observer != null) {
            observer.notifyChangeStocks(stockList.getAllStocks());
            observer.notifyPlayerUpdate(players[turnPlayer]);
        }

    }

    /**
     * Draws a tile from the tile deque, inserts it into the turn player's hand, and ends the turn
     */
    public void drawTile() {
        if (players[turnPlayer].getTiles().size() < 6)
            players[turnPlayer].addTile(tileDeque.drawTile());

        turnPlayer = (turnPlayer + 1) % players.length;
        activePlayer = turnPlayer;

        if (observer != null)
            observer.notifyPlayerUpdate(players[activePlayer]);

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

            assert tile.getCorporation() != null;
            players[turnPlayer].addStock(tile.getCorporation(), 1);
            stockList.subtractStock(tile.getCorporation(), 1);

            if (observer != null) {
                observer.notifyPlayerUpdate(players[turnPlayer]);
                observer.notifyChangeStocks(stockList.getAllStocks());
            }
        }

        if (board.wouldTriggerMerge(tile)) {
            List<String> options = board.getMergeOptions(tile);
            assert options.size() >= 2;

            int size1 = board.countCorporation(options.get(0));
            int size2 = board.countCorporation(options.get(1));

            if (size1 == size2) {
                if (observer != null)
                    observer.notifyMergeDecision(options.get(0), options.get(1), tile);
            }
            else {
                if (size1 > size2)
                    tile.setCorporation(options.get(0));
                else
                    tile.setCorporation(options.get(1));
            }

            assert tile.getCorporation() != null;

            String to = tile.getCorporation();
            String from = to.equals(options.get(0)) ? options.get(1) : options.get(0);


            int start = activePlayer;
            for(int id = activePlayer; id < players.length + start; id++) {
                if (observer != null)
                    observer.notifyStockDecision(players[id % players.length], from, to);
                activePlayer = (activePlayer + 1) % players.length;
            }

            activePlayer = start;
        }

        board.placeTile(tile);

        if (observer != null)
            board.forEachTile(signaltile -> observer.notifyTilePlaced(signaltile));

        List<String> formedCorporations = board.getCurrentCorporationList();
        for(String corp : formedCorporations)
            if (board.countCorporation(corp) >= 41) {
                String[] names = new String[players.length];
                Integer[] dollars = new Integer[players.length];

                Arrays.sort(players);

                for(int i = 0; i < players.length; i++) {
                    names[i] = players[i].getName();
                    dollars[i] = players[i].getDollars();
                }

                observer.notifyGameEnd(names, dollars);
            }

        return true;
    }

    /**
     * Has the active player purchase a stock
     * @param stock - which corporation the player is buying a stock from
     * @return - success of purchase
     */
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

        if (observer != null) {
            observer.notifyChangeStocks(stockList.getAllStocks());
            observer.notifyPlayerUpdate(players[activePlayer]);
        }

        return true;
    }

    /**
     * Checks whether a tile can currently be played
     * @param tile the tile to check
     * @return whether the tile can be played
     */
    public boolean isTilePlaceable(@NonNull Tile tile) {
        return board.isTilePlaceable(tile);
    }

    /**
     * Checks whether a tile can ever be played
     * @param tile the tile to check
     * @return whether the tile can ever be played
     */
    public boolean isTileUnplayable(@NonNull Tile tile) {
        return board.isTileUnplayable(tile);
    }

    /**
     * Sell a stock from the active player's hand
     * @param stock the corporation to sell stock of
     */
    public void sellStock(@NonNull String stock) {
        if (players[activePlayer].stockAmount(stock) <= 0)
            throw new RuntimeException("Player has insufficient stock to sell");

        int price = gameInfo.getCost(stock,board.countCorporation(stock));

        if (stockList.isInStock(stock)) {
            players[activePlayer].subtractStocks(stock,1);
            players[activePlayer].addDollars(price);
            stockList.addStock(stock, 1);

            if (observer != null) {
                observer.notifyChangeStocks(stockList.getAllStocks());
                observer.notifyPlayerUpdate(players[activePlayer]);
            }
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

            if (observer != null) {
                observer.notifyChangeStocks(stockList.getAllStocks());
                observer.notifyPlayerUpdate(players[activePlayer]);
            }
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

            if (observer != null) {
                observer.notifyPlayerUpdate(players[turnPlayer]);
                observer.notifyChangeStocks(stockList.getAllStocks());

                board.forEachTile(tile -> observer.notifyTilePlaced(tile));
            }
        }
        catch (FileNotFoundException ex) {
            throw new RuntimeException("Can't find save game file to load");
        }
    }

    /**
     * Register a game-state observer to get notifications about changes in game state
     * @param observer the observer to register
     */
    public void registerObserver(@NonNull GameObserver observer) {
        this.observer = observer;
        if (players != null && players.length != 0)
            observer.notifyPlayerUpdate(players[turnPlayer]);
    }
}
