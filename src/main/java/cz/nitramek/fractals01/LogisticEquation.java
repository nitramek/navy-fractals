package cz.nitramek.fractals01;


import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j;

@Log4j
public class LogisticEquation implements Initializable {

    @FXML
    public Canvas canvas;
    @FXML
    public TextField drawFromIterationsField;

    @FXML
    public TextField iterationsField;

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
    public void drawRefurbication() {
        log.debug("#drawRefurbication");
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        prepareParameters();
        for (double r = rStart; r < rEnd; r += rStep) {
            double x = iterate(r);
            log.debug("Value" + x);
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
        int xAxis = (int) (((r - rStart)* xFactor));
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
