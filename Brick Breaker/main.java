import javax.swing.JFrame;

public class main {

	public static void main(String[] args) {
		JFrame frameObject = new  JFrame();
		GamePlayScenario gameplayscenario = new GamePlayScenario();
		frameObject.setBounds(10,10,600,600);
		frameObject.setTitle("Breakout Ball Game");
		frameObject.setResizable(false);
		
		frameObject.setVisible(true);
		frameObject.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameObject.add(gameplayscenario);
		
 
	}

}
 