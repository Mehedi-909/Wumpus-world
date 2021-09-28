import java.awt.Color;
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.*;

public class App extends Application{

    private String path = "D:/6th Semester/JavaFx Projects/WumpusWorld/src/images/";
    private Image playerImage;
    Cave cave= new Cave();
    

    @Override
    public void start(Stage stage) throws Exception {
        
        stage.setTitle("Wumpus world");
        FileInputStream inputStream = new FileInputStream(path + "boy.jpg");
        playerImage = new Image(inputStream,100,100,false,false);

        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        Canvas canvas = new Canvas(800,600);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.drawImage(playerImage, 10, 10);
        //gc.fillRect(10, 10, 60, 60);
        

        stage.show();

        // FileInputStream inputStream = new FileInputStream(path + "player.png");
        // playerImage = new Image(inputStream);

        // stage.setTitle("Wumpus world");
        // StackPane layout = new StackPane();
        // Scene scene = new Scene(layout, 800, 600);
        // stage.setScene(scene);

        // Canvas c = new Canvas(800,600);
        // GraphicsContext gc = c.getGraphicsContext2D();
        // gc.drawImage(playerImage, 400, 400);

        // stage.show();
        
        
    }

    
    public static void main(String[] args) throws Exception {
        launch(args);
    }


    
}
