package cs2263_project

import spock.lang.Specification

class GameTest extends Specification {
    def "starting a game"() {
        given:
        Game game = Game.getInstance()
        List<String> players = new ArrayList<>()
        players.add("Bob")
        players.add("Joe")

        when:
        game.start(players)

        then:
        game.players[0].getName().equals("Bob") && game.players[1].getName().equals("Joe")
    }
}
