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
        Button button1 = new Button("Single Thread");
        button1.setTranslateX(100);
        Button button2 = new Button("Optimal threads");
        button2.setTranslateX(200);
        int numberOfRectangles = 3 + random.nextInt(8);
        //int numberOfRectangles = 5;
        Rectangle[] rectangles = new Rectangle[numberOfRectangles];
        button.setOnAction(event -> {                                       //creates new thread for each rectangle
            for (int i = 0; i < numberOfRectangles; i++) {
                rectangles[i] = rectangleCreation(root);
                startMoving(rectangles[i]).start();
            }
        });

        int processors = Runtime.getRuntime().availableProcessors();        //checking amount of processors
        System.out.println("amount of processors = " + processors);
        button1.setOnAction(event -> {                                      //creates array of rectangles and than new Thread to proceed moving

            for (int i = 0; i < numberOfRectangles; i++) {                    //starts moving all rectangles in one thread
                rectangles[i] = rectangleCreation(root);
            }
            startMoving(rectangles).start();

        });
        button2.setOnAction(event -> {                                      //creates array of rectangles and than new Thread to proceed moving

            final int newNumberOfRectangles = numberOfRectangles / processors;
            for (int j = 0; j < processors; j++) {
                if ((numberOfRectangles % 2 == 1) && (j == processors - 1)) {
                    Rectangle[] newRectangles;
                    newRectangles = new Rectangle[newNumberOfRectangles + 1];
                    for (int i = 0; i < newNumberOfRectangles + 1; i++) {
                        newRectangles[i] = rectangleCreation(root);
                    }
                    startMoving(newRectangles).start();
                } else {
                    Rectangle[] newRrectangles;
                    newRrectangles = new Rectangle[newNumberOfRectangles];
                    for (int i = 0; i < newNumberOfRectangles; i++) {
                        newRrectangles[i] = rectangleCreation(root);
                    }
                    startMoving(newRrectangles).start();
                }
            }
        });
        root.getChildren().addAll(button, button1, button2);
    }


    public Rectangle rectangleCreation(Pane root) {
        int height = 20 + random.nextInt(MAX_SQUARE_SIDE_SIZE);                         //setting random height of rectangle in range 20 - 200
        int width = 20 + random.nextInt(MAX_SQUARE_SIDE_SIZE);                          //setting random weight of rectangle in range 20 - 200
        double setX = random.nextInt((int) scene_width - width);                 // setting random X and Y in a scene
        double setY = random.nextInt((int) scene_hight - height);
        Rectangle rectangle = new Rectangle(width, height,                              //creating rectangle
                Color.color(random.nextDouble(), random.nextDouble(), random.nextDouble()));        //color randomization
        rectangle.setTranslateX(setX);                                                  //puts rectangle to x coordinate
        rectangle.setTranslateY(setY);
        root.getChildren().addAll(rectangle);
        return rectangle;
    }

    public Thread startMoving(Rectangle rectangle) {            //new thread that moves one rectangle
        Thread thread = new Thread(() -> {
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
        });
        return thread;
    }

    public Thread startMoving(Rectangle[] rectangle) {                 //overload to accept array of rectangles
        Thread thread = new Thread(() -> {
            Integer[] stepx = new Integer[rectangle.length];
            for (int i = 0; i < rectangle.length; i++) {

                if (random.nextBoolean() == true)
                    stepx[i] = 1;
                else stepx[i] = -1;
            }
            Integer[] stepy = new Integer[rectangle.length];
            for (int i = 0; i < rectangle.length; i++) {

                if (random.nextBoolean() == true)
                    stepy[i] = 1;
                else stepy[i] = -1;
            }
            while (!Thread.interrupted()) {

                for (int i = 0; i < rectangle.length; i++) {

/*                        for (int j = 0; j<rectangle.length;j++) {                                         //проверка на столкновение с другим прямоугольником
                            if ((rectangle[i].getTranslateY() == rectangle[j].getTranslateY() + rectangle[j].getHeight()) ||
                                    (rectangle[i].getTranslateY() + rectangle[i].getHeight() == rectangle[j].getTranslateY())) {
                                if ((rectangle[i].getTranslateX() > rectangle[j].getTranslateX()) &&
                                        (rectangle[i].getTranslateX() < rectangle[j].getTranslateX() + rectangle[j].getWidth()) ||
                                        rectangle[i].getTranslateX()+rectangle[i].getWidth()>rectangle[j].getTranslateX()&&
                                        rectangle[i].getTranslateX()+rectangle[i].getWidth() < rectangle[j].getTranslateX() + rectangle[j].getWidth()) {
                                    stepy[i] *= (-1);
                                    stepy[j] *= (-1);
                                }
                            }

                            if ((rectangle[i].getTranslateX() == rectangle[j].getTranslateX() + rectangle[j].getWidth()) ||
                                    (rectangle[i].getTranslateX() + rectangle[i].getWidth() == rectangle[j].getTranslateX())) {
                                if ((rectangle[i].getTranslateY() > rectangle[j].getTranslateY()) &&
                                        (rectangle[i].getTranslateY() < rectangle[j].getTranslateY() + rectangle[j].getHeight()) ||
                                        rectangle[i].getTranslateY()+rectangle[i].getHeight()>rectangle[j].getTranslateY()&&
                                        rectangle[i].getTranslateY()+rectangle[i].getHeight() < rectangle[j].getTranslateY() + rectangle[j].getHeight()) {
                                    stepx[i] *= (-1);
                                    stepx[j] *= (-1);
                                }
                            }
                        }*/

                    if ((rectangle[i].getTranslateX() == 0) || (rectangle[i].getTranslateX() == scene_width - rectangle[i].getWidth())) {
                        stepx[i] *= (-1);
                    }
                    if ((rectangle[i].getTranslateY() == 0) || (rectangle[i].getTranslateY() == scene_hight - rectangle[i].getHeight())) {
                        stepy[i] *= (-1);
                    }
                    final double y = rectangle[i].getTranslateY() + stepy[i];
                    final double x = rectangle[i].getTranslateX() + stepx[i];
                    int finalI = i;
                    Platform.runLater(() -> {
                        rectangle[finalI].setTranslateX(x);
                        rectangle[finalI].setTranslateY(y);
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        return thread;
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
