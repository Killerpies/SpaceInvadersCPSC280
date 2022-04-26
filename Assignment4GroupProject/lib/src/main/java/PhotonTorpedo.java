import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 * 
 * @author
 * @version
 */
public class PhotonTorpedo
    extends Drawable {

    private Image       image;
    private Rectangle2D shape;

    public PhotonTorpedo(int x, int y) {
        super(x, y);
        image = getImage("missilecolor.png");

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void draw(Graphics2D g2) {
        var x = getX();
        var y = getY();

        g2.drawImage(image, x, y, 2, 10, null);

    }


    public Rectangle2D getDimensions() {
        var x = getX();
        var y = getY();
        return new Rectangle2D.Double(x, y, 2, 10);
    }

}
