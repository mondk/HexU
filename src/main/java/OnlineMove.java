import org.jspace.ActualField;
import org.jspace.FormalField;

import javax.swing.*;
import java.util.ArrayList;

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


    @Override
    public void run() {
        while(true){
            try {
                Integer move = gs.host ? (Integer)gs.gameSpace.get(new ActualField("Player2"), new FormalField(Integer.class))[1] : (Integer)gs.gameSpace.get(new ActualField("Player1"), new FormalField(Integer.class))[1];
                for (MoveListener listener : moveListeners) {
                    listener.performedMove(move);
                }
                Object[] reset = null;
                if (!gs.host) reset = gs.gameSpace.getp(new ActualField("Player2"), new ActualField("reset"));
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
