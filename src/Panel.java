import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
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


// Initially made by Jasper 
// Edited/expanded by the whole group

// Class called Panel that represents the panel in the GUI application.
// It handles the user interactions e.g. button clicks and mouse events.

public class Panel extends JPanel implements Runnable, MoveListener{

	// Defining buttons and variables used in the class
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
	ImageIcon reMatchIcon = new ImageIcon(this.getClass().getResource("res/rematch.png"));  //  <a target="_blank" href="https://icons8.com/icon/PT3001yzoXgN/match">Match</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
	boolean singlePlayer;
	int once = 0;
	private Graphics graphic;

	// Constructor for the class Panel
	// Initialize the panel and its components,
	// set event listeners for buttons and mouse clicks
	// and start a new thread to run the game loop
	public Panel(GameState gs) {
		this.gs=gs;
		this.img = new ImageIcon(this.getClass().getResource(gs.randomBackground()));
		this.singlePlayer=gs.singlePlayer;
		this.setFocusable(true);
		this.setPreferredSize(gs.SCREEN_SIZE);
		gs.createGrid();
		this.ai = new AI(gs);
		
		paneT.setText(gs.paneTurnString);
		paneT.setBackground(gs.paneTColor);

		//Button to generate an AI move
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

				// The follwing code handles sound effects
				try {
					playSound("res/converted_mixkit-water-sci-fi-bleep-902.wav");
				} catch (LineUnavailableException | IOException e1) {
					e1.printStackTrace();
				} 

				try {
					Thread.sleep(hex);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				switch(gs.whosTurn){
					case Player1:
						repaint();
						if (gs.winingState(gs.startP1, gs.players.get(0).color, gs.winP1)) {
							try {
								playSound("res/mixkit-ethereal-fairy-win-sound-2019.wav");
							} catch (LineUnavailableException | IOException e1) {
								e1.printStackTrace();
							}
							
							start=false;
							drawExsplosion(graphic);
							break;
						}
					case Player2:
						if (gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2)) {
							repaint();
							try {
								playSound("res/mixkit-ethereal-fairy-win-sound-2019.wav");
							} catch (LineUnavailableException | IOException e1) {
								e1.printStackTrace();
							}
							start=false;
							drawExsplosion(graphic);
							break;
						}
				}
				// Switching player turn
				gs.nextTurn();
				paneT.setText(gs.paneTurnString);
				paneT.setBackground(gs.paneTColor);
				repaint();
			}
		});
		// Defines actions for when the undo button is pressed
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
		// Hides the undo button in online mode
		undo.setVisible(gs.onlineMove == null);
		backtoMenu.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				if (gs.waitingRoom != null)gs.disconnectFromOnline();
				gs.resetGame(0);
				gs.returnToMenu();
				
			}
		});

		//Adding buttons for when the game has started
		this.add(paneT);
		this.add(undo);
		if (gs.numberOfHexagons<13)
			this.add(generate);
		this.add(backtoMenu);

		// Actions when a hex is clicked
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				for(Hexagon h : gs.grid) {
					if(h.getPolygon().contains(e.getPoint())&&!h.clicked) {

						// This part handles sound effects
						try {
							playSound("res/mixkit-twig-breaking-2945.wav");
						} catch (LineUnavailableException | IOException e1) {
							e1.printStackTrace();
						}


						gs.q.add(h.id);

						switch(gs.whosTurn) {
							
							// Actions for when player 1 clicks on a hex
							case Player1:
								h.clicked=true;
								h.color=gs.players.get(0).color;
								if (gs.onlineMove != null) gs.onlineMove.makeMove(h.id);
								repaint();
								
								// Check if player 1 won with the current move
								if (gs.winingState(gs.startP1, gs.players.get(0).color, gs.winP1)) {
									try {
										playSound("res/mixkit-ethereal-fairy-win-sound-2019.wav");
									} catch (LineUnavailableException | IOException e1) {
										e1.printStackTrace();
									}
									
									start=false;
									drawExsplosion(graphic);
									repaint();
									break;

								// Switching turn to player 2 if player 1 didn't win
								}else{
									gs.nextTurn();
									paneT.setBackground(gs.paneTColor);
									paneT.setText(gs.paneTurnString);
									repaint();
									break;
								}
							
							// Actions for when player 2 clicks on a hex
							case Player2:
								h.clicked=true;
								h.color=gs.players.get(1).color;
								if (gs.onlineMove != null) gs.onlineMove.makeMove(h.id);
								repaint();

								// Check if player 2 won with the current move
								if (gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2)) {

									try {
										playSound("res/mixkit-ethereal-fairy-win-sound-2019.wav");
									} catch (LineUnavailableException | IOException e1) {
										e1.printStackTrace();
									}
									
									start=false;
									drawExsplosion(graphic);
									repaint();
									break;
								}
									// Switching turn to player 1 if player 2 didn't win
									gs.nextTurn();
									paneT.setBackground(gs.paneTColor);
									paneT.setText(gs.paneTurnString);
									repaint();
									break;
								
							case ONLINE_PLAYER:
								System.out.println("It is not your turn");
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

	// Method for playing sound effects in the game 
	protected void playSound(String soundFile) throws LineUnavailableException, IOException {
		try {
			BufferedInputStream fileName = new BufferedInputStream(this.getClass().getResourceAsStream(soundFile));
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(fileName);
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	// Method that represents the game loop
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks =60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		while(true) {
			long now = System.nanoTime();
			delta += (now -lastTime)/ns;
			lastTime = now;
			checkMouseHover(MouseInfo.getPointerInfo().getLocation());
			if(delta >=1&&start||start) {
				// Updating the game logic and repainting the panel
				
				if(start) {
					repaint();
					delta--;
					this.graphic=this.getGraphics();
				}
				switch(gs.whosTurn) {

					// Generate AI's move and update the game state
					case AI:
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						int[] move = ai.nextMove(ai.gridToMatrix(gs.grid,gs.numberOfHexagons), gs.players.get(1).color.toString());
						// This part handles sound effects
						try {
							playSound("res/converted_mixkit-water-sci-fi-bleep-902.wav");
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
								playSound("res/mixkit-funny-fail-low-tone-2876.wav");
							} catch (LineUnavailableException |IOException e) {
								e.printStackTrace();
							}
							repaint();
							start=false;
							drawExsplosion(graphic);
							break;
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

	// Pop-up message for when the game is over, 
	// with the option for a rematch or return to the start menu
	public void showDiaButton(String name){
		String msg;
		if (!(name==gs.AIname)){
			msg = "HURRAY! " + name + " was victorius!\nUp for a rematch?";
		}else 
			msg = "Sorry " + gs.players.get(0).name + "... \nUp for a rematch?";
		dialogbutton = JOptionPane.showConfirmDialog(null, msg ,"", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
		// If yes is clicked the game is started over with the same options
		if (!(dialogbutton == JOptionPane.NO_OPTION)) {
			reset(1);
			repaint();
			
		// If no is clicked the player is returned to the menu,
		// online game is disconnected
		}else{
			reset(0);
			if(gs.onlineMove != null)gs.online.disconnect(gs.onlineId);
			gs.returnToMenu();
		} 
	}

	public static Point componentToScreen(Component component, Point point) {
		Point locationOnScreen = new Point(0,0);
		try {
			locationOnScreen = component.getLocationOnScreen();
		} catch (IllegalComponentStateException e){}
		return new Point( point.x-locationOnScreen.x ,  point.y-locationOnScreen.y);
	}

	// Method for highlighting (in redd) the hex that the mouse hovers over
	private void checkMouseHover(Point mouse) {
		mouse.setLocation(componentToScreen(this,mouse));
		for(Hexagon h: gs.grid) {

			// If mouse hovers and isn't claimed in game, color = red
			if(h.getPolygon().contains(mouse)&&!h.clicked) {
				h.color=Color.red;
			}

			// If mouse don't hover and isn't claimed in game, color = grey 
			else if (!h.clicked&&h.color==Color.red) {
				h.color=Color.gray;
			}
		}
	}

	@Override
	// Method for painting the different components in the game
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.img.getImage(),0,0, this.getWidth(), this.getHeight(), this);
		draw(g);	
	}

	public void draw(Graphics g) {
		// Drawing the border
		for (BorderR t : gs.border){
			g.setColor(t.color);
			g.fillPolygon(t.getPolygon());
		}
		// Drawing the hexagons 
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
		// Drawing final path that won the game
		// (the outlines of the hexagons in the path)
		if(!gs.exsplosion.isEmpty()) {
			for(int i: gs.exsplosion) {
				Hexagon h = gs.grid.get(i);
				g.setColor(h.color);
				g.fillPolygon(h.getPolygon());
				g.setColor(gs.calcTint(h.color));
				g.fillPolygon(h.getPolygonInner());
				g.setColor(gs.calcComplementColor(h.color));
				drawThickHexagon((Graphics2D) g,h.center.x,h.center.y,(int) h.radius);
			}
		}
		
	}

	// Method for drawing the winning path that won the game
	private void drawExsplosion(Graphics g) {
		for(Integer h: gs.finalPath) {
			Hexagon q = gs.grid.get(h);
			g.setColor(q.color);
			g.fillPolygon(q.getPolygon());
			g.setColor(Color.black);
			g.drawPolygon(q.getPolygon());
			g.setColor(gs.calcTint(q.color));
			g.fillPolygon(q.getPolygonInner());
		}

		for(Integer h: gs.finalPath) {
			Hexagon q = gs.grid.get(h);
			g.setColor(gs.calcComplementColor(q.color));
			gs.exsplosion.add(h);
			drawThickHexagon((Graphics2D) g,q.center.x,q.center.y,(int) q.radius);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
		if (gs.host)showDiaButton(gs.paneTurnString);
		
	}
	// Making the outlined hexagons
	public static void drawThickHexagon(Graphics2D g2d, int centerX, int centerY, int radius) {
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = 2 * Math.PI * i / 6;
            xPoints[i] = (int) (centerX + radius * Math.sin(angle));
            yPoints[i] = (int) (centerY + radius * Math.cos(angle));
        }
        
        // Set a thicker stroke
        int thickness = 5; // Adjust the thickness value as desired
        g2d.setStroke(new BasicStroke(thickness));
        
        // Draw the hexagon
        g2d.drawPolygon(xPoints, yPoints, 6);
		g2d.setStroke(new BasicStroke(1));
    }

	@Override
	// Handle the performed move event
    // Update the game state based on the move
	public void performedMove(int moveId, int playerId) {
		gs.grid.get(moveId).clicked=true;
		gs.q.add(moveId);
		System.out.println(playerId);
		gs.grid.get(moveId).color= gs.players.get(playerId).color;
		if (gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2) || gs.winingState(gs.startP1, gs.players.get(0).color, gs.winP1)) {
			repaint();
			drawExsplosion(graphic);
			return;
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
	}
}