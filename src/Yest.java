import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JTextPane;

public class Yest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Initalize panel and gamestate
		GameState gs = new GameState();
		
		
		
		Panel panel = new Panel(gs);
		
		

		//Initalize frame
		JFrame frame = new JFrame();
		frame.setTitle("Hex");
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.black);
		frame.add(panel);
	
		frame.setVisible(true);
		frame.pack();
		
		
	}
	

}
