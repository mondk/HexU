import org.jspace.RemoteSpace;
import org.jspace.SequentialSpace;
import org.jspace.SpaceRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class OnlinePanel extends JPanel {
    OnlinePanel(GameState gs){
        TextField name = new TextField("PlayerName");
        TextField ip = new TextField("127.0.0.1");
        JButton join = new JButton();
        JButton host = new JButton();
        join.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    RemoteSpace space = new RemoteSpace("tcp://" + ip.getText() + ":9001/game?keep");
                    gs.setGameSpace(space);
                    space.put("Player2Name",name.getText());
                    gs.host = false;
                    gs.playerState = GameState.State.ONLINE;
                    gs.whosTurn = GameState.Turn.Player2;
                    gs.player1Name = name.getText();

                    //Panel game = new Panel(gs);
                    WaitingRoom waitingRoom = new WaitingRoom(gs);
                } catch (IOException e) {
                    System.out.println("Invalid ip");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        host.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SpaceRepository repository = new SpaceRepository();
                SequentialSpace space = new SequentialSpace();
                repository.add("game",space);
                repository.addGate("tcp://" + ip.getText() + ":9001/?keep");
                gs.setGameSpace(space);
                try {
                    space.put("Player1Name", name.getText());
                    space.put("players",new HashMapIntegerString());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                gs.playerState = GameState.State.ONLINE;
                gs.host = true;
                gs.player1Name = name.getText();
                //Panel panel = new Panel(gs);
                WaitingRoom waitingRoom = new WaitingRoom(gs);
            }
        });
        join.setText("Join game");
        host.setText("Host game");
        add(name);
        add(ip);
        add(join);
        add(host);
    }
}
