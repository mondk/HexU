import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JOptionPane;


import java.util.*;



public class Panel extends JPanel{
	
	private GameState gs;
	private Image image;
	private Graphics graphics;//TextFrame for player turn
	JTextPane paneT = new JTextPane();
	JTextPane winPane = new JTextPane();
	int dialogbutton;
	
	
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
	 				if(h.getColor()==Color.red && h.getPolygon().contains(e.getPoint())) {
	 					
	 					switch(gs.whosTurn) {
	 					case Player1:
	 						h.color=gs.colorP1;
	 						gs.nextTurn();
	 						System.out.println("Player 1 clicked on hexagon: "+h.id);
	 						if (winingState(gs.startP1, gs.colorP1, gs.winP1)) {
	 							repaint();
	 							JOptionPane.showConfirmDialog(null, "HURRAY! PLayer 1 was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton);
	 		 					if (dialogbutton == JOptionPane.YES_OPTION) {
	 		 						gs.resetGame();
	 		 						paneT.setText("Player 1");
	 		 						break;
	 		 					}else {
	 		 						remove(dialogbutton);
	 		 					}
	 						}
	 						paneT.setText("Player 2");
	 						break;
	 					case Player2:
	 						h.color=gs.colorP2;
	 						gs.nextTurn();
	 						System.out.println("Player 2 clicked on hexagon: "+h.id);
	 						if (winingState(gs.startP2, gs.colorP2, gs.winP2)) {
	 							repaint();
	 							JOptionPane.showConfirmDialog(null, "HURRAY! PLayer 2 was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton);
	 							if (dialogbutton == JOptionPane.YES_OPTION) {
	 		 						gs.resetGame();
	 		 						paneT.setText("Player 1");
	 		 						break;
	 		 					}else {
	 		 						remove(dialogbutton);
	 		 					}
	 						}
	 						paneT.setText("Player 1");
	 						break;
	 					}
	 					
	 					
	 					repaint();

	 				}
	 			}
	          }                
	       });
		
	}

	public void createGrid() {
		//create grid
				int id=0;
				for(int i =0;i<gs.numberOfHexagons;i++) {
					for(int j =0;j<gs.numberOfHexagons;j++) {
						gs.grid.add( new Hexagon(new Point((int) (gs.startPoint.x+gs.shift*j+i*gs.shift*Math.cos(60*(Math.PI/180))),(int) (gs.startPoint.y+i*gs.shift*Math.sin(60*(Math.PI/180)))),gs.raidus,id));
						id++;
					}
				}
				gs.fillWinStateArrays();
				gs.createAdjacenyMatrix();
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
	
	public boolean winingState(List<Hexagon> s, Color p, List<Hexagon> win) {
		for(Hexagon v : s) {
			if (v.color != p) {
				continue;
			}
			boolean visited[] = new boolean[gs.numberOfHexagons*gs.numberOfHexagons];
			LinkedList<Integer> queue = new LinkedList<Integer>();
			visited[v.id] = true;
			queue.add(v.id);
			
			
			while (queue.size()!=0) {
				int inter = queue.poll();
				
				Iterator<Integer> i = gs.adj.get(inter).listIterator();
				while(i.hasNext()) {
					int n = i.next();
					if(visited[n] == false && gs.grid.get(n).color == p) {
						visited[n] = true;
						queue.add(n);
						if (win.contains(gs.grid.get(n))) {
							return true;
						}
					}
				}
				
				
				
			}
		}
		
		
		return false;
	}
	
	

}
