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

import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.text.Style;
import java.io.File;
import java.util.*;

/**
 * A roughshod implementation of the ui related to managing the game state
 * @author Tyson Cox
 */
class GameUI implements GameObserver {
    private final Game game;

    private final Stage stage;

    private final BorderPane root;

    private BorderPane mainGameRoot;
    private Label lblPlayerTurn;
    private Label lblGamePhase;
    private Label lblPlayerHandHeader;
    private Label lblPlayerMoney;
    private GridPane grdTiles;
    private GridPane grdPlayerHand;
    private GridPane grdPlayerHandAction;
    private HBox hbTileAction;
    private HBox hbStockAction;
    private Label[][] tileGrid;
    private Map<String, Label> lblPlayerStocks;
    private Map<String, Label> lblStockList;
    private Map<String, Label> lblStockPriceList;

    private BorderPane menuRoot;

    /* Hard coded variables to control sizing and spacing */
    //private static final int BOARD_TILE_SPACING = 10;
    //private static final int PANEL_SPACING = 10;
    //private static final int TILE_WIDTH = 30;
    //private static final int TILE_HEIGHT = 30;

    /* Maps to map control types to jfx css code. Put entries in the static block below */
    /*
    private static final Map<String, String> fillColors = new HashMap<>();
    private static final Map<String, String> borderColors = new HashMap<>();
    private static final Map<String, String> borderThickness = new HashMap<>();
    private static final Map<String, String> fonts = new HashMap<>();
    */

    /* TODO: this stuff needs exported to an external file and read in
     * Control categories: [] indicates an optional addition, while () a mandatory one
     *
     * EmptyTile - a Tile of the board that is empty
     * PlayerInfo[Header] - info about the player and its corresponding header
     * CorporationsInfo[Header] - info about the stocklist corporations and its corresponding header
     * GameStatusHeader - header about game status
     * CorporationKeyColor(corp name) - the corporation color key block
     * FilledTile(corp name) - a Tile of the board filled with a given corporation
     * ButtonTile - buttons that place the tiles
     * ButtonTileUnplaceable - buttons for tiles that can't be played right now
     * ButtonTileUnplayable - buttons for tiles that can't ever be played
     * PaneBoard, PaneTitle, PanePlayer, PaneStocks, PaneAction - the background panes
     */
    /*
    static {
        borderThickness.put("PaneBoard", "15 15 15 15");
        borderColors.put(   "PaneBoard", "#000000");

        borderThickness.put("PaneTitle", "15 15 15 15");
        borderColors.put(   "PaneTitle", "#000000");

        borderThickness.put("PanePlayer", "15 15 15 15");
        borderColors.put(   "PanePlayer", "#000000");

        borderThickness.put("PaneStocks", "15 15 15 15");
        borderColors.put(   "PaneStocks", "#000000");

        borderThickness.put("PaneAction", "15 15 15 15");
        borderColors.put(   "PaneAction", "#000000");

        fillColors.put("ButtonTileUnplaceable", "#FF0000");
        fillColors.put("ButtonTileUnplayable", "#FF0000");

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
     */

    public GameUI(Stage stage) {
        StyleManager.applyStyle("light.json");
        game = Game.getInstance();
        this.stage = stage;
        root = new BorderPane();
        constructMenuRoot();
        constructGameRoot();
        updateScene(mainGameRoot);
    }

    /**
     * Get the root element of this ui
     * @return the root element of this ui
     */
    public BorderPane getRoot() {
        return root;
    }

    /**
     * Generate the jfx css code for a control category
     * @param category the category of control to generate the code for
     * @return the generated jfx css code
     */
    /*
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
     */

    /**
     * Swap this scene for a different one
     * @param node The container to swap to
     */
    private void updateScene(Node node) {
        root.getChildren().clear();
        root.setCenter(node);
    }

    /**
     * Construct the ui related to the main game screen
     */
    private void constructGameRoot() {
        mainGameRoot = new BorderPane();
        mainGameRoot.setTop(addGameTop());
        mainGameRoot.setCenter(addGameCenter());
        mainGameRoot.setLeft(addGameLeft());
        mainGameRoot.setRight(addGameRight());

        hbTileAction = addGameBottomTileAction();
        hbStockAction = addGameBottomStockAction();
        lblGamePhase.setText("Phase: Null");
    }

    /**
     * Construct the ui related to the pause/options menu
     */
    private void constructMenuRoot() {
        menuRoot = new BorderPane();
        VBox controls = new VBox();
        controls.setAlignment(Pos.CENTER);
        controls.setPadding(new Insets(10, 100, 10, 100));
        controls.setSpacing(25);

        Button save = new Button("Save");
        Button quit = new Button("Quit");
        Button back = new Button("Back");

        save.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Save");
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Save files", "*.json"),
                    new FileChooser.ExtensionFilter("All Files", "*.*")
            );

            File savefile = chooser.showSaveDialog(stage);
            if (savefile == null)
                return;

            if (savefile.exists()) {
                Dialog<ButtonType> sure = new Dialog<>();
                sure.setTitle("Are you sure?");
                sure.setContentText("Are you sure you want to overwrite your old save?");
                sure.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
                var res = sure.showAndWait();
                if (!res.isPresent() || res.get() == ButtonType.NO) {
                    return;
                }
            }

            game.save(savefile.getPath());
        });

        quit.setOnAction(e -> {
            Dialog<ButtonType> sure = new Dialog<>();
            sure.setTitle("Are you sure?");
            sure.setContentText("Are you sure you want to exit the game?");
            sure.getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);
            var res = sure.showAndWait();
            if (res.isPresent() && res.get() == ButtonType.YES) {
                Platform.exit();
            }
        });

        back.setOnAction(e -> {
            updateScene(mainGameRoot);
        });

        controls.getChildren().addAll(save, quit, back);
        menuRoot.setCenter(controls);
    }

    /**
     * Construct the top panel of the game screen
     * @return the top panel of the game screen
     */
    private BorderPane addGameTop() {
        BorderPane topbox = new BorderPane();
        StyleManager.registerControl("GameTopPanel", topbox);
        HBox lefttopbox = new HBox();
        HBox centertopbox = new HBox();
        HBox righttopbox = new HBox();
        lefttopbox.setAlignment(Pos.CENTER);
        centertopbox.setAlignment(Pos.CENTER);
        righttopbox.setAlignment(Pos.CENTER);

        lblPlayerTurn = new Label("Turn: Null");
        lblGamePhase = new Label("Phase: Null");
        StyleManager.registerControl("GameTopHeader", lblPlayerTurn);
        StyleManager.registerControl("GameTopHeader", lblGamePhase);

        Button menu = new Button();
        StyleManager.registerControl("GameTopButton", menu);
        menu.setText("Menu");
        menu.setOnAction(e -> updateScene(menuRoot));

        lefttopbox.getChildren().add(lblPlayerTurn);
        centertopbox.getChildren().add(lblGamePhase);
        righttopbox.getChildren().add(menu);

        topbox.setLeft(lefttopbox);
        topbox.setCenter(centertopbox);
        topbox.setRight(righttopbox);

        return topbox;
    }

    /**
     * Construct the center panel (board) of the game screen
     * @return the center panel of the game screen
     */
    private GridPane addGameCenter() {
        grdTiles = new GridPane();
        StyleManager.registerControl("GameBoardPane", grdTiles);
        tileGrid = new Label[GameBoard.WIDTH][GameBoard.HEIGHT];

        for (int x = 0; x < GameBoard.WIDTH; x++) {
            for (int y = 0; y < GameBoard.HEIGHT; y++) {
                char letter = 'A';
                letter += y;

                Label label = new Label(Integer.toString(x + 1) + letter);
                StyleManager.registerControl("GameBoardTileEmpty", label);
                grdTiles.getChildren().add(label);
                GridPane.setRowIndex(label, y);
                GridPane.setColumnIndex(label, x);
                tileGrid[x][y] = label;

            }
        }

        return grdTiles;
    }

    /**
     * Utility method - adds all tiles from a players hand to a gridpane as labels
     * @param pane the pane to add to
     * @param player the player to read the hand from
     */
    private void addHandTiles(GridPane pane, Player player) {
        pane.getChildren().clear();
        List<Tile> tiles = player.getTiles();

        for(int i = 0; i < tiles.size(); i++) {
            Label label = new Label();
            label.setText(tiles.get(i).getTileName());
            StyleManager.registerControl("GameBoardTileEmpty", label);
            pane.add(label, i % 3, (i / 3) + 1);
        }
    }

    /**
     * Utility method - add the tiles of a players hand to a gridpane as buttons that play the tiles
     * @param pane the pane to add to
     * @param player the player to read the hand of
     */
    private void addHandTileButtons(GridPane pane, Player player) {
        pane.getChildren().removeIf(child -> child.getClass() == Button.class);
        List<Tile> tiles = player.getTiles();

        for(int i = 0; i < tiles.size(); i++) {
            Tile tile = tiles.get(i);

            Button btn = new Button(tile.getTileName());

            if (!game.isTilePlaceable(tile))
                StyleManager.registerControl("GameBottomButtonTileUnplaceable", btn);
            else if (game.isTileUnplayable(tile))
                StyleManager.registerControl("GameBottomButtonTileUnplayable", btn);
            else
                StyleManager.registerControl("GameBottomButtonTile", btn);

            pane.add(btn, i % 3, (i / 3));

            btn.setOnAction(e -> {
                if (game.placeTile(tile)) {
                    pane.getChildren().removeIf(child -> child.getClass() == Button.class && tile.getTileName().equals(((Button)child).getText()));
                }
            });
        }
    }

    /**
     * Construct the left pane of the game screen
     * @return the left pane of the game screen
     */
    private VBox addGameLeft() {
        VBox box = new VBox();
        StyleManager.registerControl("GameLeftPane", box);
        lblPlayerStocks = new HashMap<>();

        lblPlayerHandHeader = new Label("Hand: Null");
        StyleManager.registerControl("GameLeftHeader", lblPlayerHandHeader);

        lblPlayerMoney = new Label("Money: none");
        StyleManager.registerControl("GameLeftText", lblPlayerMoney);

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

    /**
     * Construct the right pane of the game screen
     * @return the right pane of the game screen
     */
    private VBox addGameRight() {
        VBox box = new VBox();
        StyleManager.registerControl("GameRightPane", box);
        lblStockList = new HashMap<>();
        lblStockPriceList = new HashMap<>();

        Label lblCorpStats = new Label("Corporation Stats");
        StyleManager.registerControl("GameRightHeader", lblCorpStats);

        box.getChildren().add(lblCorpStats);

        for(int i = 0; i < GameInfo.Corporations.length; i++) {
            String corp = GameInfo.Corporations[i];
            VBox corpbox = new VBox();
            StyleManager.registerControl("GameRightCorporationPane", corpbox);

            HBox titlebox = new HBox();
            Label colorlabel = new Label("   ");
            StyleManager.registerControl(String.format("GameRightCorporationLegend%d", i), colorlabel);

            Label namelabel = new Label(corp);
            Label countlabel = new Label("    Qty: ???");
            Label costlabel = new Label("    Cost: ???");

            StyleManager.registerControls("GameRightText", namelabel, countlabel, costlabel);

            titlebox.getChildren().addAll(colorlabel, namelabel);

            corpbox.getChildren().addAll(titlebox, countlabel, costlabel);

            box.getChildren().add(corpbox);
            lblStockList.put(corp, countlabel);
            lblStockPriceList.put(corp, costlabel);
        }

        return box;
    }

    /**
     * Construct one of the two bottom panels of the game screen
     * @return the tile panel of the game screen
     */
    private HBox addGameBottomTileAction() {
        HBox box = new HBox();
        StyleManager.registerControl("GameBottomPaneTiles", box);

        grdPlayerHandAction = new GridPane();
        StyleManager.registerControl("GameBottomGridButtonTile", grdPlayerHandAction);

        Label lbl = new Label("Choose a tile to place");
        grdPlayerHandAction.add(lbl, 0, 2);
        GridPane.setColumnSpan(lbl, 3);

        box.getChildren().add(grdPlayerHandAction);

        return box;
    }

    /**
     * Utility method - pops up a dialog with some text
     * @param title the title of the dialog
     * @param message the text of the dialog
     */
    private void popupMessage(String title, String message) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setContentText(message);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    /**
     * Construct one of the two bottom panels of the game screen
     * @return the stock / end turn panel
     */
    private HBox addGameBottomStockAction() {
        HBox box = new HBox();
        StyleManager.registerControl("GameBottomPaneAction", box);

        Button buyStock = new Button("Buy Stock");
        StyleManager.registerControl("GameBottomButtonAction", buyStock);

        ChoiceDialog<String> buydialog = new ChoiceDialog<>();

        buyStock.setOnAction(e -> {
            buydialog.getItems().clear();
            buydialog.getItems().addAll(game.getCurrentCorporations());
            buydialog.getItems().add("I changed my mind!");

            var result = buydialog.showAndWait();

            if (result.isPresent() && !result.get().equals("I changed my mind!")) {
                if (!game.buyStock(result.get()))
                    popupMessage("Failed to buy", "Unable to purchase stock in " + result.get());
            }
        });


        Button endTurn = new Button("End Turn");
        StyleManager.registerControl("GameBottomButtonAction", endTurn);
        endTurn.setOnAction(e -> {
            game.drawTile();
        });

        box.getChildren().addAll(buyStock, endTurn);

        return box;
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
        addHandTileButtons(grdPlayerHandAction, player);
    }

    @Override
    public void notifyStockDecision(Player player, List<String> fromCorps, String toCorp) {
        Set<Player> players = new HashSet<>();

        for(String stock : fromCorps) {
            List<Player> stockplayers = game.getPlayersWithStock(stock);
            players.addAll(stockplayers);
        }

        Node shownode = mainGameRoot;
        Iterator<Player> iter = players.iterator();

        for(int i = players.size() - 1; i >= 0; i--) {
            Player p = iter.next();
            TradePane tradepane = new TradePane(p, fromCorps, toCorp);
            Node finalShownode = shownode;
            tradepane.setDoneCallback(() -> { updateScene(finalShownode); });
            shownode = tradepane;
        }

        updateScene(shownode);
    }

    @Override
    public void notifyMergeDecision(List<String> options, List<String> goingAway, Tile tile) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.getDialogPane().setContentText("Which corporation survives the merge?");
        dialog.getItems().addAll(options);
        var result = dialog.showAndWait();

        if (result.isPresent()) {
            String win = result.get();
            tile.setCorporation(win);
        }
    }

    @Override
    public void notifyGameEnd(String[] names, Integer[] dollars) {
        BorderPane winScreen = new BorderPane();

        HBox titlebox = new HBox();
        titlebox.setAlignment(Pos.CENTER);
        titlebox.setPadding(new Insets(30));

        Label winTitle = new Label(String.format("%s wins!", names[0]));
        titlebox.getChildren().add(winTitle);

        GridPane scores = new GridPane();
        scores.gridLinesVisibleProperty().setValue(true);
        scores.setVgap(10);
        scores.setHgap(50);
        scores.setAlignment(Pos.CENTER);
        scores.setPadding(new Insets(10, 100, 10, 100));

        scores.add(new Label("Player"), 0, 0);
        scores.add(new Label("Score"), 1, 0);

        for(int i = 0; i < names.length; i++) {
            scores.add(new Label(names[i]), 0, i + 1);
            scores.add(new Label(String.format("$%d", dollars[i])), 1, i + 1);
        }

        HBox buttonHolder = new HBox();
        buttonHolder.setPadding(new Insets(10, 100, 10, 100));
        buttonHolder.setAlignment(Pos.CENTER);

        Button endGame = new Button("Okay!");
        buttonHolder.getChildren().add(endGame);

        endGame.setOnAction(e -> Platform.exit());

        winScreen.setTop(titlebox);
        winScreen.setCenter(scores);
        winScreen.setBottom(buttonHolder);

        updateScene(winScreen);
        StyleManager.debugdump();
    }

    @Override
    public void notifyChangeStocks(Map<String, Integer> param) {
        for(String corp : param.keySet()) {
            lblStockList.get(corp).setText(String.format("    QTY: %d", param.get(corp)));

            int cost = game.getCurrentCorporationCost(corp);
            if (cost == -1) {
                lblStockPriceList.get(corp).setText("    Cost: N/A");
            }
            else {
                lblStockPriceList.get(corp).setText(String.format("    Cost: $%d", game.getCurrentCorporationCost(corp)));
            }
        }
    }

    @Override
    public void notifyFormOption(String[] options, Tile tile) {
        assert options.length > 0;

        ChoiceDialog<String> dialog = new ChoiceDialog<>();
        dialog.getDialogPane().setContentText("Which corporation would you like to form?");
        dialog.getItems().addAll(options);
        var result = dialog.showAndWait();

        if (result.isPresent()) {
            String win = result.get();
            tile.setCorporation(win);
        }
    }

    @Override
    public void notifyTilePlaced(Tile tile) {
        if (tile.getCorporation() != null) {
            String style = String.format("GameBoardFilledTile%d", GameInfo.getCorporationID(tile.getCorporation()));
            Label control = tileGrid[tile.getX()][tile.getY()];
            StyleManager.restyleAs(style, control);
        }
        else {
            StyleManager.restyleAs("GameBoardFilledTile", tileGrid[tile.getX()][tile.getY()]);
        }
    }

    @Override
    public void notifyGamePhaseChanged(int phase) {
        if (phase == 0) {
            mainGameRoot.setBottom(hbTileAction);
            lblGamePhase.setText("Game Phase: Placing tile");
        }
        else {
            mainGameRoot.setBottom(hbStockAction);
            lblGamePhase.setText("Game Phase: Taking action");
        }
    }
}
