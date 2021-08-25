package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.Observable;
import java.util.Observer;

/**
 * Class - MyViewController, implements IView class extends Observer class
 * Represents the MyViewController class which responsible to the the GUI requests
 * Observer - looking at someone and waiting to get a message when and what he had been changed
 * By using:
 *         setChanged();
 *         notifyObservers(String_What_I_Changed->Can be use with Switch-Case);
 */
public class MyViewController implements Observer, IView
{
    @FXML
    private MyViewModel m_myViewModel;

    public MazeDisplayer m_mazeDisplayer;
    public CharacterDisplayer m_characterDisplayer;
    public SolutionDisplayer m_solutionDisplayer;

    public javafx.scene.control.TextField txt_rowsNum;
    public javafx.scene.control.TextField txt_columnsNum;
    public javafx.scene.control.Label lbl_characterRow;
    public javafx.scene.control.Label lbl_characterColumn;

    public Button button_solveMaze;
    public Button button_generateMaze;

    public RadioButton radioButton_bulbasaur;
    public RadioButton radioButton_charmander;
    public RadioButton radioButton_squirtle;

    // String Property for Binding
    public StringProperty m_characterPositionRow = new SimpleStringProperty();
    public StringProperty m_characterPositionColumn = new SimpleStringProperty();

    private Boolean m_mazeIsSolved = false;

    public MediaPlayer m_mediaPlayer;
    public boolean m_isMusicOn;
    public Label lbl_music;
    public Button button_music;
    public Button button_nextMusic;
    public Button button_forwardMusic;
    public String m_music;
    public Label lbl_nowPlaying;
    public Label lbl_musicSong;

    public boolean m_firstGenerate = true;

    public Pane displayer_pane;

    /**
     * Play the Background Music by using Media and Media Player
     * @param musicSong - Music Song to Play (String type)
     * @param timeToPlay - Time To Play from the Song (int type)
     */
    private void playMusic(String musicSong, int timeToPlay)
    {
        Media media = new Media(MyViewController.class.getClassLoader().getResource(musicSong).toExternalForm());
        m_mediaPlayer = new MediaPlayer(media);
        m_mediaPlayer.setAutoPlay(true);
        m_mediaPlayer.setStartTime(Duration.seconds(0));
        m_mediaPlayer.setStopTime(Duration.seconds(timeToPlay));

//        if(musicSong.equals(File.separator + "Music" + File.separator + "PokeMaze_music_winning.mp3"))
        if(musicSong.equals("Music/PokeMaze_music_winning.mp3"))
        {
            m_mediaPlayer.setCycleCount(1);
        }
        else
        {
            m_mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }
        m_mediaPlayer.play();
        lbl_music.setVisible(true);
        button_music.setVisible(true);
        button_nextMusic.setVisible(true);
        button_forwardMusic.setVisible(true);
        m_isMusicOn = true;
    }

    /**
     * Set the MyVIewModel
     * @param viewModel - Connect the specific ViewModel to MyViewController (MyViewModel type)
     */
    public void setViewModel(MyViewModel viewModel)
    {
        this.m_myViewModel = viewModel;
        bindProperties(m_myViewModel);
    }

    /**
     * Bind the Character Row and Column Properties
     * @param viewModel - ViewModel to Bind from (MyViewModel type)
     */
    private void bindProperties(MyViewModel viewModel)
    {
        // Set Binding for Properties
        lbl_characterRow.textProperty().bind(viewModel.m_characterPositionRow);
        lbl_characterColumn.textProperty().bind(viewModel.m_characterPositionColumn);
    }

    /**
     * Generate Maze method - Generate a Maze - By using MyMyModel
     * Maze Size from the GUI
     */
    public void generateMaze()
    {
        m_mazeDisplayer.toBack();
        m_solutionDisplayer.toFront();
        m_characterDisplayer.toFront();

        /**
         * Prim Maze Algorithm can't handle in case of rowY <= 2 && colX <= 2
         * So in that case we update rowY = 10 && colX = 10
         * And generate Maze 10*10
         */
        if(txt_rowsNum.getText().equals("") || txt_columnsNum.getText().equals("")
            || Integer.valueOf(txt_rowsNum.getText()) <= 2 || Integer.valueOf(txt_columnsNum.getText()) <= 2)
        {
            showAlert("Please Enter Maze Size Bigger Then 2*2!");
        }
        else
        {
            //m_mazeIsDisplayed = false;

            m_characterDisplayer.clearCharacterCanvas();
            m_solutionDisplayer.clearSolutionCanvas();

            int numberOfRowY_Height = Integer.valueOf(txt_rowsNum.getText());
            int numberOfColX_Width = Integer.valueOf(txt_columnsNum.getText());

            m_myViewModel.generateMaze(numberOfRowY_Height, numberOfColX_Width);

//            button_generateMaze.setDisable(true);
            m_mazeIsSolved = false;
            button_solveMaze.setDisable(false);
//            button_music.setVisible(true);

            if(m_firstGenerate == true)
            {
//                playMusic(File.separator + "Music" + File.separator + "PokeMaze_music01.mp3", 200);
                playMusic("Music/PokeMaze_music01.mp3", 200);
                m_music = "PokeMaze_music01";
                m_firstGenerate = false;
            }

            lbl_nowPlaying.setVisible(true);
            lbl_musicSong.setVisible(true);
            lbl_musicSong.setText(m_music);
        }
    }

    /**
     * Solve Maze method - By using MyMyModel
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void solveMaze(ActionEvent actionEvent)
    {
        m_myViewModel.solveMaze();

        button_solveMaze.setDisable(true);
        //showAlert("Solving PokeMaze...");
    }

    /**
     * Show Alert Method - Show an Alert
     * @param alertMessage - Massage of the Alert (String type)
     */
    private void showAlert(String alertMessage)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.setHeaderText("PokeMaze Message:");
        alert.show();
    }

    @Override
    /**
     * Update things by the given arg and using Switch-Case
     * @param o - The Observable (Observable type)
     * @param arg - The arg the need to Update according to it (Object type)
     */
    public void update(Observable o, Object arg)
    {
        if (o == m_myViewModel)
        {
            switch ((String)arg)
            {
                // Generate the Maze - update Character position
                case "generateMaze":
                    displayMaze(m_myViewModel.getMaze());
                    displayCharacter();
                    m_mazeDisplayer.setGoalCharacterPosition(m_myViewModel.getCharacterGoalPositionRow(),m_myViewModel.getCharacterGoalPositionColumn());
//                    button_generateMaze.setDisable(false);
                    break;
                // Move the Character - update Character position
                case "moveCharacter":
                    displayCharacter();
                    break;
                // Solve the Maze
                case "solveMaze":
                    m_solutionDisplayer.setSolutionPathArray(m_myViewModel.getSloutionPathArray());
                    break;
                // Load the Maze - update Character position - Same as "generateMaze" case
                case "loadMaze":
                    displayMaze(m_myViewModel.getMaze());
                    displayCharacter();
                    m_mazeDisplayer.setGoalCharacterPosition(m_myViewModel.getCharacterGoalPositionRow(),m_myViewModel.getCharacterGoalPositionColumn());
                    break;
            }
//            if(m_mazeIsDisplayed == false)
//            {
//                displayMaze(m_myViewModel.getMaze());
//                //btn_generateMaze.setDisable(false);
//                m_mazeIsDisplayed = true;
//            }
//            displayMaze(m_myViewModel.getMaze());
//            //btn_generateMaze.setDisable(false);
//            displayCharacter();
        }
    }

    /**
     * Display the Character Pane
     */
    private void displayCharacter()
    {
        int characterPositionRowIndex = m_myViewModel.getCharacterPositionRow();
        int characterPositionColumnIndex = m_myViewModel.getCharacterPositionColumn();
        m_characterDisplayer.setCharacterPosition(characterPositionRowIndex, characterPositionColumnIndex);
        this.m_characterPositionRow.set(characterPositionRowIndex + "");
        this.m_characterPositionColumn.set(characterPositionColumnIndex + "");

        m_solutionDisplayer.clearCellFromSolutionCanvas(characterPositionRowIndex, characterPositionColumnIndex);

        if(characterPositionRowIndex == m_myViewModel.getCharacterGoalPositionRow()
            && characterPositionColumnIndex == m_myViewModel.getCharacterGoalPositionColumn())
        {
            m_mazeIsSolved = true;
            m_music = "PokeMaza_music_winning";
            m_mediaPlayer.stop();
//            playMusic(File.separator + "Music" + File.separator + "PokeMaza_music_winning.mp3", 200);
            playMusic("Music/PokeMaze_music_winning.mp3", 200);
            button_solveMaze.setDisable(true);

            lbl_nowPlaying.setVisible(false);
            lbl_musicSong.setVisible(false);

            m_firstGenerate = true;

            m_mazeDisplayer.clearGoalCharacter();

            try
            {
                Stage stage = new Stage();
                stage.setTitle("PokeMaze-Winner Pane");
                FXMLLoader fxmlLoader = new FXMLLoader();
                Parent root = fxmlLoader.load(getClass().getResource("Winner.fxml").openStream());
                Scene scene = new Scene(root, 600, 400);
                stage.setScene(scene);
                stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
                scene.getStylesheets().add(getClass().getResource("MyCSSPokeMaze.css").toExternalForm());
                stage.show();
            }
            catch (Exception e)
            {

            }
        }
    }


    @Override
    /**
     * Override The Interface Display Maze Pane
     * @param maze - Given Maze to draw
     */
    public void displayMaze(Maze maze)
    {
        m_mazeDisplayer.setMaze(maze);

        m_characterDisplayer.clearCharacterCanvas();
        m_solutionDisplayer.clearSolutionCanvas();

        m_characterDisplayer.setMazeAndCellSize(maze.getNumberOfRows(), maze.getNumberOfColumns());
        m_solutionDisplayer.setMazeAndCellSize(maze.getNumberOfRows(), maze.getNumberOfColumns());
    }

    /**
     * Set the Size of Panes according to the Scene Resize
     * @param scene - Scene Resize (Scene type)
     */
    public void setResizeEvent(Scene scene)
    {
        long width = 0;
        long height = 0;
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                System.out.println("Width: " + newSceneWidth);
                m_mazeDisplayer.setWidth(m_mazeDisplayer.getWidth() + newSceneWidth.doubleValue() - oldSceneWidth.doubleValue());
                m_characterDisplayer.setWidth(m_characterDisplayer.getWidth() + newSceneWidth.doubleValue() - oldSceneWidth.doubleValue());
                m_solutionDisplayer.setWidth(m_solutionDisplayer.getWidth() + newSceneWidth.doubleValue() - oldSceneWidth.doubleValue());

                m_mazeDisplayer.redraw();
                m_characterDisplayer.clearCharacterCanvas();
                m_characterDisplayer.redraw();
                m_solutionDisplayer.clearSolutionCanvas();
                m_solutionDisplayer.redraw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                System.out.println("Height: " + newSceneHeight);
                m_mazeDisplayer.setHeight(m_mazeDisplayer.getHeight() + newSceneHeight.doubleValue() - oldSceneHeight.doubleValue());
                m_characterDisplayer.setHeight(m_characterDisplayer.getHeight() + newSceneHeight.doubleValue() - oldSceneHeight.doubleValue());
                m_solutionDisplayer.setHeight(m_solutionDisplayer.getHeight() + newSceneHeight.doubleValue() - oldSceneHeight.doubleValue());

                m_mazeDisplayer.redraw();
                m_characterDisplayer.clearCharacterCanvas();
                m_characterDisplayer.redraw();
                m_solutionDisplayer.clearSolutionCanvas();
                m_solutionDisplayer.redraw();
            }
        });
    }

    /**
     * Move the Character when key pressed - if Maze didn't Solve yet
     * @param keyEvent - Key the Pressed Resize (KeyEvent type)
     */
    public void KeyPressed(KeyEvent keyEvent)
    {
        if(m_mazeIsSolved == false)
        {
            try
            {
                m_myViewModel.moveCharacter(keyEvent.getCode());
                keyEvent.consume();
            }
            catch (NullPointerException error)
            {

            }
        }
    }

    /**
     * Exit - Exit the Program - By using MyMyModel
     */
    public void exit()
    {
        m_myViewModel.exitPressedStopServers();
        System.exit(0);
    }

    /**
     * Exit - Exit the Program
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void exitMazeByMenuBarPressed(ActionEvent actionEvent)
    {
        exit();
    }

    /**
     * Open the Story Stage
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void stroyFromMenuBarPressed(ActionEvent actionEvent)
    {
        try
        {
            Stage stage = new Stage();
            stage.setTitle("PokeMaze-Story Pane");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Story.fxml").openStream());
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            scene.getStylesheets().add(getClass().getResource("MyCSSPokeMaze.css").toExternalForm());
            stage.show();
        }
        catch (Exception e)
        {

        }
    }

    /**
     * Open the Instructions Stage
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void instructionsFromMenuBarPressed(ActionEvent actionEvent)
    {
        try
        {
            Stage stage = new Stage();
            stage.setTitle("PokeMaze-Instructions Pane");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Instructions.fxml").openStream());
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            scene.getStylesheets().add(getClass().getResource("MyCSSPokeMaze.css").toExternalForm());
            stage.show();
        }
        catch (Exception e)
        {

        }
    }

    /**
     * Open the About Stage
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void aboutFromMenuBarPressed(ActionEvent actionEvent)
    {
        try
        {
            Stage stage = new Stage();
            stage.setTitle("PokeMaze-About Pane");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            scene.getStylesheets().add(getClass().getResource("MyCSSPokeMaze.css").toExternalForm());
            stage.show();
        }
        catch (Exception e)
        {

        }
    }

    /**
     * Generate new Maze
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void newMazeByMenuBarPressed(ActionEvent actionEvent)
    {
        generateMaze();
    }

    /**
     * Save exsist Maze
     * @param actionEvent - Button Pressed (ActionEvent type)
     * @throws IOException
     */
    public void saveMazeByMenuBarPressed(ActionEvent actionEvent) throws IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("pokemaze file", "*.pokemaze"));
        File selectedLocation = fileChooser.showSaveDialog(null);
        if (selectedLocation != null)
        {
            File fileToSave = new File(selectedLocation.getPath());
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileToSave));
            Object[] savedMazeDetails = new Object[4];

            byte[] mazeByteArray = m_myViewModel.getMazeByteArray();

            savedMazeDetails[0] = mazeByteArray;
            savedMazeDetails[1] = m_myViewModel.getCharacterPositionRow();
            savedMazeDetails[2] = m_myViewModel.getCharacterPositionColumn();
            savedMazeDetails[3] = m_characterDisplayer.getImageFileNameCharacter();
//            savedMazeDetails[4] = nameOfWall;
//            savedMazeDetails[5] = nameOfPrize;

            out.writeObject(savedMazeDetails);
            out.flush();
            out.close();
        }
        else
        {
            showAlert("Sorry, Saved Has Been Failed!");
        }
    }

    /**
     * Load exsist Maze
     * @param actionEvent - Button Pressed (ActionEvent type)
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void loadMazeByMenuBarPressed(ActionEvent actionEvent) throws IOException, ClassNotFoundException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("pokemaze file", "*.pokemaze"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null)
        {
            File fileToLoad = new File(selectedFile.getPath());
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileToLoad));
            Object[] savedMazeDetails = (Object[])in.readObject();

            byte[] mazeByteArray = (byte[])savedMazeDetails[0];
            int characterPositionRow = (int)savedMazeDetails[1];
            int characterPositionColumn = (int)savedMazeDetails[2];

            m_myViewModel.loadMaze(mazeByteArray,characterPositionRow,characterPositionColumn);

            String charachterName = (String) savedMazeDetails[3];
//            String nameOfWall = (String) savedMazeDetails[4];
//            String nameOfPrize = (String) savedMazeDetails[5];
            if (charachterName.equals("resources" + File.separator + "Images" + File.separator + "Bulbasaur_GIF.gif"))
            {
                setBulbasaurCharacter();
            }
            else if(charachterName.equals("resources" + File.separator + "Images" + File.separator + "Charmander_GIF.gif"))
            {
                setCharmanderCharacter();
            }
            else if(charachterName.equals("resources" + File.separator + "Images" + File.separator + "Squirtle_GIF.gif"))
            {
                setSquirtleCharacter();
            }

            in.close();

            button_solveMaze.setDisable(false);
        }
        else
        {
            showAlert("Sorry, Load Has Been Failed!");
        }
    }

    /**
     * Open the Properties Stage
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void propertiesFromMenuBarPressed(ActionEvent actionEvent)
    {
        try
        {
            Stage stage = new Stage();
            stage.setTitle("PokeMaze-Properties Pane");
            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Properties.fxml").openStream());
            Scene scene = new Scene(root, 600, 400);
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            scene.getStylesheets().add(getClass().getResource("MyCSSPokeMaze.css").toExternalForm());
            stage.show();
        }
        catch (Exception e)
        {

        }
    }

    /**
     * Mute or UnMute the Background Music
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void musicButtonPressed(ActionEvent actionEvent)
    {
        if (m_isMusicOn == true)
        {
            m_mediaPlayer.pause();

            button_music.setText("UnMute");

            m_isMusicOn = false;
        }
        else
        {
            m_mediaPlayer.play();

            button_music.setText("Mute");

            m_isMusicOn = true;
        }
    }

    /**
     * Switch the Background Music to the Next Song
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void nextMusicButtonPressed(ActionEvent actionEvent)
    {
        if(m_music.equals("PokeMaze_music01"))
        {
            m_music = "PokeMaze_music02";
            m_mediaPlayer.stop();
//            playMusic(File.separator + "Music" + File.separator + "PokeMaze_music02.mp3", 200);
            playMusic("Music/PokeMaze_music02.mp3", 200);
        }
        else if(m_music.equals("PokeMaze_music02"))
        {
            m_music = "PokeMaze_music03";
            m_mediaPlayer.stop();
//            playMusic(File.separator + "Music" + File.separator + "PokeMaze_music03.mp3", 200);
            playMusic("Music/PokeMaze_music03.mp3", 200);
        }
        else if(m_music.equals("PokeMaze_music03"))
        {
            m_music = "PokeMaze_music01";
            m_mediaPlayer.stop();
//            playMusic(File.separator + "Music" + File.separator + "PokeMaze_music01.mp3", 200);
            playMusic("Music/PokeMaze_music01.mp3", 200);
        }
        m_isMusicOn = true;
        button_music.setText("Mute");
        lbl_musicSong.setText(m_music);
    }

    /**
     * Switch the Background Music to the Forward Song
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void forwardMusicButtonPressed(ActionEvent actionEvent)
    {
        if(m_music.equals("PokeMaze_music01"))
        {
            m_music = "PokeMaze_music03";
            m_mediaPlayer.stop();
//            playMusic(File.separator + "Music" + File.separator + "PokeMaze_music03.mp3", 200);
            playMusic("Music/PokeMaze_music03.mp3", 200);

        }
        else if(m_music.equals("PokeMaze_music02"))
        {
            m_music = "PokeMaze_music01";
            m_mediaPlayer.stop();
//            playMusic(File.separator + "Music" + File.separator + "PokeMaze_music01.mp3", 200);
            playMusic("Music/PokeMaze_music01.mp3", 200);
        }
        else if(m_music.equals("PokeMaze_music03"))
        {
            m_music = "PokeMaze_music02";
            m_mediaPlayer.stop();
//            playMusic(File.separator + "Music" + File.separator + "PokeMaze_music02.mp3", 200);
            playMusic("Music/PokeMaze_music02.mp3", 200);
        }
        m_isMusicOn = true;
        button_music.setText("Mute");
        lbl_musicSong.setText(m_music);
    }

    /**
     * Set Character - Bulbasaur
     */
    public void setBulbasaurCharacter()
    {
        m_characterDisplayer.setImageFileNameCharacter("resources" + File.separator + "Images" + File.separator + "Bulbasaur_GIF.gif");
        m_characterDisplayer.clearCharacterCanvas();
        m_characterDisplayer.redraw();
        radioButton_squirtle.setSelected(false);
        radioButton_charmander.setSelected(false);
        radioButton_bulbasaur.setSelected(true);
        moveFocosToMazeDisplayer();
    }

    /**
     * Set Character - Charmander
     */
    public void setCharmanderCharacter()
    {
        m_characterDisplayer.setImageFileNameCharacter("resources" + File.separator + "Images" + File.separator + "Charmander_GIF.gif");
        m_characterDisplayer.clearCharacterCanvas();
        m_characterDisplayer.redraw();
        radioButton_bulbasaur.setSelected(false);
        radioButton_squirtle.setSelected(false);
        radioButton_charmander.setSelected(true);
        moveFocosToMazeDisplayer();
    }

    /**
     * Set Character - Squirtle
     */
    public void setSquirtleCharacter()
    {
        m_characterDisplayer.setImageFileNameCharacter("resources" + File.separator + "Images" + File.separator + "Squirtle_GIF.gif");
        m_characterDisplayer.clearCharacterCanvas();
        m_characterDisplayer.redraw();
        radioButton_bulbasaur.setSelected(false);
        radioButton_charmander.setSelected(false);
        radioButton_squirtle.setSelected(true);
        moveFocosToMazeDisplayer();
    }

    /**
     * Return the focos to the Maze Displayer Pane
     * Happens After each Button Pressed
     */
    public void moveFocosToMazeDisplayer()
    {
        m_mazeDisplayer.requestFocus();
    }

    /**
     * Move Character method by Mouse - By using MyMyModel
     * @param mouseEvent - Mouse Dragging (MouseEvent type)
     */
    public void moveCharacterByMouse(MouseEvent mouseEvent)
    {
        if(m_mazeIsSolved == false)
        {
            m_myViewModel.moveCharacterByMouse(mouseEvent.getX(), mouseEvent.getY(), m_characterDisplayer.getM_cellWidthColX(), m_characterDisplayer.getM_cellHeightRowY());
            mouseEvent.consume();
        }
    }

    /**
     * Change to Panes Zoom according to the Zoom - In or Out
     * @param scrollEvent - Scrolling (ScrollEvent type)
     */
    public void zoomScroll(ScrollEvent scrollEvent)
    {
        if(scrollEvent.isControlDown())
        {
            double zoomFactor = 1.05;
            double deltaY = scrollEvent.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 2.0 - zoomFactor;
            }
            displayer_pane.setScaleX(displayer_pane.getScaleX() * zoomFactor);
            displayer_pane.setScaleY(displayer_pane.getScaleY() * zoomFactor);
            scrollEvent.consume();
        }
    }
}
