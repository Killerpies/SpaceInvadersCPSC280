import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Random;
import javax.sound.sampled.Clip;


/**
 *  Write a one-sentence summary of your class here./**
     * Create a new Mystery object.
     * @param 
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author 
 *  @version 
 */
public class Mystery
    extends Invader {

    private Image image;
    private Clip  sound;
    private int point;

    /**
     * Create a new BottomA object.
     * @param x
     * @param y
     */
    public Mystery(int x, int y, int point) {
        super(x, y);
        this.setPoint(point);
        image = getImage("img_mystery.gif");
        sound = getSound("aud_mystery.wav");
        this.point = point;
        

        
    }

    public void playSound() {
//        sound.setFramePosition(0);
        sound.start();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics2D g2) {
        var x = getX();
        var y = getY();

        g2.drawImage(image, x, y, null);
        // TODO Auto-generated method stub

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public PhotonTorpedo shooting() {
        
        var x = getX();
        var y = getY();
        return new PhotonTorpedo(x, y+50);
    }
}