import java.awt.Graphics2D;
import java.awt.Image;
import javax.sound.sampled.Clip;


/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author 
 *  @version 
 */
public class Base
    extends Ship {
    private Image image;
    private Clip  fireSound;

    /**
     * Create a new Base object.
     * @param x
     * @param y
     */
    public Base(int x, int y) {
        super(x, y);
        image = getImage("img_base.gif");
        fireSound = getSound("aud_basefire.wav");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics2D g2) {
        var x = getX();
        var y = getY();

        g2.drawImage(image, x, y, null);

    }


    public PhotonTorpedo shooting() {
        fireSound.setFramePosition(0);
        fireSound.start();
        
        var x = getX();
        var y = getY();
        return new PhotonTorpedo(x, y-50);
    }
    
    public void dead() {
        image = getImage("img_basehit.gif");
    }

}
