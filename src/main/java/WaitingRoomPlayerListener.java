import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class WaitingRoomPlayerListener implements Runnable{
    GameState gameState;
    HashMapIntegerString players;
    JPanel names;
    Integer thisPlayer;
    public WaitingRoomPlayerListener(GameState gameState, HashMapIntegerString players, JPanel names, Integer thisPlayer){
        this.gameState = gameState;
        this.players = players;
        this.names = names;
        this.thisPlayer = thisPlayer;
        System.out.println(thisPlayer);
    }
    @Override
    public void run() {
        while(true){
            try {
                Object[] numberOfHexagons = gameState.gameSpace.getp(new ActualField(thisPlayer),new ActualField("numberOfHexagons"), new FormalField(String.class));
                players = (HashMapIntegerString) gameState.gameSpace.query(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
                if(numberOfHexagons != null && !(0 == thisPlayer)){
                    JTextField hexagonField = (JTextField)names.getParent().getComponent(0);
                    hexagonField.setEnabled(true);
                    hexagonField.setText((String)numberOfHexagons[2]);
                    hexagonField.setEnabled(false);
                }
                Object[] newName = gameState.gameSpace.getp(new ActualField(thisPlayer), new ActualField("newName"), new FormalField(Integer.class), new FormalField(String.class), new FormalField(Color.class));
                if(newName != null) {
                    try{
                        names.remove((int)newName[2]);
                    } catch (Exception ignored){
                        System.out.println("Exception");
                    }
                    JPanel newPlayer = new JPanel();
                    JLabel newPlayerName = new JLabel((String)newName[3]);
                    gameState.playerColors.set((int) newName[2], (Color) newName[4]);
                    ColorButton colorButton = new ColorButton(gameState.playerColors.get((int)newName[2]),null);
                    newPlayer.add(newPlayerName, 0);
                    newPlayer.add(colorButton);
                    System.out.println("Recieved a new name");
                    names.add(newPlayer, (int)newName[2]);
                }
                names.updateUI();
                Object[] updatedPersonalColor = gameState.gameSpace.getp(new ActualField(thisPlayer), new ActualField("newColor"), new FormalField(Color.class));
                if (updatedPersonalColor != null){
                    for(Map.Entry<Integer,String> player : players.entrySet()){
                        if(!Objects.equals(player.getKey(), thisPlayer)){
                            System.out.println("I'm here " + player);
                            System.out.println(updatedPersonalColor[2]);
                            gameState.gameSpace.put(player.getKey(), "newName",thisPlayer,gameState.player1Name,updatedPersonalColor[2]);
                        }
                    }
                }
                Object[] startGame = gameState.gameSpace.getp(new ActualField(thisPlayer),new ActualField("startGame"));
                if(startGame != null){
                    CardLayout cl = (CardLayout)gameState.cards.getLayout();
                    gameState.player1Name = (String)players.values().toArray()[0];
                    gameState.player2Name = (String)players.values().toArray()[1];
                    gameState.updateNumberOfHexagons(Integer.parseInt(((JTextField)names.getParent().getComponent(0)).getText()));
                    gameState.whosTurn = thisPlayer == 0 ? GameState.Turn.Player1 : GameState.Turn.ONLINE_PLAYER;
                    Panel panel = new Panel(gameState);
                    gameState.cards.add(panel);
                    cl.next(gameState.cards);
                    gameState.cards.remove(0);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
