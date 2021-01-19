package game;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

/**
 * Class to create and handle the credits scene.
 */
public class CreditScene {
    // the scene for the credits
    private Scene creditScene;

    // the root node to contain all credit information and nodes
    private GridPane root;

    // special toggle for adding a hat to the builder
    private ToggleButton hatButton;

    /**
     * Creates the credits scene and the root container for all nodes. Adds the
     * credits text, and the toggle for the special hat feature.
     */
    public CreditScene() {
        root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setVgap(30);

        rollCredits();
        toggleHat();

        creditScene = new Scene(root);
    }

    /**
     * Returns the credit scene
     * @return the credit scene
     */
    public Scene getScene() {
        return creditScene;
    }

    /**
     * Adds the hat event handler to the hat toggle button. When activated, the
     * builder image now has a fancy hat. When disabled, returns to default
     * buidler image. Also updates button text to show current button state.
     * @param handler the handler for toggling Builder image
     */
    public void addHatHandler(EventHandler<ActionEvent> handler) {
        hatButton.setOnAction(handler);
    }

    /**
     * Creates the credit text for the scene as well as an image.
     */
    private void rollCredits() {
        // main credits text
        String credits = "Coded By: Deon (that's me) \n\n" +
                "Thanks to: \n" +
                "CSSE2002 Lecturers and Tutors \n" +
                "& StackOverflow \n" +
                "Couldn't have done it without you.";
        Label creditText = new Label(credits);
        creditText.setTextAlignment(TextAlignment.LEFT);
        Label goBack = new Label("\n(Click the screen to go back to the game)");

        //another image of Tori the bird
        ImageView bird = new ImageView(new Image("/creditsbird.png",
                500, 500, true, false));

        root.add(creditText, 2, 1);
        root.add(bird, 1, 2);
        root.add(goBack, 2, 4);
    }

    /**
     * Adds the special toggle hat button for the builder image to the main
     * root container.
     */
    private void toggleHat() {
        VBox specialFeature = new VBox();

        Label featureLabel = new Label("As thanks for checking out " +
                "who made this, \nhere's a cool new feature.");
        hatButton = new ToggleButton("Add a Hat to the Builder!");
        specialFeature.getChildren().addAll(featureLabel, hatButton);

        root.add(specialFeature, 2, 2);
    }
}
