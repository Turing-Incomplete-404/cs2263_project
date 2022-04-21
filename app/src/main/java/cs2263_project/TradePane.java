package cs2263_project;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import javax.swing.text.Style;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A pane which manages the various stock sell/trade/hold behaviours
 * when corporations go defunct
 * @author Tyson Cox
 */
class TradePane extends BorderPane {
    private final Map<String, Integer> sells;
    private final Map<String, Integer> trades;
    private final Map<String, Integer> holds;
    private final Map<String, Integer> remaining;

    interface DoneButtonCallback {
        void execute();
    }

    private DoneButtonCallback callback;

    /**
     * Utility method - increments a value in a map if valid
     * @param map the map to increment
     * @param corp the corp to increment for
     */
    private void increment(Map<String, Integer> map, String corp) {
        if (remaining.get(corp) > 0) {
            remaining.put(corp, remaining.get(corp) - 1);
            map.put(corp, map.get(corp) + 1);
        }
    }

    /**
     * Utility method - decrements a value in a map if valid
     * @param map the map to decrement
     * @param corp the corp to decrement for
     */
    private void decrement(Map<String, Integer> map, String corp) {
        if (map.get(corp) > 0) {
            remaining.put(corp, remaining.get(corp) + 1);
            map.put(corp, map.get(corp) - 1);
        }
    }


    public TradePane(Player player, List<String> fromCorps, String toCorp) {
        StyleManager.registerControl("TradePane", this);
        Game game = Game.getInstance();
        sells = new HashMap<>();
        trades = new HashMap<>();
        holds = new HashMap<>();
        remaining = new HashMap<>();

        for(String corp : fromCorps) {
            if (player.stockAmount(corp) > 0) {
                remaining.put(corp, player.stockAmount(corp));
                sells.put(corp, 0);
                trades.put(corp, 0);
                holds.put(corp, 0);
            }
        }

        Label playername = new Label(player.getName());
        StyleManager.registerControl("TradeHeaderPlayer", playername);

        setTop(playername);

        VBox center = new VBox();
        setCenter(center);
        StyleManager.registerControl("TradeCenterPane", center);

        for(String corp : remaining.keySet()) {
            VBox corpContainer = new VBox();
            corpContainer.setAlignment(Pos.CENTER);
            Label corpname = new Label(String.format("%s: %d", corp, player.stockAmount(corp)));
            GridPane grd = new GridPane();

            StyleManager.registerControl("TradeCorporationName", corpname);
            StyleManager.registerControl("TradeCorporationButtonGrid", grd);

            corpContainer.getChildren().addAll(corpname, grd);

            Label lblSell = new Label("Sold: 0");
            Label lblTrade = new Label("Traded: 0");
            Label lblHold = new Label("Held: 0");

            StyleManager.registerControls("TradeText", lblTrade, lblHold, lblSell);

            Button btnSellPlus = new Button("+");
            Button btnSellMin = new Button("-");
            Button btnTradePlus = new Button("+");
            Button btnTradeMin = new Button("-");
            Button btnHoldPlus = new Button("+");
            Button btnHoldMin = new Button("-");


            StyleManager.registerControls("TradeButton",
                    btnSellPlus,
                    btnSellMin,
                    btnTradePlus,
                    btnTradeMin,
                    btnHoldPlus,
                    btnHoldMin
            );

            grd.add(lblSell, 0, 0);
            grd.add(btnSellPlus, 1, 0);
            grd.add(btnSellMin, 2, 0);

            grd.add(lblTrade, 0, 1);
            grd.add(btnTradePlus, 1, 1);
            grd.add(btnTradeMin, 2, 1);

            grd.add(lblHold, 0, 2);
            grd.add(btnHoldPlus, 1, 2);
            grd.add(btnHoldMin, 2, 2);

            center.getChildren().add(corpContainer);

            btnSellPlus.setOnAction(e -> {
                increment(sells, corp);
                lblSell.setText(String.format("Sold: %d", sells.get(corp)));
                corpname.setText(String.format("%s: %d", corp, remaining.get(corp)));
            });

            btnSellMin.setOnAction(e -> {
                decrement(sells, corp);
                lblSell.setText(String.format("Sold: %d", sells.get(corp)));
                corpname.setText(String.format("%s: %d", corp, remaining.get(corp)));
            });

            btnTradePlus.setOnAction(e -> {
                increment(trades, corp);
                lblTrade.setText(String.format("Traded: %d", trades.get(corp)));
                corpname.setText(String.format("%s: %d", corp, remaining.get(corp)));
            });

            btnTradeMin.setOnAction(e -> {
                decrement(trades, corp);
                lblTrade.setText(String.format("Traded: %d", trades.get(corp)));
                corpname.setText(String.format("%s: %d", corp, remaining.get(corp)));
            });

            btnHoldPlus.setOnAction(e -> {
                increment(holds, corp);
                lblHold.setText(String.format("Held: %d", holds.get(corp)));
                corpname.setText(String.format("%s: %d", corp, remaining.get(corp)));
            });

            btnHoldMin.setOnAction(e -> {
                decrement(holds, corp);
                lblHold.setText(String.format("Held: %d", holds.get(corp)));
                corpname.setText(String.format("%s: %d", corp, remaining.get(corp)));
            });
        }

        Button complete = new Button("Done");
        StyleManager.registerControl("TradeButtonDone", complete);

        complete.setOnAction(e -> {
            for(Integer rem : remaining.values()) {
                if (rem > 0) {
                    popupMessage("Error", "You must allocate all stocks you have");
                    return;
                }
            }

            for(String corp : sells.keySet())
                for(int i = 0; i < sells.get(corp); i++)
                    game.sellStock(player, corp);
            for(String trade : trades.keySet())
                for(int i = 0; i < trades.get(trade); i++)
                    game.tradeStock(player, trade, toCorp);

            if (this.callback != null)
                this.callback.execute();
        });

        setBottom(complete);
        setAlignment(complete, Pos.CENTER);
    }

    /**
     * Set the callback function that will be called when the player clicks done
     * @param callback the callback function
     */
    public void setDoneCallback(DoneButtonCallback callback) {
        this.callback = callback;
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
}
