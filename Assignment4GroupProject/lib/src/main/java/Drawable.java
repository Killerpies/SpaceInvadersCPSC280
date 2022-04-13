import java.awt.Graphics2D;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;

/**
 * Write a one-sentence summary of your class here. Follow it with additional
 * details about its purpose, what abstraction it represents, and how to use it.
 * 
 * @author
 * @version
 */
public abstract class Drawable {

    private int x;
    private int y;

    protected Drawable(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public abstract void draw(Graphics2D g2);


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public void moveHorizontaly(int pixels) {
        x += pixels;
    }


    public void moveVerticaly(int pixels) {
        y += pixels;
    }


    protected Image getImage(String filename) {
        URL url = getClass().getResource(filename);
        ImageIcon icon = new ImageIcon(url);
        return icon.getImage();
    }


    protected Clip getSound(String filename) {
        Clip clip = null;
        try {
            InputStream in = getClass().getResourceAsStream(filename);
            InputStream buf = new BufferedInputStream(in);
            AudioInputStream stream = AudioSystem.getAudioInputStream(buf);
            clip = AudioSystem.getClip();
            clip.open(stream);
        }
        catch (UnsupportedAudioFileException | IOException
            | LineUnavailableException e) {
            e.printStackTrace();
        }
        return clip;
    }

}
