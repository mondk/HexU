import java.awt.Color;
import java.awt.Polygon;

import javax.swing.JPanel;

public class BorderR extends JPanel {
    Color color;
    Polygon rectangle;

    public BorderR(Color color, int[] x, int[] y) {
        this.color = color;
        this.rectangle = createRectangle(x, y);
        this.setFocusable(true);
    }

    // Returns a polygon which is a rectangle
    private Polygon createRectangle(int[] x, int[] y) {
        Polygon polygon = new Polygon();
        polygon.addPoint(x[0], y[0]);
        polygon.addPoint(x[1], y[1]);
        polygon.addPoint(x[2], y[2]);
        polygon.addPoint(x[3], y[3]);

        return polygon;
    }

    public Polygon getPolygon() {
        return this.rectangle;
    }

    public void changeColor(Color c){
        this.color = c;
    }
}