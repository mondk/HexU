import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.FileWriter;
import java.io.IOException;

public class Yest {

	public static void main(String[] args) {
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
				try{
					FileWriter saveWriter = new FileWriter("res/saves.txt");
					if (gs.singlePlayer)
						saveWriter.write("mode: " + "true");
					else 
						saveWriter.write("mode: " + "false");
					saveWriter.write("\nhexes: " + gs.numberOfHexagons);
					saveWriter.write("\nP1: " + gs.players.get(0).name);
					saveWriter.write("\nCP1: " + String.valueOf(gs.players.get(0).color.getRGB()));
					saveWriter.write("\nP2: " + gs.players.get(1).name);
					saveWriter.write("\nCP2: " + String.valueOf(gs.players.get(1).color.getRGB()));
					saveWriter.write("\nmoves: " + gs.q.toString());
					saveWriter.close();
				} catch(IOException IOe) {
					System.out.println(IOe);
				}
				System.exit(0);
			}
		});
		frame.setBackground(Color.decode("#244b73"));
		//frame.add(panel);
		frame.setIconImage(new ImageIcon("res/Yellow-Hexagon-Background-PNG-Image.png").getImage());

		frame.add(gs.cards);
		frame.setVisible(true);
		frame.pack();
		//panel.start=true;
		
	}
	

}
