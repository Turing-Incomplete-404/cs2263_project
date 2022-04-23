package cs2263_project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import lombok.NonNull;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A utility class used for managing and applying styles to javafx controls
 */
public class StyleManager {
    record Pair<T, U>(T value1, U value2) { }

    private static String lastValidStyle;

    private static Map<String, List<Node>> registeredControls;

    private static Map<String, String> fillColor;
    private static Map<String, String> borderColor;
    private static Map<String, String> borderThickness;
    private static Map<String, String> borderInset;
    private static Map<String, String> font;
    private static Map<String, Insets> padding;
    private static Map<String, Pair<Double, Double>> sizes;
    private static Map<String, Pos> alignment;
    private static Map<String, Integer> spacing;

    static {
        registeredControls = new HashMap<>();
        fillColor = new HashMap<>();
        borderColor = new HashMap<>();
        borderThickness = new HashMap<>();
        borderInset = new HashMap<>();
        font = new HashMap<>();
        padding = new HashMap<>();
        sizes = new HashMap<>();
        alignment = new HashMap<>();
        spacing = new HashMap<>();
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
            result += String.format("-fx-border-width: %s;", borderThickness.get(type));
        if (borderInset.containsKey(type))
            result += String.format("-fx-border-insets: %s;", borderInset.get(type));
        if (font.containsKey(type))
            result += String.format("-fx-font: %s;", font.get(type));

        return result;
    }

    private static double toDouble(Object json) {
        if (json.getClass() == Long.class) {
            return ((Long) json).doubleValue();
        }
        else if (json.getClass() == Double.class) {
            return (Double) json;
        }
        else {
            return 0.0;
        }
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
            borderInset.clear();
            font.clear();
            padding.clear();
            sizes.clear();
            alignment.clear();
            spacing.clear();

            JSONObject root = (JSONObject) new JSONParser().parse(reader);

            for(Object nameobject : root.keySet()) {
                String name = (String) nameobject;
                JSONObject object = (JSONObject) root.get(name);

                for(Object params : object.keySet()) {
                    String paramname = (String) params;
                    switch (paramname) {
                        case "BackgroundColor" -> fillColor.put(name, (String) object.get(paramname));
                        case "BorderColor" -> borderColor.put(name, (String) object.get(paramname));
                        case "BorderThickness" -> borderThickness.put(name, (String) object.get(paramname));
                        case "BorderInset" -> borderInset.put(name, (String) object.get(paramname));
                        case "Font" -> font.put(name, (String) object.get(paramname));
                        case "Spacing" -> spacing.put(name, Math.toIntExact((Long) object.get(paramname)));
                        case "Padding" -> {
                            Object pad = object.get(paramname);
                            if (pad.getClass() == Long.class) {
                                double val = toDouble(pad);
                                padding.put(name, new Insets(val));
                            }
                            else if (pad.getClass() == JSONArray.class) {
                                JSONArray arr = (JSONArray) pad;
                                double v1 = toDouble(arr.get(0)); //top
                                double v2 = toDouble(arr.get(1)); //right
                                double v3 = toDouble(arr.get(2)); //bottom
                                double v4 = toDouble(arr.get(3)); //left
                                padding.put(name, new Insets(v1, v2, v3, v4));
                            }
                        }
                        case "Alignment" -> {
                            String align = (String) object.get(paramname);

                            switch (align.toLowerCase()) {
                                case "center" -> alignment.put(name, Pos.CENTER);
                                case "baseline center" -> alignment.put(name, Pos.BASELINE_CENTER);
                                case "baseline left" -> alignment.put(name, Pos.BASELINE_LEFT);
                                case "baseline right" -> alignment.put(name, Pos.BASELINE_RIGHT);
                                case "bottom center" -> alignment.put(name, Pos.BOTTOM_CENTER);
                                case "top center" -> alignment.put(name, Pos.TOP_CENTER);
                                case "center left" -> alignment.put(name, Pos.CENTER_LEFT);
                                case "center right" -> alignment.put(name, Pos.CENTER_RIGHT);
                                case "top right" -> alignment.put(name, Pos.TOP_RIGHT);
                                case "top left" -> alignment.put(name, Pos.TOP_LEFT);
                                case "bottom right" -> alignment.put(name, Pos.BOTTOM_RIGHT);
                                case "bottom left" -> alignment.put(name, Pos.BOTTOM_LEFT);
                            }

                        }
                        case "Size" -> {
                            Object sizesetting = object.get(paramname);
                            if (sizesetting.getClass() == JSONArray.class) {
                                JSONArray arr = (JSONArray) sizesetting;
                                double v1 = toDouble(arr.get(0));
                                double v2 = toDouble(arr.get(1));
                                sizes.put(paramname, new Pair<>(v1, v2));
                            }
                            else {
                                double size = toDouble(sizesetting);
                                sizes.put(name, new Pair<>(size, size));
                            }
                        }
                    }
                }
            }

            lastValidStyle = path;
        }
        catch (FileNotFoundException ex) {
            applyStyle(lastValidStyle);
            throw new RuntimeException("Can't find style file: " + path);
        } catch (IOException | ParseException e) {
            applyStyle(lastValidStyle);
            throw new RuntimeException("Unable to apply style file: " + path);
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

        String style = getStyle(type);
        if (!style.isEmpty())
            control.setStyle(style);
        setOtherParams(control, type);
    }

    /**
     * Utility method to register multiple controls at once
     * @param type the type of the control
     * @param controls the list of controls to register
     */
    public static void registerControls(@NonNull String type, Node... controls) {
        for(Node node : controls) {
            registerControl(type, node);
        }
    }

    /**
     * Restyles a control, updating its type
     * @param type the type to update to
     * @param control the control to restyle
     */
    public static void restyleAs(@NonNull String type, @NonNull Node control) {
        List<List<Node>> toRemove = new ArrayList<>();
        for(String oldtype : registeredControls.keySet()) {
            List<Node> list = registeredControls.get(type);
            if (list != null) {
                for (Node oldcontrol : registeredControls.get(type)) {
                    if (oldcontrol == control) {
                        toRemove.add(registeredControls.get(oldtype));
                        break;
                    }
                }
            }
        }

        for(List<Node> list : toRemove) {
            list.remove(control);
        }

        registerControl(type, control);
    }

    private static boolean hasMethod(Node node, String name, Class<?>... params) {
        for(Method method : node.getClass().getDeclaredMethods()) {
            if (method.getName().equals(name))
                return true;
        }

        return false;
    }

    private static void callMethod(Node node, String name, Object... params) {
        Class<?>[] classes = new Class<?>[params.length];
        for(int i = 0; i < params.length; i++) {
            if (params[i].getClass() == Double.class)
                classes[i] = double.class;
            else
                classes[i] = params[i].getClass();
        }

        try {
            Method method = node.getClass().getMethod(name, classes);
            method.invoke(node, params);
        }
        catch (NoSuchMethodException ex) {
            throw new RuntimeException(String.format("Tried to find method %s on type %s but it doesn't exist", name, node.getClass().getName()));
        }
        catch (SecurityException | IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(String.format("Unable to call method %s on type %s", name, node.getClass().getName()));
        }
    }

    private static void setOtherParams(Node control, String type) {
        if (padding.containsKey(type))
            callMethod(control, "setPadding", padding.get(type));

        if (sizes.containsKey(type)) {
            double width = sizes.get(type).value1();
            double height = sizes.get(type).value2();
            callMethod(control, "setPrefSize", width, height);
        }

        if (alignment.containsKey(type))
            callMethod(control, "setAlignment", alignment.get(type));

        if (spacing.containsKey(type)) {
            double space = spacing.get(type);

            if (hasMethod(control, "setSpacing", double.class)) {
                callMethod(control, "setSpacing", space);
            }
            else if (hasMethod(control, "setVgap", double.class) && hasMethod(control, "setHgap", double.class)) {
                callMethod(control, "setVgap", space);
                callMethod(control, "setHgap", space);
            }
        }
    }

    /**
     * Reapply the current style
     */
    public static void refresh() {
        applyStyle(lastValidStyle);
    }

    /**
     * Load a style file to be applied to all the controls
     * @param styleFilePath the path to the file
     */
    public static void applyStyle(String styleFilePath) {
        loadStyleFile(styleFilePath);

        for(String type : registeredControls.keySet()) {
            for(Node control : registeredControls.get(type)) {
                String style  = getStyle(type);
                if (!style.isEmpty())
                    control.setStyle(getStyle(type));
                setOtherParams(control, type);
            }
        }
    }
}
