package game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * Class to create and handle the introduction scene.
 */
public class IntroScene {
    // introduction scene
    private Scene introScene;

    // root layout nodes
    private GridPane root;
    private VBox introText;

    // acts as a queue for the event of the dialogue text
    private LinkedList<Label> dialogue;


    /**
     * Creates the introduction scene and sets the components with the
     * corresponding event handlers.
     */
    public IntroScene() {
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(
                Color.web("A4FFFF"), CornerRadii.EMPTY, Insets.EMPTY)));

        introScene = new Scene(root);

        createDialogue();
        addNextScene();
    }

    /**
     * Returns the introduction scene.
     * @return introduction scene
     */
    public Scene getScene() {
        return introScene;
    }

    /**
     * Returns a copy of the list of dialogue text in the introduction scene.
     * @return list of dialogue text
     */
    public List<Label> getDialogue() {
        return new LinkedList<>(dialogue);
    }

    /**
     * Adds the next text in the list of dialogues to be added. Once the text
     * is added to teh scene, it is removed from the List.
     */
    public void addNextScene() {
        introText.getChildren().addAll(dialogue.poll());
    }

    /**
     * Initialises the dialogue labels and creates the first instance of Tori
     * the bird. Does not add the dialogue text to the scene yet,
     */
    private void createDialogue() {
        // text box to contain dialogue
        introText = new VBox(30);
        root.add(introText, 1, 1);

        // creates all dialogue text and stores in queue list
        dialogue = new LinkedList<>();
        dialogue.add(new Label(""));
        dialogue.add(new Label("I've sent you here because I need your help"));
        dialogue.add(new Label("I've lost something embedded within this\n" +
                "floating island of blocks in the sky"));
        dialogue.add(new Label("Will you help me find it?"));

        // creates and adds the picture of Tori the bird
        ImageView introBird = new ImageView(new Image("/introbird.png",
                500, 500, true, false));
        root.add(introBird, 2, 1);
    }
}
