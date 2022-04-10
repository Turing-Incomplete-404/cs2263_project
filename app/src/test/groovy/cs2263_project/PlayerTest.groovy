package cs2263_project

import cs2263_project.GameInfo
import cs2263_project.Player
import cs2263_project.Tile
import jdk.jfr.Category
import spock.lang.Specification

class PlayerTest extends Specification {
    static Player player
    String corporation = GameInfo.Corporations[0]

    def "check player stock addition"() {
        given:
        player = new Player("Joe", GameInfo.Corporations)

        when:
        player.addStock(corporation, 1)

        then:
        player.stockAmount(corporation) == 1
    }

    def "check player stock subtraction"() {
        given:
        player = new Player("Joe", GameInfo.Corporations)
        player.addStock(corporation, 10)

        when:
        2+2

        then:
        player.stockAmount(corporation) == 10
    }

    def "check player stock boundary"() {
        given:
        player.addStock(corporation, 1)

        when:
        player.subtractStocks(corporation, 2)

        then:
        player.stockAmount(corporation) != -1
    }

    def "check removing tile from hand"(){
        given:
        Tile tile1 = new Tile(1,1)
        Tile tile2 = new Tile(2,1)
        Tile tile3 = new Tile(1,2)
        Tile tile4 = new Tile(2,2)
        player.addTile(tile1)
        player.addTile(tile2)
        player.addTile(tile3)
        player.addTile(tile4)

        when:
        player.removeTile(tile2)

        then:
        player.hand[0].equals(tile1)
        player.hand[1].equals(tile3)
        player.hand[2].equals(tile4)
    }
}
