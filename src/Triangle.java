import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;

import javax.swing.JPanel;

public class Triangle extends JPanel{
    Color color;
    Polygon triangle;

    public Triangle (Color color, int[] x, int[] y){
        this.color = color;
        this.triangle=createTriangle(x, y);
        this.setFocusable(true);
    
    }
    
    //returns a polygon which is a hexagon
    private Polygon createTriangle(int[] x, int[] y) {
        Polygon polygon = new Polygon();
        polygon.addPoint(x[0], y[0]);
        polygon.addPoint(x[1], y[1]);
        polygon.addPoint(x[2], y[2]);
        
        return polygon;
    }

    public Polygon getPolygon(){
        return this.triangle;
    }
}
