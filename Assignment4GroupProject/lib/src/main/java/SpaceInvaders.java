import java.awt.Color;
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

public class SpaceInvaders
    extends JFrame {

    static class KeyboardPanel
        extends JPanel {

        private int  x, y;
        private ship xwing;

        public KeyboardPanel() {
            xwing = new ship(250, 350);
            setFocusable(true);

            addKeyListener(new KeyListener() {

                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
// case KeyEvent.VK_DOWN:
// xwing.moveVerticaly(10);
// break;
// case KeyEvent.VK_UP:
// xwing.moveVerticaly(-10);
// break;
                        case KeyEvent.VK_LEFT:
                            xwing.moveHorizontaly(-10);
                            break;
                        case KeyEvent.VK_RIGHT:
                            xwing.moveHorizontaly(10);
                        case KeyEvent.VK_SPACE:
                            xwing.shootingSound();

                    }
// System.out.println("test");
                    repaint();
                }


                public void keyTyped(KeyEvent e) {

                    repaint();
                }


                public void keyReleased(KeyEvent e) {
                    // NOT IMPLEMENTED
                }
            });

        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;
            xwing.draw(g2);
        }
    }

    public SpaceInvaders() {
        setTitle("Space Invaders");

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        JMenu program = new JMenu("Program");
        JMenu game = new JMenu("Game");
        menubar.add(program);
        menubar.add(game);

        JMenuItem about = program.add("About");
        program.addSeparator();
        JMenuItem exit = program.add("Exit");

        JMenuItem reset = game.add("Reset");
        JMenuItem pause = game.add("Pause");
        JMenuItem resume = game.add("Resume");
        resume.setEnabled(false);

        pause.addActionListener(e -> {
            pause.setEnabled(false);
            resume.setEnabled(true);
            // TODO make it pause game

        });
        resume.addActionListener(e -> {
            resume.setEnabled(false);
            pause.setEnabled(true);
            // TODO make it resume game

        });

        about.addActionListener(e -> {
            JOptionPane.showMessageDialog(
                SpaceInvaders.this,
                "Austin Molina & Justin Sanders");
        });
        exit.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(
                SpaceInvaders.this,
                "Are you sure about this?");
            if (response == JOptionPane.YES_NO_OPTION) {
                dispose();

            }

        });

        add(new KeyboardPanel());
        setSize(500, 450);
        // setBackground(Color.BLACK);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }


    public static void main(String[] args) {
        var f = new SpaceInvaders();
        f.setVisible(true);
    }

}
