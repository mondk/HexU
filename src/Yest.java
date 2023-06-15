import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class Yest {

	public static void main(String[] args) throws InterruptedException {
		GameState gs = new GameState();
	
		//Panel panel = new Panel(gs);
		Menu menu = new Menu(gs);

		gs.cards.add(menu, "MENU");


		//Initalize frame
		JFrame frame = new JFrame();
		frame.setTitle("Hex");
		frame.setResizable(true);
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent windowEvent){
				if (!gs.returnPS().equals("online")){
					gs.saveGame();
				}
				if (gs.waitingRoom != null)gs.disconnectFromOnline();
				System.exit(0);
			}
		});
		frame.setBackground(Color.decode("#244b73"));
		//frame.add(panel);
		// getClass is not static, so using the GameState object to get the icon
		frame.setIconImage(new ImageIcon(gs.getClass().getResource("res/Yellow-Hexagon-Background-PNG-Image.png")).getImage());

		frame.add(gs.cards);
		frame.setVisible(true);
		frame.pack();
		//panel.start=true;
		
	}
	

}
