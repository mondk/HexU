import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;




public class Panel extends JPanel implements Runnable, MoveListener{

	private GameState gs;
	private ImageIcon img;						//TextFrame for player turn
	JTextPane paneT = new JTextPane();
	JTextPane winPane = new JTextPane();
	private AI ai;
	JButton undo = new JButton("Undo");
	JButton generate = new JButton("Generate move");
	JButton backtoMenu = new JButton("Back to Menu");
	boolean start =true;
	int dialogbutton;
	ImageIcon reMatchIcon = new ImageIcon("res/rematch.png");  //  <a target="_blank" href="https://icons8.com/icon/PT3001yzoXgN/match">Match</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
	boolean singlePlayer;
	int once = 0;
	private Graphics graphic;


	public Panel(GameState gs) {
		this.gs=gs;
		this.img = new ImageIcon(gs.randomBackground());
		this.singlePlayer=gs.singlePlayer;
		this.setFocusable(true);
		this.setPreferredSize(gs.SCREEN_SIZE);
		gs.createGrid();
		this.ai = new AI(gs);
		

		//System.out.println(gs.grid.toString());
		paneT.setText(gs.paneTurnString);
		paneT.setBackground(gs.paneTColor);

		//Button to generate an ai move
		generate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int[] move= {};

				switch(gs.whosTurn){
					case Player1:
						move = ai.nextMove(ai.gridToMatrix(gs.grid,gs.numberOfHexagons), gs.players.get(0).color.toString());
						break;
					case Player2:
						move = ai.nextMove(ai.gridToMatrix(gs.grid,gs.numberOfHexagons), gs.players.get(1).color.toString());
						break;
				}
				int hex = move[0]*gs.numberOfHexagons+move[1];
				gs.grid.get(hex).clicked = true;
				gs.grid.get(hex).color = gs.paneTColor;
				gs.q.add(hex);
				repaint();
				
				if(gs.onlineMove != null)
					gs.onlineMove.makeMove(hex);
				repaint();
				//this bit handles sound effects
				try {
					playSound("src/converted_mixkit-water-sci-fi-bleep-902.wav");
				} catch (LineUnavailableException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 

				try {
					Thread.sleep(hex);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				switch(gs.whosTurn){
					case Player1:
						if (gs.winingState(gs.startP1, gs.players.get(0).color, gs.winP1)) {
							
							start=false;
							drawExsplosion(graphic);
							repaint();
							break;
							
						}
					case Player2:
						if (gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2) && gs.host) {
							
							start=false;
							drawExsplosion(graphic);
							repaint();
							break;
						}

				}
				gs.nextTurn();
				paneT.setText(gs.paneTurnString);
				paneT.setBackground(gs.paneTColor);
				repaint();
				
			}

		});
		undo.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				try{
					if (gs.returnPS().equals("single")){
						int inter = gs.q.pollLast();
						gs.grid.get(inter).color = Color.gray;
						gs.grid.get(inter).clicked = false;
						int inter2 = gs.q.pollLast();
						gs.grid.get(inter2).color = Color.gray;
						gs.grid.get(inter2).clicked = false;
						repaint();
					} else{
						int inter = gs.q.pollLast();
						gs.grid.get(inter).color = Color.gray;
						gs.grid.get(inter).clicked = false;
						gs.nextTurn();
						paneT.setText(gs.paneTurnString);
						paneT.setBackground(gs.paneTColor);
						repaint();
					}
				} catch (Exception null_error){
					System.out.println("There are no more moves to undo:\n\t"+null_error);
				}
			}
		});
		backtoMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if (gs.waitingRoom != null)gs.disconnectFromOnline();
				gs.resetGame(0);
				gs.returnToMenu();
				
			}
		});
		this.add(paneT);
		this.add(undo);
		if (gs.numberOfHexagons<13)
			this.add(generate);
		this.add(backtoMenu);

		//actions when hex is clicked
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				for(Hexagon h : gs.grid) {
					if(h.getPolygon().contains(e.getPoint())&&!h.clicked) {

						//this bit handles sound effects
						try {
							playSound("src/mixkit-twig-breaking-2945.wav");
						} catch (LineUnavailableException | IOException e1) {
							e1.printStackTrace();
						}


						gs.q.add(h.id);

						switch(gs.whosTurn) {
							case Player1:
								gs.grid.get(h.id).clicked=true;
								h.color=gs.players.get(0).color;
								if (gs.onlineMove != null) gs.onlineMove.makeMove(h.id);
								System.out.println(gs.players.get(0).name + " clicked on hexagon: "+h.id+" score: "+h.score);
								repaint();
								gs.nextTurn();
								paneT.setBackground(gs.paneTColor);
								paneT.setText(gs.paneTurnString);
								if (gs.winingState(gs.startP1, gs.players.get(0).color, gs.winP1)) {
									try {
										playSound("src/mixkit-ethereal-fairy-win-sound-2019.wav");
									} catch (LineUnavailableException | IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									start=false;
									drawExsplosion(graphic);
									repaint();
								}
								
								break;

							case Player2:
								gs.grid.get(h.id).clicked=true;
								h.color=gs.players.get(1).color;
								if (gs.onlineMove != null) gs.onlineMove.makeMove(h.id);
								gs.nextTurn();
								paneT.setBackground(gs.paneTColor);
								paneT.setText(gs.paneTurnString);
								System.out.println(gs.players.get(1).name + " clicked on hexagon: "+h.id+" score: "+h.score);
								repaint();
								if (gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2) && gs.host) {

									try {
										playSound("src/mixkit-ethereal-fairy-win-sound-2019.wav");
									} catch (LineUnavailableException | IOException e1) {
										e1.printStackTrace();
									}
									start=false;
									drawExsplosion(graphic);
									repaint();
								}
								
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

	protected void playSound(String soundFile) throws LineUnavailableException, IOException {
		try {
			File file = new File(soundFile);
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(file);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			// Handle the exception (e.g., display an error message)
		} catch (Exception e) {
			e.printStackTrace();
		}

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
			if(delta >=1&&start||start) {
				checkMouseHover(MouseInfo.getPointerInfo().getLocation());
				if(start) {
					repaint();
					delta--;
					this.graphic=this.getGraphics();
				}
				switch(gs.whosTurn) {

					case AI:
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						int[] move = ai.nextMove(ai.gridToMatrix(gs.grid,gs.numberOfHexagons), gs.players.get(1).color.toString());
						//this bit handles sound effects
						try {
							playSound("src/converted_mixkit-water-sci-fi-bleep-902.wav");
						} catch (LineUnavailableException | IOException e1) {
							e1.printStackTrace();
						}
						int hex = move[0]*gs.numberOfHexagons+move[1];
						gs.grid.get(hex).clicked = true;
						gs.grid.get(hex).color = gs.paneTColor;
						gs.q.add(hex);
						
						repaint();
						if (gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2)) {
							try {
								playSound("src/mixkit-funny-fail-low-tone-2876.wav");
							} catch (LineUnavailableException |IOException e) {
								e.printStackTrace();
							}
							
							start=false;
							drawExsplosion(graphic);
							repaint();
						}
						gs.nextTurn();
						paneT.setBackground(gs.paneTColor);
						paneT.setText(gs.paneTurnString);
						repaint();
						break;

				}

			}
		}
	}

	public void showDiaButton(String name){
		String msg;
		if (!(name==gs.AIname)){
			msg = "HURRAY! " + name + " was victorius!\nUp for a rematch?";
		}else 
			msg = "Sorry " + gs.players.get(0).name + "... \nUp for a rematch?";
		dialogbutton = JOptionPane.showConfirmDialog(null, msg ,"", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
		if (dialogbutton == JOptionPane.YES_OPTION) {
			reset(1);
			
		}else if (dialogbutton == JOptionPane.NO_OPTION) {
			reset(0);
			if(gs.onlineMove != null)gs.online.disconnect(gs.onlineId);
			gs.returnToMenu();
		}
	}

	public static Point componentToScreen(Component component, Point point) {
		Point locationOnScreen = component.getLocationOnScreen();
		return new Point( point.x-locationOnScreen.x ,  point.y-locationOnScreen.y);
	}

	private void checkMouseHover(Point mouse) {
		mouse.setLocation(componentToScreen(this,mouse));
		for(Hexagon h: gs.grid) {
			if(h.getPolygon().contains(mouse)&&!h.clicked) {
				h.color=Color.red;
			}
			else if (!h.clicked&&h.color==Color.red) {
				h.color=Color.gray;
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.img.getImage(),0,0, this.getWidth(), this.getHeight(), this);
		draw(g);	
	}

	public void draw(Graphics g) {
		for (BorderR t : gs.border){
			g.setColor(t.color);
			g.fillPolygon(t.getPolygon());
		}
		for(Hexagon h:gs.grid) {
			g.setColor(h.color);
			g.fillPolygon(h.getPolygon());
			g.setColor(Color.black);
			g.drawPolygon(h.getPolygon());
			if (h.clicked){
				g.setColor(gs.calcTint(h.color));
				g.fillPolygon(h.getPolygonInner());
			}
		}
		if(!gs.exsplosion.isEmpty()) {
			for(int i: gs.exsplosion) {
				Hexagon h = gs.grid.get(i);
				g.setColor(Color.black);
				g.fillOval((int)((h.center.x-(h.radius*1.2)/2)),(int) ((h.center.y-(h.radius*1.2)/2)),(int) (h.radius*1.2), (int)(h.radius*1.2));
			}
		}
		
	}

	private void drawExsplosion(Graphics g) {
		repaint();
		for(Integer h: gs.finalPath) {
			g.setColor(Color.black);
			g.fillOval((int)((gs.grid.get(h).center.x-(gs.radius*1.2)/2)),(int) ((gs.grid.get(h).center.y-(gs.radius*1.2)/2)),(int) (gs.radius*1.2), (int)(gs.radius*1.2));
			gs.exsplosion.add(h);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		showDiaButton(gs.paneTurnString);
		
	}

	@Override
	public void performedMove(int moveId, int playerId) {
		gs.grid.get(moveId).clicked=true;
		gs.q.add(moveId);
		System.out.println(playerId);
		gs.grid.get(moveId).color= gs.players.get(playerId).color;
		if (gs.winingState(gs.startP2, gs.players.get(playerId).color, gs.winP2) && gs.host) {
			repaint();
			showDiaButton(gs.players.get(playerId).name);
		}
		gs.nextTurn();
		paneT.setBackground(gs.paneTColor);
		paneT.setText(gs.paneTurnString);
		repaint();
	}
	@Override
	public void reset(int id) {
		start=true;
		gs.resetGame(id);
		paneT.setText(gs.players.get(0).name);
		paneT.setBackground(gs.players.get(0).color);
		if(gs.onlineMove == null) return;
		if(gs.host) gs.onlineMove.resetGame(0);
		else gs.whosTurn = GameState.Turn.ONLINE_PLAYER;
	}
}