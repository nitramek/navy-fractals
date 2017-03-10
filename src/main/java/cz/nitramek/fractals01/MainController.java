package cz.nitramek.fractals01;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class MainController implements Initializable {


    @FXML
    public TableView<TransformData> table;

    private ObservableList<TransformData> data = FXCollections.observableArrayList();

    @FXML
    private TextField importPath;

    @FXML
    private TextField iterationCount;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.debug("Init");
        List<String> cellNames = Arrays.asList("a", "b", "c", "d", "e", "f", "p");
        for (String cellName : cellNames) {
            try {
                Method method = TransformData.class.getMethod("set" + cellName.toUpperCase(), Double.TYPE);
                TableColumn<TransformData, Double> column = new TableColumn<>(cellName);
                column.setMinWidth(75);
                column.setEditable(true);
                column.setCellValueFactory(new PropertyValueFactory<>(cellName));
                column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

                column.setOnEditCommit(e -> {
                    try {
                        method.invoke(e.getRowValue(), e.getNewValue());
                    } catch (IllegalAccessException | InvocationTargetException e1) {
                        e1.printStackTrace();
                    }
                });
                table.getColumns().add(column);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        data = FXCollections.observableArrayList();
        table.setItems(data);
    }

    public void addRow() {
        data.add(new TransformData());
    }


    public void importData() throws IOException, NoSuchMethodException, InvocationTargetException,
            IllegalAccessException {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("fractal", "*.fractal"));
        fc.setInitialDirectory(Paths.get("").toAbsolutePath().toFile());
        File file = fc.showOpenDialog(null);

        if (file != null) {
            List<String> lines = Files.readAllLines(file.toPath());
            data.clear();
            for (String line : lines) {
                TransformData transformData = new TransformData();
                String[] splitted = line.split("[; ]");
                List<String> cellNames = Arrays.asList("a", "b", "c", "d", "e", "f", "p");
                for (int i = 0; i < splitted.length; i++) {
                    String input = splitted[i];
                    String cellName = cellNames.get(i);
                    Method method = TransformData.class.getMethod("set" + cellName.toUpperCase(), Double.TYPE);
                    method.invoke(transformData, Double.parseDouble(input));
                }
                data.add(transformData);
            }
        }
    }

    public void startIFSRandom(ActionEvent actionEvent) {
        Stage stage1 = new Stage();
        stage1.setTitle("IFS stochastic");
        Canvas canvas1 = new Canvas(800, 600);
        Parent parent1 = new Pane(canvas1);
        stage1.setScene(new Scene(parent1, 800, 600));
        int iterations1 = Integer.valueOf(iterationCount.getText());
        IFSRandom ifsRandom = new IFSRandom(canvas1.getGraphicsContext2D(), data.toArray(new TransformData[data.size()]), iterations1);

        ifsRandom.start();
        stage1.show();
    }

    public void startIFSDeterministic(){
        Stage stage = new Stage();
        stage.setTitle("IFS deterministic");
        Canvas canvas = new Canvas(800, 600);
        Parent parent = new Pane(canvas);
        stage.setScene(new Scene(parent, 800, 600));
        int iterations = Integer.valueOf(iterationCount.getText());
        IFSDeterministic ifsDeterministic = new IFSDeterministic(canvas.getGraphicsContext2D(), data.toArray(new TransformData[data.size()]), iterations);
        ifsDeterministic.start();
        stage.show();
    }

    public void startChaosGame(ActionEvent actionEvent) {
        Stage stage = new Stage();
        stage.setTitle("Fractal");
        Canvas canvas = new Canvas(800, 600);
        Parent parent = new Pane(canvas);
        stage.setScene(new Scene(parent, 800, 600));
        int iterations = Integer.valueOf(iterationCount.getText());
        ChaosGame cg = new ChaosGame(canvas.getGraphicsContext2D(), iterations);
        cg.start();
        stage.show();
    }

    public void startMandelbrot(ActionEvent actionEvent) {
        Mandelbrot mandelbrot = new Mandelbrot();
        mandelbrot.draw();

    }
}


