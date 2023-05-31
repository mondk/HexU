import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PlayerSettings extends JPanel {
    private JPanel playerCards;
    private JTextField player;
    private JPanel colorMenu;
    private JButton confirmPColor;
    private ColorPicker colorPicker;
    private ColorButton playerColor;
    public PlayerSettings(GameState gs, int id) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPreferredSize(new Dimension(275, 30));
        playerCards = new JPanel(new CardLayout());
        player = new JTextField("Player" + id);
        colorMenu = new JPanel();
        confirmPColor = new JButton("Confirm");
        colorPicker = new ColorPicker(new Dimension(125, 20));
        playerColor = id == 0 ? new ColorButton(gs.colorP1, playerCards) : new ColorButton(gs.colorP2, playerCards);
        confirmPColor.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (id == 0) gs.colorP1 = colorPicker.getColor();
                else gs.colorP2 = colorPicker.getColor();
                CardLayout cl = (CardLayout) playerCards.getLayout();
                cl.next(playerCards);
                playerColor.setColor(colorPicker.getColor());
            }
        });
        confirmPColor.setText("Confirm");
        colorMenu.add(colorPicker);
        colorMenu.add(confirmPColor);
        add(player);
        add(playerColor);
        playerCards.add(this);
        playerCards.add(colorMenu);
        playerColor.setCards(playerCards);
    }

    public String getName(){
        return player.getText();
    }

    public JPanel getPlayerCards() {
        return playerCards;
    }

    public JTextField getPlayerTextField(){
        return player;
    }
}
