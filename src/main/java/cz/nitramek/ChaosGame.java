package cz.nitramek;


import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Data;

@Data
public class ChaosGame {
    private final GraphicsContext gc;
    private final int iterations;

    private Random random = new Random();
    private Vector trianglePoints[] = new Vector[3];

    public void start() {

        double x = gc.getCanvas().getWidth() / 2 + 10;
        double y = gc.getCanvas().getHeight() / 2 - 20;

        drawPoint(x, y);
        drawTriangle();
        Vector point = new BasicVector(new double[]{x, y});
        for (int i = 0; i < iterations; i++) {
            Vector chosenPoint = trianglePoints[random.nextInt(3)];
            Vector toPointVector = chosenPoint.subtract(point);
            Vector halfPoint = point.add(toPointVector.multiply(0.5));
            drawPoint(halfPoint.get(0), halfPoint.get(1));
            point = halfPoint;
        }
    }

    private void drawPoint(double x, double y) {
        gc.getPixelWriter().setColor((int) x, (int) y, Color.BLACK);
    }

    private void drawTriangle() {
        gc.moveTo(0, gc.getCanvas().getHeight() - 1);
        gc.beginPath();
        gc.lineTo(gc.getCanvas().getWidth(), gc.getCanvas().getHeight() - 1);
        gc.lineTo(gc.getCanvas().getWidth() / 2, 0);
        gc.lineTo(0, gc.getCanvas().getHeight() - 1);
        gc.stroke();
        trianglePoints[0] = new BasicVector(new double[]{gc.getCanvas().getWidth(), gc.getCanvas().getHeight() - 1});
        trianglePoints[1] = new BasicVector(new double[]{gc.getCanvas().getWidth() / 2, 0});
        trianglePoints[2] = new BasicVector(new double[]{0, gc.getCanvas().getHeight() - 1});
    }
}
