import java.awt.*;

import javax.swing.*;

public class Yest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Initalize panel and gamestate
		GameState gs = new GameState();
		
		
		
		Panel panel = new Panel(gs);
		Menu menu = new Menu(gs);

		gs.cards.add(menu, "MENU");

		//Initalize frame
		JFrame frame = new JFrame();
		frame.setTitle("Hex");
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBackground(Color.black);
		frame.add(gs.cards);
		frame.setVisible(true);
		frame.pack();
		
	}
	

}
