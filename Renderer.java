
package pong1;

/**
 *
 * @author RizelleAnn
 * got help from a video tutorial
 */
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import static pong1.Pong1.pong;

@SuppressWarnings("serial")
public class Renderer extends JPanel {
    
    @Override
    public void paint(Graphics g){
        super.paint(g);
        
        //repainting
        pong.render((Graphics2D) g);
        
    }
}
