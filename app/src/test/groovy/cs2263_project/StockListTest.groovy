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
        stocklist.stocks.get(corporation) == 24
    }

    def "check stocklist isInStock"() {
        given:
        StockList stocklist = new StockList(GameInfo.Corporations, GameInfo.STARTING_STOCKS)

        when:
        stocklist.subtractStock(corporation, 25)

        then:
        stocklist.isInStock(corporation) == false
    }

    def "check stocklist stock addition"() {
        given:
        StockList stocklist = new StockList(GameInfo.Corporations, GameInfo.STARTING_STOCKS)
        stocklist.subtractStock(corporation,5)

        when:
        stocklist.addStock(corporation, 2)

        then:
        stocklist.stocks.get(corporation) == 22
    }

    def "check stocklist constructor and add/subtract"() {
        given:
        StockList stocklist = new StockList(new String[] { "corp1","corp2","corp3" }, 69)

        when:
        stocklist.subtractStock("corp1", 1)
        stocklist.subtractStock("corp3", 59)

        then:
        stocklist.stocks.get("corp1") == 68
        stocklist.stocks.get("corp2") == 69
        stocklist.stocks.get("corp3") == 10
    }
}
