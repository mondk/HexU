import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class WaitingRoom extends JPanel {
    GameState gs;
    public WaitingRoom(GameState gameState) {
        try {
            this.gs = gameState;
            WaitingRoomPlayerListener players = new WaitingRoomPlayerListener(gs, this);

            Thread playersThread = new Thread(players);
            playersThread.start();

            PlayerSettings ownName = new PlayerSettings(gs, players.getThisPlayer());
            JPanel names = new JPanel();
            names.setLayout(new GridLayout(0,1));
            for(Map.Entry<Integer,String> player : players.getPlayers().entrySet()){
                if(Objects.equals(player.getKey(), players.getThisPlayer())) continue;
                JPanel newPlayer = new JPanel();
                JLabel newPlayerName = new JLabel(player.getValue());
                ColorButton colorButton = new ColorButton(gs.playerColors.get(player.getKey()),null);
                newPlayer.add(newPlayerName, player.getKey());
                colorButton.setEnabled(false);
                newPlayer.add(colorButton);
                names.add(newPlayer, (int)player.getKey());
            }
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
            add(names, 1);
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

    public void updateNumberOfHexagons(String numberOfHexagons){
        JTextField hexagonField = (JTextField) getComponent(0);
        hexagonField.setEnabled(true);
        hexagonField.setText(numberOfHexagons);
        hexagonField.updateUI();
        hexagonField.setEnabled(false);
    }
    public String getNumberOfHexagons(){
        return ((JTextField)getComponent(0)).getText();
    }

    public void updateNames(Integer id, String name, Color color){
        JPanel names = (JPanel)getComponent(1);
        try{
            System.out.println("removed id " + id);
            names.remove(id);
        } catch (Exception ignored){}
        System.out.println("id=" + id);
        JPanel newPlayer = new JPanel();
        JLabel newPlayerName = new JLabel(name);
        gs.playerColors.set(id, color);
        ColorButton colorButton = new ColorButton(gs.playerColors.get(id),null);
        newPlayer.add(newPlayerName);
        newPlayer.add(colorButton);
        names.add(newPlayer, (int)id);
        names.updateUI();
    }
}
