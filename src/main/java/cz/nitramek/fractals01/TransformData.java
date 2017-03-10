package cz.nitramek.fractals01;

import org.la4j.Matrix;
import org.la4j.Vector;

import lombok.Data;

@Data
public class TransformData {
    private double a, b, c, d, e, f, p;

    public void fillTransformation(Matrix transformMatrix, Vector translation) {
        transformMatrix.set(0, 0, getA());
        transformMatrix.set(0, 1, getB());
        transformMatrix.set(1, 0, getC());
        transformMatrix.set(1, 1, getD());
        translation.set(0, getE());
        translation.set(1, getF());
    }

}
