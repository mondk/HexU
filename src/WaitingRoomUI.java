import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class WaitingRoomUI extends JPanel implements WaitingRoomListener {
    GameState gs;

    ImageIcon img = new ImageIcon("res/background/space.jpg");
    public WaitingRoomUI(GameState gameState, String ip) {
        try {
            this.gs = gameState;

            gameState.startWaitingRoom();

            PlayerSettings ownName = new PlayerSettings(gameState.players.get(gs.onlineId));
            JPanel names = new JPanel();
            names.setLayout(new GridLayout(0,1));
            for(Map.Entry<Integer,Player> player : gameState.players.entrySet()){
                if(Objects.equals(player.getKey(), gameState.waitingRoom.getThisPlayer())) continue;
                JPanel newPlayer = new JPanel();
                JLabel newPlayerName = new JLabel(player.getValue().name);
                ColorButton colorButton = new ColorButton(player.getValue().color,null);
                newPlayer.add(newPlayerName, player.getKey());
                colorButton.setEnabled(false);
                newPlayer.add(colorButton);
                names.add(newPlayer, (int)player.getKey());
            }
            ownName.changeDocumentListener(new DocumentListener() {
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
                    gameState.players.get(gameState.onlineId).name = ownName.getName();
                    gameState.online.changePlayer(gameState.onlineId, gameState.players.get(gameState.onlineId));
                }
            });
            ownName.changeConfirmAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    gs.players.get(gameState.onlineId).color = ownName.getColor();
                    CardLayout cl = (CardLayout) ownName.getPlayerCards().getLayout();
                    cl.next(ownName.getPlayerCards());
                    ownName.getColorButton().setColor(ownName.getColor());
                    gs.online.changePlayer(gs.onlineId, gs.players.get(gameState.onlineId));
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
            JPanel buttons = new JPanel();
            buttons.setLayout(new GridLayout(0,1));
            JButton startButton = new JButton("Start Game");
            JButton leaveButton = new JButton("Leave Room");
            leaveButton.setAction(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    gameState.disconnectFromOnline();
                }
            });
            leaveButton.setText("Leave Room");
            buttons.add(startButton);
            buttons.add(leaveButton);
            add(buttons);
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
            JTextArea ipTextArea = new JTextArea(ip);
            JLabel ipLabel = new JLabel("The IP address is:");
            JPanel ipInfo = new JPanel();
            ipInfo.setLayout(new GridLayout(0,1));
            ipInfo.add(ipLabel);
            ipInfo.add(ipTextArea);
            add(ipInfo);
            gs.cards.add(this);
            CardLayout cl = (CardLayout)gs.cards.getLayout();
            cl.next(gs.cards);

            gameState.waitingRoom.subscribe(this);

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
    public void playerChanged(Integer id, Player player){
        JPanel names = (JPanel)getComponent(1);
        try{
            names.remove(id);
        } catch (Exception ignored){}
        JPanel newPlayer = new JPanel();
        JLabel newPlayerName = new JLabel(player.name);
        ColorButton colorButton = new ColorButton(player.color,null);
        newPlayer.add(newPlayerName);
        newPlayer.add(colorButton);
        names.add(newPlayer, (int)id);
        names.updateUI();
    }

    @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(this.img.getImage(),0,0, this.getWidth(), this.getHeight(), this);
	}

}
