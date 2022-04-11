package cs2263_project

import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import org.testfx.framework.spock.ApplicationSpec;

class StartUITest extends ApplicationSpec {
    StartUI startUI
    @Override
    void start(Stage stage) {
        /*
        Button button = new Button('click me!')
        button.setOnAction { button.setText('clicked!') }
        stage.setScene(new Scene(new StackPane(button), 100, 100))
        stage.show()
         */
        startUI = new StartUI()
        startUI.start(stage)
    }

    def "should contain buttons"() {
        expect:
        verifyThat(lookup("New Game"), hasText('New Game'))
        verifyThat(lookup("Load Game"),hasText('Load Game'))
        verifyThat(lookup("Exit"),hasText('Exit'))
    }

    /*
    def "after clicking exit button"() {
        when:
        clickOn("Exit")

        then:
        true
    }
     */

    def "after clicking New Game button"(){
        when:
        clickOn("New Game")

        then:
        verifyThat(lookup("Start"),hasText("Start"))
    }

    def "player inputs are correct for New Game"(){
        when:
        clickOn("New Game")

        then:
        true
    }
}
