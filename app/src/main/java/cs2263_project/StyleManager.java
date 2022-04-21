package cs2263_project;

import javafx.scene.Node;
import lombok.NonNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class used for managing and applying styles to javafx controls
 */
public class StyleManager {
    private static Map<String, List<Node>> registeredControls;

    private static Map<String, String> fillColor;
    private static Map<String, String> borderColor;
    private static Map<String, String> borderThickness;
    private static Map<String, String> font;

    static {
        registeredControls = new HashMap<>();
    }

    /**
     * Get the javafx style string for a given control type
     * @param type The type of control to get
     * @return The javafx style code
     */
    private static String getStyle(String type) {
        String result = "";

        if (fillColor.containsKey(type))
            result += String.format("-fx-background-color: %s;", fillColor.get(type));
        if (borderColor.containsKey(type))
            result += String.format("-fx-border-color: %s;", borderColor.get(type));
        if (borderThickness.containsKey(type))
            result += String.format("-fx-border-thickness: %s;", borderThickness.get(type));
        if (font.containsKey(type))
            result += String.format("-fx-font: %s;", font.get(type));

        return result;
    }

    /**
     * Load a style file
     * @param path the path to the file
     */
    private static void loadStyleFile(String path) {
        File stylefile = new File(path);

        try {
            FileReader reader = new FileReader(stylefile);

            fillColor.clear();
            borderColor.clear();
            borderThickness.clear();
            font.clear();

            // TODO: parse the file here
        }
        catch (FileNotFoundException ex) {
            throw new RuntimeException("Can't find style file: " + path);
        }
    }

    /**
     * Register a control to be styled by the style manager
     * @param type the type of control
     * @param control the control to be styled
     */
    public static void registerControl(@NonNull String type, @NonNull Node control) {
        if (!registeredControls.containsKey(type))
            registeredControls.put(type, new ArrayList<>());

        registeredControls.get(type).add(control);
        control.setStyle(getStyle(type));
    }

    /**
     * Load a style file to be applied to all the controls
     * @param styleFilePath the path to the file
     */
    public static void applyStyle(String styleFilePath) {
        loadStyleFile(styleFilePath);

        for(String type : registeredControls.keySet()) {
            for(Node control : registeredControls.get(type)) {
                control.setStyle(getStyle(type));
            }
        }
    }
}
