package View;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


/**
 * Class - CharacterDisplayer, extends Canvas class
 * Represents the CharacterDisplayer class which responsible to the Character Pane
 */
public class CharacterDisplayer extends Canvas
{
    private int m_mazeNumberOfRows;
    private int m_mazeNmberOfColumns;

    private double m_cellHeightRowY;
    private double m_cellWidthColX;

    private int m_characterPositionRow;
    private int m_characterPositionColumn;

    public int getCharacterPositionRow() { return m_characterPositionRow; }
    public int getCharacterPositionColumn() { return m_characterPositionColumn; }

    // Region Properties
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();

    /**
     * Get the Character Image Name
     * @return - The Character Image Name (String type)
     */
    public String getImageFileNameCharacter() { return ImageFileNameCharacter.get(); }

    /**
     * Set the Character Image Name
     * @param imageFileNameCharacter - The Character Image Name (String type)
     */
    public void setImageFileNameCharacter(String imageFileNameCharacter) { this.ImageFileNameCharacter.set(imageFileNameCharacter); }
    // Endregion

    public void setMazeAndCellSize(int numberOfRows, int numberOfColumns)
    {
        m_mazeNumberOfRows = numberOfRows;
        m_mazeNmberOfColumns = numberOfColumns;

        double canvasHeightRowY = getHeight();
        double canvasWidthColX = getWidth();

        m_cellHeightRowY = canvasHeightRowY / m_mazeNumberOfRows;
        m_cellWidthColX = canvasWidthColX / m_mazeNmberOfColumns;
    }

    public void setCharacterPosition(int rowY, int colX)
    {
        GraphicsContext graphicsContext2D = getGraphicsContext2D();
        // Clears the old Character Position
        graphicsContext2D.clearRect(m_characterPositionColumn * m_cellWidthColX, m_characterPositionRow * m_cellHeightRowY, m_cellWidthColX, m_cellHeightRowY);
        // Update the new Character Position
        m_characterPositionRow = rowY;
        m_characterPositionColumn = colX;
        redraw();
    }

    /**
     * Redraw the Character Pane
     */
    public void redraw()
    {
//        if (m_maze != null)
        {
            try {
                GraphicsContext graphicsContext2D = getGraphicsContext2D();

                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                // Draw Character in the new Position
                graphicsContext2D.drawImage(characterImage, m_characterPositionColumn * m_cellWidthColX, m_characterPositionRow * m_cellHeightRowY, m_cellWidthColX, m_cellHeightRowY);
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
     * Clear All Character Pane/Canvas
     */
    public void clearCharacterCanvas()
    {
        GraphicsContext graphicsContext2D = getGraphicsContext2D();
        // Clears the canvas
        graphicsContext2D.clearRect(0, 0, getWidth(), getHeight());
    }

    public double getM_cellHeightRowY() { return m_cellHeightRowY; }

    public double getM_cellWidthColX() { return m_cellWidthColX; }
}
