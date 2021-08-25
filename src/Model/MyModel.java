package Model;

import Client.Client;
import Client.IClientStrategy;
import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;

import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class - MyModel, implements IModel class extends Observable class
 * Represents the Model class which responsible to the real work
 * Observable - someone looking at me and waiting to get a message when and what I had been changed.
 * By using:
 *         setChanged();
 *         notifyObservers(String_What_I_Changed->Can be use with Switch-Case);
 */
public class MyModel extends Observable implements IModel
{
    private ArrayList<AState> m_solutionArray;
    private Maze m_maze;
    private Server m_mazeGeneratingServer;
    private Server m_solveSearchProblemServer;

    private ExecutorService m_threadPool = Executors.newCachedThreadPool();

    /**
     * Constructor - For MyModel class
     * Raise the MazeGeneratingServer and the SolveSearchProblemServer
     */
    public MyModel() /* throws UnknownHostException */
    {
        // Raise the servers
        m_mazeGeneratingServer = new Server(5400, 1000, new ServerStrategyGenerateMaze());
        m_solveSearchProblemServer = new Server(5401, 1000, new ServerStrategySolveSearchProblem());
    }

    /**
     * Start Servers method - Start the MazeGeneratingServer and the SolveSearchProblemServer
     */
    public void startServers()
    {
        m_mazeGeneratingServer.start();
        m_solveSearchProblemServer.start();
    }

    /**
     * Stop Servers method - Stop the MazeGeneratingServer and the SolveSearchProblemServer
     */
    public void stopServers()
    {
        m_mazeGeneratingServer.stop();
        m_solveSearchProblemServer.stop();
    }

    private int m_characterStartPositionRow;
    private int m_characterStartPositionColumn;

    private int m_characterPositionRow;
    private int m_characterPositionColumn;

    private int m_characterGoalPositionRow;
    private int m_characterGoalPositionColumn;

    @Override
    /**
     * Override The Interface Generate Maze method - Generate a Maze by using MazeGeneratingServer
     */
    public void generateMaze(int rowY_Height, int colX_Width)
    {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5400, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{rowY_Height, colX_Width};
                        toServer.writeObject(mazeDimensions); //send maze dimensions to server
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[rowY_Height * colX_Width + 10 /*BYTE ARRAY SIZE = (row*col+10)*/ /*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                        is.read(decompressedMaze); //Fill decompressedMaze with bytes
                        m_maze = new Maze(decompressedMaze);
                        m_characterStartPositionRow = m_maze.getStartPosition().getRowIndex();
                        m_characterStartPositionColumn = m_maze.getStartPosition().getColumnIndex();
                        m_characterPositionRow = m_characterStartPositionRow;
                        m_characterPositionColumn = m_characterStartPositionColumn;

                        m_characterGoalPositionRow = m_maze.getGoalPosition().getRowIndex();
                        m_characterGoalPositionColumn = m_maze.getGoalPosition().getColumnIndex();

                        m_solutionArray = null;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("generateMaze");
    }

    @Override
    /**
     * Override The Interface Solve Maze method - Solve a Maze by using SolveSearchProblemServer
     * The Solve Path is form the Character Current Position to the Maze Goal Position
     */
    public void solveMaze()
    {
        try {
            Client client = new Client(InetAddress.getLocalHost(), 5401, new IClientStrategy() {
                @Override
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        Maze maze = m_maze;

                        // Update Maze Start Position to Character current Position
                        maze.setStartPosition(m_characterPositionRow, m_characterPositionColumn);

                        toServer.writeObject(maze); //send maze to server
                        toServer.flush();
                        Solution mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                        m_solutionArray = mazeSolution.getSolutionPath();


                        //Print Maze Solution retrieved from the server
//                        System.out.println(String.format("Solution steps: %s", mazeSolution));
//                        ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
//
//                        for (int i = 0; i < mazeSolutionSteps.size(); i++) {
//                            System.out.println(String.format("%s. %s", i, mazeSolutionSteps.get(i).toString()));
//                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            client.communicateWithServer();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        setChanged();
        notifyObservers("solveMaze");
    }

    @Override
    /**
     * Override The Interface Move Character method - Check if possible to move and move it
     * @param movement - The pressed key (KeyCode type)
     */
    public void moveCharacter(KeyCode movement)
    {
        switch (movement)
        {
            // Move Up
            case UP:
                if(possibleToMoveTo(m_characterPositionRow - 1, m_characterPositionColumn))
                {
                    m_characterPositionRow = m_characterPositionRow - 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Charmander_GIF.gif");
                }
                break;
            // Move Up - NUMPAD8
            case NUMPAD8:
                if(possibleToMoveTo(m_characterPositionRow - 1, m_characterPositionColumn))
                {
                    m_characterPositionRow = m_characterPositionRow - 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Charmander_GIF.gif");
                }
                break;
            // Move Down
            case DOWN:
                if(possibleToMoveTo(m_characterPositionRow + 1, m_characterPositionColumn))
                {
                    m_characterPositionRow = m_characterPositionRow + 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Squirtle_GIF.gif");
                }
                break;
            // Move Down - NUMPAD2
            case NUMPAD2:
                if(possibleToMoveTo(m_characterPositionRow + 1, m_characterPositionColumn))
                {
                    m_characterPositionRow = m_characterPositionRow + 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Squirtle_GIF.gif");
                }
                break;
            // Move Right
            case RIGHT:
                if(possibleToMoveTo(m_characterPositionRow, m_characterPositionColumn + 1))
                {
                    m_characterPositionColumn = m_characterPositionColumn + 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Bulbasaur_GIF.gif");
                }
                break;
            // Move Right - NUMPAD6
            case NUMPAD6:
                if(possibleToMoveTo(m_characterPositionRow, m_characterPositionColumn + 1))
                {
                    m_characterPositionColumn = m_characterPositionColumn + 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Bulbasaur_GIF.gif");
                }
                break;
            // Move Left
            case LEFT:
                if(possibleToMoveTo(m_characterPositionRow, m_characterPositionColumn - 1))
                {
                    m_characterPositionColumn = m_characterPositionColumn - 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Pikachu_GIF.gif");
                }
                break;
            // Move Left-NUMPAD4
            case NUMPAD4:
                if(possibleToMoveTo(m_characterPositionRow, m_characterPositionColumn - 1))
                {
                    m_characterPositionColumn = m_characterPositionColumn - 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Pikachu_GIF.gif");
                }
                break;
            // Move UP-Right
            case NUMPAD9:
                if(possibleToMoveTo(m_characterPositionRow - 1, m_characterPositionColumn + 1)
                        && (possibleToMoveTo(m_characterPositionRow - 1, m_characterPositionColumn)
                        || possibleToMoveTo(m_characterPositionRow, m_characterPositionColumn + 1)))
                {
                    m_characterPositionRow = m_characterPositionRow - 1;
                    m_characterPositionColumn = m_characterPositionColumn + 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Bulbasaur_GIF.gif");
                }
                break;
            // Move Up-Left
            case NUMPAD7:
                if(possibleToMoveTo(m_characterPositionRow - 1, m_characterPositionColumn - 1)
                        && (possibleToMoveTo(m_characterPositionRow - 1, m_characterPositionColumn)
                        || possibleToMoveTo(m_characterPositionRow, m_characterPositionColumn - 1)))
                {
                    m_characterPositionRow = m_characterPositionRow - 1;
                    m_characterPositionColumn = m_characterPositionColumn - 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Bulbasaur_GIF.gif");
                }
                break;
            // Move Down-Right
            case NUMPAD3:
                if(possibleToMoveTo(m_characterPositionRow + 1, m_characterPositionColumn + 1)
                        && (possibleToMoveTo(m_characterPositionRow + 1, m_characterPositionColumn)
                        || possibleToMoveTo(m_characterPositionRow, m_characterPositionColumn + 1)))
                {
                    m_characterPositionRow = m_characterPositionRow + 1;
                    m_characterPositionColumn = m_characterPositionColumn + 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Bulbasaur_GIF.gif");
                }
                break;
            // Move Down-Left
            case NUMPAD1:
                if(possibleToMoveTo(m_characterPositionRow + 1, m_characterPositionColumn - 1)
                        && (possibleToMoveTo(m_characterPositionRow + 1, m_characterPositionColumn)
                        || possibleToMoveTo(m_characterPositionRow, m_characterPositionColumn - 1)))
                {
                    m_characterPositionRow = m_characterPositionRow + 1;
                    m_characterPositionColumn = m_characterPositionColumn - 1;
                    //m_mazeDisplayer.setImageFileNameCharacter("resources/Images/Bulbasaur_GIF.gif");
                }
                break;
            // Move to Start Position
            case HOME:
                m_characterPositionRow = m_characterStartPositionRow;
                m_characterPositionColumn = m_characterStartPositionColumn;
                break;
        }
        setChanged();
        notifyObservers("moveCharacter");
    }

    @Override
    /**
     * Get Maze method - By using MyModel
     * @return - Return the Maze (Maze type)
     */
    public Maze getMaze()
    {
        return m_maze;
    }

    @Override
    /**
     * Override The Interface Get Character Position Row method
     * @return - Return the Character Position Row (int type)
     */
    public int getCharacterPositionRow()
    {
        return m_characterPositionRow;
    }

    @Override
    /**
     * Override The Interface Get Character Position Column method
     * @return - Return the Character Position Column (int type)
     */
    public int getCharacterPositionColumn()
    {
        return m_characterPositionColumn;
    }

    @Override
    /**
     * Override The Interface Get Maze Start Position Row method
     * @return - Return the Maze Start Position Row (int type)
     */
    public int getCharacterStartPositionRow()
    {
        return m_characterStartPositionRow;
    }

    @Override
    /**
     * Override The Interface Get Maze Start Position Column method
     * @return - Return the Maze Start Position Column (int type)
     */
    public int getCharacterStartPositionColumn()
    {
        return m_characterStartPositionColumn;
    }

    @Override
    /**
     * Override The Interface Get Maze/Character Goal Position Row method
     * @return - Return the Maze/Character Goal Position Row (int type)
     */
    public int getCharacterGoalPositionRow() { return m_characterGoalPositionRow;}

    @Override
    /**
     * Override The Interface Get Maze/Character Goal Position Column method
     * @return - Return the Maze/Character Goal Position Column (int type)
     */
    public int getCharacterGoalPositionColumn() { return m_characterGoalPositionColumn; }

    @Override
    /**
     * Override The Interface Get Solution Path Array method
     * @return - Return the Maze Solution Path (ArrayList<AState> type)
     */
    public ArrayList<AState> getSloutionPathArray() { return m_solutionArray; }

    @Override
    /**
     * Override The Interface Get Maze Byte Array method
     * @retrun - Return the Byte Array form of the Maze (byte[] type)
     */
    public byte[] getMazeByteArray() { return m_maze.toByteArray(); }

    @Override
    /**
     * Override The Interface Generate a Maze form a given Byte Array method - Create a Maze form a given Byte Array
     * @param mazeByteArray - Given Byte Array (byte[] type)
     */
    public void generateMazeFromGivenByteArray(byte[] mazeByteArray)
    {
        m_maze = new Maze(mazeByteArray);
    }


    @Override
    /**
     * Override The Interface Load a Maze
     * @param mazeByteArray - Given Byte Array (byte[] type)
     * @param characterPositionRow - Given Character Position Row (int type)
     * @param characterPositionColumn - Given Character Position Column (int type)
     */
    public void loadMaze(byte[] mazeByteArray, int characterPositionRow, int characterPositionColumn)
    {
        m_maze = new Maze(mazeByteArray);
        m_characterPositionRow = characterPositionRow;
        m_characterPositionColumn = characterPositionColumn;

        m_characterStartPositionRow = m_maze.getStartPosition().getRowIndex();
        m_characterStartPositionColumn = m_maze.getStartPosition().getColumnIndex();

        m_characterGoalPositionRow = m_maze.getGoalPosition().getRowIndex();
        m_characterGoalPositionColumn = m_maze.getGoalPosition().getColumnIndex();

        setChanged();
        notifyObservers("loadMaze");
    }


    @Override
    /**
     * Override The Interface Move Character method by Mouse - Check if possible to move and move it
     * @param mousePixelWidthColX - Mouse Pixel Width Col X location (double type)
     * @param mousePixelHightRowY - Mouse Pixel Hight Row Y location (double type)
     * @param cellWidthColX - Cell Width Col X size (double type)
     * @param cellHeightRowY - Cell Hight Row Y size (double type)
     */
    public void moveCharacterByMouse(double mousePixelWidthColX, double mousePixelHightRowY, double cellWidthColX, double cellHeightRowY)
    {
        double minCellHeightRowY = m_characterPositionRow * cellHeightRowY;
        double minCellWidthColX = m_characterPositionColumn * cellWidthColX;

        double maxCellHeightRowY = (m_characterPositionRow + 1) * cellHeightRowY;
        double maxCellWidthColX = (m_characterPositionColumn + 1) * cellWidthColX;

        // Move Up
        if((mousePixelHightRowY < minCellHeightRowY)
                && ((mousePixelWidthColX < maxCellWidthColX) && (mousePixelWidthColX > minCellWidthColX)))
        {
            moveCharacter(KeyCode.UP);
        }
        // Move Down
        else if((mousePixelHightRowY > maxCellHeightRowY)
                && ((mousePixelWidthColX < maxCellWidthColX) && (mousePixelWidthColX > minCellWidthColX)))
        {
            moveCharacter(KeyCode.DOWN);
        }
        // Move Right
        else if((mousePixelWidthColX > maxCellWidthColX)
                && ((mousePixelHightRowY < maxCellHeightRowY) && (mousePixelHightRowY > minCellHeightRowY)))
        {
            moveCharacter(KeyCode.RIGHT);
        }
        // Move Left
        else if((mousePixelWidthColX < minCellWidthColX)
                && ((mousePixelHightRowY < maxCellHeightRowY) && (mousePixelHightRowY > minCellHeightRowY)))
        {
            moveCharacter(KeyCode.LEFT);
        }

        // Move UP-Right
        else if((mousePixelHightRowY < minCellHeightRowY) && (mousePixelWidthColX > maxCellWidthColX))
        {
            moveCharacter(KeyCode.NUMPAD9);
        }
        // Move Up-Left
        else if((mousePixelHightRowY < minCellHeightRowY) && (mousePixelWidthColX < minCellWidthColX))
        {
            moveCharacter(KeyCode.NUMPAD7);
        }
        // Move Down-Right
        else if((mousePixelHightRowY > maxCellHeightRowY) && (mousePixelWidthColX > maxCellWidthColX))
        {
            moveCharacter(KeyCode.NUMPAD3);
        }
        // Move Down-Left
        else if((mousePixelHightRowY > maxCellHeightRowY) && (mousePixelWidthColX < minCellHeightRowY))
        {
            moveCharacter(KeyCode.NUMPAD1);
        }
    }

    /**
     * Check if possible to move to new Position/Cell in Maze - Check if Position/Cell in Maze and if Position/Cell is a way
     * @param rowY - Row Y new Position/Cell (int type)
     * @param colX - Col X new Position/Cell (int type)
     * @return - True if Position/Cell in Maze and if Position/Cell is a way, else Return False
     */
    private boolean possibleToMoveTo(int rowY, int colX)
    {
        return (isPositionInMaze(rowY, colX) && positionIsWay(rowY, colX));
    }

    /**
     * Check if Position/Cell in Maze
     * @param rowY - Row Y new Position/Cell (int type)
     * @param colX - Col X new Position/Cell (int type)
     * @return - True if Position/Cell in Maze
     */
    private boolean isPositionInMaze(int rowY, int colX)
    {
        return ((rowY >= 0 && rowY < m_maze.getNumberOfRows() && colX >= 0 && colX < m_maze.getNumberOfColumns()));
    }

    /**
     * Check if Position/Cell is a way
     * @param rowY - Row Y new Position/Cell (int type)
     * @param colX - Col X new Position/Cell (int type)
     * @return - True if Position/Cell is a way, else Return False
     */
    private boolean positionIsWay(int rowY, int colX)
    {
        return (m_maze.getMazeCellValue(rowY,colX) == 0);
    }
}
