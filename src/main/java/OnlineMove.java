import org.jspace.ActualField;
import org.jspace.FormalField;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Map;

public class OnlineMove implements Runnable{
    GameState gs;
    ArrayList<MoveListener> moveListeners = new ArrayList<>();
    public OnlineMove(GameState gs){
        this.gs = gs;
        System.out.println("This id is: " + gs.onlineId);
    }

    public void subscribe(MoveListener newListener){
        moveListeners.add(newListener);
    }
    public void unsubscribe(MoveListener newListener){
        moveListeners.remove(newListener);
    }

    public void makeMove(int moveId) throws InterruptedException {
        for(Map.Entry<Integer,String> player: gs.onlinePlayers.entrySet()){
            if(gs.onlineId == player.getKey()) continue;
            gs.gameSpace.put("move", player.getKey(), gs.onlineId, moveId);
            System.out.println("Put a move for " + player.getKey());
        }
    }

    public void resetGame() throws InterruptedException {
        for(Map.Entry<Integer,String> player: gs.onlinePlayers.entrySet()){
            if(gs.onlineId == player.getKey()) continue;
            gs.gameSpace.put(player.getKey(), "reset");
            System.out.println("Put a move for " + player.getKey());
        }
    }

    @Override
    public void run() {
        while(true){
            try {
                Object[] move =gs.gameSpace.getp(new ActualField("move"),new ActualField(gs.onlineId), new FormalField(Integer.class), new FormalField(Integer.class));

                if(move != null) {
                    for (MoveListener listener : moveListeners) {
                        listener.performedMove((Integer) move[2], (Integer) move[3]);
                    }
                }

                Object[] reset = null;
                if (!gs.host) reset = gs.gameSpace.getp(new ActualField(gs.onlineId), new ActualField("reset"));
                if(reset != null){
                    for (MoveListener listener : moveListeners){
                        listener.reset(0);
                    }
                }
                /*
                gs.grid.get(move).clicked=true;
                gs.q.add(move);
                gs.grid.get(move).color= gs.host ? gs.playerColors.get(1) : gs.playerColors.get(0);
                gs.nextTurn();
                paneT.setBackground(gs.paneTColor);
                paneT.setText(gs.paneTurnString);

                ArrayList<ArrayList<Integer>>won = gs.winingState(gs.startP2, gs.playerColors.get(1), gs.winP2);
                //System.out.println(won);
                if (won.get(0).get(0)==1 && gs.host) {
                    gs.gameSpace.put("player2won");
                }
                 */
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
