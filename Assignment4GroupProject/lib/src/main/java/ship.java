import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.sound.sampled.Clip;

/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 * 
 * @author
 * @version
 */
public class ship
    extends Drawable {

    private Image image;
    private Clip  sound;

    /**
     * Create a new xWing object.
     * 
     * @param x
     * @param y
     */
    public ship(int x, int y) {
        super(x, y);
        image = getImage("img_base.gif");
        sound = getSound("aud_basefire.wav");
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


    public void shootingSound() {
        sound.setFramePosition(0);
        sound.start();
    }

}
