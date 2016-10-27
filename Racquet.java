
package pong1;

import java.awt.Color;
import java.awt.Graphics;
import static pong1.Pong1.pong;

/**
 *
 * @author RizelleAnn 
 * got help from a video tutorial
 */
public class Racquet {
   
    int x, y, width, height;  
    int racquetNumber;
    int score;
    
    
    public Racquet(Pong1 pong, int racquetNumber){
        this.racquetNumber = racquetNumber; 
        this.width = 50;
        this. height = 200;
        
        if(racquetNumber == 1) {
            //places in the left most side
            this.x = 0;
        } else if(racquetNumber == 2){
            //para adto sya sa pikas side
            this.x = pong.width - width; 
        }
        
        // para ma center sya each side in the y-axis
        this.y = (pong.height / 2) - (height / 2);
        
                
    }
    // renders the racquet
    void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }
    // movement of the racquets
    void move(boolean up) {
        int speed = 20;
        if(up) {
            if((y - speed) > 0) {
                y -= speed;
            } else {
                y = 0;
            }
        } else {
            if(y + height + speed < pong.height){
                y += speed;
            } else {
                y = Pong1.pong.height - height;
            }
        }
    }
}
