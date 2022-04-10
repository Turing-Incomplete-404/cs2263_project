package cs2263_project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.util.HashMap;
import java.util.Map;

class GameUI implements GameObserver {
    private BorderPane root;
    private Label lblPlayerTurn;
    private Label lblGamePhase;
    private Label lblPlayerHand;
    private Label lblPlayerMoney;
    private GridPane grdTiles;
    private Label[][] tileGrid;

    private static final int BOARD_SPACING = 10;
    private static final int TILE_WIDTH = 30;
    private static final int TILE_HEIGHT = 30;
    private static final Map<String, String> fillColors = new HashMap<>();
    private static final Map<String, String> borderColors = new HashMap<>();
    private static final Map<String, String> borderThickness = new HashMap<>();
    private static final Map<String, String> fonts = new HashMap<>();

    static {
        fillColors.put("EmptyTile", "#CCCCCC");
        borderColors.put("EmptyTile", "#000000");
        borderThickness.put("EmptyTile", "5 5 5 5");
        fonts.put("EmptyTile", "12 serif");

        fonts.put("PlayerInfoHeader", "16 sansserif");
        fonts.put("PlayerInfo", "12 sansserif");

        fonts.put("GameStatusHeader", "20 sansserif");
    }

    private String getStyle(String type) {
        String result = "";

        if (fillColors.containsKey(type))
            result += String.format("-fx-background-color: %s;", fillColors.get(type));
        if (borderColors.containsKey(type))
            result += String.format("-fx-border-color: %s;", borderColors.get(type));
        if (borderThickness.containsKey(type))
            result += String.format("-fx-border-thickness: %s;", borderThickness.get(type));
        if (fonts.containsKey(type))
            result += String.format("-fx-font: %s;", fonts.get(type));

        return result;
    }

    public GameUI() {
        root = new BorderPane();

        root.setTop(addTop());
        root.setCenter(addCenter());
        root.setLeft(addLeft());
        root.setRight(addRight());
        root.setBottom(addBottom());
    }

    HBox addTop() {
        HBox topbox = new HBox(50);
        HBox lefttopbox = new HBox();
        HBox centertopbox = new HBox();
        HBox righttopbox = new HBox();

        lblPlayerTurn = new Label("Turn: Null");
        lblGamePhase = new Label("Phase: Null");
        lblPlayerTurn.setStyle(getStyle("GameStatusHeader"));
        lblGamePhase.setStyle(getStyle("GameStatusHeader"));

        Button menu = new Button();
        menu.setText("Menu");

        lefttopbox.getChildren().add(lblPlayerTurn);
        centertopbox.getChildren().add(lblGamePhase);
        righttopbox.getChildren().add(menu);

        topbox.getChildren().addAll(lefttopbox, centertopbox, righttopbox);

        return topbox;
    }

    GridPane addCenter() {
        grdTiles = new GridPane();
        grdTiles.setStyle(getStyle("GridGameBoard"));
        tileGrid = new Label[GameBoard.WIDTH][GameBoard.HEIGHT];
        grdTiles.setHgap(BOARD_SPACING);
        grdTiles.setVgap(BOARD_SPACING);

        for (int x = 0; x < GameBoard.WIDTH; x++) {
            for (int y = 0; y < GameBoard.HEIGHT; y++) {
                char letter = 'A';
                letter += y;

                Label label = new Label(Integer.toString(x + 1) + letter);
                label.setPrefSize(TILE_WIDTH, TILE_HEIGHT);
                label.setAlignment(Pos.CENTER);
                label.setStyle(getStyle("EmptyTile"));
                grdTiles.getChildren().add(label);
                GridPane.setRowIndex(label, y);
                GridPane.setColumnIndex(label, x);
                tileGrid[x][y] = label;

            }
        }

        return grdTiles;
    }

    VBox addLeft() {
        VBox box = new VBox();
        lblPlayerHand = new Label("Hand: Null");
        lblPlayerHand.setStyle(getStyle("PlayerInfoHeader"));

        lblPlayerMoney = new Label("Money: none");
        lblPlayerMoney.setStyle(getStyle("PlayerInfo"));

        box.getChildren().addAll(lblPlayerHand, lblPlayerMoney);
        box.setSpacing(5);

        return box;
    }

    VBox addRight() {
        VBox box = new VBox();
        Label lblCorpStats = new Label("Corporation Stats");

        box.getChildren().add(lblCorpStats);

        return box;
    }

    HBox addBottom() {
        HBox box = new HBox();

        return box;
    }

    public BorderPane getRoot() {
        return root;
    }

    @Override
    public void notifyPlayerUpdate(Player player) {
        lblPlayerTurn.setText("Active player: " + player.getName());
        lblPlayerHand.setText(player.getName() + "'s hand");
        lblPlayerMoney.setText(String.format("Money: %d$", player.getDollars()));
    }

    @Override
    public void notifyStockDecision(Player player, String fromCorp, String toCorp) {

    }

    @Override
    public void notifyMergeDecision(String option1, String option2, Tile tile) {

    }

    @Override
    public void notifyGameEnd(String[] names, Integer[] dollars) {

    }

    @Override
    public void notifyChangeStocks(Map<String, Integer> param) {

    }

    @Override
    public void notifyFormOption(String[] options, Tile tile) {

    }
}
