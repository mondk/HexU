import org.jspace.ActualField;
import org.jspace.FormalField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class WaitingRoom extends JPanel {
    int id;
    public WaitingRoom(GameState gs) {
        try {

            JPanel names = new JPanel();
            names.setLayout(new GridLayout(0,1));
            HashMapIntegerString listOfPlayers = (HashMapIntegerString) gs.gameSpace.get(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
            id = listOfPlayers.size();
            System.out.println(listOfPlayers);
            for(Map.Entry<Integer,String> player : listOfPlayers.entrySet()){
                gs.gameSpace.put(player.getKey(), "newName", id, gs.player1Name, gs.playerColors.get(id));
                JPanel newPlayer = new JPanel();
                JLabel newPlayerName = new JLabel(player.getValue());
                ColorButton colorButton = new ColorButton(gs.playerColors.get(player.getKey()),null);
                newPlayer.add(newPlayerName, 0);
                colorButton.setEnabled(false);
                newPlayer.add(colorButton);
                names.add(newPlayer, (int)player.getKey());
            }
            listOfPlayers.put(id, gs.player1Name);
            gs.gameSpace.put("players", listOfPlayers);
            PlayerSettings ownName = new PlayerSettings(gs, id);
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
                    try {
                        HashMapIntegerString listOfPlayers = (HashMapIntegerString) gs.gameSpace.get(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
                        listOfPlayers.remove(id);
                        gs.player1Name = ownName.getName();
                        for (Map.Entry<Integer,String> player : listOfPlayers.entrySet()) {
                            gs.gameSpace.put(player.getKey(), "newName", id, gs.player1Name,gs.playerColors.get(id));
                        }
                        listOfPlayers.put(id, gs.player1Name);
                        gs.gameSpace.put("players", listOfPlayers);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
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
                        try {
                            HashMapIntegerString listOfPlayers = (HashMapIntegerString)gs.gameSpace.query(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
                            System.out.println(listOfPlayers);
                            for(Map.Entry<Integer,String> player : listOfPlayers.entrySet()) {
                                System.out.println(player.getKey());
                                gs.gameSpace.put(1, "numberOfHexagons" , numberOfHexagons.getText());
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                startButton.setAction(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        try {
                            HashMapIntegerString listOfPlayers = (HashMapIntegerString) gs.gameSpace.query(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
                            for(Map.Entry<Integer,String> player : listOfPlayers.entrySet()) {

                                gs.gameSpace.put(player.getKey(), "startGame");
                            }
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                startButton.setText("Start Game");
            }
            gs.cards.add(this);
            CardLayout cl = (CardLayout)gs.cards.getLayout();
            cl.next(gs.cards);

            WaitingRoomPlayerListener players = new WaitingRoomPlayerListener(gs, listOfPlayers, names, id);

            Thread playersThread = new Thread(players);
            playersThread.start();

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
