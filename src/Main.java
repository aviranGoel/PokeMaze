import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        MyModel m_myModel = new MyModel();
        m_myModel.startServers();
        MyViewModel m_MyViewModel = new MyViewModel(m_myModel);
        m_myModel.addObserver(m_MyViewModel);
        //--------------
        primaryStage.setTitle("PokeMaze");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("View/MyView.fxml").openStream());
        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(getClass().getResource("View/MyCSSPokeMaze.css").toExternalForm());
        primaryStage.setScene(scene);
        //--------------
        MyViewController m_myView = fxmlLoader.getController();
        m_myView.setResizeEvent(scene);
        m_myView.setViewModel(m_MyViewModel);
        m_MyViewModel.addObserver(m_myView);
        //--------------
        SetStageCloseEvent(primaryStage);
        primaryStage.show();
    }

    private void SetStageCloseEvent(Stage primaryStage)
    {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent windowEvent) {
//                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Are you sure you want to exit?");
//                Optional<ButtonType> result = alert.showAndWait();
//                if (result.get() == ButtonType.OK){
                    System.exit(0);
                    // ... user chose OK
                    // Close program
//                } else {
//                    // ... user chose CANCEL or closed the dialog
//                    windowEvent.consume();
//                }
            }
        });
    }


    public static void main(String[] args)
    {
        launch(args);
    }


//    public static void main(String[] args)
//    {
//        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
//        System.out.println(tempDirectoryPath);
//        String s = tempDirectoryPath + "SavedMaze\\";
//        System.out.println(s);
//
//    }

}
