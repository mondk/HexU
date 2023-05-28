import org.jspace.ActualField;
import org.jspace.FormalField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class WaitingRoom extends JPanel {
    public WaitingRoom(GameState gs) {
        try {
            JPanel names = new JPanel();
            names.setLayout(new GridLayout(0,1));
            WaitingRoomPlayerListener players = new WaitingRoomPlayerListener(gs, names);

            Thread playersThread = new Thread(players);
            playersThread.start();

            PlayerSettings ownName = new PlayerSettings(gs, players.getThisPlayer());
            ownName.getPlayerTextField().getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent documentEvent) {
                    changedUpdate(documentEvent);
                }

                @Override
                public void removeUpdate(DocumentEvent documentEvent) {
                    changedUpdate(documentEvent);
                }

                @Override
                public void changedUpdate(DocumentEvent documentEvent) {
                    players.updateName(ownName.getName());
                }
            });
            names.add(ownName.getPlayerCards());
            JTextField numberOfHexagons = new JTextField("" + gs.numberOfHexagons);

            add(numberOfHexagons,0);
            add(names);
            JButton startButton = new JButton("StartGame");
            add(startButton);
            if(!gs.host){
                startButton.setEnabled(false);
                numberOfHexagons.setEnabled(false);
            } else {
                numberOfHexagons.getDocument().addDocumentListener(new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent documentEvent) {
                        changedUpdate(documentEvent);
                    }

                    @Override
                    public void removeUpdate(DocumentEvent documentEvent) {
                        changedUpdate(documentEvent);
                    }

                    @Override
                    public void changedUpdate(DocumentEvent documentEvent) {
                        players.updateNumberOfHexagons(numberOfHexagons.getText());
                    }
                });
                startButton.setAction(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        players.startGame();
                    }
                });
                startButton.setText("Start Game");
            }
            gs.cards.add(this);
            CardLayout cl = (CardLayout)gs.cards.getLayout();
            cl.next(gs.cards);

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
