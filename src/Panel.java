import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;


public class Panel extends JPanel{
	
	private GameState gs;
	
	public Panel(GameState gs) {
		this.gs=gs;
		this.setFocusable(true);
		this.setPreferredSize(gs.SCREEN_SIZE);
		//create grid
		int id=1;
		for(int i =0;i<gs.numberOfHexagons;i++) {
			for(int j =0;j<gs.numberOfHexagons;j++) {
				gs.grid.add( new Hexagon(new Point((int) (gs.startPoint.x+gs.shift*j+i*gs.shift*Math.cos(60*(Math.PI/180))),(int) (gs.startPoint.y+i*gs.shift*Math.sin(60*(Math.PI/180)))),gs.raidus,id));
				id++;
			}
		}
		
	}

	
	
	@Override
	public void paintComponent(Graphics g) {
		for(Hexagon h:gs.grid) {
		   g.setColor(h.color);
       	   g.fillPolygon(h.getHexagon());
       	   g.setColor(Color.BLUE);
       	   g.drawPolygon(h.getHexagon());
       	  
		}
	}
	
	public void draw(Graphics g) {
		
	}
	
	

}
