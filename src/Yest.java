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
					try{
						FileWriter saveWriter = new FileWriter("res/saves.txt");
						if (gs.singlePlayer)
							saveWriter.write("mode: " + "true: " + gs.returnPS());
						else 
							saveWriter.write("mode: " + "false: " + gs.returnPS());
						saveWriter.write("\nhexes: " + gs.numberOfHexagons);
						for (Map.Entry<Integer,Player> player : gs.players.entrySet()){
							saveWriter.write("\nP"+player.getKey()+": " + player.getValue().name);
							saveWriter.write("\nP"+player.getKey()+"C: " + String.valueOf(player.getValue().color.getRGB()));
						}
						saveWriter.write("\nmoves: " + gs.q.toString());
						saveWriter.close();
					} catch(IOException IOe) {
						System.out.println(IOe);
					}
				}
				if (gs.waitingRoom != null)gs.waitingRoom.disconnect();
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
