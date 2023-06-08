import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

public class PlayerSettings extends JPanel {
    private JPanel playerCards;
    private JTextField playerName;
    private JPanel colorMenu;
    private JButton confirmPColor;
    private ColorPicker colorPicker;
    private ColorButton playerColor;
    private DocumentListener documentListener;
    public PlayerSettings(Player player) {
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPreferredSize(new Dimension(275, 30));
        playerCards = new JPanel(new CardLayout());
        playerName = new JTextField(player.name);
        colorMenu = new JPanel();
        confirmPColor = new JButton("Confirm");
        colorPicker = new ColorPicker(new Dimension(125, 20));
        playerColor = new ColorButton(player.color, playerCards);
        confirmPColor.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                player.color = colorPicker.getColor();
                CardLayout cl = (CardLayout) playerCards.getLayout();
                cl.next(playerCards);
                playerColor.setColor(colorPicker.getColor());
            }
        });
        documentListener = new DocumentListener() {
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
                    player.name = playerName.getText();
                } catch (Exception ignored) {}
            }
        };
        playerName.getDocument().addDocumentListener(documentListener);
        confirmPColor.setText("Confirm");
        colorMenu.add(colorPicker);
        colorMenu.add(confirmPColor);
        add(playerName);
        add(playerColor);
        playerCards.add(this);
        playerCards.add(colorMenu);
        playerColor.setCards(playerCards);
    }

    public String getName(){
        return playerName.getText();
    }

    public Color getColor(){
        return colorPicker.getColor();
    }

    public ColorButton getColorButton(){
        return playerColor;
    }

    public JPanel getPlayerCards() {
        return playerCards;
    }

    public JTextField getPlayerTextField(){
        return playerName;
    }
    public void changeConfirmAction(AbstractAction action){
        confirmPColor.setAction(action);
        confirmPColor.setText("Confirm");
    }
    public void changeDocumentListener(DocumentListener documentListener){
        playerName.getDocument().removeDocumentListener(this.documentListener);
        playerName.getDocument().addDocumentListener(documentListener);
    }
}
