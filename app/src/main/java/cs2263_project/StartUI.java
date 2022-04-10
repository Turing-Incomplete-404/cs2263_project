package cs2263_project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.text.Text;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.KeyEvent;
import java.io.*;

public class StartUI extends Application {
    private int mainWidth = 500;
    private int mainHeight = 600;
    private int menuNumber;
    private Scene scene;
    private BorderPane root;
    private Game game;
    private GameUI gameUI;


    public StartUI() {
        menuNumber = 1;
        root = new BorderPane();
    }

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("Acquire");

        root.setTop(addTop());
        root.setCenter(addCenter());
        root.setBottom(addBottom());

        scene = new Scene(root, mainWidth, mainHeight);

        stage.setScene(scene);
        stage.show();
        stage.setAlwaysOnTop(true);
        stage.setAlwaysOnTop(false);
        
    }

    @Override
    public void start(Stage stage) throws Exception{
        stage.setTitle("Acquire");

        updateScene(stage);

        // Commented out for the time being.
        // scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
        //     @Override
        //     public void handle(KeyEvent event) {
        //         switch (event.getCode()) {
        //             case ESCAPE:
        //                 stage.close();
        //             default: 
        //         }
        //     }
        // });

        
    }

    private HBox addTop() {
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
        HBox titleContainer = new HBox();
        gameName.setStyle("-fx-font: normal bold 40px 'sansserif' ");
        authors.setStyle("-fx-font: normal bold 16px 'sansserif' ");
        
        titleBox.getChildren().addAll(gameName, authors);
        topHBox.getChildren().addAll(titleBox);

        return topHBox;
    }

    private HBox addCenter() {
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
        Button testButton = new Button("Center");

        newGameButton.setPrefSize(buttonWidth, buttonHeight);
        loadGameButton.setPrefSize(buttonWidth, buttonHeight);
        exitButton.setPrefSize(buttonWidth, buttonHeight);
        newGameButton.setStyle(buttonStyle);
        loadGameButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);

        VBox secondMenuBox = new VBox();
        secondMenuBox.setSpacing(10);
        secondMenuBox.setStyle("-fx-border-color: #000; -fx-border-width: 0 3 0 3; -fx-background-color: " + menuColor);
        secondMenuBox.setAlignment(Pos.CENTER);
        secondMenuBox.setPrefSize(mainWidth - (mainWidth * 0.75), mainHeight - (mainHeight * 0.5));

        VBox thirdMenuBox = new VBox();
        thirdMenuBox.setSpacing(10);
        secondMenuBox.setStyle("-fx-border-color: #000; -fx-border-width: 0 3 0 3; -fx-background-color: " + menuColor);
        secondMenuBox.setAlignment(Pos.CENTER);
        secondMenuBox.setPrefSize(mainWidth - (mainWidth * 0.75), mainHeight - (mainHeight * 0.5));


        switch (menuNumber) {
            case 1:
            centerHBox.getChildren().clear();
            centerHBox.getChildren().addAll(firstMenuBox);
                break;
            case 2:
            centerHBox.getChildren().clear();
            centerHBox.getChildren().addAll(secondMenuBox);
                break;
            case 3:
            centerHBox.getChildren().clear();
            centerHBox.getChildren().addAll(thirdMenuBox);    
                break;
            default:
                break;
        }

        firstMenuBox.getChildren().addAll(newGameButton, loadGameButton, exitButton);
        
        newGameButton.setOnAction((ActionEvent changeMenu) -> {
            menuNumber = 2;
            System.out.print("Button Pressed!!");
<<<<<<< HEAD
            startGame();            
=======
            updateScene();
>>>>>>> origin/feature/Adding_Second_Title_Screen
        });

        loadGameButton.setOnAction((ActionEvent loadGame) -> {
            menuNumber = 3;
            System.out.print("Loading game... JK");
<<<<<<< HEAD
            
=======
            updateScene();
>>>>>>> origin/feature/Adding_Second_Title_Screen
        });

        exitButton.setOnAction((ActionEvent changeMenu) -> {
            Platform.exit();
        });

        return centerHBox;
    }

    private HBox addBottom() {
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
        GameUI gui = new GameUI();
        game.registerObserver(gui);
        //updateScene(gui.getRoot());
    }
}
