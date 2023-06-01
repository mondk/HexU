import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JTextPane;
<<<<<<< Updated upstream
=======
import java.util.ArrayList;
import java.util.Random;

import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
>>>>>>> Stashed changes




public class Panel extends JPanel{
	
	private GameState gs;
	private Image image;
<<<<<<< Updated upstream
	private Graphics graphics;//TextFrame for player turn
=======
	private Graphics graphics;							//TextFrame for player turn
	Point hexCenter1;
	Point hexCenter2;
	Point hexCenter3;
	Point hexCenter4;
>>>>>>> Stashed changes
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
	 				if(h.getPolygon().contains(e.getPoint())) {
	 					
	 					switch(gs.whosTurn) {
	 					case Player1:
	 						h.color=Color.PINK;
	 						gs.nextTurn();
	 						System.out.println("Player 1 clicked on hexagon: "+h.id);
	 						paneT.setText("Player 2");
	 						break;
	 					case Player2:
	 						h.color=Color.GREEN;
	 						gs.nextTurn();
	 						System.out.println("Player 2 clicked on hexagon: "+h.id);
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
				int id=1;
				for(int i =0;i<gs.numberOfHexagons;i++) {
					for(int j =0;j<gs.numberOfHexagons;j++) {
						gs.grid.add( new Hexagon(new Point((int) (gs.startPoint.x+gs.shift*j+i*gs.shift*Math.cos(60*(Math.PI/180))),(int) (gs.startPoint.y+i*gs.shift*Math.sin(60*(Math.PI/180)))),gs.raidus,id));
						id++;
					}
				}
<<<<<<< Updated upstream
=======
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
					System.out.println("1"+hexCenter1);
				}
				else if (i==gs.numberOfHexagons-1 && j ==gs.numberOfHexagons-1) {		//Last Hexagon
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					hexCenter3 = gs.grid.get(hex).getCenter();
					System.out.println("3"+hexCenter3);
				}
				else if(i==0 & j==gs.numberOfHexagons-1) {								//Last hexagon first row
					gs.grid.get(hex).adj.add(hex-1);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex+gs.numberOfHexagons-1);
					hexCenter2 = gs.grid.get(hex).getCenter();
					System.out.println("2"+hexCenter2);
				}
				else if (i == gs.numberOfHexagons-1 && j ==0) {							//First hexagon last row
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons);
					gs.grid.get(hex).adj.add(hex-gs.numberOfHexagons+1);
					gs.grid.get(hex).adj.add(hex+1);
					hexCenter4 = gs.grid.get(hex).getCenter();
					System.out.println("4"+hexCenter4);
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
				//System.out.println(""+hex + " " + gs.grid.get(hex).adj.toString());
			}
		}
		//Colors for borders, indicating players sides
		int halfHex = gs.numberOfHexagons/2;
		/*
		int[] x1= {gs.grid.get(0).center.x-(int) (gs.radius*(halfHex-1.1)), //x[0]
			gs.grid.get(gs.numberOfHexagons-1).center.x+(int) (gs.radius*(halfHex-1.1)), //x[1]
			gs.grid.get(0).center.x-(int) (gs.radius*(halfHex-1.1)), //x[2]
			gs.grid.get(gs.numberOfHexagons-1).center.x+(int) (gs.radius*(halfHex-1.1))};//x[3]
		int[] y1= {50, //y[0]
			50, //y[1]
			80, //y[2]
			80};//y[3]
			
			
			int[] x2= {gs.grid.get(0).center.x-(int) (gs.radius*(halfHex-1.1)-gs.radius), //x[0]
			gs.grid.get(gs.numberOfHexagons-1).center.x+(int) (gs.radius*(halfHex)), //x[1]
			gs.grid.get(0).center.x-(int) (gs.radius*(halfHex-1.1)-gs.radius), //x[2]
			gs.grid.get(gs.numberOfHexagons-1).center.x+(int) (gs.radius*(halfHex))};//x[3]

		int[] y2= {gs.grid.get(gs.numberOfHexagons).center.y+(int) (gs.radius*(halfHex)+gs.radius), //y[0]
			gs.grid.get(gs.numberOfHexagons).center.y+(int) (gs.radius*(halfHex)+gs.radius),//y[1]
			gs.grid.get(gs.numberOfHexagons).center.y+(int) (gs.radius*(halfHex)+gs.radius+gs.radius), //y[2]
			gs.grid.get(gs.numberOfHexagons).center.y+(int) (gs.radius*(halfHex)+gs.radius+gs.radius)};//y[3]

		*/
		//Point hc1 = hexCenter1;
		int xp1 = (int) Math.round(hexCenter1.getX());
		int xp2 = (int) Math.round(hexCenter2.getX());
		int xp3 = (int) Math.round(hexCenter3.getX());
		int xp4 = (int) Math.round(hexCenter4.getX());
		int yp1 = (int) Math.round(hexCenter1.getY());
		int yp2 = (int) Math.round(hexCenter2.getY());
		int yp3 = (int) Math.round(hexCenter3.getY());
		int yp4 = (int) Math.round(hexCenter4.getY());
		//int rInt = (int) Math.round(gs.radius*1.5);
		int[] x1= {xp1-(int) Math.round(gs.radius),xp2-(int) Math.round(gs.radius*0.75),xp2-(int) Math.round(gs.radius*0.75),xp1-(int) Math.round(gs.radius)};
		int[] x2= {xp3-(int) Math.round(gs.radius*0.75),xp4-(int) Math.round(gs.radius),xp4-(int) Math.round(gs.radius),xp3-(int) Math.round(gs.radius*0.75)};
		int[] x3= {xp1-(int) Math.round(gs.radius*1.75),xp1-(int) Math.round(gs.radius),xp4-(int) Math.round(gs.radius),xp4-(int) Math.round(gs.radius*1.75)};
		int[] x4= {xp2-(int) Math.round(gs.radius),xp2,xp3,xp3-(int) Math.round(gs.radius)};
		int[] y1= {yp1-(int) Math.round(gs.radius*1.5),yp2-(int) Math.round(gs.radius*1.5),yp2,yp1};
		int[] y2= {yp3,yp4,yp4+(int) Math.round(gs.radius*0.5),yp3+(int) Math.round(gs.radius*0.5)};
		int[] y3= {yp1,yp1,yp4,yp4};
		int[] y4= {yp2-(int) Math.round(gs.radius),yp2-(int) Math.round(gs.radius),yp3-(int) Math.round(gs.radius),yp3-(int) Math.round(gs.radius)};



		for (int element: x3) {
            System.out.println("x-value: " + element);
        }
		for (int element: y3) {
            System.out.println("y-value: " + element);
        }
		System.out.println("radius:"+ gs.radius);
		System.out.println("center,nul:"+ gs.grid.get(0).center.x);
		System.out.println("center,fire:"+ gs.grid.get(4).center.x);

		//System.out.println("x-value for boarder:" + Arrays.toString(x));
		//System.out.println("y-value for boarder:"+y);
		
		
		gs.border.add(new Rectangle(gs.colorP1, x1 , y1)); //top
		gs.border.add(new Rectangle(gs.colorP1, x2 , y2)); //bund
		gs.border.add(new Rectangle(gs.colorP2, x3 , y3)); //venstre
		gs.border.add(new Rectangle(gs.colorP2, x4 , y4)); //højre
		// gs.border.add(new Triangle());


		
		gs.fillWinStateArrays();
>>>>>>> Stashed changes
	}
	
	@Override
	public void paintComponent(Graphics g) {
		image = createImage(getWidth(),getHeight());
		graphics = image.getGraphics();
		draw(graphics);
		g.drawImage(image,0,0,this);
		
	}
	
	/*
	public void draw(Graphics g) {
		for(Hexagon h:gs.grid) {
			   g.setColor(h.color);
	       	   g.fillPolygon(h.getPolygon());
	       	   g.setColor(Color.BLUE);
	       	   g.drawPolygon(h.getPolygon());
	       	  
			}
	}
	*/
	

	public void draw(Graphics g) {
		
		for (Rectangle t : gs.border){
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
