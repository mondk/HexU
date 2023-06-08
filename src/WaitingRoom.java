import java.util.*;

public class WaitingRoom implements Runnable {
    GameState gameState;
    int thisPlayer;
    ArrayList<WaitingRoomListener> waitingRoomListeners = new ArrayList<>();

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

    public void startGame(int startingPlayer){
        gameState.online.startGame(startingPlayer);
    }

    public void updateNumberOfHexagons(int numberOfHexagons){
        gameState.online.updateNumberOfHexagons(numberOfHexagons);
        gameState.updateNumberOfHexagons(numberOfHexagons);
    }

    public void changePlayer(int id, Player player){
        gameState.online.changePlayer(id, player);
    }

    @Override
    public void run() {
        while(!Thread.interrupted()){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // For some reason, interrupt doesn't always interrupt. However, if this is here, it works
                return;
            }
            Integer numberOfHexagonsChanged = gameState.online.numberOfHexagonsChanged(gameState.onlineId);
            if(numberOfHexagonsChanged != null && !(0 == thisPlayer)){
                gameState.updateNumberOfHexagons(numberOfHexagonsChanged);
                for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                    waitingRoomListener.numberOfHexagonsChanged(numberOfHexagonsChanged);
                }
            }
            Map.Entry<Integer,Player> newPlayer = gameState.online.getNewPlayer(gameState.onlineId);
            if(newPlayer != null) {
                gameState.players.put(newPlayer.getKey(), newPlayer.getValue());
                for(WaitingRoomListener waitingRoomListener : waitingRoomListeners) {
                    waitingRoomListener.playerChanged(newPlayer.getKey(), newPlayer.getValue());
                }
            }

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
