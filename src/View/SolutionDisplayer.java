package View;

import algorithms.search.AState;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Class - SolutionDisplayer, extends Canvas class
 * Represents the SolutionDisplayer class which responsible to the Solution Pane
 */
public class SolutionDisplayer extends Canvas
{
    private int m_mazeNumberOfRows;
    private int m_mazeNmberOfColumns;

    private double m_cellHeightRowY;
    private double m_cellWidthColX;

    private ArrayList<AState> m_solutionArray;

    // Region Properties
    private StringProperty ImageFileNameSolution = new SimpleStringProperty();

    /**
     * Get the Solution Image Name
     * @return - The Solution Image Name (String type)
     */
    public String getImageFileNameSolution() { return ImageFileNameSolution.get(); }

    /**
     * Set the Solution Image Name
     * @param imageFileNameSolution - The Solution Image Name (String type
     */
    public void setImageFileNameSolution(String imageFileNameSolution) { this.ImageFileNameSolution.set(imageFileNameSolution); }
    // Endregion

    /**
     * Set the Maze and Cell Size
     * @param numberOfRows - Maze Number of Rows (int type)
     * @param numberOfColumns  - Maze Number of Columns (int type)
     */
    public void setMazeAndCellSize(int numberOfRows, int numberOfColumns)
    {
        m_mazeNumberOfRows = numberOfRows;
        m_mazeNmberOfColumns = numberOfColumns;

        double canvasHeightRowY = getHeight();
        double canvasWidthColX = getWidth();

        m_cellHeightRowY = canvasHeightRowY / m_mazeNumberOfRows;
        m_cellWidthColX = canvasWidthColX / m_mazeNmberOfColumns;
    }

    /**
     * Set the Maze Solution Path
     * @param solutionPathArray - The Solution Path (ArrayList<AState> type)
     */
    public void setSolutionPathArray(ArrayList<AState> solutionPathArray)
    {
        m_solutionArray = solutionPathArray;

        // Remove the First and Last AState of the Solution - Don't Draw under the Character Position and under the Goal Position
        m_solutionArray.remove(0);
        m_solutionArray.remove(m_solutionArray.size() - 1);

        redraw();
    }

    /**
     * Redraw the Solution Pane
     */
    public void redraw()
    {
        double deltaWidthColX = m_cellWidthColX / 4;
        double deltaHeightRowY = m_cellHeightRowY / 4;
        if (m_solutionArray != null)
        {
            try
            {
                GraphicsContext graphicsContext2D = getGraphicsContext2D();

                Image solutionImage = new Image(new FileInputStream(ImageFileNameSolution.get()));

                for (AState solutionNode : m_solutionArray)
                {
                    // Draw the Solution Path from after Character Position to before Goal Position
//                    graphicsContext2D.drawImage(solutionImage, solutionNode.getColumnIndex() * m_cellWidthColX, solutionNode.getRowIndex() * m_cellHeightRowY, m_cellWidthColX, m_cellHeightRowY);
                    graphicsContext2D.drawImage(solutionImage, solutionNode.getColumnIndex() * m_cellWidthColX + deltaWidthColX, solutionNode.getRowIndex() * m_cellHeightRowY + deltaHeightRowY, m_cellWidthColX - 2*deltaWidthColX, m_cellHeightRowY - 2*deltaHeightRowY);
                }
            }
            catch (FileNotFoundException e)
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText(String.format("Image doesn't exist: %s",e.getMessage()));
                alert.setHeaderText("PokeMaze Error!");
                alert.show();
            }
        }
    }

    /**
     * Clear specific Cell form Solution Pane
     * @param row - Row to Clear (int type)
     * @param column - Column to Clear (int type)
     */
    public void clearCellFromSolutionCanvas(int row, int column)
    {
        GraphicsContext graphicsContext2D = getGraphicsContext2D();
        // Clears the canvas
        graphicsContext2D.clearRect(column * m_cellWidthColX, row * m_cellHeightRowY, m_cellWidthColX, m_cellHeightRowY);
    }

    /**
     * Clear All Solution Pane/Canvas
     */
    public void clearSolutionCanvas()
    {
        GraphicsContext graphicsContext2D = getGraphicsContext2D();
        // Clears the canvas
        graphicsContext2D.clearRect(0, 0, getWidth(), getHeight());
    }
}
