
import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

public class Hexagon extends JPanel{
		
		Color color = Color.red;
		Point center;
		double radius;
		Polygon hexagon;
		int id;
		public Hexagon (Point center, double radius, int id){
			this.center=center;
			this.radius=radius;
			this.hexagon=createHexagon(60);
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
		
<<<<<<< Updated upstream
=======
		public String toString() {
			return Integer.toString(this.id);
		}
		public Point getCenter() {
			int centerX = (int) (center.getX() + (Math.cos(30 * Math.PI / 180) * radius));
			int centerY = (int) (center.getY() + (Math.sin(30 * Math.PI / 180) * radius));
			return new Point(centerX, centerY);
		}
		@Override
		public Hexagon clone() {
		Hexagon hex = new Hexagon(this.center, this.radius, this.id);
		hex.color=this.color;
		hex.clicked=this.clicked;
		hex.adj=this.adj;
		hex.score=this.score;
		
		return hex;
		}
>>>>>>> Stashed changes
		
	}
