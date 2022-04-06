package cs2263_project;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Holds the Tiles the players have played, as well as a
 * collection of behaviours related to managing it
 * @author Tyson Cox
 */
class GameBoard {
    public static final int WIDTH = 12;
    public static final int HEIGHT = 9;

    private final Tile[][] board;
    private int currentCorporationCount;

    public GameBoard() {
        board = new Tile[WIDTH][HEIGHT];
        currentCorporationCount = 0;

        for(int x = 0; x < WIDTH; x++)
            for(int y = 0; y < HEIGHT; y++)
                board[x][y] = null;
    }

    private List<Tile> getNeighbors(Tile tile) {
        int x = tile.getX();
        int y = tile.getY();

        List<Tile> neighbors = new ArrayList<>();

        x = max(0, x - 1);
        if (board[x][y] != null)
            neighbors.add(board[x][y]);

        x = tile.getX();
        y = min(HEIGHT, y + 1);
        if (board[x][y] != null)
            neighbors.add(board[x][y]);

        y = tile.getY();
        x = min(WIDTH, x + 1);
        if (board[x][y] != null)
            neighbors.add(board[x][y]);

        x = tile.getX();
        y = max(0, y - 1);
        if (board[x][y] != null)
            neighbors.add(board[x][y]);

        return neighbors;
    }

    /**
     * Places the tile in the board
     * @param tile the tile to place
     */
    void placeTile(Tile tile) {
        if (isTilePlaceable(tile)) {

            if (wouldTriggerFormation(tile)) {
                currentCorporationCount++;
            } else if (wouldTriggerMerge(tile)) {
                currentCorporationCount--;
            }

            board[tile.getX()][tile.getY()] = tile;
        }
    }

    /**
     * Gets the number of tiles that a corporation has on the board
     * @param name The name of the corporation to count
     * @return The number of tiles the corporation has on the board
     */
    int countCorporation(String name) {
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
    boolean isTilePlaceable(Tile tile) {
        return !isTileUnplayable(tile) && currentCorporationCount >= GameInfo.Corporations.length;
    }

    /**
     * Returns whether the tile can ever be played
     * @param tile the tile to check
     * @return whether the tile can never be played
     */
    boolean isTileUnplayable(Tile tile) {
        List<Tile> neighbors = getNeighbors(tile);

        if (neighbors.size() >= 2) {
            Set<String> corpnames = new HashSet<>();
            for(Tile t : neighbors)
                if (t.getCorporation() != null)
                    corpnames.add(t.getCorporation());

            if (corpnames.size() > 1) {
                int safeCorps = 0;

                for(String corp : corpnames)
                    if (countCorporation(corp) >= 11)
                        safeCorps++;

                return safeCorps > 1;
            }
        }

        return false;
    }

    boolean wouldTriggerMerge(Tile tile) {
        List<Tile> neighbors = getNeighbors(tile);
        if (neighbors.size() >= 2) {
            Set<String> corpnames = new HashSet<>();

            for(Tile t : neighbors)
                if (t.getCorporation() != null)
                    corpnames.add(t.getCorporation());

            int safeCorps = 0;
            for(String s : corpnames)
                if (countCorporation(s) >= 11)
                    safeCorps++;

            return safeCorps < 2;
        }

        return false;
    }

    /**
     * Checks whether the tile would form a corporation when placed
     * @param tile The tile to check
     * @return Whether the tile would cause a corporation formation
     */
    boolean wouldTriggerFormation(Tile tile) {
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
}
