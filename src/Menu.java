import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

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
        JButton continueLastGame = new JButton("Continue where you left off");
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons,BoxLayout.PAGE_AXIS));
        buttons.add(startComputerGameButton);
        buttons.add(startMultiplayerButton);
        buttons.add(startOnlineButton);
        buttons.add(continueLastGame);

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
                try {
                    File Obj = new File("res/saves.txt");
                    Scanner Reader = new Scanner(Obj);
                    while (Reader.hasNextLine()) {
                        String[] data = Reader.nextLine().split(": ");
                        //System.out.println(data[0].toString()+ " " + data[1].toString());
                        if (data[0].equals("mode")){
                            if (data[1].equals("false"))
                                gs.singlePlayer = false;
                            else 
                                gs.singlePlayer = true;
                        }
                        else if (data[0].equals("hexes")){
                            gs.updateNumberOfHexagons(Integer.parseInt(data[1]));
                        }
                        else if (data[0].equals("P1")){
                            gs.players.get(0).name = data[1];
                        }
                        else if (data[0].equals("CP1")){
                            gs.players.get(0).color = new Color(Integer.parseInt(data[1]));
                        }
                        else if (data[0].equals("P2")){
                            gs.players.get(1).name = data[1];
                        }
                        else if (data[0].equals("CP2")){
                            gs.players.get(1).color = new Color(Integer.parseInt(data[1]));
                        }
                        else if (data[0].equals("moves")){
                            gs.load = data[1].substring(1, data[1].length()-1).split(", ");
                        }
                    }
                    Reader.close();
                    gs.startGame(gs.numberOfHexagons, gs.singlePlayer);
                }
                catch (FileNotFoundException e) {
                    System.out.println("An error has occurred.");
                    e.printStackTrace();
                }
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
        gs.addPlayer();
        playerNames.add(new PlayerSettings(gs, playerNames.getComponentCount()).getPlayerCards());
        playerNames.updateUI();
    }
    private void removePlayer(GameState gs, JPanel playerNames){
        playerNames.remove(playerNames.getComponentCount()-1);
        playerNames.updateUI();
    }
}
