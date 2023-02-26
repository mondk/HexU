import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTextPane;




public class Panel extends JPanel implements Runnable{
	
	private GameState gs;
	private Image image;
	private Graphics graphics;//TextFrame for player turn
	JTextPane paneT = new JTextPane();
	
	
	public Panel(GameState gs) {
		this.gs=gs;
		this.setFocusable(true);
		this.setPreferredSize(gs.SCREEN_SIZE);
		createGrid();
		paneT.setText("Player 1");
		this.add(paneT);
		this.addMouseListener(new MouseAdapter(){

			    
			
	         public void mouseClicked(MouseEvent e) {
	        	 for(Hexagon h: gs.grid) {
	 				if(h.getPolygon().contains(e.getPoint())&&!h.clicked) {
	 					
	 					
	 					switch(gs.whosTurn) {
	 					case Player1:
	 						h.color=Color.decode("#d032f0");
	 						
	 						gs.nextTurn();
	 						System.out.println("Player 1 clicked on hexagon: "+h.id);
	 						paneT.setText("Player 2");
	 						break;
	 					case Player2:
	 						h.color=Color.decode("#247324");
	 						gs.nextTurn();
	 						System.out.println("Player 2 clicked on hexagon: "+h.id);
	 						paneT.setText("Player 1");
	 						break;
	 					}
	 					h.clicked=true;
	 					repaint();
	 				}
	 			}
	          }                
	       });
		Thread gameThread = new Thread(this);
		gameThread.start();
	}
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks =60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now -lastTime)/ns;
			lastTime = now;
			if(delta >=1) {
				checkMouseHover();
				repaint();
				delta--;
				
			}
		}
	}

	private void checkMouseHover() {
		// TODO Auto-generated method stub
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		mouse.setLocation(mouse.x-10, mouse.y-32);
		for(Hexagon h: gs.grid) {
			if(h.getPolygon().contains(mouse)&&!h.clicked) {
				h.color=Color.red;
			}
			else if (!h.clicked&&h.color==Color.red) {
				h.color=Color.gray;
			}
		}
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
	       	   g.fillPolygon(h.getPolygon());
	       	   g.setColor(Color.BLUE);
	       	   g.drawPolygon(h.getPolygon());
	       	  
			}
	}
	
	

}
