package cz.nitramek.fractals01;


import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import java.util.Random;

import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
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
            transformData.fillTransformation(transformMatrix, translation);
            point = loop(point, transformMatrix, translation);
        }
        gc.restore();
        Canvas canvas = gc.getCanvas();
        Affine transform = new Affine();
        transform.appendRotation(180, new Point3D(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2,0), new Point3D(0,0,1));
        transform.appendRotation(180, new Point3D(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2,0), new Point3D(0,1,0));
        gc.getCanvas().getTransforms().add(transform);
    }
    private void drawPixel(int x, int y) {
        gc.getPixelWriter().setColor(x, y, fractalColor);
    }

    private Vector loop(Vector pointBefore, Matrix transformMatrix, Vector translation) {
        translation.set(0, translation.get(0) * gc.getCanvas().getWidth());
        translation.set(1, translation.get(1) * gc.getCanvas().getHeight());
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
