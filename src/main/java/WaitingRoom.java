import org.jspace.ActualField;
import org.jspace.FormalField;

import javax.swing.*;
import java.awt.*;

public class WaitingRoom extends JPanel {
    public WaitingRoom(GameState gs) {
        try {
            TextField waiting = new TextField("Waiting for other player");
            add(waiting);
            gs.cards.add(this);
            CardLayout cl = (CardLayout)gs.cards.getLayout();
            cl.next(gs.cards);
            for(int i = Integer.MIN_VALUE; i < Integer.MAX_VALUE; i++);
            System.out.println("Hi");
            String player1Name = (String) gs.gameSpace.query(new ActualField("Player1Name"), new FormalField(String.class))[1];
            String player2Name = (String) gs.gameSpace.query(new ActualField("Player2Name"), new FormalField(String.class))[1];
            gs.player1Name = player1Name;
            gs.player2Name = player2Name;
            Panel panel = new Panel(gs);
            gs.cards.add(panel);
            cl.next(gs.cards);
            gs.cards.remove(0);
        } catch (Exception e){

        }
    }
}
