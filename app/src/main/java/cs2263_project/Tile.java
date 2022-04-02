package cs2263_project;

/**
 * Holds information about the tiles that are held and played
 * @author Tyson Cox
 */
class Tile {
    private final int X;
    private final int Y;
    private String corporation;

    Tile(int x, int y) {
        X = x;
        Y = y;
    }

    public String getTileName() {
        char letter = 'A';
        letter += Y;
        return Integer.toString(X + 1) + letter;
    }
}
