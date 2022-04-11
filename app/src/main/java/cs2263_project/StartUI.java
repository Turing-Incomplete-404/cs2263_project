package cs2263_project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.checkerframework.checker.units.qual.C;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
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

        stage.setScene(scene);
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
        String topColor = "#00A2E8;";
        String titleBoxColor = "#FFFFFF";

        HBox topHBox = new HBox();
        topHBox.setPadding(new Insets(15, 5, 0, 5)); // Top, Right, Bottom, Left
        topHBox.setSpacing(10);
        topHBox.setStyle("-fx-background-color: " + topColor);
        topHBox.setAlignment(Pos.CENTER);

        VBox titleBox = new VBox();
        titleBox.setPadding(new Insets(15, 5, 0, 5)); // Top, Right, Bottom, Left
        titleBox.setSpacing(10);
        titleBox.setStyle("-fx-border-width: 5 5 0 5; -fx-border-color: #000; -fx-background-color: " + titleBoxColor);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPrefSize(mainWidth - (mainWidth * 0.2), mainHeight - (mainHeight * 0.8));

        Text gameName = new Text("ACQUIRE");
        Text authors = new Text("By: Team Turing-Incomplete");
        gameName.setStyle("-fx-font: normal bold 40px 'sansserif' ");
        authors.setStyle("-fx-font: normal bold 16px 'sansserif' ");
        
        titleBox.getChildren().addAll(gameName, authors);
        topHBox.getChildren().addAll(titleBox);

        return topHBox;
    }

    private HBox addMenu1Center() {
        String centerColor = "#55BED9;";
        String menuColor = "#99D9EA";

        HBox centerHBox = new HBox();
        centerHBox.setPadding(new Insets(0, 5, 0, 5)); // Top, Right, Bottom, Left
        centerHBox.setSpacing(10);
        centerHBox.setStyle("-fx-border-color: #000; -fx-border-width: 3;-fx-background-color: " + centerColor);
        centerHBox.setAlignment(Pos.CENTER);

        VBox firstMenuBox = new VBox();
        firstMenuBox.setSpacing(10);
        firstMenuBox.setStyle("-fx-border-color: #000; -fx-border-width: 0 3 0 3; -fx-background-color: " + menuColor);
        firstMenuBox.setAlignment(Pos.CENTER);
        firstMenuBox.setPrefSize(mainWidth - (mainWidth * 0.5), mainHeight - (mainHeight * 0.5));

        double buttonWidth = (firstMenuBox.getPrefWidth() - (firstMenuBox.getPrefWidth() * 0.1));
        double buttonHeight = (firstMenuBox.getPrefHeight() - (firstMenuBox.getPrefHeight() * 0.95));
        String buttonStyle = "-fx-focus-color: transparent;-fx-faint-focus-color: transparent; -fx-border-color: #000; -fx-border-width: 2; -fx-border-insets: 3;-fx-font: normal bold 28px 'sansserif' ";
        
        Button newGameButton = new Button("New Game");
        Button loadGameButton = new Button("Load Game");
        Button exitButton = new Button("Exit");

        newGameButton.setPrefSize(buttonWidth, buttonHeight);
        loadGameButton.setPrefSize(buttonWidth, buttonHeight);
        exitButton.setPrefSize(buttonWidth, buttonHeight);
        newGameButton.setStyle(buttonStyle);
        loadGameButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

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
        String menuColor = "#99D9EA";
        String centerColor = "#55BED9;";

        HBox box = new HBox();
        box.setPadding(new Insets(0, 5, 0, 5));
        box.setSpacing(10);
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-border-color: #000; -fx-border-width: 0 3 0 3; -fx-background-color: " + centerColor);

        HBox split = new HBox();
        split.setPadding(new Insets(5, 5, 5, 5));
        split.setSpacing(10);
        split.setAlignment(Pos.CENTER);
        split.setStyle("-fx-border-color: #000; -fx-border-width: 0 3 0 3; -fx-background-color: " + menuColor);

        box.getChildren().add(split);


        VBox names = new VBox();
        names.setSpacing(10);

        VBox controls = new VBox();
        controls.setSpacing(10);

        Label playerCountlabel = new Label("Number of players");

        Button startGame = new Button("Start");
        startGame.setOnAction(event -> startGame());

        Button backMenu = new Button("Back");
        backMenu.setOnAction(event -> updateScene(menu1));

        ComboBox<Integer> playerCounts = new ComboBox<>();
        playerCounts.getItems().addAll(2, 3, 4, 5, 6);

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
