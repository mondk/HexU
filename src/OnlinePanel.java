import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * The GUI panel for the initial online screen, where it is possible to host or join a game
 * @author Jónas Holm Wentzlau s203827
 */
public class OnlinePanel extends JPanel {
    ImageIcon img = new ImageIcon(this.getClass().getResource("res/background/space.jpg"));
    OnlinePanel(GameState gs){
        TextField ip = new TextField(getIp());
        JButton join = new JButton();
        JButton host = new JButton();
        JButton backtoMenuOnline = new JButton();
        // When clicking the joinGame button, the player will join an existing waiting room
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
        // When clicking the hostGame button, the player will create a room
        host.setAction(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                gs.hostGame(getIp());
            }
        });
        // When clicking the backToMenuOnline button, the program returns to the main menu.
        backtoMenuOnline.setAction(new AbstractAction() {
            @Override
			public void actionPerformed(ActionEvent e){
				gs.returnToMenu();
			}
        });
        join.setText("Join game");
        host.setText("Host game");
        backtoMenuOnline.setText("Back to Menu");
        add(ip);
        add(join);
        add(host);
        add(backtoMenuOnline);
    }

    /**
     * Gets the local IP address of the machine
     * @return  The IP address of the machine, formatted in a String
     */
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

    /**
     * Draws the background
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
        g.drawImage(this.img.getImage(),0,0, this.getWidth(), this.getHeight(), this);
	}
}
