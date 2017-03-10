package cz.nitramek.fractals01;


import org.la4j.Matrix;
import org.la4j.Vector;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.vector.dense.BasicVector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Point3D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import lombok.Data;

import static java.util.stream.Collectors.toList;

@Data
public class IFSDeterministic {
    public static final int SCALE = 20;
    private final GraphicsContext gc;
    private final TransformData[] data;
    private Color fractalColor = Color.BLACK;
    private Random random = new Random();
    private final int iterations;
    private List<Vector> points;


    public void start() {
        //triangle points
        points = new ArrayList<>();
        addTrianglePoints();
        Matrix transformMatrix = new Basic2DMatrix(2, 2);
        Vector translationVector = new BasicVector(new double[]{0, 0});
        for (int i = 0; i < iterations; i++) {
            List<Vector> iterationPoints = new ArrayList<>(3);
            for (TransformData transformData : data) {
                transformData.fillTransformation(transformMatrix, translationVector);
                translationVector.set(0, translationVector.get(0) * gc.getCanvas().getWidth());
                translationVector.set(1, translationVector.get(1) * gc.getCanvas().getHeight());
                List<Vector> transformedPoints = points.stream()
                        .map(transformMatrix::multiply)
                        .map(v -> v.add(translationVector))
                        .collect(toList());
                iterationPoints.addAll(transformedPoints);
            }
            points.clear();
            points.addAll(iterationPoints);
        }
        double[] xs = new double[3];
        double[] ys = new double[3];
        for (int i = 0; i < points.size(); i += 3) {
            Vector first = points.get(i);
            Vector second = points.get(i + 1);
            Vector third = points.get(i + 2);
            xs[0] = first.get(0);
            xs[1] = second.get(0);
            xs[2] = third.get(0);

            ys[0] = first.get(1);
            ys[1] = second.get(1);
            ys[2] = third.get(1);

            gc.strokePolygon(xs, ys, 3);
        }
        gc.getCanvas().setScaleX(1);
        gc.getCanvas().setScaleY(1);
        Affine transform = new Affine();
        transform.appendRotation(180, new Point3D(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2,0), new Point3D(0,0,1));
        transform.appendRotation(180, new Point3D(gc.getCanvas().getWidth() / 2, gc.getCanvas().getHeight() / 2,0), new Point3D(0,1,0));
        gc.getCanvas().getTransforms().add(transform);

    }

    private void addTrianglePoints() {
        points.add(new BasicVector(new double[]{0, gc.getCanvas().getHeight()}));
        points.add(new BasicVector(new double[]{gc.getCanvas().getWidth(), gc.getCanvas().getHeight()}));
        points.add(new BasicVector(new double[]{gc.getCanvas().getWidth() / 2.0, 0}));
    }
}

