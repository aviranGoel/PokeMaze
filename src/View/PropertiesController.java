package View;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.input.InputMethodEvent;

import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Class - PropertiesController, implements Initializable class
 * Represents the PropertiesController class which responsible to the Properties of the Generate and Solve and Threads
 */
public class PropertiesController implements Initializable
{
    @FXML
    public RadioButton radioButton_bestFirstSearch;
    public RadioButton radioButton_depthFirstSearch;
    public RadioButton radioButton_breadthFirstSearch;
    public RadioButton radioButton_simpleMazeGenerator;
    public RadioButton radioButton_myMazeGenerator;
    public javafx.scene.control.TextField txt_TotalThreads;

    public String m_searchingAlgorithm;
    public String m_mazeGeneratingAlgorithm;
    public String m_toatalThreads;


    @Override
    /**
     * Override The Interface Initialize method - Showing the Current Properties
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources)
    {
        try (BufferedReader reader = new BufferedReader(new FileReader(new File("resources" + File.separator + "config.properties"))))
        {
            String line;
            reader.readLine();
            String[] propertiesValues = new String[3];
            int index = 0;
            while ((line = reader.readLine()) != null)
            {
                String[] lineSplit = line.split("=");
                propertiesValues[index] = lineSplit[1];
                index++;
            }
            m_searchingAlgorithm = propertiesValues[0];
            m_mazeGeneratingAlgorithm = propertiesValues[1];
            m_toatalThreads = propertiesValues[2];
//            System.out.println(m_searchingAlgorithm);
//            System.out.println(m_mazeGeneratingAlgorithm);
//            System.out.println(m_toatalThreads);
            selectedRadioButton();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Select and UuSelect Properties Radio Button
     */
    private void selectedRadioButton()
    {
        selectedSearchingAlgorithmRadioButton();
        selectedMazeGeneratingAlgorithmRadioButton();
        selectedToatalThreadsRadioButton();
    }

    /**
     * Select and UuSelect Searching Algorithm Radio Button
     */
    private void selectedSearchingAlgorithmRadioButton()
    {
        String propValue = m_searchingAlgorithm;
        if(propValue.equals("BestFirstSearch"))
        {
            radioButton_bestFirstSearch.setSelected(true);
            radioButton_depthFirstSearch.setSelected(false);
            radioButton_breadthFirstSearch.setSelected(false);
//            System.out.println("best");
        }
        else if(propValue.equals("DepthFirstSearch"))
        {
            radioButton_bestFirstSearch.setSelected(false);
            radioButton_depthFirstSearch.setSelected(true);
            radioButton_breadthFirstSearch.setSelected(false);
//            System.out.println("dfs");
        }
        else /*if(propValue.equals("BreadthFirstSearch"))*/
        {
            radioButton_bestFirstSearch.setSelected(false);
            radioButton_depthFirstSearch.setSelected(false);
            radioButton_breadthFirstSearch.setSelected(true);
//            System.out.println("bfs");
        }
    }

    /**
     * Select and UuSelect Maze Generating Radio Button
     */
    private void selectedMazeGeneratingAlgorithmRadioButton()
    {
        String propValue = m_mazeGeneratingAlgorithm;
        if(propValue.equals("SimpleMazeGenerator"))
        {
            radioButton_simpleMazeGenerator.setSelected(true);
            radioButton_myMazeGenerator.setSelected(false);
        }
        else /*if(propValue.equals("MyMazeGenerator"))*/
        {
            radioButton_simpleMazeGenerator.setSelected(false);
            radioButton_myMazeGenerator.setSelected(true);
        }
    }

    /**
     * Select and UuSelect Toatal Threads Radio Button
     */
    private void selectedToatalThreadsRadioButton()
    {
        txt_TotalThreads.setText(m_toatalThreads);
    }

    /**
     * UpdateBestFirstSearchProperty
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void updateBestFirstSearchProperty(ActionEvent actionEvent)
    {
        m_searchingAlgorithm = "BestFirstSearch";
        updateProperties();
        selectedSearchingAlgorithmRadioButton();
    }

    /**
     * UpdateDepthFirstSearchProperty
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void updateDepthFirstSearchProperty(ActionEvent actionEvent)
    {
        m_searchingAlgorithm = "DepthFirstSearch";
        updateProperties();
        selectedSearchingAlgorithmRadioButton();
    }

    /**
     * UpdateBreadthFirstSearchProperty
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void updateBreadthFirstSearchProperty(ActionEvent actionEvent)
    {
        m_searchingAlgorithm = "BreadthFirstSearch";
        updateProperties();
        selectedSearchingAlgorithmRadioButton();
    }

    /**
     * UpdateSimpleMazeGeneratorProperty
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void updateSimpleMazeGeneratorProperty(ActionEvent actionEvent)
    {
        m_mazeGeneratingAlgorithm = "SimpleMazeGenerator";
        updateProperties();
        selectedMazeGeneratingAlgorithmRadioButton();
    }

    /**
     * UpdateMyMazeGeneratorProperty
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void updateMyMazeGeneratorProperty(ActionEvent actionEvent)
    {
        m_mazeGeneratingAlgorithm = "MyMazeGenerator";
        updateProperties();
        selectedMazeGeneratingAlgorithmRadioButton();
    }

    /**
     * UpdateTotalThreadsProperty
     * @param actionEvent - Button Pressed (ActionEvent type)
     */
    public void updateTotalThreadsProperty(ActionEvent actionEvent)
    {
        m_toatalThreads = txt_TotalThreads.getText();
        updateProperties();
        selectedToatalThreadsRadioButton();
    }

    /**
     * Update All Properties
     */
    private void updateProperties()
    {
        // File.separator - Indicate to which side the separator turn - It is different in each operating system
        try (OutputStream output = new FileOutputStream("resources" + File.separator + "config.properties"))
        {
            Properties prop = new Properties();

            // Set the property value
            prop.setProperty("Searching Algorithm", m_searchingAlgorithm);
            prop.setProperty("Maze Generating Algorithm", m_mazeGeneratingAlgorithm);
            prop.setProperty("Toatal Threads", m_toatalThreads);

            // Save properties to project root folder
            prop.store(output, null);

            System.out.println(prop);
        }
        catch (IOException io)
        {
            io.printStackTrace();
        }
    }
}
