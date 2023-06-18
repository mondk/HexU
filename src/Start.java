import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Start {

	public static void main(String[] args) throws InterruptedException {
		GameState gs = new GameState();
	
	
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
		frame.setIconImage(new ImageIcon("res/Yellow-Hexagon-Background-PNG-Image.png").getImage());

		frame.add(gs.cards);
		frame.setVisible(true);
		frame.pack();
		
		
	}
	

}
