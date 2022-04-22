import java.awt.Graphics2D;

/**
 *  Write a one-sentence summary of your class here.
 *  Follow it with additional details about its purpose, what abstraction
 *  it represents, and how to use it.
 * 
 *  @author 
 *  @version 
 */
public abstract class Invader
    extends Ship {
    private int point;

    /**
     * Create a new Invader object.
     * @param x
     * @param y
     */
    public Invader(int x, int y) {
        super(x, y);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void draw(Graphics2D g2);
    
    
    public void setPoint(int in) {
        point = in;
        
    }
    public int getPoint() {
//        changeImage("img_invaderhit.gif");
        return point;
        
    }

}
