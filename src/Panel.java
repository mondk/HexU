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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JButton;




public class Panel extends JPanel implements Runnable{
	
	private GameState gs;
	private Image image;
	private Graphics graphics;							//TextFrame for player turn
	Point hexCenter1;
	Point hexCenter2;
	Point hexCenter3;
	Point hexCenter4;
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
		this.singlePlayer=gs.singlePlayer;
		this.setFocusable(true);
		this.setPreferredSize(gs.SCREEN_SIZE);
		createGrid();
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
				} catch (LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int hex = move[0]*gs.numberOfHexagons+move[1];
				gs.grid.get(hex).clicked = true;
				gs.grid.get(hex).color = gs.paneTColor;
				gs.q.add(hex);
				ArrayList<ArrayList<Integer>> won = new ArrayList<>();
				switch(gs.whosTurn){
					case Player1:
						won = gs.winingState(gs.startP1, gs.players.get(0).color, gs.winP1);
						if (won.get(0).get(0)==1) {
							repaint();
							JOptionPane.showConfirmDialog(null, "HURRAY! " + gs.players.get(0).name + " was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
							if (dialogbutton == JOptionPane.YES_OPTION) {
								gs.resetGame();
								paneT.setText(gs.players.get(0).name);
								paneT.setBackground(gs.players.get(0).color);
								
							}else {
								remove(dialogbutton);
								
							}
						}
					case Player2:
						won = gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2);
						if (won.get(0).get(0)==1) {
							repaint();
							JOptionPane.showConfirmDialog(null, "HURRAY! " + gs.players.get(1).name + " was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
							if (dialogbutton == JOptionPane.YES_OPTION) {
								gs.resetGame();
								paneT.setText(gs.players.get(1).name);
								paneT.setBackground(gs.players.get(1).color);
								
							}else {
								remove(dialogbutton);
							}
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
	 					
	 					//this bit handles sound effects
	 					try {
							playSound("src/mixkit-twig-breaking-2945.wav");
						} catch (LineUnavailableException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
	 					
	 					gs.grid.get(h.id).clicked=true;
	 					
						gs.q.add(h.id);

						ArrayList<ArrayList<Integer>> won = new ArrayList<>();

	 					switch(gs.whosTurn) {
	 					case Player1:
	 						h.color=gs.players.get(0).color;
	 						gs.nextTurn();
	 						
	 						System.out.println(gs.players.get(0).name + " clicked on hexagon: "+h.id+" score: "+h.score);
							won = gs.winingState(gs.startP1, gs.players.get(0).color, gs.winP1);
							
							
	 						if (won.get(0).get(0)==1) {
	 							
	 							try {
									playSound("src/mixkit-ethereal-fairy-win-sound-2019.wav");
								} catch (LineUnavailableException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	 							repaint();
	 							JOptionPane.showConfirmDialog(null, "HURRAY! " + gs.players.get(0).name + " was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
	 		 					if (dialogbutton == JOptionPane.YES_OPTION) {
	 		 						gs.resetGame();
	 		 						paneT.setText(gs.players.get(0).name);
									paneT.setBackground(gs.players.get(0).color);
	 		 						break;
	 		 					}else {
	 		 						remove(dialogbutton);
	 		 					}
	 						}
	 						paneT.setBackground(gs.paneTColor);
	 						paneT.setText(gs.paneTurnString);
	 						break;
	 					case Player2:
	 						h.color=gs.players.get(1).color;
	 						gs.nextTurn();
	 						System.out.println(gs.players.get(1).name + " clicked on hexagon: "+h.id+" score: "+h.score);
							won = gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2);
							//System.out.println(won);
	 						if (won.get(0).get(0)==1) {
	 							
	 							try {
									playSound("src/mixkit-ethereal-fairy-win-sound-2019.wav");
								} catch (LineUnavailableException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (IOException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
	 							
	 							repaint();
	 							JOptionPane.showConfirmDialog(null, "HURRAY! " + gs.players.get(1).name + " was victorius!\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
	 							if (dialogbutton == JOptionPane.YES_OPTION) {
	 		 						gs.resetGame();
	 		 						paneT.setText(gs.players.get(0).name);
									paneT.setBackground(gs.players.get(0).color);
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
	 				}
	 			}
	          }                
	       });
		Thread gameThread = new Thread(this);
		gameThread.start();
	}

	
	

	protected void playSound(String soundFile) throws LineUnavailableException, IOException {
		// TODO Auto-generated method stub
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
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int[] move = ai.nextMove(ai.gridToMatrix(gs.grid,gs.numberOfHexagons), gs.players.get(1).color.toString());
					//this bit handles sound effects
					try {
					playSound("src/converted_mixkit-water-sci-fi-bleep-902.wav");
				} catch (LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int hex = move[0]*gs.numberOfHexagons+move[1];
				gs.grid.get(hex).clicked = true;
				gs.grid.get(hex).color = gs.paneTColor;
				gs.q.add(hex);
				gs.nextTurn();
				
				ArrayList<ArrayList<Integer>> won = new ArrayList<>();
				
				won = gs.winingState(gs.startP2, gs.players.get(1).color, gs.winP2);
				if (won.get(0).get(0)==1) {
					try {
						playSound("src/mixkit-funny-fail-low-tone-2876.wav");
					} catch (LineUnavailableException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					repaint();
					JOptionPane.showConfirmDialog(null, "Sorry! " + gs.players.get(0).name + " you lost...\nUp for a rematch?","", JOptionPane.YES_NO_OPTION, dialogbutton,reMatchIcon);
					if (dialogbutton == JOptionPane.YES_OPTION) {
						gs.resetGame();
						paneT.setText(gs.players.get(0).name);
						paneT.setBackground(gs.players.get(0).color);
						
					}else {
						remove(dialogbutton);
					}
				}
					break;
				
				}

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
			}
			else if (!h.clicked&&h.color==Color.red) {
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
				
			}
			else if(i>gs.numberOfHexagons/2) {
				scoreI--;
			}
			else {
				scoreI++;
			}
			for(int j =0;j<gs.numberOfHexagons;j++) {
				Hexagon h1 = new Hexagon(new Point((int) (gs.startPoint.x+gs.shift*j+i*gs.shift*Math.cos(60*(Math.PI/180))),(int) (gs.startPoint.y+i*gs.shift*Math.sin(60*(Math.PI/180)))),gs.radius,id);
				if(j==gs.numberOfHexagons/2) {
					
				}
				else if(j>gs.numberOfHexagons/2) {
					scoreJ--;
				}
				else {
					scoreJ++;
				}
				h1.score=scoreI+scoreJ;
				
				gs.grid.add( h1);
				id++;
				int hex = i*gs.numberOfHexagons+j;
				
				if (i==0 && j==0) {														//First hexagon
					gs.grid.get(hex).adj.add(1);
					gs.grid.get(hex).adj.add(gs.numberOfHexagons);
					hexCenter1 = gs.grid.get(hex).getCenter();
				}
				else if (i==gs.numberOfHexagons-1 && j ==gs.numberOfHexagons-1) {		//Last Hexagon
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					hexCenter3 = gs.grid.get(hex).getCenter();
				}
				else if(i==0 & j==gs.numberOfHexagons-1) {								//Last hexagon first row
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons-1);
					hexCenter2 = gs.grid.get(hex).getCenter();
				}
				else if (i == gs.numberOfHexagons-1 && j ==0) {							//First hexagon last row
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons+1);
					gs.grid.get(hex).adj.add(hex+1);
					hexCenter4 = gs.grid.get(hex).getCenter();
				}
				else if (i==0) {														//Rest of first row
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);;
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons-1);
					gs.grid.get(hex).adj.add(hex+1);
					gs.grid.get(hex).adj.add(hex-1);
				}
				else if (i==gs.numberOfHexagons-1) {									//Rest of last row
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons+1);
					gs.grid.get(hex).adj.add(hex+1);
				}
				else if (j==0) {														//Rest of first column
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons+1);
					gs.grid.get(hex).adj.add(hex+1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);
				}
				else if(j==gs.numberOfHexagons-1) {										//Rest of last column
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons-1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);
				}
				else {																	//Everything in between
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex+1);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons+1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons-1);
				}
			}
		}

		//Colors for borders, indicating players sides
		int xp1 = (int) Math.round(hexCenter1.getX());
		int xp2 = (int) Math.round(hexCenter2.getX());
		int xp3 = (int) Math.round(hexCenter3.getX());
		int xp4 = (int) Math.round(hexCenter4.getX());
		int yp1 = (int) Math.round(hexCenter1.getY());
		int yp2 = (int) Math.round(hexCenter2.getY());
		int yp3 = (int) Math.round(hexCenter3.getY());
		int yp4 = (int) Math.round(hexCenter4.getY());
		//int rInt = (int) Math.round(gs.radius*1.5);
		int[] x1= {xp1-(int) Math.round(gs.radius)+3,xp2-(int) Math.round(gs.radius*0.75)-3,xp2-(int) Math.round(gs.radius*0.75)-3,xp1-(int) Math.round(gs.radius)+3};
		int[] x2= {xp3-(int) Math.round(gs.radius*0.75)-3,xp4-(int) Math.round(gs.radius)+3,xp4-(int) Math.round(gs.radius)+3,xp3-(int) Math.round(gs.radius*0.75)-3};
		int[] x3= {xp1-(int) Math.round(gs.radius*1.75),xp1-(int) Math.round(gs.radius),xp4-(int) Math.round(gs.radius),xp4-(int) Math.round(gs.radius*1.75)};
		int[] x4= {xp2-(int) Math.round(gs.radius),xp2,xp3,xp3-(int) Math.round(gs.radius)};
		int[] y1= {yp1-(int) Math.round(gs.radius*1.5)+2,yp2-(int) Math.round(gs.radius*1.5)+2,yp2,yp1};
		int[] y2= {yp3,yp4,yp4+(int) Math.round(gs.radius*0.5),yp3+(int) Math.round(gs.radius*0.5)};
		int[] y3= {yp1,yp1,yp4,yp4};
		int[] y4= {yp2-(int) Math.round(gs.radius),yp2-(int) Math.round(gs.radius),yp3-(int) Math.round(gs.radius),yp3-(int) Math.round(gs.radius)};

		gs.border.add(new BorderR(gs.players.get(0).color, x1 , y1)); //top
		gs.border.add(new BorderR(gs.players.get(0).color, x2 , y2)); //buttom
		gs.border.add(new BorderR(gs.players.get(1).color, x3 , y3)); //left
		gs.border.add(new BorderR(gs.players.get(1).color, x4 , y4)); //right
	
		gs.fillLoadMoves(gs.load);
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
		
		for (BorderR t : gs.border){
			g.setColor(t.color);
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
