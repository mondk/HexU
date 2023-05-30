import org.jspace.ActualField;
import org.jspace.FormalField;

import java.awt.*;
import java.util.*;

public class WaitingRoom implements Runnable {
    GameState gameState;
    String numberOfHexagons;
    int thisPlayer;
    ArrayList<WaitingRoomListener> waitingRoomListeners = new ArrayList<>();
    public WaitingRoom(GameState gameState) throws InterruptedException {
        this.gameState = gameState;
        this.numberOfHexagons = String.valueOf(gameState.numberOfHexagons);
        gameState.onlinePlayers = (HashMapIntegerString) gameState.gameSpace.get(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
        gameState.onlineId = gameState.onlinePlayers.size();
        this.thisPlayer = gameState.onlineId;
        for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
            try {
                gameState.playerColors.get(player.getKey());
            } catch (IndexOutOfBoundsException e) {
                gameState.playerColors.add(Color.ORANGE);
            }
        }
        for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
            try {
                gameState.gameSpace.put(player.getKey(), "newName", thisPlayer, gameState.player1Name, gameState.playerColors.get(thisPlayer));
            } catch (Exception e) {
                gameState.playerColors.add(Color.ORANGE);
                gameState.gameSpace.put(player.getKey(), "newName", thisPlayer, gameState.player1Name, gameState.playerColors.get(thisPlayer));
            }
        }
        gameState.onlinePlayers.put(thisPlayer, gameState.player1Name);
        gameState.gameSpace.put("players", gameState.onlinePlayers);
    }

    public void subscribe(WaitingRoomListener newWaitingRoomListener){
        waitingRoomListeners.add(newWaitingRoomListener);
    }

    public void unsubscribe(WaitingRoomListener newWaitingRoomListener){
        waitingRoomListeners.remove(newWaitingRoomListener);
    }

    public void startGame(){
        try {
            for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
                gameState.gameSpace.put(player.getKey(), "startGame");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateNumberOfHexagons(String numberOfHexagons){
        try {
            for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
                gameState.gameSpace.put(player.getKey(), "numberOfHexagons" , numberOfHexagons);
            }
            this.numberOfHexagons = numberOfHexagons;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateName(String name){
        try {
            gameState.onlinePlayers = (HashMapIntegerString) gameState.gameSpace.get(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
            gameState.onlinePlayers.remove(thisPlayer);
            gameState.player1Name = name;
            for (Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
                System.out.println(player.getKey() + " " + thisPlayer);
                gameState.gameSpace.put(player.getKey(), "newName", thisPlayer, gameState.player1Name,gameState.playerColors.get(thisPlayer));
            }
            gameState.onlinePlayers.put(thisPlayer, gameState.player1Name);
            gameState.gameSpace.put("players", gameState.onlinePlayers);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                gameState.onlinePlayers = (HashMapIntegerString) gameState.gameSpace.query(new ActualField("players"), new FormalField(HashMapIntegerString.class))[1];
                Object[] numberOfHexagons = gameState.gameSpace.getp(new ActualField(thisPlayer),new ActualField("numberOfHexagons"), new FormalField(String.class));
                if(numberOfHexagons != null && !(0 == thisPlayer)){
                    this.numberOfHexagons = (String) numberOfHexagons[2];
                    for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                        waitingRoomListener.numberOfHexagonsChanged((String) numberOfHexagons[2]);
                    }
                }
                Object[] newName = gameState.gameSpace.getp(new ActualField(thisPlayer), new ActualField("newName"), new FormalField(Integer.class), new FormalField(String.class), new FormalField(Color.class));

                if(newName != null) {
                    for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                        waitingRoomListener.nameChanged((int) newName[2], (String) newName[3], (Color) newName[4]);
                    }
                }

                Object[] updatedPersonalColor = gameState.gameSpace.getp(new ActualField(thisPlayer), new ActualField("newColor"), new FormalField(Color.class));
                if (updatedPersonalColor != null){
                    for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()){
                        if(!Objects.equals(player.getKey(), thisPlayer)){
                            gameState.gameSpace.put(player.getKey(), "newName",thisPlayer,gameState.player1Name,updatedPersonalColor[2]);
                        }
                    }
                }
                Object[] startGame = gameState.gameSpace.getp(new ActualField(thisPlayer),new ActualField("startGame"));
                if(startGame != null){
                    CardLayout cl = (CardLayout)gameState.cards.getLayout();
                    gameState.player1Name = (String)gameState.onlinePlayers.values().toArray()[0];
                    gameState.player2Name = (String)gameState.onlinePlayers.values().toArray()[1];
                    gameState.updateNumberOfHexagons(Integer.parseInt(this.numberOfHexagons));
                    gameState.whosTurn = thisPlayer == 0 ? GameState.Turn.Player1 : GameState.Turn.ONLINE_PLAYER;
                    gameState.startOnlineMove();
                    Panel panel = new Panel(gameState);
                    gameState.onlineMove.subscribe(panel);
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
        return gameState.onlinePlayers;
    }
}
