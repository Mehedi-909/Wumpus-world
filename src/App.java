import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.*;

public class App extends Application{

    private String path = "D:/6th Semester/JavaFx Projects/WumpusWorld/src/images/";
    ArrayList<String> input = new ArrayList<String>();
    private Image playerImage;
    Cave cave= new Cave();
    private int mouseX,mouseY;
    private int currentlySelectedTile = -1;
    Player player = new Player(cave);
    public int score = 0;
    public boolean goldFound = false;
    public boolean isWumpusDead = false;
    public boolean isAgentDead = false;
    int rPrev = 9, cPrev = 0, n = 10;
    int[][] isVisited = new int[10][10];
    Board [][] knowledgeBoard = new Board[10][10];
    Location cLocation;
    Location pLocation;
    int cRow,cCol,pRow,pCol;
    

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

        Button button = new Button();
      //Setting text to the button
        button.setText("Start Game");
        button.setLayoutX(650);
        button.setLayoutY(350);
        root.getChildren().add(button);
        button.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {

                Timer timer = new Timer();
                int begin = 0;
                int timeInterval = 2000;
                timer.schedule(new TimerTask() {
                    int counter = 0;
                    @Override
                    public void run() {
                        for(int i=0; i<10; i++){
                            for(int j=0; j<10; j++){
                                isVisited[i][j] = 0;
                            }
                        }
                        //call the method
                        AImove2();
                        counter++;
                        if (counter >= 1){
                            timer.cancel();
                        }
                    }
                }, begin, timeInterval);
                
                //System.out.println("Button pressed");
                //AImove();
            }

        });

        

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
        //AImove();

        
        
    }

    public void updateChaining(){

    }

    public void AImove2(){
            // Location curLocation = player.getPlayerLocation();
            // int currentRow = curLocation.getRow();
            // int currentColumn = curLocation.getCol();
            // int previousRow = currentRow;
            // int previousColumn =currentColumn;
            // player.moveRight();
            // boolean foundNewPath = false;

            while(goldFound == false && isAgentDead == false ){

                //pRow = player.getPlayerLocation().getRow();
                //pCol = player.getPlayerLocation().getCol();

                int hint = player.checkStatus();
                if(hint  == 0){
                    System.out.println("OK");
                    Random random = new Random();
                    int low = 1;
                    int high = 5;
                    int result = random.nextInt(high-low) + low;

                    if(result == 1){
                        
                        pRow = player.getPlayerLocation().getRow();
                        pCol = player.getPlayerLocation().getCol();
                        player.moveRight();
                        cRow = player.getPlayerLocation().getRow();
                        cCol = player.getPlayerLocation().getCol();
                         
                        checkTile(player.checkStatus());
                        // if(player.checkStatus() == 3){
                        //     goldFound = true;
                    //     System.out.println("Gold found");

                        // }
                    }
                    else if(result == 2){
                        pRow = player.getPlayerLocation().getRow();
                        pCol = player.getPlayerLocation().getCol();
                        player.moveLeft();
                        cRow = player.getPlayerLocation().getRow();
                        cCol = player.getPlayerLocation().getCol();
                        
                        checkTile(player.checkStatus());
            
                    }
                    else if(result == 3){
                        pRow = player.getPlayerLocation().getRow();
                        pCol = player.getPlayerLocation().getCol();
                        player.moveUp();
                        cRow = player.getPlayerLocation().getRow();
                        cCol = player.getPlayerLocation().getCol();
                        
                        checkTile(player.checkStatus());
                    }
                    else{
                        pRow = player.getPlayerLocation().getRow();
                        pCol = player.getPlayerLocation().getCol();
                        player.moveDown();
                        cRow = player.getPlayerLocation().getRow();
                        cCol = player.getPlayerLocation().getCol();
                        
                        checkTile(player.checkStatus());
                    }
                    
                    score = score - 1000;
                }


                else if(hint  == 1){
                    System.out.println("PIT, Game Over");
                    isAgentDead = true;
                    score = score - 1000;
                }
                else if(hint  == 2){
                    System.out.println("Wumpus, Game Over");
                    // AudioClip scream = new AudioClip(this.getClass().getResource("scream.wav").toString());
                    // scream.play();
                    String musicFile = "D:/6th Semester/JavaFx Projects/WumpusWorld/src/images/scream.wav";     // For example
        
                    Media sound = new Media(new File(musicFile).toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(sound);
                    mediaPlayer.play();
                    isAgentDead = true;
                    score = score - 1000;
                }
                else if(hint  == 3){
                    System.out.println("Gold found");
                    goldFound = true;
                }
                else if(hint  == 10){
                    System.out.println("Breeze");
                    //cRow = player.getPlayerLocation().getRow();
                    //cCol = player.getPlayerLocation().getCol(); 
                    
                    player.move(new Location(pRow, pCol));
                    //upper right
                    if(pRow > cRow && cRow >= pCol && cCol < pCol){
                        knowledgeBoard[cRow][pCol].tileID = 1;

                    }
                    else if(pRow > cRow && cRow >= pCol && cCol > pCol){
                        knowledgeBoard[cRow][pCol].tileID = 1;

                    }
                    else if(pRow < cRow && cRow >= pCol && cCol > pCol){
                        knowledgeBoard[cRow][pCol].tileID = 1;
                        
                    }
                    else if(pRow < cRow && cRow >= pCol && cCol < pCol){
                        knowledgeBoard[cRow][pCol].tileID = 1;
                        
                    }
                    else if(pRow > cRow && cRow < pCol && cCol < pCol){
                        knowledgeBoard[pRow][cCol].tileID = 1;
                        
                    }
                    else if(pRow < cRow && cRow < pCol && cCol < pCol){
                        knowledgeBoard[pRow][cCol].tileID = 1;
                        
                    }
                    else if(pRow < cRow && cRow < pCol && cCol > pCol){
                        knowledgeBoard[pRow][cCol].tileID = 1;
                        
                    }
                    else if(pRow > cRow && cRow < pCol && cCol > pCol){
                        knowledgeBoard[pRow][cCol].tileID = 1;
                        
                    }
                    // else {
                    //     if(cave.isValid(new Location(pRow, cCol))){

                    //         try {
                    //             knowledgeBoard[pRow][cCol].tileID = 1;
                    //         } catch (NullPointerException e) {
                    //             //TODO: handle exception
                    //             System.out.print("NullPointerException Caught");
                    //         }
                            

                    //     }
                        
                    // }

                }
                else if(hint  == 20){
                    System.out.println("Stench");
                    cRow = player.getPlayerLocation().getRow();
                    cCol = player.getPlayerLocation().getCol(); 
                    
                    player.move(new Location(pRow, pCol));
                    //upper right
                    if(pRow > cRow && cRow >= pCol && cCol < pCol){
                        knowledgeBoard[cRow][pCol].tileID = 2;

                    }
                    else if(pRow > cRow && cRow >= pCol && cCol > pCol){
                        knowledgeBoard[cRow][pCol].tileID = 2;

                    }
                    else if(pRow < cRow && cRow >= pCol && cCol > pCol){
                        knowledgeBoard[cRow][pCol].tileID = 2;
                        
                    }
                    else if(pRow < cRow && cRow >= pCol && cCol < pCol){
                        knowledgeBoard[cRow][pCol].tileID = 2;
                        
                    }

                    else if(pRow > cRow && cRow < pCol && cCol < pCol){
                        knowledgeBoard[pRow][cCol].tileID = 2;
                        
                    }
                    else if(pRow < cRow && cRow < pCol && cCol < pCol){
                        knowledgeBoard[pRow][cCol].tileID = 2;
                        
                    }
                    else if(pRow < cRow && cRow < pCol && cCol > pCol){
                        knowledgeBoard[pRow][cCol].tileID = 2;
                        
                    }
                    else if(pRow > cRow && cRow < pCol && cCol > pCol){
                        knowledgeBoard[pRow][cCol].tileID = 2;
                        
                    }
                    // else {
                    //     if(cave.isValid(new Location(pRow, cCol))){
                    //         knowledgeBoard[pRow][cCol].tileID = 2;

                    //     }
                        
                    // }
                }
                else if(hint  == 30){
                    System.out.println("Glitter");
                }

            }

    }

    public void AImove(){
        int count = 0;

        while(goldFound == false && isAgentDead == false ){
            int hint = player.checkStatus();
            Location curLocation = player.getPlayerLocation();
            int currentRow = curLocation.getRow();
            int currentColumn = curLocation.getCol();

            int r = currentRow;
            int c = currentColumn;
            boolean foundNewPath = false;


            if(r >= 1 && !((r-1) == rPrev && c == cPrev) && (cave.getTileStatus(r-1, c) ==0 || cave.getTileStatus(r-1, c) ==3 ) && isVisited[r-1][c] ==0 ) {
                System.out.println("1");
   			 rPrev = r;
   			 cPrev = c;
   			 
   			 r--;
   			 foundNewPath = true;
                Location newLocation = new Location(r, c);
                isVisited[r][c] =1;
                
                player.move(newLocation);
                System.out.println(r + " " + c);
                //player.moveRight();
                checkTile(player.checkStatus());
                count++;
   		    }

            else if(r <= (n-2) && !((r+1) == rPrev && c == cPrev) && (cave.getTileStatus(r+1, c) ==0 || cave.getTileStatus(r+1, c) ==3 ) && isVisited[r+1][c] ==0) {
                System.out.println("2");
   			 rPrev = r;
   			 cPrev = c;
   			 
   			 r++;
   			 foundNewPath = true;
                Location newLocation = new Location(r, c);
                isVisited[r][c] =1;
                player.move(newLocation);
                System.out.println(r + " " + c);
                checkTile(player.checkStatus());
                count++;
   		 }
   		 else if(c >= 1 && !(r == rPrev && (c-1) == cPrev) && (cave.getTileStatus(r, c-1) ==0 || cave.getTileStatus(r, c-1) ==3 ) && isVisited[r][c-1] ==0 ) {
            System.out.println("3");
   			 rPrev = r;
   			 cPrev = c;
   			 
   			 c--;
   			 foundNewPath = true;
                Location newLocation = new Location(r, c);
                isVisited[r][c] =1;
                player.move(newLocation);
                System.out.println(r + " " + c);
                checkTile(player.checkStatus());
                count++;
   		 }
   		 else if(c <= (n-2) && !(r == rPrev && (c+1) == cPrev) && (cave.getTileStatus(r, c+1) ==0 || cave.getTileStatus(r, c+1) ==3 )  && isVisited[r][c+1] ==0) {
            System.out.println("4");
   			 rPrev = r;
   			 cPrev = c;
   			 
   			 c++;
   			 foundNewPath = true;
                Location newLocation = new Location(r, c);
                isVisited[r][c] =1;
                player.move(newLocation);
                System.out.println(r + " " + c);
                checkTile(player.checkStatus());
                count++;
   		 }
   		 
   		 if(!foundNewPath) {
            System.out.println("5");
   			 int temp1 = rPrev;
   			 int temp2 = cPrev;
   			 
   			 rPrev = r;
   			 cPrev = c;
   			 
   			 r = temp1;
   			 c = temp2;
                Location newLocation = new Location(r, c);
                isVisited[r][c] =1;
                player.move(newLocation);
                checkTile(player.checkStatus());
                count++;
   		 }

            // Random random = new Random();
            // int low = 1;
            // int high = 5;
            // int result = random.nextInt(high-low) + low;

            // if(result == 1){
            //     player.moveRight();
            //     checkTile(player.checkStatus());
            //     // if(player.checkStatus() == 3){
            //     //     goldFound = true;
            // //     System.out.println("Gold found");

            //     // }
            // }
            // else if(result == 2){
            //     player.moveLeft();
            //     checkTile(player.checkStatus());
            
            // }
            // else if(result == 3){
            //     player.moveUp();
            //     checkTile(player.checkStatus());
            // }
            // else{
            //     player.moveDown();
            //     checkTile(player.checkStatus());
            // }


        }

        

        

    }

    public void checkTile(int hint){

        if(hint  == 0){
            System.out.println("OK");
            score = score - 1000;
        }
        else if(hint  == 1){
            System.out.println("PIT, Game Over");
            isAgentDead = true;
            score = score - 1000;
        }
        else if(hint  == 2){
            System.out.println("Wumpus, Game Over");
            // AudioClip scream = new AudioClip(this.getClass().getResource("scream.wav").toString());
            // scream.play();
            String musicFile = "D:/6th Semester/JavaFx Projects/WumpusWorld/src/images/scream.wav";     // For example

            Media sound = new Media(new File(musicFile).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
            isAgentDead = true;
            score = score - 1000;
        }
        else if(hint  == 3){
            System.out.println("Gold found");
            goldFound = true;
        }
        else if(hint  == 10){
            System.out.println("Breeze");
        }
        else if(hint  == 20){
            System.out.println("Stench");
        }
        else if(hint  == 30){
            System.out.println("Glitter");
        }

    }

    public void processInput(){
        for(int i=0;i <input.size(); i++){
            if(input.get(i).equals("RIGHT")){
                player.moveRight();
                player.checkStatus();
                input.remove(i);
                i--;
            }
            else if(input.get(i).equals("LEFT")){
                player.moveLeft();
                player.checkStatus();
                input.remove(i);
                i--;
            }
            else if(input.get(i).equals("UP")){
                player.moveUp();
                player.checkStatus();
                input.remove(i);
                i--;
            }
            else if(input.get(i).equals("DOWN")){
                player.moveDown();
                player.checkStatus();
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

    public void checkBreeze(int r, int c){
        if(cave.getTileStatus(r, c) == 10){
            if(r == 0 && c==0){
                knowledgeBoard[r+1][c].tileID = 1;
                knowledgeBoard[r][c+1].tileID = 1;

            }
            else if(r == 0 && c==9){
                knowledgeBoard[r+1][c].tileID = 1;
                knowledgeBoard[r][c-1].tileID = 1;

            }
            else if(r == 9 && c==0){
                knowledgeBoard[r][c+1].tileID = 1;
                knowledgeBoard[r-1][c].tileID = 1;

            }
            else if(r == 9 && c==9){
                knowledgeBoard[r][c-1].tileID = 1;
                knowledgeBoard[r-1][c].tileID = 1;

            }
            else if(r == 0){
                
                knowledgeBoard[r][c+1].tileID = 1;
                knowledgeBoard[r][c-1].tileID = 1;
                knowledgeBoard[r+1][c].tileID = 1;

            }
            else if(r == 9){
                knowledgeBoard[r][c+1].tileID = 1;
                knowledgeBoard[r][c-1].tileID = 1;
                knowledgeBoard[r-1][c].tileID = 1;

            }
            else if(c==0){
                knowledgeBoard[r-1][c].tileID = 1;
                knowledgeBoard[r][c+1].tileID = 1;
                knowledgeBoard[r+1][c].tileID = 1;

            }
            else if(c==9){
                knowledgeBoard[r-1][c].tileID = 1;
                knowledgeBoard[r+1][c].tileID = 1;
                knowledgeBoard[r][c-1].tileID = 1;

            }

            else {
                knowledgeBoard[r-1][c].tileID = 1;
                knowledgeBoard[r+1][c].tileID = 1;
                knowledgeBoard[r][c-1].tileID = 1;
                knowledgeBoard[r][c+1].tileID = 1;

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
