import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class OnlinePanel extends JPanel {
    ImageIcon img = new ImageIcon("res/background/space.jpg");
    OnlinePanel(GameState gs){
        TextField ip = new TextField(getIp());
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
                gs.hostGame(getIp());
            }
        });
        join.setText("Join game");
        host.setText("Host game");
        //add(playerSettings.getPlayerCards());
        add(ip);
        add(join);
        add(host);
    }

    private String getIp() {
        String ip;

        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (SocketException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }

    @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(this.img.getImage(),0,0, this.getWidth(), this.getHeight(), this);
	}
}
