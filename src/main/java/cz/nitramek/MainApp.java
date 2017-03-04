package cz.nitramek;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    public static void main(String[] args) throws Exception {
        launch(args);
    }


    public void start(Stage stage) throws Exception {

        log.info("Fractals");

        String fxmlFile = "/fxml/hello.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        //loader.setController(new MainController());
        Pane rootNode = loader.load();

        log.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode, 400, 400);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("Fractals");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }
}
