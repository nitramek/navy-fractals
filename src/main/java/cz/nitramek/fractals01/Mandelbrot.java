package cz.nitramek.fractals01;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Mandelbrot {


    public static final int SIZE = 800;
    public static final double SCALE = 4.0 / SIZE; //scale na pravy roh 4,4,
    private final static int interations = 5000;
    private final Canvas canvas;
    private final Stage stage;
    private final Color outColor = Color.rgb(255, 0, 0);
    private final Color inColor = Color.rgb(0, 0, 0);
    private Point2D translation = new Point2D(0, 0);
    private Point2D scale = new Point2D(1, 1);

    public Mandelbrot() {
        stage = new Stage();
        stage.setTitle("Mandelbrot");
        canvas = new Canvas(SIZE, SIZE);
        Parent parent = new Pane(canvas);
        Scene scene = new Scene(parent, SIZE, SIZE);
        scene.setOnKeyPressed(event -> {
            System.out.println(event);
            switch (event.getCode()) {
                case UP:
                    translation = new Point2D(translation.getX(), translation.getY() + 0.1);
                    draw();
                    break;
                case DOWN:
                    translation = new Point2D(translation.getX(), translation.getY() - 0.1);
                    draw();
                    break;
                case LEFT:
                    translation = new Point2D(translation.getX() - 0.1, translation.getY());
                    draw();
                    break;
                case RIGHT:
                    translation = new Point2D(translation.getX() + 0.1, translation.getY());
                    draw();
                    break;
                case SUBTRACT:
                    scale = new Point2D(scale.getX() * 1.1, scale.getY() * 1.1);
                    draw();
                    break;
                case ADD:
                    scale = new Point2D(scale.getX() * 0.9, scale.getY() * 0.9);
                    draw();
                    break;
                case S:
                    saveToFile("png");
                    break;
                case P:
                    saveToFile("jpg");
                    break;
                case B:
                    saveToFile("bmp");
                    break;

            }
        });
        stage.setScene(scene);

    }

    private void saveToFile(String format) {
        WritableImage wi = new WritableImage(SIZE, SIZE);
        canvas.snapshot(null, wi);

        BufferedImage bi = SwingFXUtils.fromFXImage(wi, null);
        Path root = Paths.get("");
        try {
            File output = new File(root.toAbsolutePath().toFile(), "mandelbrot." + format);
            System.out.println("Saving " + format + " to " + output);
            ImageIO.write(bi, format, output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void draw() {
        double fallOffMatrix[] = new double[(SIZE + 1) * (SIZE + 1)];

        IntStream.rangeClosed(0, SIZE)
                .forEach(x -> IntStream.rangeClosed(0, SIZE).parallel().forEach(y -> {
                    Point2D converted = convertCoords(new Point2D(x, y));
                    int fallOff = fallOff(converted);
                    int normalizedFallof = (int) (fallOff * (1.0 / interations));

                    fallOffMatrix[x * (SIZE + 1) + y] = normalizedFallof;
                }));
        for (int i = 0; i <= SIZE; i++) {
            for (int j = 0; j <= SIZE; j++) {
                Color mixed = inColor.interpolate(outColor, fallOffMatrix[i * (SIZE + 1) + j]);
                canvas.getGraphicsContext2D().getPixelWriter().setColor(i, j, mixed);
            }
        }
        stage.show();
    }


    int fallOff(Point2D point) {
        int i = 0;
        double c_re = point.getX();
        double c_im = point.getY();
        double x = 0, y = 0;
        while (x * x + y * y < 4 && i < interations) {
            double x_new = x * x - y * y + c_re;
            y = 2 * x * y + c_im;
            x = x_new;
            i++;
        }
        return interations - (interations - i);
    }

    public Point2D convertCoords(Point2D orig) {
        Point2D p = orig.multiply(SCALE).add(-2, -2);
        double x = p.getX();
        double y = p.getY() * -1;

        x += translation.getX();
        y += translation.getY();

        x *= scale.getX();
        y *= scale.getY();
        return new Point2D(x, y);
    }

}
