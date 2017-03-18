package cz.nitramek.chaos;


import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.extern.log4j.Log4j;

import static java.lang.Math.abs;
import static java.lang.String.format;

@Log4j
public class LoziAttractor implements Initializable {

    @FXML
    public TextField alfaField;
    @FXML
    public TextField betaField;
    @FXML
    public TextField iterationsField;

    @FXML
    public Canvas canvas;

    private GraphicsContext gc;

    @Parseable(parsingField = "alfaField")
    private double alfa;
    @Parseable(parsingField = "betaField")
    private double beta;
    @Parseable(parsingField = "iterationsField")
    private int iterations;

    private static final double X_MAX = 1.5;
    private static final double X_MIN = -1.5;
    private static final double X_WIDTH = X_MAX - X_MIN;

    private static final double Y_MAX = 1.5;
    private static final double Y_MIN = -1.5;
    private static final double Y_WIDTH = Y_MAX - Y_MIN;

    private double xFactor;
    private double yFactor;

    public void drawRefurbication() {
        log.info("Clicked #drawRefurbication");
        parseParameters();
        clearCanvas();
        double x = 0;
        double y = 0;
        xFactor = canvas.getWidth() / X_WIDTH;
        yFactor = canvas.getHeight() / Y_WIDTH;
        for (int i = 0; i < iterations; i++) {
            drawPixel(x, y);
            double xOld = x;
            x = 1 - alfa * abs(x) + beta * y;
            y = xOld;
        }
    }

    private void drawPixel(double x, double y) {
        log.info(format("[%s, %s]", x, y));
        int xAxis = (int) ((x - X_MIN) * xFactor);
        int yAxis = (int) ((y - Y_MIN) * yFactor);
        gc.getPixelWriter().setColor(xAxis, yAxis, Color.BLACK);
    }

    private void clearCanvas() {
        this.gc.setFill(Color.WHITE);
        this.gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        this.gc.setFill(Color.BLACK);
    }

    private void parseParameters() {
        Arrays.stream(LoziAttractor.class.getDeclaredFields())
                .peek(f -> f.setAccessible(true))
                .filter(f -> f.getAnnotation(Parseable.class) != null)
                .forEach(field -> {
                    Parseable parseable = field.getAnnotation(Parseable.class);
                    Class<?> type = field.getType();
                    String fieldName = parseable.parsingField();
                    try {
                        Field textFieldField = this.getClass().getField(fieldName);
                        TextField tf = (TextField) textFieldField.get(this);
                        String textValue = tf.getText();
                        if (type.equals(Integer.TYPE)) {
                            field.setInt(this, Integer.parseInt(textValue));
                        } else if (type.equals(Double.TYPE)) {
                            field.setDouble(this, Double.parseDouble(textValue));
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gc = canvas.getGraphicsContext2D();
        this.gc.setFill(Color.BLACK);
    }
}



