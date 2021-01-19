package game;

import csse2002.block.world.WorldMap;
import csse2002.block.world.Tile;
import csse2002.block.world.NoExitException;
import csse2002.block.world.TooHighException;
import csse2002.block.world.TooLowException;
import csse2002.block.world.InvalidBlockException;

import java.util.Map;

/**
 * Class to handle all actions performed on the WorldMap.
 */
public class MapAction {
    // current world map instance and position of builder
    private WorldMap currentMap;
    private int currentX;
    private int currentY;

    // keeps track of selected inventory block for DROP_BLOCK action
    private int selectedInvBlock;

    // keeps track of total times MOVE_BUILDER is performed
    private int totalSteps;

    /**
     * Links the given WorldMap instance and position to the class methods, and
     * sets default values for the selected inventory block and total steps.
     * @param map instance of WorldMap to perform actions on
     * @param positionX x oc-ordinate of current position
     * @param positionY y co-ordinate of current position
     */
    public MapAction(WorldMap map, int positionX, int positionY) {
        currentMap = map;
        currentX = positionX;
        currentY = positionY;

        selectedInvBlock = -1;
        totalSteps = 0;
    }

    /**
     * Returns the current builder x co-ordinate.
     * @return x co-ordinate of builder
     */
    public int getCurrentX() {
        return currentX;
    }

    /**
     * Returns the current builder y co-ordinate.
     * @return y co-ordinate of builder
     */
    public int getCurrentY() {
        return currentY;
    }

    /**
     * Returns the index of the currently selected inventory block. Returns -1
     * if no blcok is selected, and PLACE_BLOCK can not be performed yet.
     * @return index of selected inventory block
     */
    public int getSelectedInvBlock() {
        return selectedInvBlock;
    }

    /**
     * Returns the total number of steps taken by the builder. This is analogous
     * to the number of times MOVE_BUILDER has been successfully performed on
     * the world map
     * @return total number of steps taken
     */
    public int getTotalSteps() {
        return totalSteps;
    }

    /**
     * Sets the index of the currently selected inventory block to the specified
     * int.
     * @param index the index of the selected block
     */
    public void setSelectedInvBlock(int index) {
        selectedInvBlock = index;
    }

    /**
     * Performs the MOVE_BUILDER action on the world map. The given direction is
     * the direction the action is performed. The alert message is updated
     * depending on if the action was performed successfully or not.
     * @param direction direction of the MOVE_BUILDER action
     * @return message indicative of action performance
     */
    public String moveBuilder(String direction) {
        String message;
        Map<String, Tile> availableExits = currentMap.getBuilder().getCurrentTile().getExits();

        if (availableExits.containsKey(direction)) {
            try {
                currentMap.getBuilder().moveTo(availableExits.get(direction));
                updatePosition(direction);
                message = "You moved";
            } catch (NoExitException e) {
                message = "Tile too high / low";
            }
        } else {
            message = "No exit that way";
        }

        return message;
    }

    /**
     * Performs the MOVE_BLOCK action on the world map. The given direction is
     * the direction the block will move. The alert message is updated depending
     * on if the action was performed successfully or not.
     * @param direction direction of the MOVE__BLOCK action
     * @return message indicative of action performance
     */
    public String moveBlock(String direction) {
        String message;
        try {
            currentMap.getBuilder().getCurrentTile().moveBlock(direction);
            message = "You moved a block";
        } catch (InvalidBlockException e) {
            message = "Too weak to \nmove that stone block";
        } catch (TooHighException e) {
            message = "That tile's\ntoo high";
        } catch (NoExitException e) {
            message = "There's no exit\nthat way";
        }
        return message;
    }

    /**
     * Performs the DIG action on the world map. The alert message is updated
     * depending on if the action was performed successfully or not.
     * @return message indicative of action performance
     */
    public String digBlock() {
        String message;
        try {
            currentMap.getBuilder().digOnCurrentTile();
            message = "You dug something \nup.";
        } catch (InvalidBlockException e) {
            message = "Too weak to \nremove that block";
        } catch (TooLowException e) {
            message = "You might not want \nto dig that";
        }
        return message;
    }

    /**
     * Performs the DROP_BLOCK action on the world map. The given index is the
     * index of the inventory block to be placed. The alert message is updated
     * depending on if the action was performed successfully or not.
     * @param index index of inventory block to be placed
     * @return message indicative of action performance
     */
    public String dropBlock(int index) {
        String message;
        try {
            currentMap.getBuilder().dropFromInventory(index);
            message = "You dropped something";
        } catch (InvalidBlockException e) {
            message = "You can't drop that there";
        } catch (TooHighException e) {
            message = "You're getting too high there";
        }
        selectedInvBlock = -1;
        return message;
    }

    /**
     * Changes the x and y co-ordinates of the builder based on the direction
     * of the exit name. The total number of steps taken is incremented.
     * This is only used in the MOVE_BUILDER action.
     * @param exitName direction of action
     */
    private void updatePosition(String exitName) {
        switch(exitName) {
            case "north":
                --currentY;
                break;
            case "east":
                ++currentX;
                break;
            case "south":
                ++currentY;
                break;
            case "west":
                --currentX;
                break;
        }
        ++totalSteps;
    }
}
