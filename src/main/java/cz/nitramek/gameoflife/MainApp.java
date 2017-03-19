package cz.nitramek.gameoflife;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j;

import static java.lang.String.format;

@Log4j
public class MainApp extends Application {

    public static final int WIDTH = 800;
    private Canvas canvas;
    private GraphicsContext gc;
    private GameOfLife gameOfLife;
    private int rectSize;
    private int cellsRowCount;

    public static void main(String[] args) throws Exception {
        launch(args);
    }


    public void start(Stage stage) throws Exception {
        canvas = new Canvas(800, 800);
        cellsRowCount = 100;//100 cells
        rectSize = WIDTH / cellsRowCount; // pixels is on screen size
        gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.setStroke(Color.BLACK);
        Pane root = new Pane(canvas);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnMouseClicked(this::onMouseClicked);
        startGame();
        stage.setScene(scene);
        stage.show();
    }

    private void onMouseClicked(MouseEvent mouseEvent) {
        log.debug(format("Clicked [%s, %s]", mouseEvent.getX(), mouseEvent.getY()));
        int x = ((int) mouseEvent.getX()) / rectSize;
        int y = ((int) mouseEvent.getY()) / rectSize;
        log.debug(format("Clicked [%s, %s]", x, y));
        gameOfLife.switchState(x, y);
        drawForPosition(x, y);

    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case SPACE:
                gameOfLife.advance();
                drawOnCanvas();
                break;
            case R:
                startGame();
                break;
            case S:
                save();
                break;
            case L:
                load();
                break;
        }
    }

    private void load() {
        Stage ownerWindow = new Stage();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open game of life save");
        fileChooser.setInitialDirectory(Paths.get("").toAbsolutePath().toFile());
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Game of life file", "*.gol"));

        File file = fileChooser.showOpenDialog(ownerWindow);
        Path path = Paths.get(file.toURI());
        try {
            int[] savedState = Files.lines(path)
                    .flatMapToInt(line -> line.chars().map(ch -> ch - '0'))
                    .toArray();
            gameOfLife = new GameOfLife(savedState);
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawOnCanvas();
    }

    private void save() {
        try (BufferedWriter br = new BufferedWriter(new FileWriter("saved.gol"))) {
            for (int y = 0; y < cellsRowCount; y++) {
                for (int x = 0; x < cellsRowCount; x++) {
                    br.append((char) (gameOfLife.getState(x, y) + '0'));
                }
                br.append("\n");
            }
            br.flush();
        } catch (IOException e) {
            log.error("Error write", e);
        }
    }

    private void initGame() {
        gameOfLife = new GameOfLife(cellsRowCount);
    }

    public void startGame() {
        initGame();
        drawOnCanvas();
    }

    public void drawOnCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int y = 0; y < cellsRowCount; y++) {
            for (int x = 0; x < cellsRowCount; x++) {
                drawForPosition(x, y);
            }
        }
    }

    private void drawForPosition(int x, int y) {
        int leftTopX = x * rectSize;
        int leftTopY = y * rectSize;
        if (gameOfLife.getState(x, y) == 1) {
            gc.fillRect(leftTopX, leftTopY, rectSize, rectSize);
        } else {
            gc.clearRect(leftTopX, leftTopY, rectSize, rectSize);
        }
        gc.strokeRect(leftTopX, leftTopY, rectSize, rectSize);
    }
}
