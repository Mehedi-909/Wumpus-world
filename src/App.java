import java.awt.Color;
import java.io.FileInputStream;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.*;

public class App extends Application{

    private String path = "D:/6th Semester/JavaFx Projects/WumpusWorld/src/images/";
    ArrayList<String> input = new ArrayList<String>();
    private Image playerImage;
    Cave cave= new Cave();
    private int mouseX,mouseY;
    private int currentlySelectedTile = -1;
    Player player = new Player(cave);
    

    @Override
    public void start(Stage stage) throws Exception {
        
        stage.setTitle("Wumpus world");
        FileInputStream inputStream = new FileInputStream(path + "boy4.png");
        playerImage = new Image(inputStream,50,50,false,false);

        Group root = new Group();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        Canvas canvas = new Canvas(800,600);
        root.getChildren().add(canvas);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent event) {
                String code =event.getCode().toString();
                if(!input.contains(code)){
                    input.add(code);
                }
                
            }
            
        });

        scene.setOnMouseClicked(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
               mouseX = (int)event.getX();
               mouseY = (int)event.getY();

               if(mouseX >= 650 && mouseX <= 700 && mouseY >=80 && mouseY <=130){
                   currentlySelectedTile = cave.PIT;

               }
               if(mouseX >= 650 && mouseX <= 700 && mouseY >=130 && mouseY <=180){
                   currentlySelectedTile = cave.GOLD;

               }
               if(mouseX >= 650 && mouseX <= 700 && mouseY >=180 && mouseY <=230){
                currentlySelectedTile = cave.WUMPUS;

               }
               if(mouseX >= 650 && mouseX <= 700 && mouseY >=230 && mouseY <=280){
                currentlySelectedTile = cave.GROUND;

               }

               if(currentlySelectedTile != -1){
                Location clickLocation = convertClickToLocation(mouseX, mouseY);
                if(cave.isValid(clickLocation)){
                    cave.setTile(clickLocation, currentlySelectedTile);
                    currentlySelectedTile = -1;

                }
                

               }
               
                
            }
            
        });

        scene.setOnMouseMoved(new EventHandler<MouseEvent>(){

            @Override
            public void handle(MouseEvent event) {
                mouseX = (int)event.getX();
                mouseY = (int)event.getY();
                
            }
            
        });

        new AnimationTimer(){

            @Override
            public void handle(long now) {

                //gc.setFill(Color.LIGHT_GRAY);
                gc.fillRect(0, 0, 800, 600);

                processInput();
                cave.draw(gc);
                drawToolBar(gc);
                player.draw(gc);
                //gc.drawImage(playerImage, 10, 10);
                
            }
            
        }.start();;
        
        stage.show();

        
        
    }

    public void processInput(){
        for(int i=0;i <input.size(); i++){
            if(input.get(i).equals("RIGHT")){
                player.moveRight();
                input.remove(i);
                i--;
            }
            else if(input.get(i).equals("LEFT")){
                player.moveLeft();
                input.remove(i);
                i--;
            }
            else if(input.get(i).equals("UP")){
                player.moveUp();
                input.remove(i);
                i--;
            }
            else if(input.get(i).equals("DOWN")){
                player.moveDown();
                input.remove(i);
                i--;
            }
            else {
                
                input.remove(i);
                i--;
            }
        }
    }

    public void drawToolBar(GraphicsContext gc){
        gc.fillText("Toolbar", 650, 60);
        gc.drawImage(cave.getPitImage(), 650, 80);
        gc.drawImage(cave.getGoldImage(), 650, 130);
        gc.drawImage(cave.getWumpusImage(), 650, 180);
        gc.drawImage(cave.getGroundImage(), 650, 230);

        if(currentlySelectedTile != -1){

            if(currentlySelectedTile == cave.PIT){
                gc.drawImage(cave.getPitImage(), mouseX-25, mouseY-25);
            }
            else if(currentlySelectedTile == cave.WUMPUS){
                gc.drawImage(cave.getWumpusImage(), mouseX-25, mouseY-25);
            }
            else if(currentlySelectedTile == cave.GOLD){
                gc.drawImage(cave.getGoldImage(), mouseX-25, mouseY-25);
            }
            else if(currentlySelectedTile == cave.GROUND){
                gc.drawImage(cave.getGroundImage(), mouseX-25, mouseY-25);
            }
            
        }
        

    }

    public Location convertClickToLocation(int x, int y){
        int row = (y-Cave.xOffset)/50;
        int col = (x-Cave.yOffset)/50;
        Location location = new Location(row, col);
        return location;
    }

    
    public static void main(String[] args) throws Exception {
        launch(args);
    }


    
}
