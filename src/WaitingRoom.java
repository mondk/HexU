import org.jspace.ActualField;
import org.jspace.FormalField;

import java.awt.*;
import java.util.*;

public class WaitingRoom implements Runnable {
    GameState gameState;
    int thisPlayer;
    ArrayList<WaitingRoomListener> waitingRoomListeners = new ArrayList<>();
    public static final String PLAYERS_LIST_IDENTIFIER = "PLAYERS";
    private final String NEW_NAME_IDENTIFIER = "NEW_NAME";
    private final String START_GAME_IDENTIFIER = "START_GAME";
    private final String NUMBER_OF_HEXAGONS_IDENTIFIER = "NUMBER_OF_HEXAGONS";
    private final String LEAVE_GAME_IDENTIFIER = "LEAVE_GAME";

    public WaitingRoom(GameState gameState) throws InterruptedException {
        this.gameState = gameState;
        gameState.onlinePlayers = (HashMapIntegerString) gameState.gameSpace.get(new ActualField(PLAYERS_LIST_IDENTIFIER), new FormalField(HashMapIntegerString.class))[1];
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
                gameState.gameSpace.put(player.getKey(), NEW_NAME_IDENTIFIER, thisPlayer, gameState.player1Name, gameState.playerColors.get(thisPlayer).getRGB());
            } catch (Exception e) {
                gameState.playerColors.add(Color.ORANGE);
                gameState.gameSpace.put(player.getKey(), NEW_NAME_IDENTIFIER, thisPlayer, gameState.player1Name, gameState.playerColors.get(thisPlayer).getRGB());
            }
        }
        gameState.onlinePlayers.put(thisPlayer, gameState.player1Name);
        gameState.gameSpace.put(PLAYERS_LIST_IDENTIFIER, gameState.onlinePlayers);
    }

    public void subscribe(WaitingRoomListener newWaitingRoomListener){
        waitingRoomListeners.add(newWaitingRoomListener);
    }

    public void unsubscribe(WaitingRoomListener newWaitingRoomListener){
        waitingRoomListeners.remove(newWaitingRoomListener);
    }

    public void startGame(int startingPlayer){
        try {
            for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
                gameState.gameSpace.put(player.getKey(), START_GAME_IDENTIFIER, startingPlayer);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateNumberOfHexagons(int numberOfHexagons){
        try {
            for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
                gameState.gameSpace.put(player.getKey(), NUMBER_OF_HEXAGONS_IDENTIFIER , numberOfHexagons);
            }
            gameState.numberOfHexagons = numberOfHexagons;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateName(String name){
        try {
            gameState.onlinePlayers = (HashMapIntegerString) gameState.gameSpace.get(new ActualField(PLAYERS_LIST_IDENTIFIER), new FormalField(HashMapIntegerString.class))[1];
            gameState.onlinePlayers.remove(thisPlayer);
            gameState.player1Name = name;
            for (Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
                System.out.println(player.getKey() + " " + thisPlayer);
                gameState.gameSpace.put(player.getKey(), NEW_NAME_IDENTIFIER, thisPlayer, gameState.player1Name,gameState.playerColors.get(thisPlayer).getRGB());
            }
            gameState.onlinePlayers.put(thisPlayer, gameState.player1Name);
            gameState.gameSpace.put(PLAYERS_LIST_IDENTIFIER, gameState.onlinePlayers);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateColor(Color color) throws InterruptedException {
        for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()){
            if(!Objects.equals(player.getKey(), thisPlayer)){
                gameState.gameSpace.put(player.getKey(), PLAYERS_LIST_IDENTIFIER,thisPlayer,gameState.player1Name, color.getRGB());
            }
        }
    }
    public void initializeMap() throws InterruptedException {
        gameState.gameSpace.put(PLAYERS_LIST_IDENTIFIER,new HashMapIntegerString());
    }

    public void disconnect() throws InterruptedException {
        gameState.onlinePlayers = (HashMapIntegerString) gameState.gameSpace.get(new ActualField(PLAYERS_LIST_IDENTIFIER), new FormalField(HashMapIntegerString.class))[1];
        gameState.onlinePlayers.remove(gameState.onlineId);
        for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()){
            gameState.gameSpace.put(player.getKey(), LEAVE_GAME_IDENTIFIER, thisPlayer);
        }
        gameState.gameSpace.put(PLAYERS_LIST_IDENTIFIER, gameState.onlinePlayers);
    }

    @Override
    public void run() {
        while(true){
            try {
                gameState.onlinePlayers = (HashMapIntegerString) gameState.gameSpace.query(new ActualField(PLAYERS_LIST_IDENTIFIER), new FormalField(HashMapIntegerString.class))[1];
                Object[] numberOfHexagons = gameState.gameSpace.getp(new ActualField(thisPlayer),new ActualField(NUMBER_OF_HEXAGONS_IDENTIFIER), new FormalField(Integer.class));
                if(numberOfHexagons != null && !(0 == thisPlayer)){
                    gameState.numberOfHexagons = (Integer) numberOfHexagons[2];
                    for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                        waitingRoomListener.numberOfHexagonsChanged((Integer) numberOfHexagons[2]);
                    }
                }
                Object[] newName = gameState.gameSpace.getp(new ActualField(thisPlayer), new ActualField(NEW_NAME_IDENTIFIER), new FormalField(Integer.class), new FormalField(String.class), new FormalField(Integer.class));

                if(newName != null) {
                    Color newColor = new Color((Integer) newName[4]);
                    for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                        waitingRoomListener.nameChanged((int) newName[2], (String) newName[3], newColor);
                    }
                }

                Object[] playerLeft = gameState.gameSpace.getp(new ActualField(thisPlayer), new ActualField(LEAVE_GAME_IDENTIFIER), new FormalField(Integer.class));
                if(playerLeft != null) {
                    if((Integer)playerLeft[2] < thisPlayer){
                        thisPlayer--;
                        gameState.onlineId--;
                    }
                    for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                        waitingRoomListener.playerLeft((int) playerLeft[2]);
                    }
                }

                Object[] startGame = gameState.gameSpace.getp(new ActualField(thisPlayer),new ActualField(START_GAME_IDENTIFIER), new FormalField(Integer.class));
                if(startGame != null){
                    gameState.startOnlineGame((int)startGame[2]);
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
