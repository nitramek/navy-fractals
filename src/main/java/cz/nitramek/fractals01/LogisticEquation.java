package cz.nitramek.fractals01;


import com.carrotsearch.hppc.DoubleArrayList;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.DoubleStream;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;

import static java.lang.String.format;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@Log4j
public class LogisticEquation implements Initializable {

    @FXML
    public Canvas canvas;
    @FXML
    public TextField drawFromIterationsField;

    @FXML
    public TextField iterationsField;

    @FXML
    public TextField intervalsField;

    @FXML
    private TextField rStartField;

    @FXML
    private TextField rEndField;

    @FXML
    private TextField rStepField;

    private GraphicsContext gc;

    private int iterations = 1100;
    private int treshold = iterations - 100;
    private double xFactor;
    private int yFactor;
    private double rStart;
    private double rEnd;
    private double rStep;

    @FXML
    private TextField categoriesCount;

    private DoubleArrayList xValues = new DoubleArrayList(iterations);


    @FXML
    public void drawRefurbication() {
        log.debug("#drawRefurbication");
        xValues.clear();
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        prepareParameters();
        for (double r = rStart; r < rEnd; r += rStep) {
            double x = iterate(r);
            xValues.add(x);
        }
    }

    @FXML
    public void showHistogram() {
        log.info("showHistogram clicked");
        if (!xValues.isEmpty()) {
            @SuppressWarnings("OptionalGetWithoutIsPresent")
            double max = DoubleStream.of(xValues.buffer)
                    .limit(xValues.elementsCount)
                    .max().getAsDouble();
            int intervalsCount = Integer.parseInt(intervalsField.getText());
            double intervalLength = max / intervalsCount;
            log.info("We have" + xValues.size() + " elements");
            Map<Integer, Long> data = DoubleStream.of(xValues.buffer)
                    .limit(xValues.size())
                    .mapToObj(x -> {
                        for (int i = 0; i < intervalsCount; i++) {
                            double intervalStart = i * intervalLength;
                            double intervalEnd = (i + 1) * intervalLength;
                            if (x >= intervalStart && x < intervalEnd) {
                                return i;
                            }
                        }
                        return (int) intervalLength - 1;
                    }).collect(groupingBy(Function.identity(), counting()));
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            data.forEach((intervalIndex, count) -> {
                double intervalStart = intervalIndex * intervalLength;
                double intervalEnd = (intervalIndex + 1) * intervalLength;
                series.getData().add(new XYChart.Data<>(format("<%.2f, %.3f)", intervalStart, intervalEnd), count));
            });

            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            final BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
            bc.getData().add(series);
            bc.setMinWidth(800);
            bc.setMinHeight(640);
            bc.setTitle("Histogram");
            Pane pane = new Pane(bc);
            Scene scene = new Scene(pane, 800, 640);
            xAxis.setLabel("Value");
            yAxis.setLabel("Hits");
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Logistic histogram");
            stage.show();

        }
    }

    private void prepareParameters() {
        rStart = Double.parseDouble(this.rStartField.getText());
        rEnd = Double.parseDouble(this.rEndField.getText());
        rStep = Double.parseDouble(this.rStepField.getText());
        iterations = Integer.parseInt(this.iterationsField.getText());
        treshold = Integer.parseInt(this.drawFromIterationsField.getText());
        yFactor = (int) canvas.getHeight();
        xFactor = (canvas.getWidth() / (rEnd - rStart));

    }

    private void drawPoint(double r, double x) {
        int xAxis = (int) (((r - rStart) * xFactor));
        int yAxis = (int) (yFactor - x * yFactor);
        gc.getPixelWriter().setColor(xAxis, yAxis, Color.BLACK);
    }

    double iterate(double r) {
        double x = 0.5;
        for (int i = 0; i < iterations; i++) {
            x = solve(r, x);
            if (i > treshold) {
                drawPoint(r, x);
            }
        }
        return x;
    }

    double solve(double r, double x) {
        return r * x * (1 - x);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("LogisticEquation#initialize");
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
    }
}
