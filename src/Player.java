import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
public class Player {
    private String path = "D:/6th Semester/JavaFx Projects/WumpusWorld/src/images/";
    private Location location;
    private Image playerImage;
    private Cave cave;
    public int score = 0;
    

    public Player(Cave cave){
        this.cave = cave;
        location = new Location(9, 0);

        try {
            FileInputStream inputStream = new FileInputStream(path + "boy4.png");
            playerImage = new Image(inputStream,50,50,false,false);
            
            
            
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
    }

    public void draw(GraphicsContext gc){
        gc.drawImage(playerImage, location.getCol()*50 + Cave.xOffset, location.getRow()*50 + Cave.yOffset);
    }

    public void moveRight(){
        Location rLocation = new Location(location.getRow(), location.getCol() + 1);

        if(cave.isValid(rLocation)){
            location.setCol(location.getCol() + 1);

        }
        
    }
    public void moveLeft(){
        Location lLocation = new Location(location.getRow(), location.getCol() - 1);

        if(cave.isValid(lLocation)){
            location.setCol(location.getCol() - 1);

        }
        
    }
    public void moveUp(){
        Location uLocation = new Location(location.getRow()-1, location.getCol());
        //checking bound
        if(cave.isValid(uLocation)){
            location.setRow(location.getRow() - 1);

        }
        
    }
    public void moveDown(){

        Location dLocation = new Location(location.getRow()+1, location.getCol());

        if(cave.isValid(dLocation)){
            location.setRow(location.getRow() + 1);

        }
        
    }

    public int checkStatus(){
        
        int row = location.getRow();
        int col = location.getCol();
        int hint = cave.getTileStatus(row, col);
        // if(hint  == 0){
        //     System.out.println("OK");
        //     score = score - 1;
        // }
        // else if(hint  == 1){
        //     System.out.println("PIT");
        //     score = score - 1000;
        // }
        // else if(hint  == 2){
        //     System.out.println("Wumpus");
        //     score = score - 1000;
        // }
        // else if(hint  == 3){
        //     System.out.println("Gold");
        // }
        // else if(hint  == 10){
        //     System.out.println("Breeze");
        // }
        // else if(hint  == 20){
        //     System.out.println("Stench");
        // }
        // else if(hint  == 30){
        //     System.out.println("Glitter");
        // }

        return hint;

    }
    
}
