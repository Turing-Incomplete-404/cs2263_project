package cs2263_project

import spock.lang.Specification

class StockListTest extends Specification {
    String corporation = GameInfo.Corporations[0]
    def "check stocklist stock subtraction"() {
        given:
        StockList stocklist = new StockList(GameInfo.Corporations, GameInfo.STARTING_STOCKS)

        when:
        stocklist.subtractStock(corporation, 1)

        then:
        stocklist.Stocks.get(corporation) == 24
    }

    def "check stocklist isInStock"() {
        given:
        StockList stocklist = new StockList(GameInfo.Corporations, GameInfo.STARTING_STOCKS)

        when:
        stocklist.subtractStock(corporation, 25)

        then:
        stocklist.isInStock(corporation)
    }

}
