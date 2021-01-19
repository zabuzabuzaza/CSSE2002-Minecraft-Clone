package game;

import javafx.stage.Stage;


/**
 * Main class to initialise application.
 */
public class MainApplication extends javafx.application.Application {
    /**
     * Main method launches the application. Calls the start method to
     * initialise the stage and the corresponding classes.
     * @param args input arguments for the program
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Creates a main stage to display the contents in a window. Also creates
     * the View object to handle all visual aspects and the Controller object
     * to link events between the block world classes and the view.
     * @param stage main window the application will display its contents
     */
    @Override
    public void start(Stage stage) {
        // initial window sizes
        final int MAINHEIGHT = 1000;
        final int MAINWIDTH = 900;

        // minimum window sizes so content formatting are unaffected
        final int MINHEIGHT = 950;
        final int MINWIDTH = 700;

        stage.setTitle("Not Minecraft");

        View view = new View(stage);
        new Controller(view);
        stage.setScene(view.getScene());

        stage.setHeight(MAINHEIGHT);
        stage.setWidth(MAINWIDTH);
        stage.setMinHeight(MINHEIGHT);
        stage.setMinWidth(MINWIDTH);

        stage.show();
    }
}
