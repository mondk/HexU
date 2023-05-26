import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import java.util.ArrayList;
import java.util.List;

public class MoveListener implements Runnable{
    GameState gs;
    public MoveListener(GameState gs){
        this.gs = gs;
    }

    @Override
    public void run() {
        while(true){
            try {
                Integer move = gs.host ? (Integer)gs.gameSpace.get(new ActualField("Player2"), new FormalField(Integer.class))[1] : (Integer)gs.gameSpace.get(new ActualField("Player1"), new FormalField(Integer.class))[1];
                gs.grid.get(move).clicked=true;

                gs.q.add(move);
                gs.grid.get(move).color= gs.host ? gs.colorP2 : gs.colorP1;
                gs.nextTurn();
                System.out.println("Got a move!");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
