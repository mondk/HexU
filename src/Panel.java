import java.awt.Graphics;

import javax.swing.JPanel;

public class Panel extends JPanel{
	
	
	public Panel(GameState gs) {
		
		this.setFocusable(true);
		this.setPreferredSize(gs.SCREEN_SIZE);
		
		
	}

	
	
	@Override
	public void paintComponent(Graphics g) {
		
	}
	
	public void draw(Graphics g) {
		
	}
	
	

}
