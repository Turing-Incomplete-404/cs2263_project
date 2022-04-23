/**
 * MIT License
 *
 * Copyright (c) 2022 Turing-Incomplete-404
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package cs2263_project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the UI components related to starting and loading games
 * @author Scott Brown
 * @author Tyson Cox
 */
public class StartUI extends Application {
    private int mainWidth = 500;
    private int mainHeight = 600;
    private int menuNumber;
    private Scene scene;
    private BorderPane root;
    private BorderPane menu1;
    private BorderPane menu2;
    private Game game;
    private GameUI gameUI;
    private Stage stage;

    private List<TextField> playerNameFields;


    public StartUI() {
        menuNumber = 1;
        root = new BorderPane();
        menu1 = new BorderPane();
        menu2 = new BorderPane();
        playerNameFields = new ArrayList<>();
    }

    private void updateScene(Node node) {
        root.getChildren().clear();
        root.setCenter(node);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setTitle("Acquire");

        buildMenu1();
        updateScene(menu1);

        buildMenu2();

        //scene = new Scene(root, mainWidth, mainHeight);
        scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.R)
                StyleManager.refresh();
        });
    
        stage.setScene(scene);
        stage.setX(((Screen.getPrimary().getVisualBounds().getWidth() /2) - (mainWidth /2)));
        stage.setY(50);
        stage.show();
        stage.setAlwaysOnTop(true);
        stage.setAlwaysOnTop(false);

        game = Game.getInstance();
        gameUI = new GameUI(stage);
        game.registerObserver(gameUI);
    }

    private void buildMenu1() {
        menu1.setTop(addMenu1Top());
        menu1.setCenter(addMenu1Center());
        menu1.setBottom(addMenu1Bottom());
    }

    private void buildMenu2() {
        menu2.setTop(addMenu1Top());
        menu2.setCenter(addMenu2Center());
        menu2.setBottom(addMenu1Bottom());
    }

    private HBox addMenu1Top() {
        HBox topHBox = new HBox();
        VBox titleBox = new VBox();
        titleBox.setPrefSize(mainWidth - (mainWidth * 0.2), mainHeight - (mainHeight * 0.8));

        Text gameName = new Text("ACQUIRE");
        Text authors = new Text("By: Team Turing-Incomplete");

        titleBox.getChildren().addAll(gameName, authors);
        topHBox.getChildren().addAll(titleBox);

        StyleManager.registerControl("StartTopPane", topHBox);
        StyleManager.registerControl("StartTopTitlePane", titleBox);
        StyleManager.registerControl("StartTopHeader", gameName);
        StyleManager.registerControl("StartTopSubheader", authors);

        return topHBox;
    }

    private HBox addMenu1Center() {
        String centerColor = "#55BED9;";
        String menuColor = "#99D9EA";

        HBox centerHBox = new HBox();


        VBox firstMenuBox = new VBox();
        firstMenuBox.setPrefSize(mainWidth - (mainWidth * 0.5), mainHeight - (mainHeight * 0.5));

        StyleManager.registerControl("StartMenu1CenterPane", centerHBox);
        StyleManager.registerControl("StartMenu1CenterColumn", firstMenuBox);

        double buttonWidth = (firstMenuBox.getPrefWidth() - (firstMenuBox.getPrefWidth() * 0.1));
        double buttonHeight = (firstMenuBox.getPrefHeight() - (firstMenuBox.getPrefHeight() * 0.95));

        Button newGameButton = new Button("New Game");
        Button loadGameButton = new Button("Load Game");
        Button exitButton = new Button("Exit");

        newGameButton.setPrefSize(buttonWidth, buttonHeight);
        loadGameButton.setPrefSize(buttonWidth, buttonHeight);
        exitButton.setPrefSize(buttonWidth, buttonHeight);

        StyleManager.registerControls("StartMenu1Button", newGameButton, loadGameButton, exitButton);

        centerHBox.getChildren().add(firstMenuBox);

        firstMenuBox.getChildren().addAll(newGameButton, loadGameButton, exitButton);
        
        newGameButton.setOnAction((ActionEvent changeMenu) -> {
            updateScene(menu2);
        });

        loadGameButton.setOnAction((ActionEvent loadGame) -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Game file to load");
            File chosen = chooser.showOpenDialog(stage);
            if (chosen != null && chosen.exists()) {
                loadGame(chosen.getPath());
            }
        });

        exitButton.setOnAction((ActionEvent changeMenu) -> {
            Platform.exit();
        });

        return centerHBox;
    }

    private HBox addMenu2Center() {

        HBox box = new HBox();
        HBox split = new HBox();

        box.getChildren().add(split);

        StyleManager.registerControl("StartMenu2Pane", box);
        StyleManager.registerControl("StartMenu2Split", split);

        VBox names = new VBox();
        VBox controls = new VBox();

        StyleManager.registerControl("StartMenu2NamesColumn", names);
        StyleManager.registerControl("StartMenu2ControlsColumn", controls);

        Label playerCountlabel = new Label("Number of players");

        StyleManager.registerControl("StartMenu2Text", playerCountlabel);

        Button startGame = new Button("Start");
        Button backMenu = new Button("Back");

        startGame.setOnAction(event -> startGame());
        backMenu.setOnAction(event -> updateScene(menu1));

        StyleManager.registerControls("StartMenu2Button", startGame, backMenu);

        ComboBox<Integer> playerCounts = new ComboBox<>();
        playerCounts.getItems().addAll(2, 3, 4, 5, 6);
        StyleManager.registerControl("StartMenu2Dropdown", playerCounts);

        controls.getChildren().addAll(playerCountlabel, playerCounts, startGame, backMenu);

        names.getChildren().clear();
        playerNameFields.clear();

        names.getChildren().add(new Label("Player names"));

        for(int i = 0; i < 2; i++) {
            TextField field = new TextField();
            playerNameFields.add(field);
            names.getChildren().add(field);
        }

        playerCounts.setOnAction(event -> {
            names.getChildren().clear();
            playerNameFields.clear();

            names.getChildren().add(new Label("Player names"));

            int selAmount = playerCounts.getValue();
            for(int i = 0; i < selAmount; i++) {
                TextField field = new TextField();
                playerNameFields.add(field);
                names.getChildren().add(field);
            }
        });

        playerCounts.getSelectionModel().select(0);

        split.getChildren().addAll(controls, names);

        return box;
    }

    private void popupError(String msg) {
        Dialog<ButtonType> box = new Dialog<>();
        box.getDialogPane().getButtonTypes().add(new ButtonType("OK", ButtonBar.ButtonData.OK_DONE));
        box.setTitle("Error");
        box.setContentText(msg);
        box.show();
    }

    private HBox addMenu1Bottom() {
        String bottomColor = "#C3C3C3;";

        HBox bottomHBox = new HBox();
        bottomHBox.setPadding(new Insets(15, 5, 15, 5)); // Top, Right, Bottom, Left
        bottomHBox.setSpacing(10);
        bottomHBox.setStyle("-fx-background-color: " + bottomColor);
        bottomHBox.setAlignment(Pos.CENTER);

        Button testButton = new Button("Bottom");
        testButton.setStyle("-fx-font: normal bold 12px 'sansserif' ");

        // Set sizes
        testButton.setPrefSize(100, 20);
        

        bottomHBox.getChildren().addAll();

        return bottomHBox;
    }

    private void startGame() {
        List<String> players = new ArrayList<>();
        for(TextField t : playerNameFields) {
            players.add(t.getText());
        }

        if (players.size() < 2 || players.size() > 6) {
            popupError("Invalid player count");
            return;
        }

        for(String n : players) {
            if (n == null || n.isEmpty()) {
                popupError("Player names may not be empty");
                return;
            }
        }

        game.start(players);
        updateScene(gameUI.getRoot());
        stage.sizeToScene();
    }

    private void loadGame(String path) {
        try {
            game.load(path);
            updateScene(gameUI.getRoot());
            stage.sizeToScene();
        }
        catch (Exception ex) {
            popupError("Game failed to load. Are you loading a saved game json file? \n\n" + ex.getMessage());
        }
    }
}
