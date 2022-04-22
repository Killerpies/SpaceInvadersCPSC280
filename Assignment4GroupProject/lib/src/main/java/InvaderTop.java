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
public class InvaderTop
    extends Invader {

    private Image image;
    private Clip  sound;
    private int point = 30;

    /**
     * Create a new BottomA object.
     * @param x
     * @param y
     */
    public InvaderTop(int x, int y) {
        super(x, y);
        this.setPoint(point);
        image = getImage("img_invadertopA.gif");
        sound = getSound("aud_basefire.wav");
        
    }
    public InvaderTop(int x, int y, char gif) {
        super(x, y);
        this.setPoint(point);
        
        switch(gif) {
            case 'a':
                image = getImage("img_invadertopA.gif");
            case 'b':
                image = getImage("img_invadertopB.gif");
        }
//        sound = getSound("aud_basefire.wav");
        
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