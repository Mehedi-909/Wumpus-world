import java.io.FileNotFoundException;
import java.io.FileInputStream;
import javafx.scene.image.Image;

public class Cave {

    private int tiles[][];
    private boolean visible[][];
    public static final int GROUND = 0, PIT=1, WUMPUS=2, GOLD=3, WIND=10, STENCH=20, GLITTER=30;
    private String path = "D:/6th Semester/JavaFx Projects/WumpusWorld/src/images/";
    private Image groundImage;

    public Cave(){
        tiles = new int[10][10];
        visible = new boolean[10][10];
        try {
            FileInputStream inputStream = new FileInputStream(path + "player.png");
            groundImage = new Image(inputStream);
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }
        

    }
    
}
