package cs2263_project;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Holds the list of Tiles to be drawn from and discarded to
 * @author Tyson Cox
 */
class TileDeque {
    private final List<Tile> tiles;

    TileDeque() {
        tiles = new ArrayList<>();
        for(int x = 0; x < 12; x++) {
            for (int y = 0; y < 9; y++) {
                tiles.add(new Tile(x, y));
            }
        }

        Collections.shuffle(tiles);
    }

    /**
     * Draw a tile
     * @return the drawn tile
     */
    public Tile drawTile() {
        Tile returnVal = tiles.get(0);
        tiles.remove(0);
        return returnVal;
    }

    /**
     * Insert a tile at the bottom of the deque
     * @param tile The tile to add
     */
    public void addTile(Tile tile) {
        tiles.add(tile);
    }
}
