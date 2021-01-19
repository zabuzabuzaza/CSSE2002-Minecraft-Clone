package game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

import java.util.*;

/**
 * Class to create and handle the main game scene. The primary scene from which
 * the other scenes can be accessed from.
 */
public class GameScene {
    // main interactive game scene
    private Scene gameScene;

    // root container
    private BorderPane rootPane;

    // graphical container for main tile grid
    private GraphicsContext mapCanvas;

    // inventory box containers
    private ScrollPane inventoryScroll;
    private HBox inventory;

    // contains the controls for the application
    private Map<String, Button> settingButtons;
    private Map<String, Button> moveButtons;
    private Map<String, Button> actionButtons;
    private List<Button> inventoryButtons;
    private ToggleButton moveBlockButton;

    // on screen text that updates periodically
    private Label alertMessage;
    private Label statMessage;
    private Label selectedBlock;

    // default canvas sizes
    private final int CANVASHEIGHT = 600;
    private final int CANVASWIDTH = 600;

    /**
     * Creates a new scene that contains teh main graphical contents of the
     * game. Primary hub of the application.
     */
    public GameScene() {
        rootPane = new BorderPane();
        rootPane.setPadding(new Insets(30, 30, 30, 30));
        rootPane.setBackground(new Background(new BackgroundFill(
                Color.web("A4FFFF"), CornerRadii.EMPTY, Insets.EMPTY)));

        populate();

        gameScene = new Scene(rootPane);
    }

    /**
     * Returns the main game scene.
     * @return primary game scene
     */
    public Scene getScene() {
        return gameScene;
    }

    /**
     * Returns the canvas container of the game scene, which contains the
     * tile grid.
     * @return the canvas container
     */
    public GraphicsContext getMapCanvas() {
        return mapCanvas;
    }

    /**
     * Returns the horizontal container that stores each inventory entry in the
     * scroll bar.
     * @return the inventory container
     */
    public HBox getInventory() {
        return inventory;
    }

    /**
     * Returns a map of all setting buttons. This includes the open and save
     * map buttons, as well as the credit and help buttons that redirect to
     * their respective scene.
     * @return map of setting function buttons
     */
    public Map<String, Button> getSettingButtons() {
        return new HashMap<>(settingButtons);
    }

    /**
     * Returns a map of all direction buttons. These buttons function as the
     * MOVE_BUILDER actions and indicates the direction for the MOVE_BLOCK
     * action.
     * @return map of direction buttons
     */
    public Map<String, Button> getMoveButtons() {
        return new HashMap<>(moveButtons);
    }

    /**
     * Returns a map of all specific action buttons. These buttons include the
     * DIG action, and the PLACE_BLOCK action. Does not include the MOVE_BLOCK
     * button.
     * @return map of action buttons
     */
    public Map<String, Button> getActionButtons() {
        return new HashMap<>(actionButtons);
    }

    /**
     * Returns an ordered list of all buttons for each entry in the inventory.
     * These buttons indicate the selected block to be placed once the
     * PLACE_BLOCK button is used.
     * @return list of inventory entry buttons
     */
    public List<Button> getInventoryButtons() {
        return new LinkedList<>(inventoryButtons);
    }

    /**
     * Returns the toggle state (ON / OFF) of the MOVE_BLOCK button.
     * @return true if move block button is selected, otherwise false
     */
    public boolean getMoveBlockState() {
        return moveBlockButton.isSelected();
    }


    /**
     * Given a string, sets the current alert message to the specified string.
     * @param message string to set as alert message
     */
    public void setAlertMessage(String message) {
        alertMessage.setText(message);
    }

    /**
     * Given a string, sets the current statistics message to the specified
     * string.
     * @param message string to set as statistics message
     */
    public void setStatMessage(String message) {
        statMessage.setText(message);
    }

    /**
     * Given a string, sets the current inventory state message to the
     * specified string.
     * @param message string to set as the inventory state message
     */
    public void setSelectedBlock(String message) {
        selectedBlock.setText(message);
    }

    /**
     * Adds the file chooser event handler to the Open and Save buttons.
     * @param handler the file chooser handler
     */
    public void addFileHandler(EventHandler<ActionEvent> handler) {
        settingButtons.get("open").setOnAction(handler);
        settingButtons.get("save").setOnAction(handler);
    }

    /**
     * Adds the event handler to switch the current scene on the stage to a
     * CreditScene scene.
     * @param handler the scene changer event handler
     */
    public void addCreditsHandler(EventHandler<ActionEvent> handler) {
        settingButtons.get("credits").setOnAction(handler);
    }

    /**
     * Adds the event handler to switch the current scene on the stage to a
     * HelpScene scene.
     * @param handler the scene changer event handler
     */
    public void addHelpHandler(EventHandler<ActionEvent> handler) {
        settingButtons.get("help").setOnAction(handler);
    }

    /**
     * Adds the event handler for the primary builder actions to both the
     * move buttons, and the specific action buttons. Does not add the handler
     * to the MOVE_BLOCK button.
     * @param handler the button event handler for builder actions
     */
    public void addActionHandler(EventHandler<ActionEvent> handler) {
        for (Button button : moveButtons.values()) {
            button.setOnAction(handler);
        }
        for (Button button : actionButtons.values()) {
            button.setOnAction(handler);
        }
    }

    /**
     * Adds the event handler to specify the selected block to each block entry
     * in the inventory.
     * @param handler the inventory selected block handler
     */
    public void addInventoryHandler(EventHandler<ActionEvent> handler) {
        for (Button button : inventoryButtons) {
            button.setOnAction(handler);
        }
    }

    /**
     * Adds the event handler for the WASD key press events. These keys are
     * mapped to the MOVE_BUILDER action.
     * @param handler the key event handler
     */
    public void addKeyHandler(EventHandler<KeyEvent> handler) {
        rootPane.setOnKeyPressed(handler);
    }

    /**
     * For a given block index and button, adds the entry to the ordered list of
     * inventory blocks.
     * @param index index of block
     * @param blockButton button corresponding to the block
     */
    public void addToInventory(int index, Button blockButton) {
        inventoryButtons.add(index, blockButton);
    }

    /**
     * Based on the button name, enables / disables the specified MOVE_BUILDER
     * button.
     * @param buttonName direction button to enable / disable
     * @param disable the desired state of the button
     */
    public void disableMove(String buttonName, boolean disable) {
        moveButtons.get(buttonName).setDisable(disable);
    }

    /**
     * Resets the current inventory bar to empty.
     */
    public void resetInventory() {
        inventory = new HBox(10);
        inventoryButtons = new LinkedList<>();
        inventoryScroll.setContent(inventory);
    }

    /**
     * Creates and adds all required nodes to the root application pane. The
     * root pane is a BorderPane, where the top, centre and bottom positions are
     * used to layout the application. <br>
     * The top contains the main graphical display where the tile grid will be
     * draw onto the canvas. <br>
     * The centre contains the information panel, which houses the control
     * buttons on the left, the inventory gallery in the centre, and general
     * messages on the right. <br>
     * The bottom contains the buttons for actions such as opening ans saving
     * the current map, as well as switching to different scenes.
     */
    private void populate() {
        // canvas pane
        StackPane mapScreen = new StackPane();
        addScreen(mapScreen);

        // control buttons, inventory and messages
        BorderPane information = new BorderPane();
        addInfoPane(information);

        // bottom settings
        HBox settings = new HBox(20);
        settings.setAlignment(Pos.CENTER);
        settings.setPadding(new Insets(10, 0, 0, 0));
        addSettings(settings);

        // add to root BorderPane
        rootPane.setTop(mapScreen);
        rootPane.setCenter(information);
        rootPane.setBottom(settings);
    }

    /**
     * Adds the canvas nodes to the screen container located at the top of the
     * BorderPane root container.
     * @param screen the parent pane to add the canvas node to
     */
    private void addScreen(Pane screen) {
        Canvas canvas = new Canvas(CANVASWIDTH, CANVASHEIGHT);
        mapCanvas = canvas.getGraphicsContext2D();
        screen.getChildren().addAll(canvas);
    }

    /**
     * Adds the information nodes to the information pane located in the centre
     * of the Border root container. The information nodes include the action
     * button controls (left), the inventory gallery (centre) and the alert
     * and status information (right).
     * @param infoPane the parent pane to add the information nodes to
     */
    private void addInfoPane(BorderPane infoPane) {
        // vertical stacking of action buttons
        VBox controls = new VBox(20);
        addControls(controls);

        // now add inventory gallery (includes the scroll bar and status info)
        VBox gallery = new VBox(10);
        gallery.setAlignment(Pos.TOP_CENTER);

        inventoryScroll = new ScrollPane();
        inventoryScroll.setStyle("-fx-border-color: #A4FFFF; " +
                "-fx-background: #A4FFFF;");
        inventoryScroll.setMinViewportHeight(150);
        resetInventory();

        // label to indicate selected block
        selectedBlock = new Label("");

        gallery.getChildren().addAll(inventoryScroll, selectedBlock);

        // finally add alert message and builder status info
        StackPane alertBox = new StackPane();
        alertBox.setAlignment(Pos.CENTER);
        alertBox.setMinWidth(250);
        addAlertMessage(alertBox);

        // sets positioning
        infoPane.setLeft(controls);
        infoPane.setCenter(gallery);
        infoPane.setRight(alertBox);
    }

    /**
     * Adds all control buttons relating to the builder actions to the control
     * panel pane (located on the left side of the information pane).
     * @param controlPanel container for action buttons
     */
    private void addControls(VBox controlPanel) {
        // direction buttons first (also adds buttons to map for future use)
        GridPane directions = new GridPane();
        moveButtons = new LinkedHashMap<>();
        moveButtons.put("north", new Button("Up"));
        moveButtons.put("east", new Button("Left"));
        moveButtons.put("south", new Button("Down"));
        moveButtons.put("west", new Button("Right"));

        for (Button button : moveButtons.values()) {
            button.setMinSize(70, 30);
        }

        directions.add(moveButtons.get("north"), 1, 0);
        directions.add(moveButtons.get("east"), 2, 1);
        directions.add(moveButtons.get("south"),  1, 2);
        directions.add(moveButtons.get("west"), 0, 1);

        // now for specific action buttons (again, adds to map as well)
        VBox actions = new VBox(10);
        HBox clickActions = new HBox(20);
        actions.setAlignment(Pos.CENTER);
        clickActions.setAlignment(Pos.CENTER);

        actionButtons = new LinkedHashMap<>();
        actionButtons.put("dig", new Button("Dig"));
        actionButtons.put("drop", new Button("Place"));
        moveBlockButton = new ToggleButton("Move Block");

        for (Button button : actionButtons.values()) {
            button.setMinSize(70, 30);
        }

        clickActions.getChildren().addAll(actionButtons.get("dig"),
                actionButtons.get("drop"));
        actions.getChildren().addAll(clickActions, moveBlockButton);

        // initially, direction buttons are disabled until map is loaded
        // action buttons will prompt an the alert message
        for (Button button : moveButtons.values()) {
            button.setDisable(true);
        }

        controlPanel.getChildren().addAll(directions, actions);
    }

    /**
     * Adds the alert and status nodes to the right side of the information
     * pane.
     * @param overlayBox pane to add message nodes to
     */
    private void addAlertMessage(Pane overlayBox) {
        // alerts user for invalid actions and general information
        alertMessage = new Label("Load a map to \nstart a new game");
        alertMessage.setTextAlignment(TextAlignment.CENTER);

        // general information that isn't displayed from the tile grid graphics
        statMessage = new Label();

        // bird to convey this information to the user
        ImageView bird = new ImageView(new Image("/gamebird.png",
                300, 250, true, false));

        //container for messages
        VBox messageBox = new VBox();
        messageBox.setPadding(new Insets(0, 0, 100, 0));
        messageBox.getChildren().addAll(alertMessage, statMessage);
        messageBox.setAlignment(Pos.CENTER);

        overlayBox.getChildren().addAll(bird, messageBox);
    }

    /**
     * Adds all auxiliary buttons to the bottom of the root BorderPane.
     * @param settingsBar container for all settings buttons
     */
    private void addSettings(Pane settingsBar) {
        // creates buttons and adds to map for future use
        settingButtons = new LinkedHashMap<>();
        settingButtons.put("open", new Button("Open Map"));
        settingButtons.put("save", new Button("Save Map"));
        settingButtons.put("credits", new Button("Credits"));
        settingButtons.put("help", new Button("How to Play"));

        for (Button button : settingButtons.values()) {
            settingsBar.getChildren().add(button);
        }
    }
}
