package View;

import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Class - MazeDisplayer, extends Canvas class
 * Represents the MazeDisplayer class which responsible to the Maze Pane
 */
public class MazeDisplayer extends Canvas
{
    private Maze m_maze;

//    private int m_characterPositionRow;
//    private int m_characterPositionColumn;
//
//    public int getCharacterPositionRow() { return m_characterPositionRow; }
//    public int getCharacterPositionColumn() { return m_characterPositionColumn; }


    private int m_goalCharacterPositionRow;
    private int m_goalCharacterPositionColumn;

    public int getGoalCharacterPositionRow() { return m_goalCharacterPositionRow; }
    public int getGoalCharacterPositionColumn() { return m_goalCharacterPositionColumn; }

    // Region Properties
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameWay = new SimpleStringProperty();
//    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileNameEndCharacter = new SimpleStringProperty();


    /**
     * Get the Way Image Name
     * @return - The Way Image Name (String type)
     */
    public String getImageFileNameWay() { return ImageFileNameWay.get(); }

    public void setImageFileNameWay(String imageFileNameWay) { this.ImageFileNameWay.set(imageFileNameWay); }

    /**
     * Get the Wall Image Name
     * @return - The Wall Image Name (String type)
     */
    public String getImageFileNameWall() { return ImageFileNameWall.get(); }

    /**
     * Set the Wall Image Name
     * @param imageFileNameWall - The Wall Image Name (String type)
     */
    public void setImageFileNameWall(String imageFileNameWall) { this.ImageFileNameWall.set(imageFileNameWall); }

//    public String getImageFileNameCharacter() { return ImageFileNameCharacter.get(); }
//
//    public void setImageFileNameCharacter(String imageFileNameCharacter) { this.ImageFileNameCharacter.set(imageFileNameCharacter); }

    /**
     * Get the End Character Image Name
     * @return - The End Character Image Name (String type)
     */
    public String getImageFileNameEndCharacter() { return ImageFileNameEndCharacter.get(); }

    /**
     * Set the End Character Image Name
     * @param imageFileNameEndCharacter - The End Character Image Name
     */
    public void setImageFileNameEndCharacter(String imageFileNameEndCharacter) { this.ImageFileNameEndCharacter.set(imageFileNameEndCharacter); }
    // Endregion


    /**
     * Set the Maze
     * @param maze - Given Maze to Set (Maze type)
     */
    public void setMaze(Maze maze)
    {
        this.m_maze = maze;
        redraw();
    }

//    public void setCharacterPosition(int rowY, int colX)
//    {
//        m_characterPositionRow = rowY;
//        m_characterPositionColumn = colX;
//        redraw();
//    }

    /**
     * Set the Goal Character Position
     * @param rowY - Character Goal Position Row Y (int type)
     * @param colX - Character Goal Position Column X (int type)
     */
    public void setGoalCharacterPosition(int rowY, int colX)
    {
        m_goalCharacterPositionRow = rowY;
        m_goalCharacterPositionColumn = colX;
        redraw();
    }

    /**
     * Redraw the Maze Pane
     */
    public void redraw()
    {
        if (m_maze != null)
        {
            double canvasHeightRowY = getHeight();
            double canvasWidthColX = getWidth();

            double cellHeightRowY = canvasHeightRowY / m_maze.getNumberOfRows();
            double cellWidthColX = canvasWidthColX / m_maze.getNumberOfColumns();

            try {
                GraphicsContext graphicsContext2D = getGraphicsContext2D();
                // Clears the canvas
                graphicsContext2D.clearRect(0, 0, getWidth(), getHeight());

                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image wayImage = new Image(new FileInputStream(ImageFileNameWay.get()));

                // Draw Maze
                for (int y = 0; y < m_maze.getNumberOfRows(); y++) {
                    for (int x = 0; x < m_maze.getNumberOfColumns(); x++)
                    {
                        // Draw Wall
                        if (m_maze.getMazeCellValue(y, x) == 1)
                        {
                            graphicsContext2D.drawImage(wallImage, x * cellWidthColX, y * cellHeightRowY, cellWidthColX, cellHeightRowY);
                        }
                        // Draw Way
                        else
                        {
                            graphicsContext2D.drawImage(wayImage, x * cellWidthColX, y * cellHeightRowY, cellWidthColX, cellHeightRowY);
                        }
                    }
                }
                // Draw Character
//                Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
//                graphicsContext2D.drawImage(characterImage, m_characterPositionColumn * cellWidthColX, m_characterPositionRow * cellHeightRowY, cellWidthColX, cellHeightRowY);

                // Draw End Character
                Image endCharacterImage = new Image(new FileInputStream(ImageFileNameEndCharacter.get()));
                graphicsContext2D.drawImage(endCharacterImage, m_goalCharacterPositionColumn * cellWidthColX, m_goalCharacterPositionRow * cellHeightRowY, cellWidthColX, cellHeightRowY);
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
     * Clear All Maze Pane/Canvas
     */
    public void clearMazeCanvas()
    {
        GraphicsContext graphicsContext2D = getGraphicsContext2D();
        // Clears the canvas
        graphicsContext2D.clearRect(0, 0, getWidth(), getHeight());
    }

    /**
     * Clear the Goal Position form Maze Pane
     */
    public void clearGoalCharacter()
    {
        double canvasHeightRowY = getHeight();
        double canvasWidthColX = getWidth();

        double cellHeightRowY = canvasHeightRowY / m_maze.getNumberOfRows();
        double cellWidthColX = canvasWidthColX / m_maze.getNumberOfColumns();

        try
        {
            GraphicsContext graphicsContext2D = getGraphicsContext2D();
        // Draw Way instead of End Character
        Image wayImage = new Image(new FileInputStream(ImageFileNameWay.get()));
        graphicsContext2D.drawImage(wayImage, m_goalCharacterPositionColumn * cellWidthColX, m_goalCharacterPositionRow * cellHeightRowY, cellWidthColX, cellHeightRowY);
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
