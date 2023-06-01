import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ColorButton extends JPanel {
    Color color = Color.red;
    JPanel cards;

    @Override
    protected void paintComponent(Graphics g) {
        g.setColor(color);
        g.fillRect(0,0,50,50);
    }

    ColorButton(Color color, JPanel cards){
        this.cards = cards;
        this.color = color;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent mouseEvent) {
                System.out.println("Clicked on button");
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

    void setColor(Color color){
        this.color = color;
    }
    void setCards(JPanel cards){
        this.cards = cards;
    }
}
