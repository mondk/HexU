import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

public class Menu extends JPanel {
    ImageIcon img = new ImageIcon("res/background/space.jpg");
    int dialogbutton;
    Menu(GameState gs) {
        
        // Field where you set amount of hexagons
        JPanel numberOfHexagons = new JPanel();
        numberOfHexagons.setLayout(new GridLayout(0,1));
        JLabel numberOfHexagonsLabel = new JLabel("Number Of Hexagons");
        JTextField numberOfHexagonsTextField = new JTextField("" + gs.numberOfHexagons);
        numberOfHexagons.add(numberOfHexagonsLabel);
        numberOfHexagons.add(numberOfHexagonsTextField);
        numberOfHexagons.setOpaque(true);
        numberOfHexagons.setBackground(new Color(0,0,0,80));
        setPreferredSize(gs.SCREEN_SIZE);

        //Start Game Buttons
        JButton startGameButton = new JButton("Start Multiplayer Game");
        JButton startOnlineButton = new JButton("Start Online Game");
        JButton continueLastGame = new JButton("Continue where you left off");
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons,BoxLayout.PAGE_AXIS));
        buttons.setOpaque(true);
        buttons.setBackground(new Color(0,0,0,0));
        buttons.add(startGameButton);
        buttons.add(startOnlineButton);
        buttons.add(continueLastGame);

        // Player Names
        JPanel playerNames = new JPanel();
        playerNames.setLayout(new GridLayout(0,1));
        playerNames.setOpaque(true);
        playerNames.setBackground(new Color(0,0,0,0));;

        // Add/Remove player buttons
        JPanel changePlayersButtons = new JPanel();
        changePlayersButtons.setLayout(new GridLayout(1,0));
        changePlayersButtons.setOpaque(true);
        changePlayersButtons.setBackground(new Color(0,0,0,0));;
        JButton removePlayer = new JButton();
        JButton addPlayer = new JButton();
        removePlayer.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                changePlayersButtons.add(addPlayer);
                changePlayersButtons.remove(removePlayer);
                startGameButton.setText("Start Game against Computer");
                gs.removePlayer();
                removePlayer(gs, playerNames);
            }
        });
        removePlayer.setText("Remove Player");
        addPlayer.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                changePlayersButtons.remove(addPlayer);
                changePlayersButtons.add(removePlayer);
                startGameButton.setText("Start Multiplayer Game");
                gs.addPlayer();
                addPlayer(gs, playerNames);
            }
        });
        addPlayer.setText("Add Player");
        changePlayersButtons.add(removePlayer);

        JPanel namesAndButtons = new JPanel();
        namesAndButtons.setLayout(new GridLayout(0,1));
        namesAndButtons.add(playerNames);
        namesAndButtons.add(changePlayersButtons);
        namesAndButtons.setOpaque(true);
        namesAndButtons.setBackground(new Color(0,0,0,0));;


        addPlayer(gs, playerNames);
        addPlayer(gs, playerNames);

        startGameButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (Integer.parseInt(numberOfHexagonsTextField.getText())< 2){
                    JOptionPane.showMessageDialog(null, "Thats to few hexes, enter a number higher than 1","", JOptionPane.ERROR_MESSAGE);
                }
                else if (Integer.parseInt(numberOfHexagonsTextField.getText())< 13){
                    gs.startGame(Integer.parseInt(numberOfHexagonsTextField.getText()), gs.players.size() == 1);
                }else {
                    if (gs.players.size()==1){
                        JOptionPane.showMessageDialog(null, "Thats to many hexes, enter a number between 3 and 12 \nto play against the AI","", JOptionPane.ERROR_MESSAGE);
                    }
                    else{
                        dialogbutton = JOptionPane.showConfirmDialog(null, "uhh to many hexes, this number of hexes will disable the AI.\nContinue anyway?","", JOptionPane.YES_NO_OPTION, dialogbutton);
                        if (dialogbutton == JOptionPane.YES_OPTION) {
                            gs.startGame(Integer.parseInt(numberOfHexagonsTextField.getText()), gs.players.size() == 1);
                        }
                    }
                }
            }
        });
        startOnlineButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                OnlinePanel op = new OnlinePanel(gs);
                gs.cards.add(op);
                CardLayout cl = (CardLayout)gs.cards.getLayout();
                cl.next(gs.cards);
                gs.cards.remove(0);
            }
        });

        continueLastGame.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent){
                gs.loadFile();
                gs.startGame(gs.numberOfHexagons, gs.singlePlayer);
            }
        });
        startGameButton.setText("Start Multiplayer Game");
        startOnlineButton.setText("Start Online Game");
        continueLastGame.setText("Continue last game");
        add(numberOfHexagons);
        add(namesAndButtons);
        add(buttons);
    }

    private void addPlayer(GameState gs, JPanel playerNames){
        try {
            playerNames.add(new PlayerSettings(gs.players.get(playerNames.getComponentCount())).getPlayerCards());
        } catch (Exception e) {
            gs.addPlayer();
            playerNames.add(new PlayerSettings(gs.players.get(playerNames.getComponentCount())).getPlayerCards());
        }
        playerNames.updateUI();
    }
    private void removePlayer(GameState gs, JPanel playerNames){
        playerNames.remove(playerNames.getComponentCount()-1);
        playerNames.updateUI();
    }

    @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(this.img.getImage(),0,0, this.getWidth(), this.getHeight(), this);
	}

}
