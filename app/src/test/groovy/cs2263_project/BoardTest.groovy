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

    def "board detects formation"() {
        given:
        GameBoard board = new GameBoard()
        Tile A1 = new Tile(0, 0)
        Tile A2 = new Tile(1, 0)
        Tile I9 = new Tile(8, 8)
        board.placeTile(A1)

        expect:
        board.wouldTriggerFormation(A2)
        board.wouldTriggerFormation(I9) == false
    }

    def "invalid tile placement error"() {
        given:
        GameBoard board = new GameBoard()
        Tile impossible = new Tile(20, 20)

        when:
        board.placeTile(impossible)

        then:
        thrown(AssertionError)
    }

    def "double tile placement error"() {
        given:
        GameBoard board = new GameBoard()
        Tile tile = new Tile(2, 2)
        board.placeTile(tile)

        when:
        board.placeTile(tile)

        then:
        thrown(AssertionError)
    }

    void addAllTiles(GameBoard board, Tile... tiles) {
        for(Tile t : tiles)
            board.placeTile(t)
    }

    def "board detects merger"() {
        given:
        GameBoard board = new GameBoard()
        Tile A1 = new Tile(0, 0)
        Tile A2 = new Tile(1, 0)
        A2.setCorporation(GameInfo.Corporations[0])
        Tile C1 = new Tile(0, 2)
        Tile C2 = new Tile(1, 2)
        C2.setCorporation(GameInfo.Corporations[1])
        Tile B2merge = new Tile(1, 1)
        Tile I9nomerge = new Tile(8, 8)
        addAllTiles(board, A1, A2, C1, C2)

        expect:
        board.wouldTriggerMerge(B2merge)
        board.wouldTriggerMerge(I9nomerge) == false
    }
}
