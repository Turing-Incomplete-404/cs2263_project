package cs2263_project

import spock.lang.Specification

class BoardTest extends Specification {
    def "tile placement"() {
        given:
        GameBoard board = new GameBoard()
        Tile A1 = new Tile(0, 0)

        when:
        board.placeTile(A1)

        then:
        board.board[0][0] != null
    }
}
