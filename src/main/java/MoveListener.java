import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class MoveListener implements Runnable{
    GameState gs;
    JTextPane paneT;
    public MoveListener(GameState gs, JTextPane paneT){
        this.gs = gs;
        this.paneT = paneT;
    }

    @Override
    public void run() {
        while(true){
            try {
                Integer move = gs.host ? (Integer)gs.gameSpace.get(new ActualField("Player2"), new FormalField(Integer.class))[1] : (Integer)gs.gameSpace.get(new ActualField("Player1"), new FormalField(Integer.class))[1];
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
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
