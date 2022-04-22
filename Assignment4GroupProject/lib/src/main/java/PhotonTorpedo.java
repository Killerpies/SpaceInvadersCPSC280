import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 * 
 * @author
 * @version
 */
public class PhotonTorpedo
    extends Drawable {

    private Image image;

    public PhotonTorpedo(int x, int y) {
        super(x, y);
        image = getImage("photon.png");

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics2D g2) {
        var x = getX();
        var y = getY();

        g2.drawImage(image, x, y, 50,50, null);

    }
    
    public Rectangle getDimensions() {
        var x = getX();
        var y = getY();
        var w = image.getWidth(null);
        var h = image.getHeight(null);
        return new Rectangle( x, y, 1, 1);
    }

}
