import cs2263_project.GameInfo
import cs2263_project.Player
import cs2263_project.Tile
import jdk.jfr.Category
import spock.lang.Specification

class PlayerTest extends Specification {
    Player player
    String corporation = GameInfo.Corporations[0]

    def setupSpec() {
        player = new Player("Joe", GameInfo.Corporations)
    }

    def "check player stock addition"() {
        when:
        player.addStock(corporation, 1)

        then:
        player.stockAmount(corporation) == 1
    }

    def "check player stock subtraction"() {
        given:
        player.addStock(corporation, 10)

        when:
        player.subtractStocks(corporation, 2)

        then:
        player.stockAmount(corporation) == 8
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
        player.addtile(tile1)
        player.addtile(tile2)
        player.addtile(tile3)
        player.addtile(tile4)

        when:
        player.removeTile(tile2)

        then:
        int i = 0;
        if (player.hand[0].equals(tile1) && player.hand[1].equals(tile3) && player.hand[2].equals(tile4)){return true;}
    }
}
