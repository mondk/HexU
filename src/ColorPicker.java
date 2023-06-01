import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ColorPicker extends JPanel {
    private Rectangle colors = new Rectangle(getHeight(), getWidth());
    private Color color = Color.getHSBColor(0,1,1);
    private int circleX;
    @Override
    protected void paintComponent(Graphics g) {
        for(int i = 0; i < colors.getWidth(); i++){
            int hue = scale(0, (int)colors.getWidth(),0,360, i);
            float realHue = hue/(float)360.0;
            g.setColor(Color.getHSBColor(realHue,1,1));
            g.drawLine(i,colors.x,i,colors.height);
        }
        g.setColor(color);
        g.fillOval(circleX-colors.height/2, colors.y, colors.height,colors.height);
        g.setColor(Color.white);
        g.drawOval(circleX-colors.height/2, colors.y, colors.height,colors.height);
    }
    ColorPicker(Dimension size){
        colors.setSize(size.width, size.height);
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPreferredSize(size);

        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {
                if(colors.contains(mouseEvent.getPoint())){
                    circleX = mouseEvent.getPoint().x;
                    int hue = scale(0, (int)colors.getWidth(),0,360, mouseEvent.getPoint().x);
                    float realHue = hue/(float)360.0;
                    color = Color.getHSBColor(realHue,1,1);
                    repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent mouseEvent) {

            }
        });

    }

    Color getColor(){
        return color;
    }

    int scale(int ogMin, int ogMax, int min, int max, int original){
        return (original-ogMin)*(max-min)/(ogMax-ogMin)+min;
    }
}
