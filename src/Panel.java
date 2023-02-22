import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;




public class Panel extends JPanel{
	
	private GameState gs;
	private Image image;
	private Graphics graphics;
	public Panel(GameState gs) {
		this.gs=gs;
		this.setFocusable(true);
		this.setPreferredSize(gs.SCREEN_SIZE);
		createGrid();
		
		
		this.addMouseListener(new MouseAdapter(){
			
			public void mouseEntered(MouseEvent e) {
				
			}
			
	         public void mouseClicked(MouseEvent e) {
	        	 for(Hexagon h: gs.grid) {
	 				if(h.getHexagon().contains(e.getPoint())) {
	 					h.color=Color.cyan;
	 					System.out.println("Clicked on hexagon: "+h.id);
	 					repaint();
	 				}
	 			}
	          }                
	       });
		
	}

	public void createGrid() {
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
		image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
		
	}
	
	public void draw(Graphics g) {
		for(Hexagon h:gs.grid) {
			   g.setColor(h.color);
	       	   g.fillPolygon(h.getHexagon());
	       	   g.setColor(Color.BLUE);
	       	   g.drawPolygon(h.getHexagon());
	       	  
			}
	}
	
	

}
