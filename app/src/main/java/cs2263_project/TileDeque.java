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

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Holds the list of Tiles to be drawn from and discarded to
 * @author Tyson Cox
 */
public class TileDeque {
    private final List<Tile> tiles;

    TileDeque() {
        tiles = new ArrayList<>();
        for(int x = 0; x < GameBoard.WIDTH; x++) {
            for (int y = 0; y < GameBoard.HEIGHT; y++) {
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
     * Insert a tile at the top of the deque
     * @param tile The tile to add
     */
    public void addTile(Tile tile) {
        tiles.add(0,tile);
    }
}
