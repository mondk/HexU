import java.util.ArrayList;
import java.util.Map;

public class OnlineMove implements Runnable{
    GameState gs;
    ArrayList<MoveListener> moveListeners = new ArrayList<>();
    public OnlineMove(GameState gs){
        this.gs = gs;
    }

    /**
     * Subscribes to listening to the onlineMoves class
     * @param newListener   The listener to be added
     */
    public void subscribe(MoveListener newListener){
        moveListeners.add(newListener);
    }
    /**
     * Unsubscribes to listening to the onlineMoves class
     * @param newListener   The listener to be removed
     */
    public void unsubscribe(MoveListener newListener){
        moveListeners.remove(newListener);
    }

    /**
     * Signal to the other players, that a move has been made
     * @param moveId    The ID of the hexagon, that has been clicked
     */
    public void makeMove(int moveId) {
        if(gs.whosTurn != GameState.Turn.ONLINE_PLAYER) {
            gs.online.makeMove(moveId, gs.onlineId);
        }
    }

    /**
     * Signal to the other players, that the game should reset, and the new game should start with startingPlayer
     * @param startingPlayer    The ID of the new starting player
     */
    public void resetGame(Integer startingPlayer) {
        gs.online.resetGame(gs.onlineId, startingPlayer);
    }

    /**
     * The run method checks, as long as the moveThread is not interrupted, if a move has been made, if the
     * game should be reset and if a player has left
     */
    @Override
    public void run() {
        while(!gs.moveThread.isInterrupted()){
            try {
                // Check if a move has been made
                Map.Entry<Integer, Integer> move = gs.online.getMove(gs.onlineId);
                if(move != null) {
                    for (MoveListener listener : moveListeners) {
                        listener.performedMove(move.getValue(), move.getKey());
                    }
                }
                // Check if the game should be reset
                Integer reset = gs.online.getReset(gs.onlineId);
                if(reset != null){
                    for (MoveListener listener : moveListeners){
                        listener.reset(1);
                    }
                }
                // Check if a player has left
                Integer playerLeft = gs.online.getPlayerLeft(gs.onlineId);
                if(playerLeft != null) {
                    gs.disconnectFromOnline();
                    return;
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
