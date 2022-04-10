package cs2263_project

import spock.lang.Specification

class TileTest extends Specification {
    def "test tile name"() {
        given:
        Tile A1 = new Tile(0, 0)
        Tile B2 = new Tile(1, 1)

        expect:
        A1.getTileName().equals("1A")
        B2.getTileName().equals("2B")
    }
}
