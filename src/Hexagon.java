//@author Jasper
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Hexagon extends JPanel{
		
		Color color = Color.gray;
		Point center;
		double radius;
		boolean clicked;
		Polygon hexagon;
		Polygon hexagon_inner;
		int id;
		double score = 1;
		ArrayList<Integer> adj = new ArrayList<>();
		public Hexagon (Point center, double radius, int id){
			this.center=center;
			this.radius=radius;
			createHexagon(60);
			this.clicked=false;
			this.id=id;
			this.setFocusable(true);

		
		}
		
		//Creates a hexagon and stores it as a polygon within the object
		private void createHexagon(double theta) {
            Polygon polygon = new Polygon();
			Polygon polygon_inner =new Polygon();
            
            for (int i = 0; i < 6; i++) {
                int xval = (int) (center.x + radius* Math.sin(i * 2 * Math.PI / 6D));
                int yval = (int) (center.y + radius* Math.cos(i * 2 * Math.PI / 6D));
				if (i<5 && i>0){
					int xval_inner = (int) (center.x + (radius*0.75)* Math.sin(i * 2 * Math.PI / 6D));
					int yval_inner = (int) (center.y + (radius*0.75)* Math.cos(i * 2 * Math.PI / 6D));
					polygon_inner.addPoint(xval_inner, yval_inner);
				}
                polygon.addPoint(xval ,yval);
				
            }
          //  rotate(polygon,60);
			this.hexagon = polygon;
			this.hexagon_inner = polygon_inner;
        }
		
		
		public Polygon getPolygon(){
			return this.hexagon;
		}
		public Polygon getPolygonInner(){
			return this.hexagon_inner;
		}
		public Color getColor() {
			return this.color;
		}
		
		public String toString() {
			return Integer.toString(this.id);
		}
		public Point getCenter() {
			int centerX = (int) (center.getX());
			int centerY = (int) (center.getY());
			return new Point(centerX, centerY);
		}
	
		
	}
