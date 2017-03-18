package cz.nitramek.chaos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);
    private Stage stage;

    public static void main(String[] args) throws Exception {
        launch(args);
    }


    public void start(Stage stage) throws Exception {

        log.info("Fractals");

        GridPane gp = new GridPane();
        Button logistic = new Button("Logistic");
        logistic.setOnAction(e -> showLogistic());
        Button lozi = new Button("Lozi");
        lozi.setOnAction(e -> showLozi());
        gp.addRow(0, logistic, lozi);
        Scene scene = new Scene(gp);
        stage.setTitle("Chaos");
        stage.setScene(scene);
        this.stage = stage;
        this.stage.show();

    }
    public void showLogistic(){
        openFromFXML("/fxml/logistic.fxml", "Logistic");
        this.stage.close();
    }
    public void showLozi(){
        openFromFXML("/fxml/lozi.fxml", "Lozi");
        this.stage.close();
    }

    private void showLogistic(Stage stage) throws java.io.IOException {


    }
    public static void openFromFXML(String fxmlFile, String windowName){
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
        Parent rootNode = null;
        try {
            rootNode = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add("/styles/styles.css");
        Stage stage = new Stage();
        stage.setTitle(windowName);
        stage.setScene(scene);
        stage.show();
    }
}
