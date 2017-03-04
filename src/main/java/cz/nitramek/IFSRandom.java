package cz.nitramek;


import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Data;

@Data
public class IFSRandom {
    private final GraphicsContext gc;
    private final TransformData[] data;
    private Color fractalColor = Color.BLACK;
    private Random random = new Random();
    private final int iterations;


    public void start() {
        Random random = new Random();
        int x = random.nextInt((int) gc.getCanvas().getWidth());
        int y = random.nextInt((int) gc.getCanvas().getHeight());
        gc.save();
        Vector point = new BasicVector(new double[]{x, y});
        Matrix transformMatrix = new Basic2DMatrix(2,2);
        Vector translation = new BasicVector(2);
        drawPixel(x, y);
        for (int i = 0; i < iterations; i++) {
            TransformData transformData = pickData();
            fillTransformation(transformMatrix, translation, transformData);
            point = loop(point, transformMatrix, translation);
        }
        gc.restore();
    }

    private void fillTransformation(Matrix transformMatrix, Vector translation, TransformData transformData) {
        transformMatrix.set(0, 0, transformData.getA());
        transformMatrix.set(0, 1, transformData.getB());
        transformMatrix.set(1, 0, transformData.getC());
        transformMatrix.set(1, 1, transformData.getD());
        translation.set(0, transformData.getE());
        translation.set(1, transformData.getF());
    }

    private void drawPixel(int x, int y) {
        gc.getPixelWriter().setColor(x, y, fractalColor);
    }

    private Vector loop(Vector pointBefore, Matrix transformMatrix, Vector translation) {
        Vector newPoint = transformMatrix.multiply(pointBefore).add(translation);
        drawPixel(newPoint.get(0), newPoint.get(1));
        return newPoint;
    }

    private void drawPixel(double x, double y) {
        drawPixel((int) x, (int) y);
    }

    private TransformData pickData() {
        double p = random.nextDouble();
        double sumP = 0;
        for (TransformData aData : data) {
            sumP += aData.getP();
            if (p <= sumP) {
                return aData;
            }
        }
        throw new RuntimeException("Probability wasnt sum of one?");
    }


}
