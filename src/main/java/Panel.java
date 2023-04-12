import org.jspace.ActualField;
import org.jspace.FormalField;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;




public class Panel extends JPanel implements Runnable{

	private GameState gs;
	private Image image;
	private Graphics graphics;                            //TextFrame for player turn
	JTextPane paneT = new JTextPane();
	JTextPane winPane = new JTextPane();
	JButton undo = new JButton("Undo");
	JButton generate = new JButton("Generate move");
	boolean start =false;
	int dialogbutton;
	ImageIcon reMatchIcon = new ImageIcon("res/rematch.png");  //  <a target="_blank" href="https://icons8.com/icon/PT3001yzoXgN/match">Match</a> icon by <a target="_blank" href="https://icons8.com">Icons8</a>

	public Panel(GameState gs) {
		this.gs=gs;
		this.setFocusable(true);
		this.setPreferredSize(gs.SCREEN_SIZE);
		createGrid();


		//System.out.println(gs.grid.toString());
		paneT.setText(gs.paneTurnString);
		paneT.setBackground(gs.paneTColor);

		//Button to generate an ai move
		generate.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int i = AI.nextMove(gs);
				for(Hexagon h: gs.grid) {
					if(h.clicked)
						System.out.println(h.id+" is clicked");
				}
				gs.grid.get(i).clicked=true;
				gs.grid.get(i).color=gs.paneTColor;
				gs.q.add(i);

				ArrayList<ArrayList<Integer>> won = new ArrayList<>();
				won = gs.winingState(gs.startP1, gs.colorP1, gs.winP1);
				System.out.println(gs.evaluate(won));
				gs.nextTurn();
				if (won.get(0).get(0)==1) {
					repaint();
					JOptionPane.showConfirmDialog(null, "HURRAY! " + gs.player1Name + " was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
					if (dialogbutton == JOptionPane.YES_OPTION) {
						gs.resetGame();
						paneT.setText(gs.player1Name);
						paneT.setBackground(gs.colorP1);

					}else {
						remove(dialogbutton);
					}
				}
				paneT.setText(gs.paneTurnString);
				paneT.setBackground(gs.paneTColor);
				repaint();


			}

		});
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
		this.add(generate);

		//actions when hex is clicked
		this.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e) {
				for(Hexagon h : gs.grid) {
					if(h.getPolygon().contains(e.getPoint())&&!h.clicked) {
						gs.grid.get(h.id).clicked=true;

						gs.q.add(h.id);

						ArrayList<ArrayList<Integer>> won = new ArrayList<>();
						System.out.println("The turn is " + gs.whosTurn);
						switch(gs.whosTurn) {
							case Player1:
								h.color=gs.colorP1;
								gs.nextTurn();
								if (gs.gameSpace != null) {
									try {
										gs.gameSpace.put("Player1", h.id);
									} catch (InterruptedException ignored) {

									}
								}
								System.out.println(gs.player1Name + " clicked on hexagon: "+h.id+" score: "+h.score);
								won = gs.winingState(gs.startP1, gs.colorP1, gs.winP1);
								System.out.println(gs.evaluate(won));

								if (won.get(0).get(0)==1) {
									repaint();
									JOptionPane.showConfirmDialog(null, "HURRAY! " + gs.player1Name + " was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
									if (dialogbutton == JOptionPane.YES_OPTION) {
										gs.resetGame();
										paneT.setText(gs.player1Name);
										paneT.setBackground(gs.colorP1);
										break;
									}else {
										remove(dialogbutton);
									}
								}
								paneT.setBackground(gs.paneTColor);
								paneT.setText(gs.paneTurnString);
								break;
							case Player2:
								h.color=gs.colorP2;
								gs.nextTurn();
								if (gs.gameSpace != null) {
									try {
										gs.gameSpace.put("Player2", h.id);
									} catch (InterruptedException ignored) {

									}
								}
								System.out.println(gs.player2Name + " clicked on hexagon: "+h.id);
								won = gs.winingState(gs.startP2, gs.colorP2, gs.winP2);
								//System.out.println(won);
								if (won.get(0).get(0)==1) {
									repaint();
									JOptionPane.showConfirmDialog(null, "HURRAY! " + gs.player2Name + " was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
									if (dialogbutton == JOptionPane.YES_OPTION) {
										gs.resetGame();
										paneT.setText(gs.player1Name);
										paneT.setBackground(gs.colorP1);
										break;
									}else {
										remove(dialogbutton);

									}
								}
								paneT.setBackground(gs.paneTColor);
								paneT.setText(gs.paneTurnString);
								break;
						}
						repaint();
						if(gs.playerState == GameState.State.ONLINE) {
							try {
								gs.nextTurn();
								System.out.println("Awaiting move");
								int move;
								if(gs.host) move = (int) gs.gameSpace.get(new ActualField("Player2"), new FormalField(Integer.class))[1];
								else move = (int) gs.gameSpace.get(new ActualField("Player1"), new FormalField(Integer.class))[1];
								//int move = 0;
								System.out.println("Got move");
								h.color = gs.host ? gs.colorP2 : gs.colorP1;
								won = gs.host ? gs.winingState(gs.startP2, gs.colorP2, gs.winP2) : gs.winingState(gs.startP1, gs.colorP1, gs.winP1);
								gs.grid.get(move).clicked=true;
								gs.grid.get(move).color=gs.paneTColor;
								gs.q.add(move);
							} catch (InterruptedException ex) {
								System.out.println("You have lost connection");
							}

						}
					}
				}
				repaint();
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
			if(delta >=1&&start) {
				checkMouseHover(MouseInfo.getPointerInfo().getLocation());
				repaint();
				delta--;

			}
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
			} else if (!h.clicked&&h.color==Color.red) {
				h.color=Color.gray;
			}
		}
	}

	public void createGrid() {
		if (!gs.grid.isEmpty())
			return;
		int id=0;
		int scoreI =0;

		for(int i =0;i<gs.numberOfHexagons;i++) {
			int scoreJ =0;
			if(i==gs.numberOfHexagons/2) {

			} else if(i>gs.numberOfHexagons/2) {
				scoreI--;
			} else {
				scoreI++;
			}
			for(int j =0;j<gs.numberOfHexagons;j++) {
				Hexagon h1 = new Hexagon(new Point((int) (gs.startPoint.x+gs.shift*j+i*gs.shift*Math.cos(60*(Math.PI/180))),(int) (gs.startPoint.y+i*gs.shift*Math.sin(60*(Math.PI/180)))),gs.radius,id);
				if(j==gs.numberOfHexagons/2) {

				} else if(j>gs.numberOfHexagons/2) {
					scoreJ--;
				} else {
					scoreJ++;
				}
				h1.score=scoreI+scoreJ;

				gs.grid.add( h1);
				id++;
				int hex = i*gs.numberOfHexagons+j;

				if (i==0 && j==0) {                                                        //First hexagon
					gs.grid.get(hex).adj.add(1);
					gs.grid.get(hex).adj.add(gs.numberOfHexagons);
				} else if (i==gs.numberOfHexagons-1 && j ==gs.numberOfHexagons-1) {        //Last Hexagon
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
				} else if(i==0 & j==gs.numberOfHexagons-1) {                                //Last hexagon first row
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons-1);
				} else if (i == gs.numberOfHexagons-1 && j ==0) {                            //First hexagon last row
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons+1);
					gs.grid.get(hex).adj.add(hex+1);
				} else if (i==0) {                                                        //Rest of first row
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);;
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons-1);
					gs.grid.get(hex).adj.add(hex+1);
					gs.grid.get(hex).adj.add(hex-1);
				} else if (i==gs.numberOfHexagons-1) {                                    //Rest of last row
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons+1);
					gs.grid.get(hex).adj.add(hex+1);
				} else if (j==0) {                                                        //Rest of first column
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons+1);
					gs.grid.get(hex).adj.add(hex+1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);
				} else if(j==gs.numberOfHexagons-1) {                                        //Rest of last column
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons-1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);
				} else {                                                                    //Everything in between
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex+1);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons+1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);;
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons-1);
				}
				//System.out.println(""+hex + " " + gs.grid.get(hex).adj.toString());
			}
		}
		int[] x= {gs.grid.get(0).center.x-(int) (gs.radius*2), gs.grid.get(gs.numberOfHexagons-1).center.x+(int) (gs.radius*1.3), gs.grid.get(4).center.x};
		int[] y= {gs.grid.get(0).center.y-(int) (gs.radius*1.5), gs.grid.get(gs.numberOfHexagons-1).center.y-(int) (gs.radius*1.5),gs.grid.get(4).center.y};
		gs.border.add(new Triangle(gs.colorP1, x , y));
		// gs.border.add(new Triangle());
		// gs.border.add(new Triangle());
		// gs.border.add(new Triangle());


		gs.fillWinStateArrays();
	}

	@Override
	public void paintComponent(Graphics g) {
		image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);

	}

	public void draw(Graphics g) {

		for (Triangle t : gs.border){
			g.setColor(gs.colorP1);
			g.fillPolygon(t.getPolygon());
		}
		for(Hexagon h:gs.grid) {
			g.setColor(h.color);
			g.fillPolygon(h.getPolygon());
			g.setColor(Color.BLUE);
			g.drawPolygon(h.getPolygon());
		}


	}


}
