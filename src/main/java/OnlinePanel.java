import org.jspace.RemoteSpace;
import org.jspace.SpaceRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class OnlinePanel extends JPanel {
    OnlinePanel(GameState gs){
        this.setPreferredSize(gs.SCREEN_SIZE);
        TextField ip = new TextField("127.0.0.1");
        JButton join = new JButton();
        JButton host = new JButton();
        join.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    RemoteSpace space = new RemoteSpace("tcp://" + ip.getText() + ":9001/game?keep");
                } catch (IOException e) {
                    System.out.println("Invalid ip");
                }
            }
        });
        host.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                SpaceRepository repository = new SpaceRepository();
            }
        });
        join.setText("Join game");
        host.setText("Host game");
        add(ip);
        add(join);
        add(host);
    }
}
