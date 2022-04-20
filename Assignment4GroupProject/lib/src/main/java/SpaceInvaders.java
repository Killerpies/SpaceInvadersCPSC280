import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SpaceInvaders
    extends JFrame {

    private Timer timer;

    class KeyboardPanel
        extends JPanel {

        private int           x, y;
        private int score = 0;
        private Base          xwing;
        private BottomA a;
        

        
        private PhotonTorpedo torpedo = null;
//        private boolean       up;
//        private boolean       down;
        private boolean       left;
        private boolean       right;

        public KeyboardPanel() {
            setBackground(Color.BLACK);
            reset();
            
            
            
            
            timer = new Timer(10, e -> {
                if (torpedo != null) {
                    torpedo.moveVerticaly(-10);
                    if (torpedo.getY() < 0) {
                        torpedo = null;
                    }
                    else {
                        if (a != null
                            && a.colission(torpedo.getDimensions())) {
                            score +=a.getPoint();
                            torpedo = null;
                            a = null; // added later makes enemy leave

                        }

                    }

                }
//                if (up)
//                    xwing.moveVerticaly(-10);
//                if (down)
//                    xwing.moveVerticaly(10);
                if (left)
                    xwing.moveHorizontaly(-10);
                if (right)
                    xwing.moveHorizontaly(10);
                repaint();
            });
            timer.start();

            setFocusable(true);

            addKeyListener(new KeyListener() {

                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
//                        case KeyEvent.VK_DOWN:
//                            down = true;
//                            break;
//                        case KeyEvent.VK_UP:
//                            up = true;
//                            break;
                        case KeyEvent.VK_LEFT:
                            left = true;
                            break;
                        case KeyEvent.VK_RIGHT:
                            right = true;
                            break;
                        case KeyEvent.VK_SPACE:
                            if (torpedo == null) {
                                torpedo = xwing.shooting();
                            }
                    }
                }


                @Override
                public void keyReleased(KeyEvent e) {
                    switch (e.getKeyCode()) {
//                        case KeyEvent.VK_DOWN:
//                            down = false;
//                            break;
//                        case KeyEvent.VK_UP:
//                            up = false;
//                            break;
                        case KeyEvent.VK_LEFT:
                            left = false;
                            break;
                        case KeyEvent.VK_RIGHT:
                            right = false;
                            break;
                    }

                }


                @Override
                public void keyTyped(KeyEvent e) {
                }

            });

        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            
            var message = "Score: " + score ;
            var font = new Font("Helvetica", Font.BOLD, 30);
            var fm = g2.getFontMetrics(font);
            
            int x = getWidth() - fm.stringWidth(message);
            int y = 35;
            
            g2.setFont(font);
            g2.setColor(Color.red);
            g2.drawString(message, x, y);
            
            xwing.draw(g2);
            if (torpedo != null) {
                torpedo.draw(g2);
            }
            
            if (a != null) {
                a.draw(g2);
            }
        }
        
        
        
       public void reset() {
           xwing = new Base(250,350);
           a = new BottomA(250, 50,'b');
           
       }

    }

    public SpaceInvaders() {

        setTitle("Space Invaders");

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu game = new JMenu("Game");
        JMenu helpMenu = new JMenu("Help");

        menubar.add(game);
        menubar.add(helpMenu);

        JMenuItem about = helpMenu.add("About...");
        helpMenu.addSeparator();

        JMenuItem reset = game.add("New Game");
        JMenuItem pause = game.add("Pause");
        JMenuItem resume = game.add("Resume");
        JMenuItem exit = game.add("Exit");
        resume.setEnabled(false);

        pause.addActionListener(e -> {
            timer.stop();
            pause.setEnabled(false);
            resume.setEnabled(true);
            // TODO make it pause game

        });
        resume.addActionListener(e -> {
            timer.start();
            resume.setEnabled(false);
            pause.setEnabled(true);
            // TODO make it resume game

        });
        
        reset.addActionListener(e -> {
//            SpaceInvaders.reset();
            
            
        });

        about.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                SpaceInvaders.this,
                "Austin Molina & Justin Sanders");
        });
        exit.addActionListener(e -> {
            confirmClosing();

            }

        );

        add(new KeyboardPanel());
        setSize(500, 450);
//        getContentPane().setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }
    
    private void confirmClosing() {
        int response = JOptionPane.showConfirmDialog(SpaceInvaders.this, "Do you want to quit");
        if (response == JOptionPane.YES_OPTION) {
            dispose();
        }
    }


    public static void main(String[] args) {
        var f = new SpaceInvaders();
        f.setVisible(true);
    }

}
