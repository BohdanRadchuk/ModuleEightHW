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
    Random random = new Random();


    public void buttonMultyTreads(Pane root){
        Button button = new Button("Multy Threads");
        root.getChildren().addAll(button);
        int numberOfRectangles = 3 + random.nextInt(8);
        Thread[] threads = new Thread[numberOfRectangles];
        button.setOnAction(event->{
            for (int i = 0; i < numberOfRectangles ; i++) {
                int height = 20 + random.nextInt(MAX_SQUARE_SIDE_SIZE);
                int width = 20 + random.nextInt(MAX_SQUARE_SIDE_SIZE);
                int setX = random.nextInt(PANE_ROOT_WIDTH - 2 * width);
                int setY = random.nextInt(PANE_ROOT_HEIGHT - 2 * height);

                Rectangle rectangle = new Rectangle(width, height,
                        Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));

                rectangle.setTranslateX(setX);
                rectangle.setTranslateY(setY);
                root.getChildren().addAll(rectangle);



                threads[i] = new Thread(() -> {
                    while (true) {
                        final double x = rectangle.getX() + 1;
                        Platform.runLater(() -> {

                            rectangle.setTranslateX(x);
                        });
                        try {
                            Thread.sleep(2500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
                threads[i].start();
            }


                //new Thread(Platform.runLater()rectangleDraws(root)).start();
        /*    for (int i = 0; i < 3 + random.nextInt(8); i++){
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        rectangleDraws(root);
                    }
                };
                new Thread(runnable).start();
            }*/
        });

    }

/*
    public Rectangle rectangleDraws(Pane root, int width, int height, int setX, int setY){



        rectangle = new Rectangle(width, height,
                Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));

        rectangle.setTranslateX(setX);
        rectangle.setTranslateY(setY);
        root.getChildren().addAll(rectangle);
        return rectangle;
    }
*/
    public void rectangleMoove(Rectangle rectangle){
        double rectx = rectangle.getX()+1;
        double recty = rectangle.getY()+1;
        rectangle.setTranslateX(rectx);
        rectangle.setTranslateY(recty);

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



    }
}
