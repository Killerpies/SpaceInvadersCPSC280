import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import java.util.Random;

public class SpaceInvaders
    extends JFrame {

    private Timer   timer;
    private boolean test = false;

    class KeyboardPanel
        extends JPanel {

        private int                 score             = 0;
        private int                 livesLeft         = 1;
        private Base                xwing;
        private Mystery             mShip;

        private List<Invader>       inv               = new ArrayList<>();
        private List<PhotonTorpedo> enemyTorpedo      = new ArrayList<>();
        private PhotonTorpedo       torpedo           = null;
        private boolean             left;
        private boolean             right;
        private boolean             goRight           = true;
        private boolean             finalWaveStart    = false;

        private double              invaderPulse      = 40; // when the invader
                                                            // will
                                                            // move
        private double              pulseCount        = 0; // checkint to see if
                                                           // its
                                                           // time to move
        private double              enemyMissileSpeed = 0;
        private int                 lastX             = 500;
        private int                 lastY             = 500;
        String                      endGameMessage    = "";

        public KeyboardPanel() {
            setBackground(Color.BLACK);
            reset();
// testSpawn();
            timer = new Timer(10, e -> {
                if (test) {
                    reset();
                    test = false;
                }
                // TODO Change font of score/lives
                // TODO Drop ships a couple of pixes to the mystery doesnt fly
                // through them
                // TODO Fix Colision
                // TODO Fix enemy missile removing like 20hp per hit

                // Invader Movement
                pulseCount += 1;
                if (pulseCount > invaderPulse) {
                    // reset pulseCount
                    pulseCount = 0;
                    // move invader
                    moveInvaderX();
                    // check if inBounds, change direction if not
                    goRight = rightOrLeft(goRight);
                    // Enemy Fire
                    enemyShoot();
                    // detect if enemy hit bottom of map 370y
                    enemyBounds();
                }
                // Mystery Ship Spawn
                getMystery();
                // Enemy Missiles and mystery Ship
                enemyMissileSpeed++;
                if (enemyMissileSpeed == 2) {
                    // move enemyTorpedo
                    moveEnemyTorpedo();
                    enemyMissileSpeed = 0;

                    if (mShip != null) {
                        mShip.moveHorizontaly(5);
                    }
                }
                // check if enemyTorpedo left bounds
                enemyTorpedoBounds();
                // End Game or spawn 10 more
                finalWave();
                // Check if enemy was hit
                checkEnemyHit();
                // remove hit ship
                removeHitShip();

                // count if base got hit
                baseHitCount();

                if (left && xwing.getX() > 0)
                    xwing.moveHorizontaly(-5);
                if (right && xwing.getX() < 455)
                    xwing.moveHorizontaly(5);
                repaint();
            });
            // timer.start();

            setFocusable(true);

            addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent arg0) {
                    int result = JOptionPane
                        .showConfirmDialog(SpaceInvaders.this, "Close?");
                    if (result == JOptionPane.YES_OPTION) {
                        timer.stop();
                        dispose();
                    }
                }
            });

            addKeyListener(new KeyListener() {

                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
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


        public void getMystery() {
            Random spawn = new Random();

            if (spawn.nextInt(500) == 1 && mShip == null) {
                mShip = new Mystery(0, 35);
            }

        }


        public void finalWave() {

            // keep track of last y and x
            if (inv.size() > 0) {

                for (int i = 0; i < inv.size(); i++) {
                    if (inv.get(i).getY() < lastY) {
                        lastY = inv.get(i).getY();
                        lastX = inv.get(i).getX();
                    }

                }

            }

            // if the first 50 died and the finalwave has not been activated
            // spawn 10 more where the previous ones died
            if (inv.size() <= 0 && finalWaveStart == false
                && enemyTorpedo.size() <= 0) {
                finalWaveStart = true;

                for (int i = 0; i < 10; i++) {
                    Invader temp = new InvaderTop(lastX, lastY, 'b');
                    inv.add(temp);
                    lastX += 30;
                }

            }

            if (inv.size() < 1 && finalWaveStart == true) {
                endGameMessage = "Game Over";
                timer.stop();
            }
        }


        public void enemyBounds() {
            for (int i = 0; i < inv.size(); i++) {
                // if enemy reaches your ship game ends
                if (inv.get(i).getY() > 350) {
                    xwing.dead();
                    endGameMessage = "Game Over";
                    timer.stop();

                }
            }

        }


        public void enemyTorpedoBounds() {
            for (int i = 0; i < enemyTorpedo.size(); i++) {
                if (enemyTorpedo.get(i).getY() > 500) {
                    enemyTorpedo.remove(i);
                }
            }
        }


        public void baseHitCount() {
            if (xwing.getHit() == true) {
                livesLeft--;
                xwing.setHit(false);
            }
            if (livesLeft == 0) {
                xwing.dead();
            }
        }


        public void checkEnemyHit() {
            boolean enemyTorpe = false;
            int enemyTorpeIndex = 0;
            for (int i = 0; i < enemyTorpedo.size(); i++) {
                if (xwing.colission(enemyTorpedo.get(i).getDimensions())) {
                    xwing.setHit(true);
                    enemyTorpeIndex = i;
                    enemyTorpe = true;
// enemyTorpedo.remove(i);
                }
            }

            if (torpedo != null) {
                torpedo.moveVerticaly(-5);
                if (torpedo.getY() < 0) {
                    torpedo = null;
                }
                else {
                    boolean torpe = false;
                    for (int i = 0; i < inv.size(); i++) {
                        if (inv.get(i).colission(torpedo.getDimensions())) {
                            score += inv.get(i).getPoint();
                            inv.get(i).setHit(true);
                            torpe = true;
                        }

                    }




                    if (mShip != null
                        && mShip.colission(torpedo.getDimensions())) {
                        score += mShip.getPoint();
                        mShip.setHit(true);
                        torpe = true;
                    }
                    if (torpe == true) {
                        torpedo = null;
                    }
                    if (enemyTorpe == true) {
                        enemyTorpedo.remove(enemyTorpeIndex);
                    }
                }

            }

        }


        public void removeHitShip() {

            for (int i = 0; i < inv.size(); i++) {
                if (inv.get(i).getHit() == true) {
                    inv.remove(i);
                }

            }
//            if (mShip != null && mShip.getHit() == true) {
//                mShip = null;
//            }

        }


        public void enemyShoot() {
            int maxy = 0;
            int x = 0;
            // find bottom row y
            for (int i = 0; i < inv.size(); i++) {
                // get bottom row
                if (inv.get(i).getY() > maxy) {
                    // get bottom row y
                    maxy = inv.get(i).getY();

                }

            }
            // find bottom row x
            for (int i = 0; i < inv.size(); i++) {
                if (inv.get(i).getY() == maxy) {
                    int rnd = new Random().nextInt(inv.size());
                    x = inv.get(rnd).getX();
                }

            }

            Random spawn = new Random();

            if (spawn.nextInt(4) == 1) {
                if (enemyTorpedo.size() < 3) {
                    PhotonTorpedo tempTorpedo = new PhotonTorpedo(x, maxy);
                    enemyTorpedo.add(tempTorpedo);
                }
            }

        }


        public void moveEnemyTorpedo() {
            for (int i = 0; i < enemyTorpedo.size(); i++) {
                enemyTorpedo.get(i).moveVerticaly(5);
            }
        }


        public boolean rightOrLeft(boolean check) {
            for (int i = 0; i < inv.size(); i++) {
                if (inv.get(i).getX() > 450) {
                    check = false;
                    moveInvaderY();
                    break;

                }
                else if (inv.get(i).getX() < 5) {
                    check = true;

                    moveInvaderY();
                    break;

                }

            }
            return check;

        }


        public void moveInvaderY() {
            for (int i = 0; i < inv.size(); i++) {
                inv.get(i).moveVerticaly(12);

            }
            invaderPulse = invaderPulse * .80;

        }


        public void moveInvaderX() {
            if (goRight) {
                // if goRight then move right
                for (int i = 0; i < inv.size(); i++) {
                    inv.get(i).moveHorizontaly(5);
                }
            }
            else {
                for (int i = 0; i < inv.size(); i++) {
                    inv.get(i).moveHorizontaly(-5);
                }
            }

        }


        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D)g;

            var message = "Score: " + score;
            var livesLeftMsg = "Lives: " + livesLeft;
            var font = new Font("Helvetica", Font.BOLD, 25);
            var fm = g2.getFontMetrics(font);

            int x = getWidth() - fm.stringWidth(message);
            int y = 25;

            g2.setFont(font);
            g2.setColor(Color.GREEN);
            g2.drawString(message, x, y);
            g2.drawString(livesLeftMsg, 0, y);

            xwing.draw(g2);

            if (mShip != null) {
                mShip.draw(g2);
            }

            if (torpedo != null) {
                torpedo.draw(g2);
            }
//            for (var i : inv) {
//
//                i.draw(g2);
//
//            }
            
            for (int i=0; i<inv.size();i++) {
                inv.get(i).draw(g2);
            }

//            for (var i : enemyTorpedo) {
//
//                i.draw(g2);
//
//            }
            for (int i=0; i<enemyTorpedo.size();i++) {

                enemyTorpedo.get(i).draw(g2);

            }

            if (endGameMessage != "") {
                var fontEnd = new Font("Helvetica", Font.BOLD, 45);
                var fmEnd = g2.getFontMetrics(fontEnd);

                int xEnd = getWidth() / 2 - fm.stringWidth(message) / 2;
                int yEnd = getHeight() / 2;
                g2.drawString(endGameMessage, xEnd, yEnd);

            }

        }


        public void testSpawn() {
            inv.clear();
            score = 0;
            xwing = new Base(250, 350);

            // BOTTOMS
            char letter = 'a';
            int startx = 100;
            int ypos = 160;
            for (int i = 0; i < 1; i++) {
                if (i == 10) {
                    ypos -= 30;
                    startx = 100;
                    letter = 'b';
                }
                Invader temp = new BottomA(startx, ypos, letter);
                inv.add(temp);
                startx += 30;
            }

        }


        public void reset() {
            inv.clear();
            score = 0;
            xwing = new Base(250, 370);
            endGameMessage = "";

            // BOTTOMS
            char letter = 'a';
            int startx = 100;
            int ypos = 160;
            for (int i = 0; i < 20; i++) {
                if (i == 10) {
                    ypos -= 30;
                    startx = 100;
                    letter = 'b';
                }
                Invader temp = new BottomA(startx, ypos, letter);
                inv.add(temp);
                startx += 30;
            }

            letter = 'a';
            startx = 100;
            ypos -= 30;
            for (int i = 0; i < 20; i++) {
                if (i == 10) {
                    ypos -= 30;
                    startx = 100;
                    letter = 'b';
                }
                Invader temp = new InvaderMiddle(startx, ypos, letter);
                inv.add(temp);
                startx += 30;
            }

            letter = 'a';
            startx = 100;
            ypos -= 30;
            for (int i = 0; i < 10; i++) {
                if (i == 10) {
                    ypos -= 30;
                    startx = 100;
                    letter = 'b';
                }
                Invader temp = new InvaderTop(startx, ypos, letter);
                inv.add(temp);
                startx += 30;
            }

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

        JMenuItem newGame = game.add("New Game");
        JMenuItem pause = game.add("Pause");
        JMenuItem resume = game.add("Resume");
        JMenuItem exit = game.add("Quit");
        resume.setEnabled(false);
        pause.setEnabled(false);

        pause.addActionListener(e -> {

            pause.setEnabled(false);
            resume.setEnabled(true);
            System.out.println(pause.isEnabled());
            timer.stop();
            // TODO make it pause game

        });
        resume.addActionListener(e -> {

            resume.setEnabled(false);
            pause.setEnabled(true);
            timer.start();
            // TODO make it resume game

        });

        newGame.addActionListener(e -> {
            if (newGameConfirm()) {
                resume.setEnabled(false);
                pause.setEnabled(true);
                timer.start();
                test = true;

            }

// SpaceInvaders.reset();

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
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

    }


    private boolean newGameConfirm() {
        int response = JOptionPane.showConfirmDialog(
            SpaceInvaders.this,
            "Do you want to start a new game?");
        if (response == JOptionPane.YES_OPTION) {
            return true;
        }
        return false;
    }


    private void confirmClosing() {
        int response = JOptionPane
            .showConfirmDialog(SpaceInvaders.this, "Do you want to quit");
        if (response == JOptionPane.YES_OPTION) {
            timer.stop();
            dispose();
        }
    }


    public static void main(String[] args) {
        var f = new SpaceInvaders();
        f.setVisible(true);
    }

}
