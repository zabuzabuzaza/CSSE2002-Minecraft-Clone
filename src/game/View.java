package game;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

import javafx.stage.Stage;


/**
 * Class to handle all graphical and visual aspects of the application.
 */
public class View {
    // main window of application
    private Stage mainWindow;

    // keeps track of scene that's currently displayed
    private Scene currentScene;
    private int currentSceneNum;

    // all possible scenes that can be accessed.
    private IntroScene intro;
    private GameScene game;
    private CreditScene credits;
    private HelpScene help;

    /**
     * Creates a new View to handle all graphics of the application. Creates the
     * required scenes to be utilised by the application, and initialises the
     * first scene.
     * @param stage main window to display scenes
     */
    public View(Stage stage) {
        mainWindow = stage;

        intro = new IntroScene();
        game = new GameScene();
        credits = new CreditScene();
        help = new HelpScene();

        startUp();
    }

    /**
     * Returns the scene that's currently displayed on the window.
     * @return currently displayed scene
     */
    public Scene getScene() {
        return currentScene;
    }

    /**
     * Returns the initialised GameScene object.
     * @return GameScene object
     */
    public GameScene getGameScene() {
        return game;
    }

    /**
     * Returns the initialised IntroScene object.
     * @return IntroScene object
     */
    public IntroScene getIntroScene() {
        return intro;
    }

    /**
     * Returns the initialised CreditScene object.
     * @return CreditScene object
     */
    public CreditScene getCreditScene() {
        return credits;
    }

    /**
     * Returns the initialised HelpScene object.
     * @return HelpScene object
     */
    public HelpScene getHelpScene() {
        return help;
    }

    /**
     * Returns the int corresponding to the currently displayed scene in the
     * application window.
     * <ol>
     *     <li>Introduction Scene</li>
     *     <li>Main Game Scene</li>
     *     <li>Credits Scene</li>
     *     <li>Help & Information Scene</li>
     * </ol>
     * @return int corresponding to currently displayed scene
     */
    public int getCurrentScene() {
        return currentSceneNum;
    }

    /**
     * Sets the main window's scene to be the specified scene. Also updates the
     * current scene and corresponding int identifier.
     * @param scene scene to display on the main window
     */
    public void setMainWindow(Scene scene) {
        if (scene == intro.getScene()) {
            currentSceneNum = 1;
        } else if (scene == game.getScene()) {
            currentSceneNum = 2;
        } else if (scene == credits.getScene()) {
            currentSceneNum = 3;
        } else if (scene == help.getScene()) {
            currentSceneNum = 4;
        }
        mainWindow.setScene(scene);
        currentScene = scene;
    }

    /**
     * Adds the mouse events to the currently set screen. When triggered, the
     * screen will perform the mouse event action.
     * @param handler EventHandler object to add to the current scene
     */
    public void addClickHandlers(EventHandler<MouseEvent> handler) {
        currentScene.setOnMouseClicked(handler);
    }

    /**
     * Initialises the first scene to be displayed on the main application
     * window. This is the introduction scene IntroScene.
     */
    private void startUp() {
        currentScene = intro.getScene();
        currentSceneNum = 1;
    }
}
