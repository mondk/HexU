import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Menu extends JPanel {

    Menu(GameState gs) {
        // Field where you set amount of hexagons
        JPanel numberOfHexagons = new JPanel();
        numberOfHexagons.setLayout(new GridLayout(0,1));
        JLabel numberOfHexagonsLabel = new JLabel("Number Of Hexagons");
        JTextField numberOfHexagonsTextField = new JTextField("" + gs.numberOfHexagons);
        numberOfHexagons.add(numberOfHexagonsLabel);
        numberOfHexagons.add(numberOfHexagonsTextField);
        //setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        setPreferredSize(gs.SCREEN_SIZE);

        //Start Game Buttons
        JButton startComputerGameButton = new JButton("Start Game against computer");
        JButton startMultiplayerButton = new JButton("Start Multiplayer Game");
        JButton startOnlineButton = new JButton("Start Online Game");
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons,BoxLayout.PAGE_AXIS));
        buttons.add(startComputerGameButton);
        buttons.add(startMultiplayerButton);
        buttons.add(startOnlineButton);

        // Player Names
        JPanel playerNames = new JPanel();
        playerNames.setLayout(new GridLayout(0,1));

        // Add/Remove player buttons
        JPanel changePlayersButtons = new JPanel();
        changePlayersButtons.setLayout(new GridLayout(1,0));
        JButton removePlayer = new JButton();
        removePlayer.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                removePlayer(gs, playerNames);
            }
        });
        removePlayer.setText("Remove Player");
        JButton addPlayer = new JButton();
        addPlayer.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
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


        addPlayer(gs, playerNames);
        addPlayer(gs, playerNames);

        // Set the actions for the startgame buttons
        startComputerGameButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.startGame(Integer.parseInt(numberOfHexagonsTextField.getText()), true);
            }
        });
        startMultiplayerButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.startGame(Integer.parseInt(numberOfHexagonsTextField.getText()), false);
            }
        });
        startOnlineButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.out.println("Not yet implemented");
            }
        });
        startComputerGameButton.setText("Start Game against Computer");
        startMultiplayerButton.setText("Start Multiplayer Game");
        startOnlineButton.setText("Start Online Game");
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
}
