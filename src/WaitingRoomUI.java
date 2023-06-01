import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class WaitingRoomUI extends JPanel implements WaitingRoomListener {
    GameState gs;
    public WaitingRoomUI(GameState gameState) {
        try {
            this.gs = gameState;

            gameState.startWaitingRoom();

            gameState.waitingRoom.subscribe(this);

            PlayerSettings ownName = new PlayerSettings(gs, gameState.waitingRoom.getThisPlayer());
            JPanel names = new JPanel();
            names.setLayout(new GridLayout(0,1));
            for(Map.Entry<Integer,String> player : gameState.waitingRoom.getPlayers().entrySet()){
                if(Objects.equals(player.getKey(), gameState.waitingRoom.getThisPlayer())) continue;
                JPanel newPlayer = new JPanel();
                JLabel newPlayerName = new JLabel(player.getValue());
                ColorButton colorButton = new ColorButton(gs.players.get(player.getKey()).color,null);
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
                    gameState.waitingRoom.updateName(ownName.getName());
                }
            });
            names.add(ownName.getPlayerCards());
            JPanel numberOfHexagons = new JPanel();
            numberOfHexagons.setLayout(new GridLayout(0,1));
            JLabel numberOfHexagonsLabel = new JLabel("Number Of Hexagons");
            JTextField numberOfHexagonsTextField = new JTextField("" + gs.numberOfHexagons);
            numberOfHexagons.add(numberOfHexagonsLabel);
            numberOfHexagons.add(numberOfHexagonsTextField);


            add(numberOfHexagons,0);
            add(names, 1);
            JButton startButton = new JButton("StartGame");
            add(startButton);
            if(!gs.host){
                startButton.setEnabled(false);
                numberOfHexagonsTextField.setEnabled(false);
            } else {
                numberOfHexagonsTextField.getDocument().addDocumentListener(new DocumentListener() {
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
                            gameState.waitingRoom.updateNumberOfHexagons(Integer.parseInt(numberOfHexagonsTextField.getText()));
                        } catch(Exception e) {
                            System.out.println("You should only input numbers");
                        }
                    }
                });
                startButton.setAction(new AbstractAction() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        gameState.waitingRoom.startGame(0);
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

    @Override
    public void numberOfHexagonsChanged(Integer numberOfHexagons){
        JTextField hexagonField = (JTextField) ((JPanel)getComponent(0)).getComponent(1);
        hexagonField.setEnabled(true);
        hexagonField.setText(String.valueOf(numberOfHexagons));
        hexagonField.updateUI();
        hexagonField.setEnabled(false);
    }

    @Override
    public void playerLeft(Integer id) {
        JPanel names = (JPanel)getComponent(1);
        names.remove(id);
        names.updateUI();
    }

    @Override
    public void nameChanged(Integer id, String name, Color color){
        JPanel names = (JPanel)getComponent(1);
        try{
            System.out.println("removed id " + id);
            names.remove(id);
        } catch (Exception ignored){}
        System.out.println("id=" + id);
        JPanel newPlayer = new JPanel();
        JLabel newPlayerName = new JLabel(name);
        try {
            gs.players.get(id).color = color;
        } catch (IndexOutOfBoundsException e) {
            gs.players.put(id, new Player("Player" + id, color));
        }
        ColorButton colorButton = new ColorButton(gs.players.get(id).color,null);
        newPlayer.add(newPlayerName);
        newPlayer.add(colorButton);
        names.add(newPlayer, (int)id);
        names.updateUI();
    }

}
