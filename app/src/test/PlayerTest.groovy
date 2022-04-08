import cs2263_project.GameInfo
import cs2263_project.Player
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
}
