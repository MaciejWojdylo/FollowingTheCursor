package com.example.followcursor;

import javafx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MeduzaAnimation extends Application {

    private static final double WIDTH = 800;
    private static final double HEIGHT = 600;
    private Group meduza;
    private double targetX = 400;
    private double targetY = 300;
    private static final double MIN_DISTANCE = 50;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Rectangle background = new Rectangle(WIDTH, HEIGHT, Color.LIGHTBLUE);
        meduza = createMeduza();
        meduza.setLayoutX(targetX);
        meduza.setLayoutY(targetY);
        root.getChildren().addAll(background, meduza);
        root.setOnMouseMoved(event -> {
            targetX = event.getX();
            targetY = event.getY();
        });
        animateMeduza();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        primaryStage.setTitle("Medusa Follow Cursor");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Group createMeduza() {
        Group meduza = new Group();
        Arc head = new Arc(50, 50, 50, 40, 0, 180);
        head.setFill(Color.PINK);
        head.setStroke(Color.BLACK);
        head.setStrokeWidth(1);
        Path[] tentacles = new Path[8];
        for (int i = 0; i < tentacles.length; i++) {
            tentacles[i] = new Path();
            tentacles[i].setStroke(Color.LAVENDER);
            tentacles[i].setStrokeWidth(2);
            double angle = Math.toRadians(180.0 / (tentacles.length - 1) * i);
            double startX = 50 + Math.cos(angle) * 50;
            double startY = 50 + Math.sin(angle) * 40;

            tentacles[i].getElements().add(new MoveTo(startX, startY));
            tentacles[i].getElements().add(new QuadCurveTo(
                    startX, startY + 50,
                    startX + Math.sin(angle) * 20, startY + 80));

            meduza.getChildren().add(tentacles[i]);
        }

        meduza.getChildren().add(head);
        return meduza;
    }

    private void animateMeduza() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), _ -> {
            double dx = targetX - meduza.getLayoutX();
            double dy = targetY - meduza.getLayoutY();
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance > MIN_DISTANCE) {
                double step = 5;
                meduza.setLayoutX(meduza.getLayoutX() + dx / distance * step);
                meduza.setLayoutY(meduza.getLayoutY() + dy / distance * step);
            }
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
