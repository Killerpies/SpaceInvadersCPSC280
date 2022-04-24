import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
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
    private boolean hit;
    private Image image;
    private Clip  hitSound;
    private Rectangle2D shape;

    /**
     * Create a new Ship object.
     * @param x
     * @param y
     */
    public Ship(int x, int y) {
        super(x, y);
//        hit = false;
        shape = new Rectangle2D.Double( x , y , 30, 30);
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
    
    public void setHit(boolean t) {
        hit = t;
    }
    
    public boolean getHit() {
        return hit;
    }
    public void changeImage(String imagename) {
        image = getImage(imagename);
    }
    
    public boolean colission(Rectangle2D rectangle2d) {
        var x = getX();
        var y = getY();
        Rectangle2D tempShape = new Rectangle2D.Double( x , y , 30, 30);
        if (tempShape.intersects(rectangle2d) ) {
//            System.out.println("Hit");
            hitSound.setFramePosition(0);
            hitSound.start();
//            image = getImage("img_invaderhit.gif");
            hit = true;
            return true;
        }
        return false;
        
    }


    

}
