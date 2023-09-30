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

public class GamePlayScenario extends JPanel implements KeyListener, ActionListener{

	private boolean play = false; 
	private int score = 0;			//We create a variable for the score and assign 0 to the initial value.
	private int TotalBricks = 21;	//We create a variable for the total bricks and assign 21 to the initial value.
	
	private Timer timer;
	private int delay = 8;
	
	private int player1 = 310;	//We create a variable for the user and assign 310 to the initial value
	
	private int ballpositionX = 120;	//We create a variable for the x direction and assign 120 to the initial value.
	private int ballpositionY = 350;	//We create a variable for the y direction and assign 350 to the initial value.
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	ImageIcon icon;						//we create variables for images
	
	
	public GamePlayScenario() {
		
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	//Thanks to this part, we were able to include the desired images in the code.
	ImageIcon yellow = new ImageIcon("resources\\yellow.png");
	ImageIcon red = new ImageIcon("resources\\red.png");
	ImageIcon ball = new ImageIcon("resources\\ball.png");
	ImageIcon paddle = new ImageIcon("resources\\paddle.png");
	
	
	
	
	public void paint(Graphics g) {
		super.paint(g);								//
		//we don't add border down side it should be empty for ball
	
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 8; j++) {
				if(i == 0) {
					g.drawImage(yellow.getImage(),5 +(j*(70 + 2)),3 + ((i)*(25+2)) ,70, 25, getFocusCycleRootAncestor());
				}
				else if(i == 1) {
					g.drawImage(red.getImage(),5 +(j*(70 + 2)),3 + ((i)*(25+2)) ,70, 25, getFocusCycleRootAncestor());
				}
				else if(i == 2) {
					g.drawImage(yellow.getImage(),5 +(j*(70 + 2)),3 + ((i)*(25+2)) ,70, 25, getFocusCycleRootAncestor());
				}
				else {
					g.drawImage(red.getImage(),5 +(j*(70 + 2)),3 + ((i)*(25+2)) ,70, 25, getFocusCycleRootAncestor());
				}
			}
		}
		

		
		
		//paddel
		g.drawImage(paddle.getImage(), player1, 500, 120, 40, getFocusCycleRootAncestor());
		
		//ball
		g.drawImage(ball.getImage(), ballpositionX, ballpositionY, 20, 20, null);
		//ball image dimensions adjusted
		
	
		
		g.dispose();
		//code in case the ball falls off the panel
		
	}
		
		
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {    //we don't need to call it will be called automatically
		timer.start();
		//we write the function in the action part to move the ball
		
		if(play) {
			
			//function to detect intersection between pedal and ball
			
			//we form a rectangle around the ball
			if(new Rectangle(ballpositionX, ballpositionY,20,20).intersects(new Rectangle(player1,500,120,40))) {
				ballYdir = -ballYdir;
			}
			
			
			ballpositionX += ballXdir;
			ballpositionY += ballYdir;
			if(ballpositionX <0) {			//This is for the left border
				ballXdir = -ballXdir;
			}
			if(ballpositionY <0) {			//This is for the tap border
				ballYdir = -ballYdir;
			}
			if(ballpositionX >570) {		//This is for the right border
				ballXdir = -ballXdir;
			}
			
		}
		
		repaint();
		
	}

	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
	// We write the function in the press part to detect the direction keys.
	if(e.getKeyCode()== KeyEvent.VK_RIGHT) {
		if(player1 >=470) {
			player1 = 470;		//We put a condition so that when the user presses the right arrow key, it does not exceed the 600 limit.
		}else {
			moveRight();
			
		}
	}
	
	if(e.getKeyCode()== KeyEvent.VK_LEFT) {
		if(player1 <10) {
			player1 = 10;		//If it is less than 10 when the user presses the left arrow key, we set a condition for it to be corrected.
		}else {
			moveLeft();
			
		}
	}
		
	//restart code when enter is pressed
	if(!play) {
		play = true;
		ballpositionX = 120;
		ballpositionY = 350;
		ballXdir = -1;
		ballYdir = -2;
		player1 = 310;

		
		repaint();
	}
	}

	public void moveRight() {
		play = true;
		player1+=20;
		
	}
	
	public void moveLeft() {
		play = true;
		player1-=20;
		
	}
	
	
	
	
	
	
	
	
	
}
