import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Menu extends JPanel {

    Menu(GameState gs) {
        // Field where you set amount of hexagons
        JTextField hexagonField = new JTextField("" + gs.numberOfHexagons);
        hexagonField.setPreferredSize(new Dimension(250,25));
        hexagonField.setMaximumSize(new Dimension(gs.SCREEN_SIZE.width,25));
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
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
        playerNames.setPreferredSize(new Dimension(550,30));
        playerNames.setMaximumSize(playerNames.getPreferredSize());
        playerNames.setLayout(new BoxLayout(playerNames,BoxLayout.LINE_AXIS));

        //player 1 settings
        JPanel player1Info = new JPanel();
        player1Info.setLayout(new BoxLayout(player1Info,BoxLayout.LINE_AXIS));
        player1Info.setPreferredSize(new Dimension(275,30));
        JPanel player1Cards = new JPanel(new CardLayout());
        JTextField player1 = new JTextField("Player 1");
        JPanel colorMenu1 = new JPanel();
        JButton confirmP1Color = new JButton("Confirm");
        ColorPicker colorPicker1 = new ColorPicker(new Dimension(125,20));
        ColorButton player1Color = new ColorButton(gs.colorP1, player1Cards);
        confirmP1Color.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.colorP1 = colorPicker1.getColor();
                CardLayout cl = (CardLayout)player1Cards.getLayout();
                cl.next(player1Cards);
                player1Color.setColor(colorPicker1.getColor());
            }
        });
        confirmP1Color.setText("Confirm");
        colorMenu1.add(colorPicker1);
        colorMenu1.add(confirmP1Color);
        player1Info.add(player1);
        player1Info.add(player1Color);
        player1Cards.add(player1Info);
        player1Cards.add(colorMenu1);
        player1Color.setCards(player1Cards);

        // player 2 settings
        JPanel player2Info = new JPanel();
        player2Info.setLayout(new BoxLayout(player2Info,BoxLayout.LINE_AXIS));
        player2Info.setPreferredSize(new Dimension(275,30));
        JPanel player2Cards = new JPanel(new CardLayout());
        JTextField player2 = new JTextField("Player 2");
        JPanel colorMenu2 = new JPanel();
        JButton confirmP2Color = new JButton("Confirm");
        ColorPicker colorPicker2 = new ColorPicker(new Dimension(125,20));
        ColorButton player2Color = new ColorButton(gs.colorP2, player2Cards);
        confirmP2Color.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.colorP2 = colorPicker2.getColor();
                CardLayout cl = (CardLayout)player2Cards.getLayout();
                cl.next(player2Cards);
                player2Color.setColor(colorPicker2.getColor());
            }
        });
        confirmP2Color.setText("Confirm");
        colorMenu2.add(colorPicker2);
        colorMenu2.add(confirmP2Color);
        player2Info.add(player2);
        player2Info.add(player2Color);
        player2Cards.add(player2Info);
        player2Cards.add(colorMenu2);
        player2Color.setCards(player2Cards);

        // Set the actions for the startgame buttons
        startComputerGameButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.singlePlayer = true;
                gs.updateNumberOfHexagons(Integer.parseInt(hexagonField.getText()));
                gs.player1Name = player1.getText();
                gs.player2Name = "Computer";
                Panel panel = new Panel(gs);
                gs.cards.add(panel, "PANEL");
                CardLayout cl = (CardLayout)gs.cards.getLayout();
                cl.next(gs.cards);
                gs.cards.remove(0);
            }
        });
        startMultiplayerButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.singlePlayer = false;
                gs.updateNumberOfHexagons(Integer.parseInt(hexagonField.getText()));
                gs.player1Name = player1.getText();
                gs.player2Name = player2.getText();
                gs.paneTurnString = gs.player1Name;
                Panel panel = new Panel(gs);
                gs.cards.add(panel, "PANEL");
                CardLayout cl = (CardLayout)gs.cards.getLayout();
                cl.next(gs.cards);
                gs.cards.remove(0);
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
        playerNames.add(player1Cards);
        playerNames.add(player2Cards);
        add(playerNames);
        add(hexagonField);
        add(buttons);
    }

}
