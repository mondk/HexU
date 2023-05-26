import org.jspace.ActualField;
import org.jspace.FormalField;
import org.jspace.Space;

import java.util.ArrayList;
import java.util.List;

public class MoveListener implements Runnable{
    List<Integer> moves;
    Space space;
    String otherPlayer;
    public MoveListener(Space space, String otherPlayer, List<Integer> moves){
        this.space = space;
        this.otherPlayer = otherPlayer;
        this.moves = moves;
    }

    @Override
    public void run() {
        while(true){
            try {
                Integer move = (Integer)space.get(new ActualField(otherPlayer), new FormalField(Integer.class))[1];
                moves.add(move);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
