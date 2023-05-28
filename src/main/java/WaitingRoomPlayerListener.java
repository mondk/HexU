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
    public WaitingRoomPlayerListener(GameState gameState, JPanel names) throws InterruptedException {
        this.gameState = gameState;
        this.names = names;
        this.players = (HashMapIntegerString) gameState.gameSpace.get(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
        this.thisPlayer = players.size();
        for(Map.Entry<Integer,String> player : this.players.entrySet()){
            gameState.gameSpace.put(player.getKey(), "newName", thisPlayer, gameState.player1Name, gameState.playerColors.get(thisPlayer));
            JPanel newPlayer = new JPanel();
            JLabel newPlayerName = new JLabel(player.getValue());
            ColorButton colorButton = new ColorButton(gameState.playerColors.get(player.getKey()),null);
            newPlayer.add(newPlayerName, 0);
            colorButton.setEnabled(false);
            newPlayer.add(colorButton);
            names.add(newPlayer, (int)player.getKey());
        }
        players.put(thisPlayer, gameState.player1Name);
        gameState.gameSpace.put("players", players);
    }

    public void startGame(){
        try {
            for(Map.Entry<Integer,String> player : players.entrySet()) {

                gameState.gameSpace.put(player.getKey(), "startGame");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateNumberOfHexagons(String numberOfHexagons){
        try {
            for(Map.Entry<Integer,String> player : players.entrySet()) {
                System.out.println(player.getKey());
                gameState.gameSpace.put(1, "numberOfHexagons" , numberOfHexagons);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateName(String name){
        try {
            System.out.println("Updating name");
            players = (HashMapIntegerString) gameState.gameSpace.get(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
            players.remove(thisPlayer);
            gameState.player1Name = name;
            for (Map.Entry<Integer,String> player : players.entrySet()) {
                System.out.println(player);
                gameState.gameSpace.put(player.getKey(), "newName", thisPlayer, gameState.player1Name,gameState.playerColors.get(thisPlayer));
            }
            players.put(thisPlayer, gameState.player1Name);
            gameState.gameSpace.put("players", players);
            System.out.println("Updated name");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
                    System.out.println("Trying something");
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
    public Integer getThisPlayer(){
        return thisPlayer;
    }
}
