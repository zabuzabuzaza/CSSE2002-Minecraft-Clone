package game;

import csse2002.block.world.*;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.List;

/**
 * Class to link the View class (which handles all graphical aspects of the
 * application, and the BlockWorld Game classes (which handles the mechanics
 * of the application.
 */
public class Controller {
    // the objects that handle the graphical aspects
    private View appView;
    private GameScene gameScene;
    private IntroScene introScene;

    // the objects that handle the technical aspects
    private MapAction mapAction;
    private WorldMap currentMap;
    private Position currentPos;

    // supplementary controller class that specifically handles the canvas
    private CanvasDrawer drawer;

    // controls the builder image
    private boolean hatON;

    /**
     * Initialises all the required scenes for the application, the drawer
     * helper class and adds all event handlers to their respective buttons,
     * screens and keys.
     * @param view the view class which handles the graphical aspects of the
     *             application
     */
    public Controller(View view) {
        appView = view;
        gameScene = view.getGameScene();
        introScene = view.getIntroScene();

        drawer = new CanvasDrawer(gameScene);

        //default builder image
        hatON = false;

        addHandlers();
        //addControlHandler();
        //addInventoryHandler();
        //addClickHandler();
        //addHatHandler();
    }

    /**
     * Adds event handlers to all required buttons, keys and screens.
     */
    private void addHandlers() {
        // setting buttons
        gameScene.addFileHandler(new FileHandler());
        gameScene.addCreditsHandler(new SceneHandler());
        gameScene.addHelpHandler(new SceneHandler());

        // control buttons
        gameScene.addActionHandler(new ActionHandler());
        gameScene.addKeyHandler(new KeyHandler());

        // hat button (in credits scene)
        appView.getCreditScene().addHatHandler(new HatHandler());

        // for each button in inventory bar
        addInventoryHandler();

        // every time a scene is changed, add handler to return to game scene\
        addClickHandler();
    }

    /**
     * For each button in each block entry in the inventory scroll bar, add the
     * handler to determine the selected block for the DROP_BLOCK action.
     */
    private void addInventoryHandler() {
        gameScene.addInventoryHandler(new InventoryHandler());
    }

    /**
     * For a mouse event, activates both the intro dialogue scene, and for any
     * scene change away from the root game scene, any resulting click will
     * return beck to the game scene.
     */
    private void addClickHandler() {
        appView.addClickHandlers(new ClickHandler());
    }

    /**
     * For any action or event that has been performed and processed, updates
     * the view. This includes redrawing the canvas, the inventory, re-adds the
     * event handler for each inventory button, updates the direction button
     * enable / disable state, and the alert and status messages.
     */
    private void updateView() {
        createMap();
        createInventory();
        addInventoryHandler();
        checkExit();

        int tileHeight = currentMap.getBuilder().getCurrentTile().getBlocks().size();
        String steps = (mapAction.getTotalSteps() > 999)? "A lot":
                Integer.toString(mapAction.getTotalSteps());

        updateStatMessage("Currently " + tileHeight + " blocks high. \n" +
                "Total Steps Taken: " + steps);
    }

    /**
     * For any given string message, updates the alert message on the view.
     * @param message string to be used as the alert message
     */
    private void updateAlertMessage(String message) {
        if (message != null) {
            gameScene.setAlertMessage(message);
        }
    }

    /**
     * For any given string message, updates the status massage on the view.
     * @param message string to be used as the status message
     */
    private void updateStatMessage(String message) {
        if (message != null) {
            gameScene.setStatMessage(message);
        }
    }

    /**
     * Resets the grid and draws the tile grid on the view canvas. The three
     * dimensional representation of the tile grid only displays the 9x9 grid
     * of tiles surrounding the builder's current position.
     */
    private void createMap() {
        drawer.clearGrid();

        // max distance from centre the grid view should be
        final int GRIDRANGE = 4;

        int currentX = currentPos.getX();
        int currentY = currentPos.getY();

        // find all tiles in 9x9 grid around current tile
        for (int x = (GRIDRANGE * -1); x <= GRIDRANGE; x++) {
            for (int y = (GRIDRANGE * -1); y <= GRIDRANGE; y++) {
                // current tile in iteration
                Tile loopTile = currentMap.getTile(new Position(currentX + x, currentY + y));

                int height = 0;

                if (loopTile != null) {
                    // base tile represents non-removable tile
                    drawer.drawBlocks(x, y, -1, "ground");

                    // draws each block on the tile
                    for (Block block : loopTile.getBlocks()) {
                        drawer.drawBlocks(x, y, height, block.getBlockType());
                        ++height;
                    }
                }

                // draws the builder and the possible exits arrows
                if (currentMap.getBuilder().getCurrentTile() == loopTile) {
                    String builderName = hatON? "hatbuilder": "builder";
                    drawer.drawBlocks(x, y, height, builderName);

                    // does not check if builder can enter it at that instance,
                    // only that the exit exists
                    for (String exitName: currentMap.getBuilder().
                            getCurrentTile().getExits().keySet()) {
                        drawer.drawExitArrows(x, y, height, exitName);
                    }
                }
            }
        }
    }

    /**
     * Resets and creates the inventory for each block in builder's current
     * inventory. Also updates the inventory status to default no block selected.
     */
    private void createInventory() {
        gameScene.resetInventory();

        List<Block> inventoryList = currentMap.getBuilder().getInventory();
        for (Block block : inventoryList) {
            drawer.drawInventory(block.getBlockType(), inventoryList.indexOf(block));
        }

        if (mapAction.getSelectedInvBlock() == -1) {
            gameScene.setSelectedBlock("No Block Currently Selected");
        }
    }

    /**
     * Enables / disables the direction control buttons based on the availability
     * of their respective exit at the builder's current position.
     */
    private void checkExit() {
        for (String buttonName : gameScene.getMoveButtons().keySet()) {
            Tile exitTile = currentMap.getBuilder().getCurrentTile().getExits().get(buttonName);

            if (!currentMap.getBuilder().canEnter(exitTile)) {
                gameScene.disableMove(buttonName, true);
            } else {
                gameScene.disableMove(buttonName, false);
            }
        }
    }

    /**
     * Nested private class for the screen click event handler. The event
     * activates when the current scene registers a mouse click anywhere that
     * does not already have an event handler on it (buttons etc.).
     */
    private class ClickHandler implements EventHandler<MouseEvent> {

        /**
         * If the current scene has been clicked, it will trigger one of two
         * things. <br>
         * If the current scene is the Intro Scene, a screen click with trigger
         * the progression of the dialogue, until the scene is finished. After
         * that, the subsequent screen click with change the scene to the main
         * game scene. <br>
         * If the current scene is not the main game scene (ie. CreditScene or
         * HelpScene), any screen click will switch the scene back to the main
         * screen. <br>
         * NOTE: the main game screen does not have a click handler. All scene
         * changes from the game scene is handled with the setting buttons.
         * @param mouse mouse event triggered
         */
        @Override
        public void handle(MouseEvent mouse) {
            if (!introScene.getDialogue().isEmpty()) {
                introScene.addNextScene();
            } else if (appView.getCurrentScene() != 2) {
                appView.setMainWindow(gameScene.getScene());
            }
        }
    }

    /**
     * Nested private class for the screen change from the main game scene
     * event handler. This is activated from the setting buttons "Credit" and
     * "How To Play".
     */
    private class SceneHandler implements EventHandler<ActionEvent> {

        /**
         * Changes the current view scene from the main game scene to either the
         * credits scene or the hot to play / help scene, depending on the
         * pressed button.
         * @param action button pressed event
         */
        @Override
        public void handle(ActionEvent action) {
            if (action.getSource() == gameScene.getSettingButtons().get("credits")) {
                CreditScene credits = appView.getCreditScene();
                appView.setMainWindow(credits.getScene());
            } else if (action.getSource() == gameScene.getSettingButtons().get("help")) {
                HelpScene help = appView.getHelpScene();
                appView.setMainWindow(help.getScene());
            }
            addClickHandler();
        }
    }

    /**
     * Nested private class for the file chooser event handler. Uses the
     * FIleChooser node to handler the opening and saving of map files for the
     * main game application.
     */
    private class FileHandler implements EventHandler<ActionEvent> {

        /**
         * Determines the pressed file button ("Open" or "Save") and opens the
         * FileChooser window to handle the selection of files.
         * @param action button pressed event
         */
        @Override
        public void handle(ActionEvent action) {
            // determine pressed button
            Button pressedButton = (Button) action.getSource();

            FileChooser fileChooser = new FileChooser();

            // configures the file extension options
            configFileChooser(fileChooser);

            if (pressedButton == gameScene.getSettingButtons().get("open")) {
                openFile(fileChooser);
            } else if (pressedButton == gameScene.getSettingButtons().get("save")) {
                saveFile(fileChooser);
            }
        }

        /**
         * If the "Open" button was pressed, uses the selected file from the
         * FileChooser window and passes the file to loadMap() to attempt to
         * create a WorldMap with. If loadMap() successful, also creates a
         * MapAction class to handle WorldMap actions.
         * @param chooser FileChooser dialog window
         */
        private void openFile(FileChooser chooser) {

            File file = chooser.showOpenDialog(null);

            if (file != null) {
                // temporarily store file and map
                String openFile = file.getName();
                WorldMap openMap = loadMap(openFile);

                // if creating World Map is successful
                if (openMap != null) {
                    currentMap = openMap;
                    currentPos = currentMap.getStartPosition();
                    mapAction = new MapAction(currentMap, currentPos.getX(), currentPos.getY());

                    updateAlertMessage("Welcome, to uhh...\n somewhere in \n the sky?");
                    updateView();
                }
            }
        }

        /**
         * Given a filename, attempts to create a new WorldMap instance. Creates
         * an alert dialog box if the map is invalid.
         * @return created WorldMap or null if unsuccessful.
         */
        private WorldMap loadMap(String fileName) {
            WorldMap map = null;
            try {
                map = new WorldMap(fileName);
            } catch (BlockWorldException | IOException e) {
                updateAlertMessage("Haha, did you choose\na bad file?");
                updateStatMessage("");

                Alert invalidMap = new Alert(Alert.AlertType.ERROR,
                        "That's not a valid map file\n" +
                                "Choose another one", ButtonType.OK);
                invalidMap.setHeaderText("Invalid File");
                invalidMap.showAndWait();
            }
            return map;
        }

        /**
         * Attempts to save the current state of the WorldMap to a .txt file.
         * Creates an alert dialog box if unsuccessful, or just updates the
         * alert message if no map has loaded yet.
         * @param chooser FileChooser dialog window
         */
        private void saveFile(FileChooser chooser) {
            if (currentMap != null) {
                File file = chooser.showSaveDialog(null);
                String extension = null;

                if (file != null) {
                    try {
                        // if user did not manually add a .txt to the end of the
                        // filename
                        if (!file.toString().endsWith(".txt")) {
                            extension = ".txt";
                        }
                        currentMap.saveMap(file.toString() + extension);
                    } catch (IOException e) {
                        updateAlertMessage("Problem Saving File");

                        Alert invalidSave = new Alert(Alert.AlertType.ERROR,
                                "Something went wrong!", ButtonType.OK);
                        invalidSave.showAndWait();
                    }
                }
            } else {
                updateAlertMessage("Load a map first\n you dummy!");
            }
        }

        /**
         * Configures the available file extension filters in the FileChooser
         * window.
         * @param chooser FileChooser dialog window
         */
        private void configFileChooser(FileChooser chooser) {
            chooser.setInitialDirectory(new File(System.getProperty("user.dir")));

            // file extensions
            chooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("All Files",
                            "*.*"),
                    new FileChooser.ExtensionFilter("Text Files",
                            "*.txt"));
        }
    }

    /**
     * Nested private class for the inventory event handler. Functions include
     * updating the inventory status message and the selected block for any
     * inventory button pressed.
     */
    public class InventoryHandler implements EventHandler<ActionEvent> {

        /**
         * For any block selected block in the inventory scroll bar, the status
         * message and the selected block referred to in the MapAction class will
         * be updated to reflect the selected block.
         * @param action button pressed event
         */
        @Override
        public void handle(ActionEvent action) {
            Button selectedButton = (Button) action.getSource();

            // set selected block in MapAction
            mapAction.setSelectedInvBlock(gameScene.getInventoryButtons().indexOf(selectedButton));

            // update inventory message and alert message
            updateAlertMessage("Try placing that.");
            gameScene.setSelectedBlock("You selected a " +
                    selectedButton.getText() + " block");
        }
    }

    /**
     * Nested private class for the control buttons events. Depending on the
     * pressed action button, will perform the corresponding action on the
     * WorldMap with MapAction.
     */
    public class ActionHandler implements EventHandler<ActionEvent> {

        /**
         * Determines the pressed button, and diverts the corresponding action
         * to their respective method in MapAction. <br>
         * If the direction buttons were activated, will either move the builder
         * or the current top block depending on the toggle state of
         * "Move Block". If "Place Block" was triggered, will perform the action
         * based on the currently selected inventory block. None selected will
         * prompt the user to then select one.
         * @param action button pressed event
         */
        @Override
        public void handle(ActionEvent action) {
            String message = null;
            if (currentMap != null) {

                Button actionButton = (Button) action.getSource();

                // for direction buttons
                if (gameScene.getMoveButtons().containsValue(actionButton)) {
                    String direction = getDirection(actionButton);

                    if (gameScene.getMoveBlockState()) {
                        // if move block button is toggled on
                        message = mapAction.moveBlock(direction);
                    } else {
                        // move builder normally
                        message = mapAction.moveBuilder(direction);
                    }
                // for the other control buttons
                } else if (actionButton == gameScene.getActionButtons().get("dig")) {
                    message = mapAction.digBlock();
                } else if (actionButton == gameScene.getActionButtons().get("drop")) {
                    // determine if an inventory block is selected
                    if (mapAction.getSelectedInvBlock() != -1) {
                        message = mapAction.dropBlock(mapAction.getSelectedInvBlock());
                    } else {
                        message = "Select a block first.";
                    }
                }
                // update current builder position and view
                currentPos = new Position(mapAction.getCurrentX(), mapAction.getCurrentY());
                updateView();
            } else {
                message = "Load a map first\n you dummy!";
            }
            updateAlertMessage(message);
        }

        /**
         * Helper method to determine the direction of the pressed direction
         * button.
         * @param button the pressed button
         * @return a string representation of the direction
         */
        private String getDirection(Button button) {
            String direction = null;
            switch (button.getText()) {
                case "Up":
                    direction = "north";
                    break;
                case "Left":
                    direction = "east";
                    break;
                case "Down":
                    direction = "south";
                    break;
                case "Right":
                    direction = "west";
                    break;
            }
            return direction;
        }
    }

    /**
     * Nested private class for the key press events. Maps the WASD keys to the
     * MOVE_BUILDER action in MapAction. Does not provide a direction for the
     * MOVE_BLOCK action even if the button is toggled on.
     */
    public class KeyHandler implements EventHandler<KeyEvent> {

        /**
         * Determines the key pressed, and performs the MOVE_BUILDER action
         * corresponding to the key.
         * @param keyPress the pressed key
         */
        @Override
        public void handle(KeyEvent keyPress) {
            String message = null;
            if (currentMap != null) {
                switch (keyPress.getCode()) {
                    case W:
                        message = mapAction.moveBuilder("north");
                        break;
                    case D:
                        message = mapAction.moveBuilder("east");
                        break;
                    case S:
                        message = mapAction.moveBuilder("south");
                        break;
                    case A:
                        message = mapAction.moveBuilder("west");
                        break;
                }

                // updates builder position and view
                currentPos = new Position(mapAction.getCurrentX(), mapAction.getCurrentY());
                updateView();
            } else {
                message = "Load a map first\n you dummy!";
            }
            updateAlertMessage(message);
        }
    }

    /**
     * Nested private class for the hat toggle event. Switches the image used
     * to draw the builder onto the canvas to either the default image or the
     * hat image based on toggle state.
     */
    private class HatHandler implements EventHandler<ActionEvent> {

        /**
         * Switches the state of the toggle button on event, and updates the
         * button text to indicate current state. <br>
         * Based on the state of the hat toggle button, changes the images used
         * to draw the builder onto the canvas to the respective hat / no hat
         * image.
         * @param action toggle button pressed
         */
        @Override
        public void handle(ActionEvent action) {
            hatON = !hatON;
            ToggleButton addHat = (ToggleButton) action.getSource();
            addHat.setText(hatON? "Builder is now much more fancy" :
                    "Builder back to normal");

            if (currentMap != null) {
                updateView();
            }
        }
    }
}
