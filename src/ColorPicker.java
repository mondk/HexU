import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

// Made by Jónas
//Class to define ColorPicker that allows the user to select a color 
public class ColorPicker extends JPanel {

    // The rectangle showing the color spectrum
    private Rectangle colors = new Rectangle(getHeight(), getWidth());

    // The current color
    private Color color = Color.getHSBColor(0,1,1);

    // X-coordinate indicating the selected color 
    private int circleX;
    @Override
    protected void paintComponent(Graphics g) {
        for(int i = 0; i < colors.getWidth(); i++){

            // Calculate the hue value based on the current position in the rectangle
            int hue = scale(0, (int)colors.getWidth(),0,360, i);
            float realHue = hue/(float)360.0;

            // Setting the color depending on the hue 
            g.setColor(Color.getHSBColor(realHue,1,1));

            // Drawing a line in the current color 
            g.drawLine(i,colors.x,i,colors.height);
        }
        g.setColor(color);

        // Making an orval so the current color on the rectangle is shown 
        g.fillOval(circleX-colors.height/2, colors.y, colors.height,colors.height);

        // White border for the orval
        g.setColor(Color.white);
        g.drawOval(circleX-colors.height/2, colors.y, colors.height,colors.height);
    }

    // Constructor for the ColorPicker class
    ColorPicker(Dimension size){

        // Size of Rectangle 
        colors.setSize(size.width, size.height);

        // Setting the layout of the ColorPicker panel
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPreferredSize(size);

        // Adding a MouseMotionListener to the ColorPicker panel to handle the mouse movements
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                if(colors.contains(mouseEvent.getPoint())){

                    // Update the x-coordinate of the circle based on the mouse position
                    circleX = mouseEvent.getPoint().x;

                    // Calculate the hue value based on the x-coordinate
                    int hue = scale(0, (int)colors.getWidth(),0,360, mouseEvent.getPoint().x);
                    float realHue = hue/(float)360.0;

                    // Update the selected color based on the hue value
                    color = Color.getHSBColor(realHue,1,1);

                    // Repaint the ColorPicker panel according to the changes
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        });

    }
    
    // Getting the selected color
    Color getColor(){
        return color;
    }

    // Method for scaling a value from min to max
    int scale(int ogMin, int ogMax, int min, int max, int original){
        return (original-ogMin)*(max-min)/(ogMax-ogMin)+min;
    }
}
