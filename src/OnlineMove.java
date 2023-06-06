import java.util.ArrayList;
import java.util.Map;

public class OnlineMove implements Runnable{
    GameState gs;
    ArrayList<MoveListener> moveListeners = new ArrayList<>();
    public OnlineMove(GameState gs){
        this.gs = gs;
    }

    public void subscribe(MoveListener newListener){
        moveListeners.add(newListener);
    }
    public void unsubscribe(MoveListener newListener){
        moveListeners.remove(newListener);
    }

    public void makeMove(int moveId) {
        if(gs.whosTurn != GameState.Turn.ONLINE_PLAYER) {
            gs.online.makeMove(moveId, gs.onlineId);
        }
    }

    public void resetGame(Integer startingPlayer) {
        gs.online.resetGame(gs.onlineId, startingPlayer);
    }

    @Override
    public void run() {
        while(true){
            try {
                Map.Entry<Integer, Integer> move = gs.online.getMove(gs.onlineId);
                if(move != null) {
                    for (MoveListener listener : moveListeners) {
                        listener.performedMove(move.getValue(), move.getKey());
                    }
                }

                Integer reset = gs.online.getReset(gs.onlineId);
                if(reset != null){
                    for (MoveListener listener : moveListeners){
                        listener.reset(0);
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
