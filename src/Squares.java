import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.Random;

public class Squares extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static final int MAX_SQUARE_SIDE_SIZE = 200;
    private static final int PANE_ROOT_HEIGHT = 600;
    private static final int PANE_ROOT_WIDTH = 800;
    private static double scene_hight;
    private static double scene_width;
    Random random = new Random();


    public void buttonMultyTreads(Pane root) {
        Button button = new Button("Multy Threads");
        int numberOfRectangles = 3 + random.nextInt(8);
        button.setOnAction(event -> {
            for (int i = 0; i < numberOfRectangles; i++) {
                int height = 20 + random.nextInt(MAX_SQUARE_SIDE_SIZE);
                int width = 20 + random.nextInt(MAX_SQUARE_SIDE_SIZE);
                int setX = random.nextInt(PANE_ROOT_WIDTH - 2 * width);
                int setY = random.nextInt(PANE_ROOT_HEIGHT - 2 * height);
                Rectangle rectangle = new Rectangle(width, height,
                        Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));
                rectangle.setTranslateX(setX);
                rectangle.setTranslateY(setY);
                startMooving(rectangle);
                root.getChildren().addAll(rectangle);
            }
        });
        root.getChildren().addAll(button);
    }

    public void startMooving(Rectangle rectangle) {
        new Thread(() -> {
            int stepx;
            if (random.nextBoolean() == true)
                stepx = 1;
            else stepx = -1;
            int stepy;
            if (random.nextBoolean() == true)
                stepy = 1;
            else stepy = -1;
            while (true) {
                if ((rectangle.getTranslateX() == 0) || (rectangle.getTranslateX() == scene_width - rectangle.getWidth())) {
                    stepx = stepx * -1;
                }
                if ((rectangle.getTranslateY() == 0) || (rectangle.getTranslateY() == scene_hight - rectangle.getHeight())) {
                    stepy = stepy * -1;
                }
                final double y = rectangle.getTranslateY() + stepy;
                final double x = rectangle.getTranslateX() + stepx;
                Platform.runLater(() -> {
                    rectangle.setTranslateX(x);
                    rectangle.setTranslateY(y);
                });
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setHeight(PANE_ROOT_HEIGHT);
        primaryStage.setWidth(PANE_ROOT_WIDTH);
        primaryStage.setTitle("squares");
        primaryStage.show();
        buttonMultyTreads(root);
        scene_hight = scene.getHeight();
        scene_width = scene.getWidth();
    }
}
