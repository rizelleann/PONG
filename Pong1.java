
package pong1;

/**
 *
 * @author RizelleAnn
 */

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Pong1 implements ActionListener, KeyListener {
    
    static Pong1 pong;
    int width = 700;
    int height = 700;
    Renderer renderer;
    Racquet player1;
    Racquet player2; 
    Ball ball;
    boolean comp = false, difficulty;
    boolean s, w, up, down;
    int gameStatus; //1 = paused, 0 = menu, 2 = play  3 = gameOver
    int scoreLimit = 3, winner, compMoves, compCoolDown = 0, compDifficulty;
    String win;
    
    public Pong1(){
        Timer timer = new Timer(20, this);
        JFrame jFrame = new JFrame("Pong");
        renderer = new Renderer();
        jFrame.setSize(width +15, height + 35);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setVisible(true); 
        jFrame.add(renderer); 
        jFrame.addKeyListener(this);
        timer.start(); 
    }
    //starts the game
    public void start(){
        gameStatus = 2;
        player1 = new Racquet(this, 1);
        player2 = new Racquet(this, 2);
        ball = new Ball(this);    
    }
   
    public void render(Graphics2D g) {
        //sets background color of the pane
        g.setColor(Color.BLACK);
        //fills the whole rectangle with black
        g.fillRect(0, 0, width, height);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // status of the game is the title and the menu
        if(gameStatus == 0) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("PONG", width / 2 - 110, 150);
            if(!difficulty) {
                g.setFont(new Font("Arial", 1, 30));
                g.drawString("Press SPACE  for 2 Players", width / 2 - 200, height / 2 - 25);
                g.drawString("Press SHIFT to play with Computer", width / 2 - 200, height / 2 + 25);
                if(scoreLimit == 1){
                    g.drawString("Score Limit: " + scoreLimit + " >>", width / 2 - 200, height / 2 + 75);
                } else {
                    g.drawString("Score Limit: << " + scoreLimit + " >>", width / 2 - 200, height / 2 + 75);
                }
            }
            else if(difficulty) {
                String string = compDifficulty == 1 ? "EASY" : (compDifficulty == 2 ? "MEDIUM" : "HARD");
                g.setFont(new Font("Arial", 1, 30));
            
                g.drawString("Difficulty: << "+ string + " >>", width / 2 - 200, height / 2 - 25);
                g.drawString("Press SPACE to Play", width / 2 - 200, height / 2 + 25);
            }
        }
        // is performed when the user decides to play(2) or the game is paused(1)
        if(gameStatus == 1 || gameStatus == 2) {
            if(gameStatus == 1) {
                g.setFont(new Font("Arial", 1, 12));
                g.drawString("press SPACE to PAUSE or PLAY game", 10,20);
                g.setColor(Color.WHITE);
                g.setFont(new Font("Ariel", 1, 30));
                g.drawString("PAUSED", width / 2 - 70, height / 2 - 25);
            }
           
            //drawing of the line at the center
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(5f));
            g.drawLine(width / 2, 0, width / 2, height);
           
            //drawing of the circle at the center
            g.setStroke(new BasicStroke(2f));
            g.drawOval(width / 2 - 150, height / 2 - 150,  300, 300);
            
            //pause or play
            g.setFont(new Font("Arial", 1, 12));
            g.drawString("press SPACE to PAUSE or PLAY game", 10,20);
            //displays the scores of each player
            g.setFont(new Font("Arial", 1, 50));
            g.drawString(String.valueOf(player1.score), width / 2 - 70, 50);//score for player1
            g.drawString(String.valueOf(player2.score), width / 2 + 45, 50);//score for player2
            
            player1.render(g);//displays the racquet of player 1 (left)
            player2.render(g);//displays the racquet of player 2 (right)
            ball.render(g);// displays the ball
           
        }
        if(gameStatus == 3) {
            //declares winner
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString("PONG", width / 2 - 110, 150);
            /*if(!comp) {
                 g.drawString("Player " + winner + " Wins!", width / 2 - 200, 250);
                 
            } else {
                 g.drawString("You Lose!", width / 2 - 200, 250);
            }*/
            g.setFont(new Font("Arial", 1, 30));
            g.drawString("Press SPACE to Play Again", width / 2 - 200, height / 2 - 25);
            g.drawString("Press ESC to return to Menu", width / 2 - 200, height / 2 + 25);
        }
    }
    
    
    //display the dialog signifying end of game
    void displayDialog() {
        JFrame frame = new JFrame();
        if(!comp){
            JOptionPane.showMessageDialog(frame,win +"WINS!");
        } else{
            if(winner == 1)
                JOptionPane.showMessageDialog(frame,"YOU WIN!");
            else 
                JOptionPane.showMessageDialog(frame,"YOU LOSE!");
        }
        //dialog for quit or restart
        int dialogButton = JOptionPane.showConfirmDialog (null, "Do you want to QUIT the game?","Game Over",JOptionPane.YES_NO_OPTION);
        if(dialogButton == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            gameStatus = 3;
        }
    } 
    @Override
    public void actionPerformed(ActionEvent e) {
        
        //when user decides to play, this calls the method update()
        if(gameStatus == 2){
            update();
        }
        renderer.repaint();
    }
    // this is the method for anything that is being added or updated
    public void update() {
        if(!comp){
            if(player1.score >= scoreLimit) {
               winner = 1;    
               win = "Player 1 ";
               displayDialog(); 
                  
            } 
            if(player2.score >= scoreLimit){ 
                winner = 2;
                win = "Player 2 ";
                displayDialog();   
            }
        } 
        if(comp) {
            if (player1.score >= scoreLimit){
                winner = 1;
                displayDialog();
            } else if(player2.score >= scoreLimit){
                winner = 2;  
                displayDialog();
            }          
        }
        //W key : move up for player 1
        if(w) {
            player1.move(true);
        }
        //S key : move down for player 1
        if(s) {  
            player1.move(false);
        }
        //the computer
        if(!comp) {
            //if player chooses 2 player 
            //Arrow key Up: move up for player 2
            if(up) {
                player2.move(true);
            }
            //arrow key down: move donw for player 2
            if(down) {
                player2.move(false);
            }
        //if user chooses to play with the computer
        } else {
            if(compCoolDown > 0){
                compCoolDown--;
                if(compCoolDown == 0) {
                    compMoves = 0;
                }
            }
            if(compMoves < 10) {
                if(player2.y + player2.height / 2 < ball.y) {
                    player2.move(true);
                    compMoves++;
                }
                if(player2.y + player2.height / 2 > ball.y) {
                    player2.move(false);
                    compMoves++;
                }
                if(compDifficulty == 1){ //easy 
                    compCoolDown = 20;   // the slowing down of the movement of comp's racquet
                }                          //higher than the rest 
                if(compDifficulty == 2) {//medium
                    compCoolDown = 15;
                }
                if(compDifficulty == 3) {//hard
                    compCoolDown = 10;
                } 
            }        
        }
        ball.update(player1, player2);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                w = true;
                break;
            case KeyEvent.VK_S:
                s = true;
                break;
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_RIGHT:
                if(difficulty){
                    if(compDifficulty < 3) {
                        compDifficulty++;
                    } else {
                     compDifficulty = 1;
                    }
                } else if(gameStatus == 0) {
                    scoreLimit++;
                }
                break;
            case KeyEvent.VK_LEFT:
                if(difficulty){
                    if(compDifficulty > 1) {
                        compDifficulty--;
                    } else {
                     compDifficulty = 3;
                    }
                } else if(gameStatus == 0 && scoreLimit > 1) { 
                    scoreLimit--;
                } 
                break;  
            case KeyEvent.VK_ESCAPE:
                 if(gameStatus == 2 || gameStatus == 3){
                     gameStatus = 0;
                     difficulty = false;
                 }
                break;
            case KeyEvent.VK_SHIFT:
                if(gameStatus == 0){
                   comp = true;
                   difficulty = true; 
                }
                break;
            case KeyEvent.VK_SPACE:
                if(gameStatus == 0 || gameStatus == 3) {
                    if(!difficulty){
                       comp = false; 
                    } else {
                        comp = true;
                        //difficulty = false;
                    }
                    start();
                }
                else if(gameStatus == 1){
                    gameStatus = 2;
                }
                else if(gameStatus == 2){
                    gameStatus = 1;
                }
                break;
            default:
                break;
        }
    }
    

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
                w = false;
                break;
            case KeyEvent.VK_S:
                s =false;
                break;
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        pong = new Pong1();
    }
}
