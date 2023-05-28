import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.util.*;

public class WaitingRoomPlayerListener implements Runnable {
    GameState gameState;
    HashMapIntegerString players;

    int thisPlayer;
    WaitingRoom waitingRoom;
    public WaitingRoomPlayerListener(GameState gameState, WaitingRoom waitingRoom) throws InterruptedException {
        this.gameState = gameState;
        this.waitingRoom = waitingRoom;
        this.players = (HashMapIntegerString) gameState.gameSpace.get(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
        this.thisPlayer = players.size();
        for(Map.Entry<Integer,String> player : this.players.entrySet()){
            gameState.gameSpace.put(player.getKey(), "newName", thisPlayer, gameState.player1Name, gameState.playerColors.get(thisPlayer));
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
                System.out.println(player.getKey() + " " + thisPlayer);
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
                    waitingRoom.updateNumberOfHexagons((String)numberOfHexagons[2]);
                }
                Object[] newName = gameState.gameSpace.getp(new ActualField(thisPlayer), new ActualField("newName"), new FormalField(Integer.class), new FormalField(String.class), new FormalField(Color.class));

                if(newName != null) {
                    waitingRoom.updateNames((int)newName[2],(String)newName[3],(Color) newName[4]);
                }

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
                    gameState.updateNumberOfHexagons(Integer.parseInt(waitingRoom.getNumberOfHexagons()));
                    gameState.whosTurn = thisPlayer == 0 ? GameState.Turn.Player1 : GameState.Turn.ONLINE_PLAYER;
                    Panel panel = new Panel(gameState);
                    gameState.cards.add(panel);
                    cl.next(gameState.cards);
                    gameState.cards.remove(0);
                    break;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public Integer getThisPlayer(){
        return thisPlayer;
    }
    public HashMapIntegerString getPlayers(){
        return players;
    }
}
