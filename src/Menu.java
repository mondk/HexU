import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;

public class Menu extends JPanel {
    ImageIcon img = new ImageIcon("res/background/space.jpg");
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
        //setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(gs.SCREEN_SIZE);

        //Start Game Buttons
        JButton startComputerGameButton = new JButton("Start Game against computer");
        JButton startMultiplayerButton = new JButton("Start Multiplayer Game");
        JButton startOnlineButton = new JButton("Start Online Game");
        JButton continueLastGame = new JButton("Continue where you left off");
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons,BoxLayout.PAGE_AXIS));
        buttons.setOpaque(true);
        buttons.setBackground(new Color(0,0,0,0));
        buttons.add(startComputerGameButton);
        buttons.add(startMultiplayerButton);
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
        removePlayer.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.removePlayer();
                removePlayer(gs, playerNames);
            }
        });
        removePlayer.setText("Remove Player");
        JButton addPlayer = new JButton();
        addPlayer.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.addPlayer();
                addPlayer(gs, playerNames);
            }
        });
        addPlayer.setText("Add Player");
        changePlayersButtons.add(removePlayer);
        changePlayersButtons.add(addPlayer);

        JPanel namesAndButtons = new JPanel();
        namesAndButtons.setLayout(new GridLayout(0,1));
        namesAndButtons.add(playerNames);
        namesAndButtons.add(changePlayersButtons);
        namesAndButtons.setOpaque(true);
        namesAndButtons.setBackground(new Color(0,0,0,0));;


        addPlayer(gs, playerNames);
        addPlayer(gs, playerNames);

        // Set the actions for the startgame buttons
        startComputerGameButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.startGame(Integer.parseInt(numberOfHexagonsTextField.getText()), true);
                gs.changeState("single");
            }
        });
        startMultiplayerButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if(gs.players.size() != 2){
                    System.out.println("You can only play with 2 players");
                }
                gs.startGame(Integer.parseInt(numberOfHexagonsTextField.getText()), false);
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
        startComputerGameButton.setText("Start Game against Computer");
        startMultiplayerButton.setText("Start Multiplayer Game");
        startOnlineButton.setText("Start Online Game");
        continueLastGame.setText("Continue last game");
        add(numberOfHexagons);
        add(namesAndButtons);
        add(buttons);
    }

    private void addPlayer(GameState gs, JPanel playerNames){
        playerNames.add(new PlayerSettings(gs, playerNames.getComponentCount()).getPlayerCards());
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
