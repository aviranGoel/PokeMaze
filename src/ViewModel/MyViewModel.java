package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Class - MyViewModel, implements Observer class extends Observable class
 * Represents the MyViewModel class which responsible to the Communication between the MyViewController to the MyModel
 * Observable - someone looking at me and waiting to get a message when and what I had been changed.
 * Observer - looking at someone and waiting to get a message when and what he had been changed
 * By using:
 *         setChanged();
 *         notifyObservers(String_What_I_Changed->Can be use with Switch-Case);
 */
public class MyViewModel extends Observable implements Observer
{
    private IModel m_myModel;

    private int m_characterPositionRowIndex;
    private int m_characterPositionColumnIndex;

    // This is for Binding - MyViewController.bindProperties
    public StringProperty m_characterPositionRow = new SimpleStringProperty("");
    public StringProperty m_characterPositionColumn = new SimpleStringProperty("");

    /**
     * Constructor - For MyViewModel class
     * @param model - Connect the specific MyModel to MyViewModel (IModel type)
     */
    public MyViewModel(IModel model)
    {
        this.m_myModel = model;
    }

    @Override
    /**
     * Update things by the given arg and using Switch-Case
     * @param o - The Observable (Observable type)
     * @param arg - The arg the need to Update according to it (Object type)
     */
    public void update(Observable o, Object arg)
    {
        if (o == m_myModel)
        {
            switch ((String)arg)
            {
                // Generate the Maze - update Character position
                case "generateMaze":
                    m_characterPositionRowIndex = m_myModel.getCharacterPositionRow();
                    m_characterPositionRow.set(m_characterPositionRowIndex + "");
                    m_characterPositionColumnIndex = m_myModel.getCharacterPositionColumn();
                    m_characterPositionColumn.set(m_characterPositionColumnIndex + "");
                    setChanged();
                    notifyObservers("generateMaze");
                    break;
                // Move the Character - update Character position
                case "moveCharacter":
                    m_characterPositionRowIndex = m_myModel.getCharacterPositionRow();
                    m_characterPositionRow.set(m_characterPositionRowIndex + "");
                    m_characterPositionColumnIndex = m_myModel.getCharacterPositionColumn();
                    m_characterPositionColumn.set(m_characterPositionColumnIndex + "");
                    setChanged();
                    notifyObservers("moveCharacter");
                    break;
                // Solve the Maze
                case "solveMaze":
                    setChanged();
                    notifyObservers("solveMaze");
                    break;
                // Load the Maze - update Character position - Same as "generateMaze" case
                case "loadMaze":
                    m_characterPositionRowIndex = m_myModel.getCharacterPositionRow();
                    m_characterPositionRow.set(m_characterPositionRowIndex + "");
                    m_characterPositionColumnIndex = m_myModel.getCharacterPositionColumn();
                    m_characterPositionColumn.set(m_characterPositionColumnIndex + "");
                    setChanged();
                    notifyObservers("loadMaze");
                    break;
            }


//            m_characterPositionRowIndex = m_myModel.getCharacterPositionRow();
//            m_characterPositionRow.set(m_characterPositionRowIndex + "");
//            m_characterPositionColumnIndex = m_myModel.getCharacterPositionColumn();
//            m_characterPositionColumn.set(m_characterPositionColumnIndex + "");
//            setChanged();
//            notifyObservers();
        }
    }

    /**
     * Generate Maze method - Generate a Maze by using MyModel
     * @param rowY_Height - Number of rows in the Maze (int type)
     * @param colX_Width - Number of columns in the Maze (int type)
     */
    public void generateMaze(int rowY_Height, int colX_Width)
    {
        m_myModel.generateMaze(rowY_Height, colX_Width);

        // Update the Character Position by the Maze Generate
        // After Maze Generate m_myModel.getCharacterPositionRow() = m_characterStartPositionRow
        // and m_myModel.getCharacterPositionColumn() = m_characterStartPositionColumn

//        m_characterPositionRowIndex = m_myModel.getCharacterPositionRow();
//        m_characterPositionRow.set(m_characterPositionRowIndex + "");
//        m_characterPositionColumnIndex = m_myModel.getCharacterPositionColumn();
//        m_characterPositionColumn.set(m_characterPositionColumnIndex + "");
    }

    /**
     * Solve Maze method - Solve a Maze by using SolveSearchProblemServer
     * The Solve Path is form the Character Current Position to the Maze Goal Position
     */
    public void solveMaze()
    {
        m_myModel.solveMaze();
    }

    /**
     * Move Character method - Move the Character by using MyModel
     * @param movement - The pressed key (KeyCode type)
     */
    public void moveCharacter(KeyCode movement)
    {
        m_myModel.moveCharacter(movement);
    }

    /**
     * Get Maze method
     * @return - Return the Maze (Maze type)
     */
    public Maze getMaze()
    {
        return m_myModel.getMaze();
    }

    /**
     * Get Character Position Row method - By using MyModel
     * @return - Return the Character Position Row (int type)
     */
    public int getCharacterPositionRow()
    {
        return m_characterPositionRowIndex;
    }

    /**
     * Get Character Position Column method - By using MyModel
     * @return - Return the Character Position Column (int type)
     */
    public int getCharacterPositionColumn()
    {
        return m_characterPositionColumnIndex;
    }

    /**
     * Get Maze Start Position Row method - By using MyModel
     * @return - Return the Maze Start Position Row (int type)
     */
    public int getCharacterStartPositionRow()
    {
        return m_myModel.getCharacterStartPositionRow();
    }

    /**
     * Get Maze Start Position Column method - By using MyModel
     * @return - Return the Maze Start Position Column (int type)
     */
    public int getCharacterStartPositionColumn()
    {
        return m_myModel.getCharacterStartPositionColumn();
    }

    /**
     * Get Maze/Character Goal Position Row method - By using MyModel
     * @return - Return the Maze/Character Goal Position Row (int type) type)
     */
    public int getCharacterGoalPositionRow()
    {
        return m_myModel.getCharacterGoalPositionRow();
    }

    /**
     * Get Maze/Character Goal Position Column method - By using MyModel
     * @return - Return the Maze/Character Goal Position Column (int type)
     */
    public int getCharacterGoalPositionColumn()
    {
        return m_myModel.getCharacterGoalPositionColumn();
    }

    /**
     * Get Solution Path Array method - By using MyModel
     * @return - Return the Maze Solution Path (ArrayList<AState> type)
     */
    public ArrayList<AState> getSloutionPathArray()
    {
        return m_myModel.getSloutionPathArray();
    }

    /**
     * Exit - Exit the Program
     * Frist Stop the Servers - By using MyModel
     */
    public void exitPressedStopServers()
    {
        m_myModel.stopServers();
    }

    /**
     * Get Maze Byte Array method - By using MyModel
     * @retrun - Return the Byte Array form of the Maze (byte[] type)
     */
    public byte[] getMazeByteArray()
    {
        return m_myModel.getMazeByteArray();
    }

    /**
     * Generate a Maze form a given Byte Array method - Create a Maze form a given Byte Array - By using MyModel
     * @param mazeByteArray - Given Byte Array (byte[] type)
     */
    public void generateMazeFromGivenByteArray(byte[] mazeByteArray)
    {
        m_myModel.generateMazeFromGivenByteArray(mazeByteArray);
    }

    /**
     * Load a Maze - By using MyModel
     * @param mazeByteArray - Given Byte Array (byte[] type)
     * @param characterPositionRow - Given Character Position Row (int type)
     * @param characterPositionColumn - Given Character Position Column (int type)
     */
    public void loadMaze(byte[] mazeByteArray, int characterPositionRow, int characterPositionColumn)
    {
        m_myModel.loadMaze(mazeByteArray,characterPositionRow,characterPositionColumn);
    }

    /**
     * Move Character method by Mouse - Check if possible to move and move it - By using MyModel
     * @param mousePixelWidthColX - Mouse Pixel Width Col X location (double type)
     * @param mousePixelHightRowY - Mouse Pixel Hight Row Y location (double type)
     * @param cellWidthColX - Cell Width Col X size (double type)
     * @param cellHeightRowY - Cell Hight Row Y size (double type)
     */
    public void moveCharacterByMouse(double mousePixelWidthColX, double mousePixelHightRowY, double cellWidthColX, double cellHeightRowY)
    {
        m_myModel.moveCharacterByMouse(mousePixelWidthColX, mousePixelHightRowY, cellWidthColX, cellHeightRowY);
    }
}
