package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * Interface class - IModel
 * Represents the class which responsible to the real work
 */
public interface IModel
{
    /**
     * Stop Servers method - Stop the MazeGeneratingServer and the SolveSearchProblemServer
     */
    void stopServers();

    /**
     * Generate Maze method - Generate a Maze by using MazeGeneratingServer
     */
    void generateMaze(int rowY_Height, int colX_Width);

    /**
     * Solve Maze method - Solve a Maze by using SolveSearchProblemServer
     * The Solve Path is form the Character Current Position to the Maze Goal Position
     */
    void solveMaze();

    /**
     * Move Character method - Check if possible to move and move it
     * @param movement - The pressed key (KeyCode type)
     */
    void moveCharacter(KeyCode movement);

    /**
     * Get Maze method - By using MyModel
     * @return - Return the Maze (Maze type)
     */
    Maze getMaze();

    /**
     * Get Character Position Row method
     * @return - Return the Character Position Row (int type)
     */
    int getCharacterPositionRow();

    /**
     * Get Character Position Column method
     * @return - Return the Character Position Column (int type)
     */
    int getCharacterPositionColumn();

    /**
     * Get Maze Start Position Row method
     * @return - Return the Maze Start Position Row (int type)
     */
    int getCharacterStartPositionRow();

    /**
     Get Maze Start Position Column method
     * @return - Return the Maze Start Position Column (int type)
     */
    int getCharacterStartPositionColumn();

    /**
     * Get Maze/Character Goal Position Row method
     * @return - Return the Maze/Character Goal Position Row (int type) type)
     */
    int getCharacterGoalPositionRow();

    /**
     * Get Maze/Character Goal Position Column method
     * @return - Return the Maze/Character Goal Position Column (int type)
     */
    int getCharacterGoalPositionColumn();

    /**
     * Get Solution Path Array method
     * @return - Return the Maze Solution Path (ArrayList<AState> type)
     */
    ArrayList<AState> getSloutionPathArray();

    /**
     * Get Maze Byte Array method
     * @retrun - Return the Byte Array form of the Maze (byte[] type)
     */
    byte[] getMazeByteArray();

    /**
     * Generate a Maze form a given Byte Array method - Create a Maze form a given Byte Array
     * @param mazeByteArray - Given Byte Array (byte[] type)
     */
    void generateMazeFromGivenByteArray(byte[] mazeByteArray);

    /**
     * Load a Maze
     * @param mazeByteArray - Given Byte Array (byte[] type)
     * @param characterPositionRow - Given Character Position Row (int type)
     * @param characterPositionColumn - Given Character Position Column (int type)
     */
    void loadMaze(byte[] mazeByteArray, int characterPositionRow, int characterPositionColumn);

    /**
     * Move Character method by Mouse - Check if possible to move and move it
     * @param mousePixelWidthColX - Mouse Pixel Width Col X location (double type)
     * @param mousePixelHightRowY - Mouse Pixel Hight Row Y location (double type)
     * @param cellWidthColX - Cell Width Col X size (double type)
     * @param cellHeightRowY - Cell Hight Row Y size (double type)
     */
    void moveCharacterByMouse(double mousePixelWidthColX, double mousePixelHightRowY, double cellWidthColX, double cellHeightRowY);
}
