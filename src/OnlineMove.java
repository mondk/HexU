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

    public void makeMove(int moveId) throws InterruptedException {
        gs.online.makeMove(moveId, gs.onlineId);
    }

    public void resetGame() {
        gs.online.resetGame(gs.onlineId);
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

                boolean reset = gs.online.getReset(gs.onlineId);
                if(reset){
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
