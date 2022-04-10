package cs2263_project

import lombok.NonNull
import spock.lang.Specification

class GameTest extends Specification {
    class GameObserverStub implements GameObserver {
        public boolean playerUpdateCalled
        public boolean stockDecisionCalled
        public boolean mergeDecisionCalled
        public boolean gameEndCalled
        public boolean changeStocksCalled
        public boolean formOptionCalled

        @Override
        void notifyPlayerUpdate(Player player) { playerUpdateCalled = true }
        @Override
        void notifyStockDecision(Player player, String fromCorp, String toCorp) { stockDecisionCalled = true }
        @Override
        void notifyMergeDecision(String option1, String option2, Tile tile) {
            mergeDecisionCalled = true
            tile.setCorporation(option1)
        }
        @Override
        void notifyGameEnd(String[] names, Integer[] dollars) { gameEndCalled = true }
        @Override
        void notifyChangeStocks(Map<String, Integer> param) { changeStocksCalled = true }
        @Override
        void notifyFormOption(String[] options, Tile tile) {
            formOptionCalled = true
            assert options.length > 0
            tile.setCorporation(options[0])
        }
    }

    List<String> get2GamePlayers() {
        List<String> players = new ArrayList<>()
        players.add("Bob")
        players.add("Joe")
        return players
    }

    def "starting a game"() {
        given:
        Game game = Game.getInstance()

        when:
        game.reset(get2GamePlayers())

        then:
        game.players[0].getName().equals("Bob")
        game.players[1].getName().equals("Joe")
    }

    def "observer called on game start"() {
        given:
        Game game = Game.getInstance()
        var observerStub = new GameObserverStub()
        game.registerObserver(observerStub)
        observerStub.playerUpdateCalled = false

        when:
        game.start(get2GamePlayers())

        then:
        observerStub.playerUpdateCalled
    }

    def "game detects corporation formation"() {
        given:
        Game game = Game.getInstance()
        var observerStub = new GameObserverStub()
        game.registerObserver(observerStub)
        game.reset(get2GamePlayers())
        Tile A1 = new Tile(0, 0)
        Tile A2 = new Tile(1, 0)

        when:
        game.placeTile(A1)
        game.placeTile(A2)

        then:
        observerStub.formOptionCalled
        game.board.getCurrentCorporationList().size() == 1
        game.board.countCorporation(game.board.getCurrentCorporationList().get(0)) == 2
    }

    def "game detects merger"() {
        given:
        Game game = Game.getInstance()
        var observerStub = new GameObserverStub()
        game.registerObserver(observerStub)
        game.reset(get2GamePlayers())
        Tile A1 = new Tile(0, 0)
        Tile A2 = new Tile(1, 0)
        Tile C1 = new Tile(0, 2)
        Tile C2 = new Tile(1, 2)
        Tile B2merge = new Tile(1, 1)
        game.placeTile(A1)
        game.placeTile(A2)
        game.placeTile(C1)
        game.placeTile(C2)

        when:
        game.placeTile(B2merge)

        then:
        observerStub.mergeDecisionCalled
        game.board.getCurrentCorporationList().size() == 1
        game.board.countCorporation(game.board.getCurrentCorporationList().get(0)) == 5
    }

    void addAllTiles(GameBoard board, Tile... tiles) {
        for(Tile t : tiles)
            board.placeTile(t)
    }

    def "check game buy stock"() {
        given:
        String corporation = GameInfo.Corporations[0]
        Game game = Game.getInstance()
        game.reset(get2GamePlayers())
        game.activePlayer = 0
        game.turnPlayer = 0

        when:
        game.buyStock(corporation)

        then:
        game.stockList.Stocks.get(corporation) == 24
        game.players[game.activePlayer].stockAmount(corporation) == 1
        game.players[0].dollars == 6000 - 500
    }
}
