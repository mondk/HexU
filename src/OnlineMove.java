import org.jspace.ActualField;
import org.jspace.FormalField;

import java.util.ArrayList;
import java.util.Map;

public class OnlineMove implements Runnable{
    GameState gs;
    ArrayList<MoveListener> moveListeners = new ArrayList<>();
    public OnlineMove(GameState gs){
        this.gs = gs;
        System.out.println("This id is: " + gs.players);
    }

    public void subscribe(MoveListener newListener){
        moveListeners.add(newListener);
    }
    public void unsubscribe(MoveListener newListener){
        moveListeners.remove(newListener);
    }

    public void makeMove(int moveId) throws InterruptedException {
        gs.online.makeMove(moveId, gs.onlineId);
        /*
        for(Map.Entry<Integer,String> player: gs.onlinePlayers.entrySet()){
            if(gs.onlineId == player.getKey()) continue;
            gs.gameSpace.put("move", player.getKey(), gs.onlineId, moveId);
            System.out.println("Put a move for " + player.getKey());
        }
         */
    }

    public void resetGame() throws InterruptedException {
        gs.online.resetGame(gs.onlineId);
        /*
        for(Map.Entry<Integer,String> player: gs.onlinePlayers.entrySet()){
            if(gs.onlineId == player.getKey()) continue;
            gs.gameSpace.put(player.getKey(), "reset");
            System.out.println("Put a move for " + player.getKey());
        }
         */
    }

    @Override
    public void run() {
        while(true){
            try {
                Map.Entry<Integer, Integer> move = gs.online.getMove(gs.onlineId);
                //Object[] move = gs.gameSpace.getp(new ActualField("move"),new ActualField(gs.onlineId), new FormalField(Integer.class), new FormalField(Integer.class));

                if(move != null) {
                    System.out.println("Got a move " + move);
                    for (MoveListener listener : moveListeners) {
                        listener.performedMove(move.getValue(), move.getKey());
                    }
                }

                //Object[] reset = null;
                //if (!gs.host) reset = gs.gameSpace.getp(new ActualField(gs.onlineId), new ActualField("reset"));
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
