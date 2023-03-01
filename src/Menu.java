import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Menu extends JPanel {

    Menu(GameState gs) {
        setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        JButton startComputerGameButton = new JButton("Start Game against computer");
        JButton startMultiplayerButton = new JButton("Start Multiplayer Game");
        JButton startOnlineButton = new JButton("Start Online Game");
        JPanel buttons = new JPanel();
        buttons.setLayout(new BoxLayout(buttons,BoxLayout.PAGE_AXIS));
        buttons.add(startComputerGameButton);
        buttons.add(startMultiplayerButton);
        buttons.add(startOnlineButton);
        JPanel playerNames = new JPanel();
        playerNames.setLayout(new BoxLayout(playerNames,BoxLayout.LINE_AXIS));
        JTextField player1 = new JTextField("Player 1");
        JTextField player2 = new JTextField("Player 2");
        playerNames.setPreferredSize(new Dimension(500,25));
        playerNames.setMaximumSize(playerNames.getPreferredSize());
        playerNames.add(player1);
        playerNames.add(player2);
        startComputerGameButton.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.numberOfHexagons = 7;
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
                gs.player1Name = player1.getText();
                gs.player2Name = player2.getText();
                Panel panel = new Panel(gs);
                gs.cards.add(panel, "PANEL");
                CardLayout cl = (CardLayout)gs.cards.getLayout();
                cl.next(gs.cards);
                gs.cards.remove(0);
            }
        });

        startComputerGameButton.setText("Start Game against Computer");
        startComputerGameButton.setBounds(130, 100, 100, 40);
        startMultiplayerButton.setText("Start Multiplayer Game");
        //startGameButton.setAlignmentX();
        add(playerNames);
        add(buttons);
    }
}
