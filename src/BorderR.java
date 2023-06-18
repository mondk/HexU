import java.awt.Color;
import java.awt.Polygon;

import javax.swing.JPanel;

// Made by Caroline
// Class to draw the rectangles that are used to make the colored borders on the board.
public class BorderR extends JPanel {
    Color color;
    Polygon rectangle;

    // Collect the current color and coordinates for the border
    public BorderR(Color color, int[] x, int[] y) {
        this.color = color;
        this.rectangle = createRectangle(x, y);
        this.setFocusable(true);
    }

    // Returns a polygon which is a rectangle made with 4 points 
    // Input: 2 arrays, one for x coordinate and y coordinates
    // Returns: Rectangle made from the coordinates in the 2 arrys
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

    // Changing the color to the current color
    public void changeColor(Color c){
        this.color = c;
    }
}