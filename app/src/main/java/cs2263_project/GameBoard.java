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

import lombok.NonNull;
import java.util.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Holds the Tiles the players have played, as well as a
 * collection of behaviours related to managing game board state
 * @author Tyson Cox
 */
public class GameBoard {
    public static final int WIDTH = 12;
    public static final int HEIGHT = 9;

    private static final int CORPORATION_SAFE_SIZE = 11;

    private final Tile[][] board;
    private int currentCorporationCount;

    public GameBoard() {
        board = new Tile[WIDTH][HEIGHT];
        currentCorporationCount = 0;

        for(int x = 0; x < WIDTH; x++)
            for(int y = 0; y < HEIGHT; y++)
                board[x][y] = null;
    }

    /**
     * Get all immediately adjacent tiles to a given tile
     * @param tile The tile to start from
     * @return The list of immediately adjacent tiles
     */
    private List<Tile> getNeighbors(@NonNull Tile tile) {
        int x = tile.getX();
        int y = tile.getY();

        Set<Tile> neighbors = new HashSet<>();

        x = max(0, x - 1);
        if (board[x][y] != null)
            neighbors.add(board[x][y]);

        x = tile.getX();
        y = min(HEIGHT - 1, y + 1);
        if (board[x][y] != null)
            neighbors.add(board[x][y]);

        y = tile.getY();
        x = min(WIDTH - 1, x + 1);
        if (board[x][y] != null)
            neighbors.add(board[x][y]);

        x = tile.getX();
        y = max(0, y - 1);
        if (board[x][y] != null)
            neighbors.add(board[x][y]);

        return neighbors.stream().toList();
    }

    /**
     * Recurses through adjacent tiles
     * @param tiles The collection to add neighbors to
     * @param tile The tile to add and check neighbors from
     */
    private void recurseTile(Set<Tile> tiles, Tile tile) {
        tiles.add(tile);

        List<Tile> neighbors = getNeighbors(tile);
        for(Tile t : neighbors) {
            if (!tiles.contains(t)) {
                recurseTile(tiles, t);
            }
        }
    }

    /**
     * Returns a set containing a given tile and all recursively adjacent tiles
     * @param tile The tile to start the chain from
     * @return The collection of adjacent tiles (chain)
     */
    private Set<Tile> getWholeChain(@NonNull Tile tile) {
        Set<Tile> tiles = new HashSet<>();
        recurseTile(tiles, tile);
        return tiles;
    }

    /**
     * Places the tile in the board
     * @param tile the tile to place
     */
    void placeTile(@NonNull Tile tile) {
        assert tile.getX() >= 0 && tile.getX() <= WIDTH - 1;
        assert tile.getY() >= 0 && tile.getY() <= HEIGHT - 1;
        assert board[tile.getX()][tile.getY()] == null;

        if (isTilePlaceable(tile)) {

            if (wouldTriggerFormation(tile)) {
                currentCorporationCount++;
                List<Tile> neighbors = getNeighbors(tile);

                for(Tile neighbor : neighbors) {
                    Set<Tile> chain = getWholeChain(neighbor);

                    for(Tile corpTile : chain)
                        corpTile.setCorporation(tile.getCorporation());
                }

                board[tile.getX()][tile.getY()] = tile;
            } else if (wouldTriggerMerge(tile)) {
                currentCorporationCount--;
                List<Tile> neighbors = getNeighbors(tile);

                for(Tile neighbor : neighbors) {
                    Set<Tile> chain = getWholeChain(neighbor);

                    for(Tile corpTile : chain) {
                        corpTile.setCorporation(tile.getCorporation());
                    }
                }

                board[tile.getX()][tile.getY()] = tile;
            }
            else {
                List<Tile> neighbors = getNeighbors(tile);
                Set<String> corps = new HashSet<>();
                for(Tile t : neighbors)
                    if (t.getCorporation() != null)
                        corps.add(t.getCorporation());

                assert (corps.size() <= 1);

                if (corps.size() == 1) {
                    String corporationToSet = corps.iterator().next();
                    tile.setCorporation(corporationToSet);

                    for (Tile t : neighbors) {
                        var chain = getWholeChain(t);
                        for (Tile c : chain) {
                            c.setCorporation(corporationToSet);
                        }
                    }
                }

                board[tile.getX()][tile.getY()] = tile;
            }
        }
    }

    /**
     * Gets the number of tiles that a corporation has on the board
     * @param name The name of the corporation to count
     * @return The number of tiles the corporation has on the board
     */
    public int countCorporation(@NonNull String name) {
        int count = 0;

        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                if (board[x][y] != null && board[x][y].getCorporation() != null) {
                    if (board[x][y].getCorporation().equals(name)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * Returns whether a tile is currently placeable
     * @param tile The tile to check
     * @return whether the tile is placeable
     */
    boolean isTilePlaceable(@NonNull Tile tile) {
        return !isTileUnplayable(tile) && !(wouldTriggerFormation(tile) && currentCorporationCount >= GameInfo.Corporations.length);
    }

    /**
     * Returns whether the tile can ever be played
     * @param tile the tile to check
     * @return whether the tile can never be played
     */
    boolean isTileUnplayable(@NonNull Tile tile) {
        List<Tile> neighbors = getNeighbors(tile);

        if (neighbors.size() >= 2) {
            Set<String> corpnames = new HashSet<>();
            for(Tile t : neighbors)
                if (t.getCorporation() != null)
                    corpnames.add(t.getCorporation());

            if (corpnames.size() > 1) {
                int safeCorps = 0;

                for(String corp : corpnames)
                    if (countCorporation(corp) >= CORPORATION_SAFE_SIZE)
                        safeCorps++;

                return safeCorps > 1;
            }
        }

        return false;
    }

    /**
     * Checks whether the tile would cause a merge between two corporations
     * @param tile The tile to check
     * @return Whether the tile would cause a merge
     */
    boolean wouldTriggerMerge(@NonNull Tile tile) {
        List<Tile> neighbors = getNeighbors(tile);

        if (neighbors.size() >= 2) {
            Set<String> corpnames = new HashSet<>();

            for(Tile t : neighbors)
                if (t.getCorporation() != null)
                    corpnames.add(t.getCorporation());

            int safeCorps = 0;
            for(String s : corpnames)
                if (countCorporation(s) >= CORPORATION_SAFE_SIZE)
                    safeCorps++;

            return corpnames.size() >= 2 && safeCorps < 2;
        }

        return false;
    }

    /**
     * Checks whether the tile would form a corporation when placed
     * @param tile The tile to check
     * @return Whether the tile would cause a corporation formation
     */
    boolean wouldTriggerFormation(@NonNull Tile tile) {
        List<Tile> neighbors = getNeighbors(tile);
        if (neighbors.size() >= 1) {
            for(Tile t : neighbors) {
                if (t.getCorporation() != null) {
                    return false;
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Returns a list containing all currently formed corporations
     * @return A list containing all currently formed corporations
     */
    public List<String> getCurrentCorporationList() {
        Set<String> corps = new HashSet<>();

        for(int x = 0; x < WIDTH; x++) {
            for(int y = 0; y < HEIGHT; y++) {
                if (board[x][y] != null && board[x][y].getCorporation() != null) {
                    corps.add(board[x][y].getCorporation());
                }
            }
        }

        return corps.stream().toList();
    }

    /**
     * Returns a list of the two corporations that would be merged by placing the tile
     * @param tile The tile to place
     * @return The two corporations that would be merged, empty otherwise
     */
    public List<String> getMergeOptions(Tile tile) {
        Set<String> corps = new HashSet<>();
        List<Tile> neighbors = getNeighbors(tile);

        for(Tile t : neighbors)
            if(t.getCorporation() != null)
                corps.add(t.getCorporation());

        if (corps.size() < 2)
            return new ArrayList<String>();
        else
            return corps.stream().toList();
    }

    /**
     * Defines a function that accepts a tile - for the purpose of passing a lambda
     */
    public interface TileIterator { void func(Tile tile); }

    /**
     * Calls a function on every tile in the board
     * @param iterator A function or lambda to call on the tiles
     */
    public void forEachTile(TileIterator iterator) {
        for (int x = 0; x < WIDTH; x++)
            for (int y = 0; y < HEIGHT; y++)
                if (board[x][y] != null)
                    iterator.func(board[x][y]);
    }
}
