package game;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * Class for the help / how to play scene, which provides general background
 * and basic information on the mechanics of the game.
 */
public class HelpScene {
    // how to play scene
    private Scene helpScene;

    // main root container for the help nodes
    private GridPane root;

    /**
     * Creates the root container and the required nodes for the help scene.
     * This contains the general plot of background for the game, and then
     * information about the block types and the control buttons.
     */
    public HelpScene() {
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(50);

        plotSynopsis();
        visualAid();

        helpScene = new Scene(root);
    }

    /**
     * Returns the help scene.
     * @return the help scene
     */
    public Scene getScene() {
        return helpScene;
    }

    /**
     * For a given block type or button name, returns a description of its
     * properties and information.
     * @param infoName block type name or button name
     * @return description of the blocks properties, or the buttons functions
     */
    private String getDescription(String infoName) {
        String description = null;
        switch (infoName) {
            case "grass":
                description = "A grass block. Diggable but not carryable. \n" +
                        "You can't place this above three blocks high.";
                break;
            case "soil":
                description = "A soil block. Diggable and carryable. \n" +
                        "You can't place this above three blocks high.";
                break;
            case "stone":
                description = "A stone block. Not diggable or carryable. \n" +
                        "No matter how hard you dig, \n" +
                        "this block ain't gonna budge";
                break;
            case "wood":
                description = "A wood block. Diggable and carryable. \n" +
                        "Place this where ever you want";
                break;
            case "Move Builder":
                description = "Use the onsreen buttons or WASD\n" +
                        "keys to move the builder";
                break;
            case "Dig":
                description = "Removes the top block \n of the current tile. \n" +
                        "If possible, the block might \n be added to your inventory";
                break;
            case "Place":
                description = "Places the selected block from the inventory \n" +
                        "on the builder's current tile. \n" +
                        "Make sure to select a block first before placing.";
                break;
            case "Move Block":
                description = "Moves the top block on the current tile to a \n" +
                        "given direction. Toggle the Move Block button, and \n" +
                        "select the desired direction. ";
                break;
        }
        return description;
    }

    /**
     * Gives background information on the game, so users aren't just playing
     * some weird Minecraft knockoff (well, they are), since plot and character
     * development is really important.
     */
    private void plotSynopsis() {
        VBox introBox = new VBox();

        String plotSummary = "So, for some inexplicable reason, \n" +
                "you awake in the middle of the sky to find that Tori \n" +
                "(that's its name) has lost something. It appears to be a \n" +
                "bird or something, although it looks more like a penguin \n" +
                "with really long legs wearing a miniskirt.\n\n" +
                "Anyway, it sends you over to a floating island in the sky \n" +
                "with no way off. It gives you a shovel, and sends you off \n" +
                "to do its bidding.";
        String instructions = "You're supposed to move around the map, \n" +
                "move around some blocks by digging and placing, and \n" +
                "hopefully, you'll find what Tori lost. \n" +
                "Oh wait, I forgot to code that part in... \n" +
                "Oh well, just move around or something, that's fun right? \n" +
                "(Click the screen to go back to the game)";

        Label plot = new Label(plotSummary);
        Label help = new Label(instructions);

        introBox.getChildren().addAll(plot, help);

        ImageView bird = new ImageView(new Image("/helpbird.png",
                400, 400, true, false));

        root.add(introBox, 1, 1);
        root.add(bird, 2, 1);
    }

    /**
     * Creates the information containers for the "How To Play" portion of this
     * scene. Block type classification is on the left, and the button functions
     * on the right.
     */
    private void visualAid() {
        VBox blocks = new VBox();

        blockInfo(blocks, "grass");
        blockInfo(blocks, "soil");
        blockInfo(blocks, "stone");
        blockInfo(blocks, "wood");

        root.add(blocks, 1, 2);

        VBox buttons = new VBox(20);

        buttonInfo(buttons, "Move Builder");
        buttonInfo(buttons, "Dig");
        buttonInfo(buttons, "Place");
        buttonInfo(buttons, "Move Block");

        root.add(buttons, 2, 2);
    }

    /**
     * For a given block type, creates the information entry with an image of
     * the block, and the corresponding description.
     * @param blocks container for each block entry
     * @param blockName type of block
     */
    private void blockInfo(VBox blocks, String blockName) {
        HBox blockTypeInfo = new HBox();

        ImageView blockImage = new ImageView(
                new Image(CanvasDrawer.getImageURL(blockName), 100,
                        100, false, false));
        blockTypeInfo.getChildren().addAll(blockImage,
                new Label(getDescription(blockName)));

        blocks.getChildren().addAll(blockTypeInfo);
    }

    /**
     * For a given button, creates teh information entry with the corresponding
     * button (with no event handling) and the description.
     * @param buttons container for each button entry
     * @param buttonName name of button
     */
    private void buttonInfo(VBox buttons, String buttonName) {
        VBox buttonTypeInfo = new VBox();
        buttonTypeInfo.setAlignment(Pos.CENTER_RIGHT);

        // no need to create example buttons for the direction buttons
        if (!buttonName.equals("Move Builder")) {
            Button exampleButton = new Button(buttonName);
            exampleButton.setMinWidth(30);
            buttonTypeInfo.getChildren().addAll(exampleButton);
        }

        Label buttonDescription = new Label(getDescription(buttonName));
        buttonDescription.setTextAlignment(TextAlignment.RIGHT);
        buttonTypeInfo.getChildren().addAll(buttonDescription);

        buttons.getChildren().addAll(buttonTypeInfo);
    }
}
