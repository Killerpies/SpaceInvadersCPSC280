import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import javax.sound.sampled.Clip;


/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author 
 *  @version 
 */
public abstract class Ship
    extends Drawable {
//    private boolean hit;
    private Image image;
    private Clip  hitSound;
    private Rectangle shape;

    /**
     * Create a new Ship object.
     * @param x
     * @param y
     */
    public Ship(int x, int y) {
        super(x, y);
//        hit = false;
        shape = new Rectangle( x , y , 50, 50);
        hitSound = getSound("aud_hit.wav");
        // TODO Auto-generated constructor stub
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void draw(Graphics2D g2);
    
    
    /**
     * shoot things
     * Place a description of your method here.
     * @return
     */
    public abstract PhotonTorpedo shooting(); 
    
    
    public void changeImage(String imagename) {
        image = getImage(imagename);
    }
    
    public boolean colission(Rectangle object) {
        if (shape.intersects(object) ) {
            System.out.println("Hit");
            hitSound.setFramePosition(0);
            hitSound.start();
//            hit = true;
            return true;
        }
        return false;
        
    }


    

}
