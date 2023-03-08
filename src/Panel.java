import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import javax.swing.ImageIcon;
import javax.swing.JButton;




public class Panel extends JPanel implements Runnable{
	
	private GameState gs;
	private Image image;
	private Graphics graphics;							//TextFrame for player turn
	JTextPane paneT = new JTextPane();
	JTextPane winPane = new JTextPane();
	JButton undo = new JButton("Undo");

	int dialogbutton;
	ImageIcon reMatchIcon = new ImageIcon("res/rematch.png");  //  <a target="_blank" href="https://icons8.com/icon/PT3001yzoXgN/match">Match</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
	
	public Panel(GameState gs) {
		this.gs=gs;
		this.setFocusable(true);
		this.setPreferredSize(gs.SCREEN_SIZE);
		createGrid();
		paneT.setText("Player 1");
		paneT.setBackground(gs.colorP1);
		undo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){

				try{
					int inter = gs.q.pollLast();
					gs.grid.get(inter).color = Color.gray;
					gs.grid.get(inter).clicked = false;
					gs.nextTurn();
					paneT.setText(gs.paneTurnString);
					paneT.setBackground(gs.paneTColor);
					repaint();
				} catch (Exception null_error){
					System.out.println("There are no more moves to undo:\n\t"+null_error);
				}
			}
		});
		this.add(paneT);
		this.add(undo);
		this.addMouseListener(new MouseAdapter(){
	         public void mouseClicked(MouseEvent e) {
	        	 for(Hexagon h: gs.grid) {
	 				if(h.getPolygon().contains(e.getPoint())&&!h.clicked) {
	 					
	 					h.clicked=true;

						gs.q.add(h.id);

	 					switch(gs.whosTurn) {
	 					case Player1:
	 						h.color=gs.colorP1;
	 						gs.nextTurn();
	 						System.out.println("Player 1 clicked on hexagon: "+h.id);
	 						if (winingState(gs.startP1, gs.colorP1, gs.winP1)) {
	 							repaint();
	 							JOptionPane.showConfirmDialog(null, "HURRAY! PLayer 1 was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
	 		 					if (dialogbutton == JOptionPane.YES_OPTION) {
	 		 						gs.resetGame();
	 		 						paneT.setText(gs.paneTurnString);
									paneT.setBackground(gs.paneTColor);
	 		 						break;
	 		 					}else {
	 		 						remove(dialogbutton);
	 		 					}
	 						}
	 						paneT.setText(gs.paneTurnString);
							paneT.setBackground(gs.paneTColor);
	 						break;
	 					case Player2:
	 						h.color=gs.colorP2;
	 						gs.nextTurn();
	 						System.out.println("Player 2 clicked on hexagon: "+h.id);
	 						if (winingState(gs.startP2, gs.colorP2, gs.winP2)) {
	 							repaint();
	 							JOptionPane.showConfirmDialog(null, "HURRAY! PLayer 2 was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
	 							if (dialogbutton == JOptionPane.YES_OPTION) {
	 		 						gs.resetGame();
	 		 						paneT.setText(gs.paneTurnString);
									paneT.setBackground(gs.paneTColor);
	 		 						break;
	 		 					}else {
	 		 						remove(dialogbutton);
	 		 						
	 		 					}
	 						}
	 						paneT.setText(gs.paneTurnString);
	 						paneT.setBackground(gs.paneTColor);
	 						break;
	 					}
	 					
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
		Point mouse = MouseInfo.getPointerInfo().getLocation();
		mouse.setLocation(mouse.x, mouse.y);
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
