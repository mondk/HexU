
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Hexagon extends JPanel{
		
		Color color = Color.gray;
		Point center;
		double radius;
		boolean clicked;
		Polygon hexagon;
		int id;
		public Hexagon (Point center, double radius, int id){
			this.center=center;
			this.radius=radius;
			this.hexagon=createHexagon(60);
			this.clicked=false;
			this.id=id;
			this.setFocusable(true);
		
		}
		
		//returns a polygon which is a hexagon
		private Polygon createHexagon(double theta) {
            Polygon polygon = new Polygon();
            
            for (int i = 0; i < 6; i++) {
                int xval = (int) (center.x + radius* Math.sin(i * 2 * Math.PI / 6D));
                int yval = (int) (center.y + radius* Math.cos(i * 2 * Math.PI / 6D));
                polygon.addPoint(xval ,yval);
            }
          //  rotate(polygon,60);
            return polygon;
        }
		
		public Polygon getPolygon(){
			return this.hexagon;
		}
		public Color getColor() {
			return this.color;
		}
		
		public String toString() {
			return Integer.toString(this.id);
		}
		
	}
