import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
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
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

import javax.lang.model.util.ElementScanner6;
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
	boolean start =true;
	int dialogbutton;
	ImageIcon reMatchIcon = new ImageIcon("res/rematch.png");  //  <a target="_blank" href="https://icons8.com/icon/PT3001yzoXgN/match">Match</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>
	boolean singlePlayer;


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
				//this bit handles sound effects
				try {
					playSound("src/converted_mixkit-water-sci-fi-bleep-902.wav");
				} catch (LineUnavailableException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 

				int hex = move[0]*gs.numberOfHexagons+move[1];
				gs.grid.get(hex).clicked = true;
				gs.grid.get(hex).color = gs.paneTColor;
				gs.q.add(hex);
				if(gs.onlineMove != null)gs.onlineMove.makeMove(hex);
				
				switch(gs.whosTurn){
					case Player1:
						if (gs.winingState(gs.startP1, gs.players.get(0).color, gs.winP1)) {
							repaint();
							showDiaButton(gs.players.get(0).name);
						}
					case Player2:
						if (gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2) && gs.host) {
							repaint();
							showDiaButton(gs.players.get(1).name);
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
						paneT.setText(gs.paneTurnString);
						paneT.setBackground(gs.paneTColor);
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
		this.add(paneT);
		this.add(undo);
		this.add(generate);

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

								if (gs.winingState(gs.startP1, gs.players.get(0).color, gs.winP1)) {
									try {
										playSound("src/mixkit-ethereal-fairy-win-sound-2019.wav");
									} catch (LineUnavailableException | IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									repaint();
									showDiaButton(gs.players.get(0).name);
								}
								gs.nextTurn();
								paneT.setBackground(gs.paneTColor);
								paneT.setText(gs.paneTurnString);
								break;
							case Player2:
								gs.grid.get(h.id).clicked=true;
								h.color=gs.players.get(1).color;
								if (gs.onlineMove != null) gs.onlineMove.makeMove(h.id);
								gs.nextTurn();
								paneT.setBackground(gs.paneTColor);
								paneT.setText(gs.paneTurnString);
								System.out.println(gs.players.get(1).name + " clicked on hexagon: "+h.id+" score: "+h.score);
								
								if (gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2) && gs.host) {

									try {
										playSound("src/mixkit-ethereal-fairy-win-sound-2019.wav");
									} catch (LineUnavailableException | IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}

									repaint();
									showDiaButton(gs.players.get(1).name);
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
			if(delta >=1&&start) {
				checkMouseHover(MouseInfo.getPointerInfo().getLocation());
				repaint();
				delta--;
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
						gs.nextTurn();
						paneT.setBackground(gs.paneTColor);
						paneT.setText(gs.paneTurnString);

						if (gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2)) {
							try {
								playSound("src/mixkit-funny-fail-low-tone-2876.wav");
							} catch (LineUnavailableException |IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							repaint();
							showDiaButton(gs.players.get(1).name);
						}
						break;

				}

			}
		}
	}

	public void showDiaButton(String name){
		String msg;
		if (!name.equals("AI")){
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

		//mouse.setLocation(mouse.x-5, mouse.y-20);
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
			if (gs.finalPath.contains(h.id))
				g.setColor(Color.WHITE);
			else
				g.setColor(Color.BLUE);
			g.drawPolygon(h.getPolygon());
			if (h.clicked){
				g.setColor(gs.calcTint(h.color));
				g.fillPolygon(h.getPolygonInner());
			}
		}
	}



	@Override
	public void performedMove(int moveId, int playerId) {
		gs.grid.get(moveId).clicked=true;
		gs.q.add(moveId);
		System.out.println(playerId);
		gs.grid.get(moveId).color= gs.players.get(playerId).color;
		gs.nextTurn();
		paneT.setBackground(gs.paneTColor);
		paneT.setText(gs.paneTurnString);

		//System.out.println(won);
		if (gs.winingState(gs.startP2, gs.players.get(playerId).color, gs.winP2) && gs.host) {
			repaint();
			showDiaButton(gs.players.get(playerId).name);
		}
		repaint();
	}
	@Override
	public void reset(int id) {
		gs.resetGame(id);
		paneT.setText(gs.players.get(0).name);
		paneT.setBackground(gs.players.get(0).color);
		if(gs.onlineMove == null) return;
		if(gs.host) gs.onlineMove.resetGame(0);
		else gs.whosTurn = GameState.Turn.ONLINE_PLAYER;
	}
}