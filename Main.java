package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import sample.entities.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    private int counter = 0;
    private double radius = 20.0;
    private List<Vertex> vertices = new ArrayList<>();

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, getSampleWidth(),getSampleHeight()));
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().add(new Button("Добавить вершину"));
        toolbar.getItems().add(new Button("Options"));
        toolbar.getItems().add(new Button("Help"));

        toolbar.getItems().get(2).setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                vertices.forEach(vertex -> System.out.println("Vertex " + vertex.getId() + " x=" + vertex.x + " y=" + vertex.y));
            }
        });

        final Rectangle rectangle = RectangleBuilder.create()
                .width(getSampleWidth()).height(getSampleHeight())
                .fill(Color.rgb(156,216,255))
                .build();

        root.getChildren().add(rectangle);

        rectangle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.out.println("X = " + event.getSceneX() + " Y = " + event.getSceneY());
                addVertex(root, event.getSceneX(), event.getSceneY(), (Button) toolbar.getItems().get(1));
            }
        });

        root.getChildren().add(toolbar);
    }

    public double getSampleWidth() { return 600; }

    public double getSampleHeight() { return 400; }

    public void addVertex(Group root, double x, double y, Button button){
        Circle circle = new Circle(radius, Color.WHITE);
        circle.setCenterX(x);
        circle.setCenterY(y);
        Text text = new Text(String.valueOf(counter));
        text.setStyle(
                "-fx-font-family: \"Times New Roman\";" +
                        "-fx-font-size: 24px;"
        );
        StackPane layout = new StackPane();
        layout.getChildren().addAll(
                circle,
                text
        );
        layout.setLayoutX(x-radius);
        layout.setLayoutY(y-radius);
        root.getChildren().add(layout);
        vertices.add(new Vertex(counter++,x-radius,y-radius, circle));
        layout.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                layout.setLayoutX(event.getSceneX()-radius);
                layout.setLayoutY(event.getSceneY()-radius);
                vertices.forEach(vertex -> {
                    if (layout.getChildren().get(0).equals(vertex.circle)){
                        vertex.x=event.getSceneX();
                        vertex.y=event.getSceneY();
                    }
                });
            }
        });
        circle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Line line = new Line();
                line.setStartX(circle.getCenterX());
                line.setStartY(circle.getCenterY());
                line.setEndX(circle.getCenterX());
                line.setEndY(circle.getCenterY());
                root.getChildren().add(line);


                circle.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        line.setEndX(event.getSceneX());
                        line.setEndY(event.getSceneY());
                    }
                });
            }
        });
    }


    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }
}
