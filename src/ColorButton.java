import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

// Made by Jónas
// Class to define ColorButton that displays a colored rectangle (the color can be set and changed).
public class ColorButton extends JPanel {
    Color color = Color.red;
    JPanel cards;

    @Override
    protected void paintComponent(Graphics g) {

        //Setting the color to the current color 
        g.setColor(color);

        //Filling a rectangle with the current color depending on the coordinates and size 
        g.fillRect(0,0,50,50);
    }
    // Constructor for the ColorButton class
    ColorButton(Color color, JPanel cards){
        this.cards = cards;
        this.color = color;

        // Add a MouseListener to the ColorButton
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                CardLayout cl = (CardLayout)cards.getLayout();
                cl.next(cards);
            }

            @Override
            public void mousePressed(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseReleased(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseEntered(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseExited(MouseEvent mouseEvent) {

            }
        });
    }
    //Setting the color of the button
    void setColor(Color color){
        this.color = color;
    }
    //Setting the cards for JPanel
    void setCards(JPanel cards){
        this.cards = cards;
    }
}
