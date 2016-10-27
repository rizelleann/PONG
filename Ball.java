
package pong1;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Random;

/**
 *
 * @author RizelleAnn
 * got help from a video tutorial
 */
public class Ball {
    private static final int DIAMETER = 25;
    int x, y;
    int motionX, motionY;
    Random random = new Random();
    private Pong1 pong;
    int hits;
    public Ball(Pong1 pong){
        this.pong = pong;
        //this.random = new Random();
        spawn();
     }
    //any update about the ball
     public void update(Racquet racquet1, Racquet racquet2) {
        int speed = 5;
        this.x += motionX * speed * 2;
        this.y += motionY * speed * 2;
        
        if(this.y + DIAMETER > pong.height || this.y <= 0) {
            if (this.motionY < 0) {
                //this.y = 0;
                this.motionY = random.nextInt(4);
            } else {
                //this.y = pong.height;
                this.motionY = -random.nextInt(4);
            }
        }
        if(checkCollision(racquet1) == 1) {
            this.motionX = 1 + (hits / 3);// every 3 hits, increase speed of ball
            //either ball go up and down
            this.motionY = -2 + random.nextInt(4);
            if(motionY == 0) {
                motionY = 1;
            }
            hits++;
        }
        else if(checkCollision(racquet2) == 1) {
            this.motionX = -1 - (hits/3);
            //either ball go up and down
            this.motionY = -2 + random.nextInt(4);
            if(motionY == 0) {
                motionY = 1;
            }
            hits++; 
            
        }
        if(checkCollision(racquet1) == 2) {
           racquet2.score++;
           spawn();
        }
        else if(checkCollision(racquet2) == 2) {
            racquet1.score++;
           spawn();
        }
    }
    //if the ball doesn't collide with either of the racquets and goes out of bounds in y
    // the ball should reappear at the center to start again the game
    public void spawn() {
        this.hits = 0;
        this.x = pong.width/2 - DIAMETER / 2;
        this.y = pong.height/2 - DIAMETER/2;
        this.motionY = -2 + random.nextInt(4);
        if(motionY == 0) {
           motionY = 1;
        }
        if(random.nextBoolean()){
            motionX = 1;
        } else { 
            motionX = -1;
        }  
    }
     
    public int checkCollision(Racquet racquet){
        
        if(this.x < racquet.x + racquet.width && this.x + DIAMETER > racquet.x && this.y < racquet.y + racquet.height && this.y + DIAMETER > racquet.y){
            return 1;//bounce
        } else if((racquet.x > x -DIAMETER && racquet.racquetNumber == 1) || (racquet.x < x - DIAMETER && racquet.racquetNumber == 2)){
            return 2;//hit/score   
        }

       return 0;//nothing
    }
    
      
    public void render(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillOval(x, y, DIAMETER, DIAMETER);
    } 

}
