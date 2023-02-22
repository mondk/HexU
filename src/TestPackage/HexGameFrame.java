package TestPackage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class HexGameFrame {
	//stores grid
	ArrayList<Hexagon> grid = new ArrayList<>();
	public  HexGameFrame(){
		
		JFrame frame = new JFrame();
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Hex game test");
		frame.setBackground(Color.black);
		
		double radius =20;
		
		//number of hexagons
		int n = 9;
		int id =1;
	
		//distance between centers in a horizontal line
		double shift = 2*radius*0.8660254;
		
		//create grid
		for(int i =0;i<n;i++) {
			for(int j =0;j<n;j++) {
				grid.add( new Hexagon(new Point((int) (200+shift*j+i*shift*Math.cos(60*(Math.PI/180))),(int) (200+i*shift*Math.sin(60*(Math.PI/180)))),radius,id));
				id++;
			}
		}
		JPanel panel = new JPanel() {
			
		
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
               
                
                //draws every hexagon
               for(Hexagon h : grid) {
            	   g.setColor(h.color);
            	   g.fillPolygon(h.getHexagon());
            	   g.setColor(Color.BLUE);
            	   g.drawPolygon(h.getHexagon());
            	  
               }
            }
        };
      
		panel.setPreferredSize(new Dimension(600,600));
		panel.setBackground(Color.black);
		panel.addMouseListener(new MouseAdapter(){
	         public void mouseClicked(MouseEvent e) {
	        	 for(Hexagon h: grid) {
	 				if(h.getHexagon().contains(e.getPoint())) {
	 					h.color=Color.cyan;
	 					System.out.println("Clicked on hexagon: "+h.id);
	 				
	 				}
	 			}
	          }                
	       });
		frame.add(panel);
		frame.setVisible(true);
		frame.pack();
		panel.setFocusable(true);
		
		
		
	}
	
	public class ML extends MouseAdapter{
		
		@Override
		public void mouseClicked(MouseEvent e) {
			System.out.println("Clicked");
			for(Hexagon h: grid) {
				if(h.contains(e.getPoint())) {
					h.color=Color.cyan;
					System.out.println("Clicked on hexagon: "+h.id);
				}
			}
			
		}
	}
	
	


}