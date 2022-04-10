package cs2263_project;

import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.*;

class GameUI implements GameObserver {
    private BorderPane root;

    private BorderPane mainGameRoot;
    private Label lblPlayerTurn;
    private Label lblGamePhase;
    private Label lblPlayerHandHeader;
    private Label lblPlayerMoney;
    private GridPane grdTiles;
    private GridPane grdPlayerHand;
    private Label[][] tileGrid;
    private Map<String, Label> lblPlayerStocks;
    private Map<String, Label> lblStockList;

    private BorderPane stockChooserRoot;


    /* Hard coded variables to control sizing and spacing */
    private static final int BOARD_TILE_SPACING = 10;
    private static final int PANEL_SPACING = 10;
    private static final int TILE_WIDTH = 30;
    private static final int TILE_HEIGHT = 30;

    /* Maps to map control types to jfx css code. Put entries in the static block below */
    private static final Map<String, String> fillColors = new HashMap<>();
    private static final Map<String, String> borderColors = new HashMap<>();
    private static final Map<String, String> borderThickness = new HashMap<>();
    private static final Map<String, String> fonts = new HashMap<>();


    /*
     * Control categories: [] indicates an optional addition, while () a mandatory one
     *
     * EmptyTile - a Tile of the board that is empty
     * PlayerInfo[Header] - info about the player and its corresponding header
     * CorporationsInfo[Header] - info about the stocklist corporations and its corresponding header
     * GameStatusHeader - header about game status
     * CorporationKeyColor(corp name) - the corporation color key block
     * FilledTile(corp name) - a Tile of the board filled with a given corporation
     */
    static {
        fillColors.put(     "EmptyTile", "#F1F1F1");
        borderColors.put(   "EmptyTile", "#000000");
        borderThickness.put("EmptyTile", "5 5 5 5");
        fonts.put(          "EmptyTile", "12 serif");

        fillColors.put(     "FilledTile", "#C1C1C1");
        borderColors.put(   "FilledTile", "#000000");
        borderThickness.put("FilledTile", "5 5 5 5");
        fonts.put(          "FilledTile", "12 serif");

        fonts.put(          "PlayerInfoHeader", "16 sansserif");
        fonts.put(          "PlayerInfo",       "12 sansserif");

        fonts.put(          "CorporationsInfoHeader",   "16 sansserif");
        fonts.put(          "CorporationsInfo",         "12 sansserif");

        fonts.put(          "GameStatusHeader", "20 sansserif");


        String[] corporationColors = new String[GameInfo.Corporations.length];
        corporationColors[0] = "#FFFFD1";
        corporationColors[1] = "#B5B9FF";
        corporationColors[2] = "#BFFCC6";
        corporationColors[3] = "#FFC9DE";
        corporationColors[4] = "#B28DFF";
        corporationColors[5] = "#6EB5FF";
        corporationColors[6] = "#C4FAF8";

        for(int i = 0; i < corporationColors.length; i++) {
            fillColors.put(String.format("CorporationKeyColor%s", GameInfo.Corporations[i]), corporationColors[i]);

            String name = String.format("FilledTile%s", GameInfo.Corporations[i]);
            fillColors.put(name, corporationColors[i]);
            borderColors.put(name, "#000000");
            borderThickness.put(name, "5 5 5 5");
            fonts.put(name, "12 serif");
        }
    }

    public GameUI() {
        root = new BorderPane();
        constructGameRoot();
        constructStockChoice();
        updateScene(mainGameRoot);
    }

    public BorderPane getRoot() {
        return root;
    }

    /**
     * Generate the jfx css code for a control category
     * @param category the category of control to generate the code for
     * @return the generated jfx css code
     */
    private String getStyle(String category) {
        String result = "";

        if (fillColors.containsKey(category))
            result += String.format("-fx-background-color: %s;", fillColors.get(category));
        if (borderColors.containsKey(category))
            result += String.format("-fx-border-color: %s;", borderColors.get(category));
        if (borderThickness.containsKey(category))
            result += String.format("-fx-border-thickness: %s;", borderThickness.get(category));
        if (fonts.containsKey(category))
            result += String.format("-fx-font: %s;", fonts.get(category));

        return result;
    }

    private void updateScene(Node node) {
        root.getChildren().clear();
        root.setCenter(node);
    }

    private void constructGameRoot() {
        mainGameRoot = new BorderPane();
        mainGameRoot.setTop(addGameTop());
        mainGameRoot.setCenter(addGameCenter());
        mainGameRoot.setLeft(addGameLeft());
        mainGameRoot.setRight(addGameRight());
        mainGameRoot.setBottom(addGameBottom());
    }

    private void constructStockChoice() {
        stockChooserRoot = new BorderPane();
    }

    private HBox addGameTop() {
        HBox topbox = new HBox(50);
        topbox.setPadding(new Insets(PANEL_SPACING));
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

    private GridPane addGameCenter() {
        grdTiles = new GridPane();
        grdTiles.setPadding(new Insets(PANEL_SPACING));
        grdTiles.setStyle(getStyle("GridGameBoard"));
        tileGrid = new Label[GameBoard.WIDTH][GameBoard.HEIGHT];
        grdTiles.setHgap(BOARD_TILE_SPACING);
        grdTiles.setVgap(BOARD_TILE_SPACING);

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

    private void addHandTiles(GridPane pane, Player player) {
        pane.getChildren().clear();
        List<Tile> tiles = player.getTiles();

        for(int i = 0; i < tiles.size(); i++) {
            Label label = new Label();
            label.setPrefSize(TILE_WIDTH, TILE_HEIGHT);
            label.setAlignment(Pos.CENTER);
            label.setText(tiles.get(i).getTileName());
            label.setStyle(getStyle("EmptyTile"));
            pane.add(label, i % 3, (i / 3) + 1);
        }
    }

    private VBox addGameLeft() {
        VBox box = new VBox();
        box.setPadding(new Insets(PANEL_SPACING));
        lblPlayerStocks = new HashMap<>();

        lblPlayerHandHeader = new Label("Hand: Null");
        lblPlayerHandHeader.setStyle(getStyle("PlayerInfoHeader"));

        lblPlayerMoney = new Label("Money: none");
        lblPlayerMoney.setStyle(getStyle("PlayerInfo"));

        box.getChildren().addAll(lblPlayerHandHeader, lblPlayerMoney);
        box.setSpacing(10);

        VBox stockbox = new VBox();

        for(String s : GameInfo.Corporations) {
            Label label = new Label(s + ": ???");
            stockbox.getChildren().add(label);
            lblPlayerStocks.put(s, label);
        }

        box.getChildren().add(stockbox);
        box.getChildren().add(new Label("Tiles:"));

        grdPlayerHand = new GridPane();
        grdPlayerHand.setVgap(10);
        grdPlayerHand.setHgap(10);

        box.getChildren().add(grdPlayerHand);

        return box;
    }

    private VBox addGameRight() {
        VBox box = new VBox();
        box.setPadding(new Insets(PANEL_SPACING));
        box.setSpacing(10);
        lblStockList = new HashMap<>();

        Label lblCorpStats = new Label("Corporation Stats");
        lblCorpStats.setStyle(getStyle("CorporationsInfoHeader"));

        box.getChildren().add(lblCorpStats);

        for(String corp : GameInfo.Corporations) {
            HBox titlebox = new HBox(5);
            Label colorlabel = new Label("   ");
            colorlabel.setStyle(getStyle(String.format("CorporationKeyColor%s", corp)));

            Label namelabel = new Label(corp + ": ???");
            namelabel.setStyle(getStyle("CorporationsInfo"));

            titlebox.getChildren().addAll(colorlabel, namelabel);

            box.getChildren().add(titlebox);
            lblStockList.put(corp, namelabel);
        }

        return box;
    }

    private HBox addGameBottom() {
        HBox box = new HBox();

        return box;
    }

    private String chooseMerge(String option1, String option2) {
        return option1;
    }

    @Override
    public void notifyPlayerUpdate(Player player) {
        lblPlayerTurn.setText("Active player: " + player.getName());
        lblPlayerHandHeader.setText(player.getName() + "'s hand");
        lblPlayerMoney.setText(String.format("Money: %d$", player.getDollars()));

        for(String corp : GameInfo.Corporations) {
            lblPlayerStocks.get(corp).setText(String.format("%s: %d", corp, player.stockAmount(corp)));
        }

        addHandTiles(grdPlayerHand, player);
    }

    @Override
    public void notifyStockDecision(Player player, String fromCorp, String toCorp) {

    }

    @Override
    public void notifyMergeDecision(String option1, String option2, Tile tile) {
        updateScene(stockChooserRoot);
        tile.setCorporation(chooseMerge(option1, option2));
    }

    @Override
    public void notifyGameEnd(String[] names, Integer[] dollars) {

    }

    @Override
    public void notifyChangeStocks(Map<String, Integer> param) {
        for(String corp : param.keySet()) {
            lblStockList.get(corp).setText(String.format("%s: %d", corp, param.get(corp)));
        }
    }

    @Override
    public void notifyFormOption(String[] options, Tile tile) {

    }

    @Override
    public void notifyTilePlaced(Tile tile) {
        if (tile.getCorporation() != null)
            tileGrid[tile.getX()][tile.getY()].setStyle(getStyle(String.format("FilledTile%s", tile.getCorporation())));
        else
            tileGrid[tile.getX()][tile.getY()].setStyle(getStyle("FilledTile"));
    }


}
