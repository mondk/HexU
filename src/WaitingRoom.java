import org.jspace.ActualField;
import org.jspace.FormalField;

import java.awt.*;
import java.util.*;

public class WaitingRoom implements Runnable {
    GameState gameState;
    int thisPlayer;
    ArrayList<WaitingRoomListener> waitingRoomListeners = new ArrayList<>();
    /*
    public static final String PLAYERS_LIST_IDENTIFIER = "PLAYERS";
    private final String NEW_NAME_IDENTIFIER = "NEW_NAME";
    private final String START_GAME_IDENTIFIER = "START_GAME";
    private final String NUMBER_OF_HEXAGONS_IDENTIFIER = "NUMBER_OF_HEXAGONS";
    private final String LEAVE_GAME_IDENTIFIER = "LEAVE_GAME";
     */

    public WaitingRoom(GameState gameState) throws InterruptedException {
        this.gameState = gameState;
        //gameState.players = (HashMapIntegerString) gameState.online.get(new ActualField(PLAYERS_LIST_IDENTIFIER), new FormalField(HashMapIntegerString.class))[1];
        gameState.players = gameState.online.getPlayers();
        gameState.onlineId = gameState.players.size();
        gameState.addPlayer();
        this.thisPlayer = gameState.onlineId;
        /*
        for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
            try {
                gameState.players.get(player.getKey());
            } catch (IndexOutOfBoundsException e) {
                //gameState.playerColors.add(Color.ORANGE);
                gameState.addPlayer();
            }
        }
        for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
            try {
                gameState.gameSpace.put(player.getKey(), NEW_NAME_IDENTIFIER, thisPlayer, gameState.players.get(gameState.onlineId).name, gameState.players.get(thisPlayer).color.getRGB());
            } catch (Exception e) {
                //gameState.playerColors.add(Color.ORANGE);
                gameState.addPlayer();
                gameState.gameSpace.put(player.getKey(), NEW_NAME_IDENTIFIER, thisPlayer, gameState.players.get(gameState.onlineId).name, gameState.players.get(thisPlayer).color.getRGB());
            }
        }
         */
        gameState.online.changePlayer(gameState.onlineId, gameState.players.get(gameState.onlineId));
        //gameState.onlinePlayers.put(thisPlayer, gameState.players.get(gameState.onlineId).name);
        //gameState.gameSpace.put(PLAYERS_LIST_IDENTIFIER, gameState.onlinePlayers);
    }

    public void subscribe(WaitingRoomListener newWaitingRoomListener){
        waitingRoomListeners.add(newWaitingRoomListener);
    }

    public void unsubscribe(WaitingRoomListener newWaitingRoomListener){
        waitingRoomListeners.remove(newWaitingRoomListener);
    }

    public void startGame(int startingPlayer){
        gameState.online.startGame(startingPlayer);
        /*
        try {
            for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
                gameState.gameSpace.put(player.getKey(), START_GAME_IDENTIFIER, startingPlayer);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         */
    }

    public void updateNumberOfHexagons(int numberOfHexagons){
        gameState.online.updateNumberOfHexagons(numberOfHexagons);
        gameState.updateNumberOfHexagons(numberOfHexagons);
        /*
        try {
            for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
                gameState.gameSpace.put(player.getKey(), NUMBER_OF_HEXAGONS_IDENTIFIER , numberOfHexagons);
            }
            gameState.numberOfHexagons = numberOfHexagons;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
         */
    }

    public void changePlayer(int id, Player player){
        gameState.online.changePlayer(id, player);
    }
    /*
    public void updateName(String name){
        try {
            for (Map.Entry<Integer, String> player : gameState.onlinePlayers.entrySet()) {
                if (!Objects.equals(player.getKey(), thisPlayer)) {
                    gameState.gameSpace.put(player.getKey(), NEW_NAME_IDENTIFIER, thisPlayer, name, gameState.players.get(gameState.onlineId).color);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        /*
        try {
            gameState.onlinePlayers = (HashMapIntegerString) gameState.gameSpace.get(new ActualField(PLAYERS_LIST_IDENTIFIER), new FormalField(HashMapIntegerString.class))[1];
            gameState.onlinePlayers.remove(thisPlayer);
            gameState. = name;
            for (Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()) {
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
                gameState.gameSpace.put(player.getKey(), NEW_NAME_IDENTIFIER,thisPlayer,gameState.players.get(gameState.onlineId).name, color.getRGB());
            }
        }
    }

    public void initializeMap() throws InterruptedException {
        gameState.gameSpace.put(PLAYERS_LIST_IDENTIFIER,new HashMapIntegerString());
    }
    */
    public void disconnect() throws InterruptedException {
        gameState.online.disconnect(gameState.onlineId);
        /*
        gameState.onlinePlayers = (HashMapIntegerString) gameState.gameSpace.get(new ActualField(PLAYERS_LIST_IDENTIFIER), new FormalField(HashMapIntegerString.class))[1];
        gameState.onlinePlayers.remove(gameState.onlineId);
        for(Map.Entry<Integer,String> player : gameState.onlinePlayers.entrySet()){
            gameState.gameSpace.put(player.getKey(), LEAVE_GAME_IDENTIFIER, thisPlayer);
        }
        gameState.gameSpace.put(PLAYERS_LIST_IDENTIFIER, gameState.onlinePlayers);
         */
    }

    @Override
    public void run() {
        while(true){
            try {
                //gameState.players = (HashMapIntegerString) gameState.gameSpace.query(new ActualField(PLAYERS_LIST_IDENTIFIER), new FormalField(HashMapIntegerString.class))[1];
                gameState.players = gameState.online.getPlayers();
                //Object[] numberOfHexagons = gameState.gameSpace.getp(new ActualField(thisPlayer),new ActualField(NUMBER_OF_HEXAGONS_IDENTIFIER), new FormalField(Integer.class));
                Integer numberOfHexagonsChanged = gameState.online.numberOfHexagonsChanged(gameState.onlineId);
                if(numberOfHexagonsChanged != null && !(0 == thisPlayer)){
                    gameState.updateNumberOfHexagons(numberOfHexagonsChanged);
                    for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                        waitingRoomListener.numberOfHexagonsChanged(numberOfHexagonsChanged);
                    }
                }
                //Player newName = gameState.gameSpace.getp(new ActualField(thisPlayer), new ActualField(NEW_NAME_IDENTIFIER), new FormalField(Integer.class), new FormalField(String.class), new FormalField(Integer.class));
                Map.Entry<Integer,Player> newPlayer = gameState.online.getNewPlayer(gameState.onlineId);
                if(newPlayer != null) {
                    for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                        waitingRoomListener.playerChanged(newPlayer.getKey(), newPlayer.getValue());
                    }
                }

                //Integer playerLeft = gameState.gameSpace.getp(new ActualField(thisPlayer), new ActualField(LEAVE_GAME_IDENTIFIER), new FormalField(Integer.class));
                Integer playerLeft = gameState.online.getPlayerLeft(gameState.onlineId);
                if(playerLeft != null) {
                    if(playerLeft < thisPlayer){
                        gameState.onlineId--;
                    }
                    for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                        waitingRoomListener.playerLeft(playerLeft);
                    }
                }

                //Object[] startGame = gameState.gameSpace.getp(new ActualField(thisPlayer),new ActualField(START_GAME_IDENTIFIER), new FormalField(Integer.class));
                Integer startGame = gameState.online.getStartGame(gameState.onlineId);
                if(startGame != null){
                    gameState.startOnlineGame(startGame);
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
}
