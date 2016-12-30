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
import sample.entities.VertexLine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application {

    private int counter = 0;
    private int lineCounter = 0;
    private double radius = 20.0;
    private List<Vertex> vertices = new ArrayList<>();
    private Set<VertexLine> lines = new HashSet<>();
    private Button createVertex = new Button("Добавить вершину");
    private Button createLine = new Button("Добавить связь");
    private Button replaceVertex = new Button("Переместить вершины");
    private Button calculate = new Button("Посчитать!");
    private StackPane firstVertex = null;
    private StackPane secondVertex = null;

    private void init(Stage primaryStage) {
        Group root = new Group();
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, getSampleWidth(),getSampleHeight()));
        ToolBar toolbar = new ToolBar();
        toolbar.getItems().add(createVertex);
        toolbar.getItems().add(createLine);
        toolbar.getItems().add(replaceVertex);
        toolbar.getItems().add(calculate);

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

        calculate.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Controller controller = new Controller(lines);
                controller.makeScheme();
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
        vertices.add(new Vertex(counter++,x-radius,y-radius, layout));

        //TODO replace vertecies

        replaceVertex.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                layout.setOnMouseDragged(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        layout.setLayoutX(event.getSceneX()-radius);
                        layout.setLayoutY(event.getSceneY()-radius);
                        vertices.forEach(vertex -> {
                            if (layout.getChildren().get(0).equals(vertex.layout)){
                                vertex.x=event.getSceneX();
                                vertex.y=event.getSceneY();
                            }
                        });
                    }
                });
            }
        });

        layout.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (lineCounter == 0){
                    firstVertex = layout;
                    lineCounter++;
                    circle.setFill(Color.LIME);
                } else if (lineCounter == 1) {
                    secondVertex = layout;
                    lineCounter = 0;
                    circle.setFill(Color.SEAGREEN);
                    VertexLine vertexLine = new VertexLine(searchVertex(firstVertex), searchVertex(secondVertex));
                    if (!isContainVertexLine(vertexLine, lines)) {
                        lines.add(vertexLine);

                        Line line = new Line();
                        line.setStartX(firstVertex.getLayoutX() + radius);
                        line.setStartY(firstVertex.getLayoutY() + radius);
                        line.setEndX(secondVertex.getLayoutX() + radius);
                        line.setEndY(secondVertex.getLayoutY() + radius);

                        root.getChildren().add(line);
                    }
                    vertices.forEach(vertex -> {
                        vertex.circle.setFill(Color.WHITE);
                        vertex.layout.toFront();
                    });
                    firstVertex = null;
                    secondVertex = null;
                }
            }
        });
        layout.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                layout.toFront();
            }
        });
    }


    @Override public void start(Stage primaryStage) throws Exception {
        init(primaryStage);
        primaryStage.show();
    }
    public static void main(String[] args) { launch(args); }

    private Vertex searchVertex(StackPane layout){
        return vertices.stream().filter(vertex -> vertex.layout.equals(layout)).findFirst().get();
    }

    private boolean isContainVertexLine(VertexLine vertexLine, Set<VertexLine> lines){
        for (VertexLine line:lines) {
            if (line.first.equals(vertexLine.first) && line.second.equals(vertexLine.second)){
                return true;
            }
        }
        return false;
    }
}

