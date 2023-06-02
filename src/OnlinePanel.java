import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class OnlinePanel extends JPanel {
    OnlinePanel(GameState gs){
        TextField ip = new TextField("127.0.0.1");
        JButton join = new JButton();
        JButton host = new JButton();
        join.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    gs.joinGame(ip.getText());
                } catch (IOException e) {
                    System.out.println("Invalid ip");
                }
            }
        });
        host.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.hostGame(ip.getText());
            }
        });
        join.setText("Join game");
        host.setText("Host game");
        //add(playerSettings.getPlayerCards());
        add(ip);
        add(join);
        add(host);
    }
}
