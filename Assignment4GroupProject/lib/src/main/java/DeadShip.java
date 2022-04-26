import java.awt.Graphics2D;
import java.awt.Image;


/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author 
 *  @version 
 */
public class DeadShip
    extends Invader {
    private Image image;

    /**
     * Create a new DeadShip object.
     * @param x
     * @param y
     */
    public DeadShip(int x, int y) {
        super(x, y);
        image = getImage("img_invaderhit.gif");
        // TODO Auto-generated constructor stub
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

}
