package game;

import javafx.geometry.Pos;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * Auxiliary controller class to handle the canvas pane in the main game scene.
 */
public class CanvasDrawer {
    // the scene and canvas
    private GameScene appView;
    private GraphicsContext mapCanvas;

    // default canvas sizes
    private final int CANVASHEIGHT = 600;
    private final int CANVASWIDTH = 600;

    // distances for tile lengths, widths and heights
    private final int STARTX = 250;
    private final int STARTY = 350;
    private final int SIZEX = 80;
    private final int SIZEY = 80;
    private final int INCREMENT = 15;

    /**
     * Links the view canvas to the CanvasDrawer methods.
     * @param appView the view class
     */
    public CanvasDrawer(GameScene appView) {
        this.appView = appView;
        mapCanvas = appView.getMapCanvas();
    }

    /**
     * Clears the canvas grid.
     */
    public void clearGrid() {
        mapCanvas.clearRect(0, 0, CANVASWIDTH, CANVASHEIGHT);
    }

    /**
     * Given a block x and y co-ordinate, tile height and block type, will
     * create an image of that block and draw it on the canvas.
     * @param x x co-ordinate (increasing from top left to bottom right)
     * @param y y co-ordinate (increasing from top right to bottom left)
     * @param height height index of block on tile
     * @param name block type
     */
    public void drawBlocks(int x, int y, int height, String name) {
        int xPosition = (STARTX + 2*x*INCREMENT) - (2*y*INCREMENT) ;
        int yPosition = (STARTY + x*INCREMENT) + (y*INCREMENT) - (2*height*INCREMENT);

        mapCanvas.drawImage(new Image(getImageURL(name)), xPosition, yPosition,
                SIZEX, SIZEY);
    }

    /**
     * Given an exit direction, the current tile's x and y co-ordinate, and the
     * builder's current height, will create an image of the exit arrow and draw
     * it on the canvas.
     * @param x x co-ordinate of current tile (increasing from top left to
     *          bottom right)
     * @param y y co-ordinate of current tile (increasing from top right to
     *          bottom left)
     * @param height height index of builder on tile
     * @param direction direction of the available exit
     */
    public void drawExitArrows(int x, int y, int height, String direction) {
        // draws the exit arrows higher than the builder to avoid cluttering
        // and blocks drawn over the arrows.
        height += 2;
        String arrow = null;

        // positions the arrows above the exit its referring to (rather than the
        // current tile
        switch (direction) {
            case "north":
                arrow = "/north.png";
                --y;
                break;
            case "east":
                arrow = "/east.png";
                ++x;
                break;
            case "south":
                arrow = "/south.png";
                ++y;
                break;
            case "west":
                arrow = "/west.png";
                --x;
                break;
        }
        if (arrow != null) {
            int xPosition = (STARTX + 2*x*INCREMENT) - (2*y*INCREMENT) ;
            int yPosition = (STARTY + x*INCREMENT) + (y*INCREMENT) - (2*height*INCREMENT);

            mapCanvas.drawImage(new Image(arrow), xPosition, yPosition,
                    SIZEX, SIZEY);
        }
    }

    /**
     * For a given block type and index in the inventory, adds the corresponding
     * image and button to the inventory gallery.
     * @param blockName block type
     * @param index block index in inventory
     */
    public void drawInventory(String blockName, int index) {
        // create container for each block entry
        VBox invBlock = new VBox();
        invBlock.setAlignment(Pos.CENTER);

        String url = getImageURL(blockName);
        ImageView blockImage = new ImageView(new Image(url, 80, 100, false, true));
        Button blockButton = new Button(blockName);

        invBlock.getChildren().addAll(blockImage, blockButton);
        appView.getInventory().getChildren().addAll(invBlock);
        appView.addToInventory(index, blockButton);
    }

    /**
     * For a block type, returns the corresponding image url.
     * @param name block type
     * @return string of block image url
     */
    public static String getImageURL(String name) {
        String url;
        switch(name) {
            case "grass":
                url = "/grass.png";
                break;
            case "soil":
                url = "/soil.png";
                break;
            case "stone":
                url = "/stone.png";
                break;
            case "wood":
                url = "/wood.png";
                break;
            case "ground":
                url = "/ground.png";
                break;
            case "builder":
                url = "/builder.png";
                break;
            case "hatbuilder":
                url = "/hatbuilder.png";
                break;
            default:
                url = "/default.png";
                break;
        }
        return url;
    }
}
