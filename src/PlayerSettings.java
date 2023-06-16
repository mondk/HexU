import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;

// Class called PlayerSettings that represents a panel for configuring player settings in a game.

public class PlayerSettings extends JPanel {
    private JPanel playerCards;
    private JTextField playerName;
    private JPanel colorMenu;
    private JButton confirmPColor;
    private ColorPicker colorPicker;
    private ColorButton playerColor;
    private DocumentListener documentListener;

    // Constructor for the PlayerSettings class
    public PlayerSettings(Player player) {
        // Setting the layout and preferred size of the panel
        setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        setPreferredSize(new Dimension(275, 30));

        // Initialize components for the player
        playerCards = new JPanel(new CardLayout());
        playerName = new JTextField(player.name);
        colorMenu = new JPanel();
        confirmPColor = new JButton("Confirm");
        colorPicker = new ColorPicker(new Dimension(125, 20));
        playerColor = new ColorButton(player.color, playerCards);

        // Action for when the confirmPColor button is clicked
        confirmPColor.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                // Update the player's color to the new one
                player.color = colorPicker.getColor();

                // Switch to the next card in the playerCards panel
                CardLayout cl = (CardLayout) playerCards.getLayout();
                cl.next(playerCards);

                // Update the color of the playerColor button
                playerColor.setColor(colorPicker.getColor());
            }
        });

        // Tracking changes in the player name text field
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
                // Update the player's name
                try {
                    player.name = playerName.getText();
                } catch (Exception ignored) {}
            }
        };

        // Add the documentListener to the playerName text field
        playerName.getDocument().addDocumentListener(documentListener);

        // Set up the colorMenu panel with colorPicker and confirmPColor
        confirmPColor.setText("Confirm");
        colorMenu.add(colorPicker);
        colorMenu.add(confirmPColor);

        // Add components to the PlayerSettings panel
        add(playerName);
        add(playerColor);
        playerCards.add(this);
        playerCards.add(colorMenu);
        playerColor.setCards(playerCards);
    }

    // Method for getting the name entered in the playerName text field
    public String getName(){
        return playerName.getText();
    }

    // Method for getting the color selected using the colorPicker
    public Color getColor(){
        return colorPicker.getColor();
    }

    // Method for getting the ColorButton component in the player's color
    public ColorButton getColorButton(){
        return playerColor;
    }

    // Method for getting the playerCards panel
    public JPanel getPlayerCards() {
        return playerCards;
    }

    // Method for getting the playerName text field
    public JTextField getPlayerTextField(){
        return playerName;
    }

    // Method for changing the action performed when confirmPColor button is clicked
    public void changeConfirmAction(AbstractAction action){
        confirmPColor.setAction(action);
        confirmPColor.setText("Confirm");
    }
    // Method for changing the document listener for the playerName text field
    public void changeDocumentListener(DocumentListener documentListener){
        playerName.getDocument().removeDocumentListener(this.documentListener);
        playerName.getDocument().addDocumentListener(documentListener);
    }
}
