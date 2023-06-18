import java.util.*;

/**
 * The WaitingRoom class is the logical part of the Waiting Room. It handles all the changes that can happen until the game starts
 * @author Jónas Holm Wentzlau s203827
 */
public class WaitingRoom implements Runnable {
    GameState gameState;
    int thisPlayer;
    ArrayList<WaitingRoomListener> waitingRoomListeners = new ArrayList<>();

    /**
     * The constructor for WaitingRoom. The initial values that have happened before the player has joined are set here
     * @param gameState
     */
    public WaitingRoom(GameState gameState) {
        this.gameState = gameState;
        gameState.players = gameState.online.getPlayers();
        gameState.onlineId = gameState.players.size();
        gameState.updateNumberOfHexagons(gameState.online.getInitialNumberOfHexagons(gameState.numberOfHexagons));
        gameState.addPlayer();
        this.thisPlayer = gameState.onlineId;
        gameState.online.changePlayer(gameState.onlineId, gameState.players.get(gameState.onlineId));
    }

    public void subscribe(WaitingRoomListener newWaitingRoomListener){
        waitingRoomListeners.add(newWaitingRoomListener);
    }

    public void unsubscribe(WaitingRoomListener newWaitingRoomListener){
        waitingRoomListeners.remove(newWaitingRoomListener);
    }

    /**
     * Sends the signal to start the game to the other players
     * @param startingPlayer    The player who should make the first move
     */
    public void startGame(int startingPlayer){
        gameState.online.startGame(startingPlayer);
    }

    /**
     * Sends a signal to the other players that the number of hexagons has been updated, including the new number of hexagons.
     * The signal includes the updated number of hexagons. Also updates the number of hexagons locally
     * @param numberOfHexagons  The new number of hexagons
     */
    public void updateNumberOfHexagons(int numberOfHexagons){
        gameState.online.updateNumberOfHexagons(numberOfHexagons);
        gameState.updateNumberOfHexagons(numberOfHexagons);
    }

    /**
     * Sends a signal that a player has been changed. The signal includes the ID and the information of the player
     * @param id        The ID of the updated player
     * @param player    The updated information of the player
     */
    public void changePlayer(int id, Player player){
        gameState.online.changePlayer(id, player);
    }

    /**
     * The run method checks, as long as the WaitingRoom thread is not interrupted, if the number of hexagons has changed,
     * if a player has joined or changed, if a player has left and if the game should start
     */
    @Override
    public void run() {
        while(!Thread.interrupted()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // For some reason, interrupt doesn't always interrupt. However, if this is here, it works
                return;
            }
            // Check if the number of hexagons has changed
            Integer numberOfHexagonsChanged = gameState.online.numberOfHexagonsChanged(gameState.onlineId);
            if(numberOfHexagonsChanged != null && !(0 == thisPlayer)){
                gameState.updateNumberOfHexagons(numberOfHexagonsChanged);
                for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                    waitingRoomListener.numberOfHexagonsChanged(numberOfHexagonsChanged);
                }
            }
            // Check if there is a new player
            Map.Entry<Integer,Player> newPlayer = gameState.online.getNewPlayer(gameState.onlineId);
            if(newPlayer != null) {
                gameState.players.put(newPlayer.getKey(), newPlayer.getValue());
                for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                    waitingRoomListener.playerChanged(newPlayer.getKey(), newPlayer.getValue());
                }
            }
            // Check if a player has left
            Integer playerLeft = gameState.online.getPlayerLeft(gameState.onlineId);
            if(playerLeft != null) {
                if (playerLeft == 0){
                    gameState.disconnectFromOnline();
                    return;
                }
                else if(playerLeft < thisPlayer){
                    gameState.onlineId--;
                }
                for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                    waitingRoomListener.playerLeft(playerLeft);
                }
            }
            // Checks if the game should start
            Integer startGame = gameState.online.getStartGame(gameState.onlineId);
            if(startGame != null){
                gameState.startOnlineGame(startGame);
                break;
            }
        }
    }
    public Integer getThisPlayer(){
        return thisPlayer;
    }
}
