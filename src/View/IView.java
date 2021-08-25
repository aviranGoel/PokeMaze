package View;

import algorithms.mazeGenerators.Maze;
import javafx.scene.Scene;

/**
 * Interface class - IView
 * Represents the class which responsible to the GUI View
 */
public interface IView
{
    /**
     * Override The Interface Display Maze Pane
     * @param maze - Given Maze to draw
     */
    void displayMaze(Maze maze);

    /**
     * Exit - Exit the Program - By using MyMyModel
     */
    void exit();

}
