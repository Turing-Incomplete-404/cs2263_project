package cs2263_project

import spock.lang.Specification

class GameInfoTest extends Specification {
    def "check primary bonus for corps"(){
        given:
        GameInfo gameInfo = new GameInfo()

        expect:
        gameInfo.getPrimaryBonus(GameInfo.Corporations[0],3) == 3000
        gameInfo.getPrimaryBonus(GameInfo.Corporations[2],5) == 6000
        gameInfo.getPrimaryBonus(GameInfo.Corporations[5],20) == 9000
    }

    def "check costs for corps"(){
        given:
        GameInfo gameInfo = new GameInfo()

        expect:
        gameInfo.getCost(GameInfo.Corporations[0],80) == 1000
        gameInfo.getCost(GameInfo.Corporations[2],80) == 1100
        gameInfo.getCost(GameInfo.Corporations[5],80) == 1200
    }

    def "check secondary bonus for corps"(){
        given:
        GameInfo gameInfo = new GameInfo()

        expect:
        gameInfo.getSecondaryBonus(GameInfo.Corporations[0],3) == 1500
        gameInfo.getSecondaryBonus(GameInfo.Corporations[4],5) == 3000
        gameInfo.getSecondaryBonus(GameInfo.Corporations[5],20) == 4500
    }

    def "check minimum boundaries"(){
        given:
        GameInfo gameInfo = new GameInfo()

        expect:
        gameInfo.getCost(GameInfo.Corporations[1],2) == 200
        gameInfo.getCost(GameInfo.Corporations[3],2) == 300
        gameInfo.getCost(GameInfo.Corporations[6],2) == 400
    }
}
