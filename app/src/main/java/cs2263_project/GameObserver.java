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

import java.util.Map;

/**
 * Represents a service that wants to receive updates about changes in game state
 * @author Tyson Cox
 */
interface GameObserver {
    /**
     * Notifies that information relating to a player has changed
     * @param player the player that has changed
     */
    void notifyPlayerUpdate(Player player);

    /**
     * Notifies that a player needs to make a decision regarding stock of a corporation that
     * lost a merger
     * @param player the player making the decision
     * @param fromCorp the corporation going away
     * @param toCorp the corporation taking over
     */
    void notifyStockDecision(Player player, String fromCorp, String toCorp);

    /**
     * Notifies that a player must make a decision regarding which corporation wins a merger
     * @param option1 The first corporation
     * @param option2 The second corporation
     * @param tile The tile that is merging them - choose by calling tile.setCorporation()
     */
    void notifyMergeDecision(String option1, String option2, Tile tile);

    /**
     * Notifies that the game is over
     * @param names The names of the players - ordered by score
     * @param dollars The dollars (scores) of the players - ordered by score
     */
    void notifyGameEnd(String[] names, Integer[] dollars);

    /**
     * Notifies that a change in the stock amounts has changed
     * @param param the list of stocks and their quantities
     */
    void notifyChangeStocks(Map<String, Integer> param);

    /**
     * Notifies that a player must make a decision regarding which corporation
     * to form from their placed tile
     * @param options the possible corporations to form
     * @param tile the tile that is form the corporation - choose by calling tile.setCorporation()
     */
    void notifyFormOption(String[] options, Tile tile);

    /**
     * Notify that a tile has changed state in some way
     * @param tile the tile that has changed
     */
    void notifyTilePlaced(Tile tile);
}
