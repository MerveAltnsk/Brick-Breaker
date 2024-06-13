import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class GamePlayScenario extends JPanel implements KeyListener, ActionListener {

    private boolean play = false;
    private int score = 0;
    private int totalBricks = 32; // Bricks reduced to fit in the panel

    private Timer timer;
    private int delay = 8;

    private int player1 = 310;

    private int ballpositionX = 120;
    private int ballpositionY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private BrickGenerator map; // Brick layout

    ImageIcon ballIcon;
    ImageIcon paddleIcon;

    public GamePlayScenario() {
        map = new BrickGenerator(4, 8); // Create a brick layout with 4 rows and 8 columns
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();

        ballIcon = new ImageIcon("resources/ball.png");
        paddleIcon = new ImageIcon("resources/paddle.png");
    }

    public void paint(Graphics g) {
        super.paint(g);

        // Drawing background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Drawing bricks
        map.draw((Graphics2D) g);

        // Drawing borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Drawing the paddle
        g.drawImage(paddleIcon.getImage(), player1, 550, 120, 20, this);

        // Drawing the ball
        g.drawImage(ballIcon.getImage(), ballpositionX, ballpositionY, 20, 20, this);

        // Scores
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        // Game Over
        if (ballpositionY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("Game Over, Scores: " + score, 190, 300);

            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // Winning condition
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("Serif", Font.BOLD, 30));
            g.drawString("You Won, Scores: " + score, 190, 300);

            g.setFont(new Font("Serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballpositionX, ballpositionY, 20, 20).intersects(new Rectangle(player1, 550, 120, 20))) {
                ballYdir = -ballYdir;
            }

            // Check brick collision with ball
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballpositionX, ballpositionY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballpositionX + 19 <= brickRect.x || ballpositionX + 1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballpositionX += ballXdir;
            ballpositionY += ballYdir;
            if (ballpositionX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballpositionY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballpositionX > 670) {
                ballXdir = -ballXdir;
            }

            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (player1 >= 570) {
                player1 = 570;
            } else {
                moveRight();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (player1 < 10) {
                player1 = 10;
            } else {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballpositionX = 120;
                ballpositionY = 350;
                ballXdir = -1;
                ballYdir = -2;
                player1 = 310;
                score = 0;
                totalBricks = 32;
                map = new BrickGenerator(4, 8);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        player1 += 20;
    }

    public void moveLeft() {
        play = true;
        player1 -= 20;
    }

    // Inner class to manage the bricks
    public class BrickGenerator {
        public int map[][];
        public int brickWidth;
        public int brickHeight;

        public BrickGenerator(int row, int col) {
            map = new int[row][col];
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    map[i][j] = 1;
                }
            }

            brickWidth = 540 / col;
            brickHeight = 150 / row;
        }

        public void draw(Graphics2D g) {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map[0].length; j++) {
                    if (map[i][j] > 0) {
                        g.setColor(Color.white);
                        g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                        // Adding border
                        g.setStroke(new java.awt.BasicStroke(3));
                        g.setColor(Color.black);
                        g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                    }
                }
            }
        }

        public void setBrickValue(int value, int row, int col) {
            map[row][col] = value;
        }
    }
}
