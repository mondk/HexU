import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class WaitingRoomTest {
    GameState gs;
    GameState gs2;

    @BeforeEach
    void setUp() {
        gs = new GameState();
        gs2 = new GameState();
    }

    @Test
    public void updateNameBeforeJoinTest() throws InterruptedException, IOException {
        gs.hostGame("127.0.0.1");
        String player1NewName = "Jonas";
        Color player1NewColor = new Color(10,10,10);
        Player newPlayer1 = new Player(player1NewName, player1NewColor);
        gs.online.changePlayer(gs.onlineId, newPlayer1);
        gs2.joinGame("127.0.0.1");
        assertEquals(player1NewName, gs2.players.get(gs.onlineId).name);
        assertEquals(player1NewColor, gs2.players.get(gs.onlineId).color);
    }
    @Test
    public void updateNameAfterJoinTest() throws InterruptedException, IOException {
        gs.hostGame("127.0.0.2");
        gs2.joinGame("127.0.0.2");
        String newPlayer1Name = "Jonas";
        Player newPlayer1 = new Player(newPlayer1Name, gs.players.get(0).color);
        gs.online.changePlayer(gs.onlineId, newPlayer1);
        String newPlayer2Name = "Wentzlau";
        Player newPlayer2 = new Player(newPlayer2Name, gs2.players.get(1).color);
        gs2.online.changePlayer(gs2.onlineId, newPlayer2);

        // Wait a while for sync
        Thread.sleep(100);
        assertEquals(newPlayer1Name, gs2.players.get(gs.onlineId).name);
        assertEquals(newPlayer2Name, gs.players.get(gs2.onlineId).name);
    }
    @Test
    public void updateColourAfterJoinTest() throws IOException, InterruptedException {
        gs.hostGame("127.0.0.3");
        gs2.joinGame("127.0.0.3");
        Color player1Color = new Color(20,20,20);
        Player newPlayer1 = new Player(gs.players.get(0).name, player1Color);
        Color player2Color = new Color(20,30,40);
        Player newPlayer2 = new Player(gs2.players.get(1).name, player2Color);
        gs.online.changePlayer(gs.onlineId, newPlayer1);
        gs2.online.changePlayer(gs2.onlineId, newPlayer2);
        // Wait a while for sync
        Thread.sleep(100);
        assertEquals(player1Color, gs2.players.get(gs.onlineId).color);
        assertEquals(player2Color, gs.players.get(gs2.onlineId).color);
    }
    @Test
    public void reconnectAfterDisconnectTest() throws IOException, InterruptedException {
        gs.hostGame("127.0.0.4");
        gs2.joinGame("127.0.0.4");
        Color player1Color = new Color(20,20,20);
        Player newPlayer1 = new Player(gs.players.get(0).name, player1Color);
        Color player2Color = new Color(20,30,40);
        Player newPlayer2 = new Player(gs2.players.get(1).name, player2Color);
        gs.online.changePlayer(gs.onlineId, newPlayer1);
        gs2.online.changePlayer(gs2.onlineId, newPlayer2);
        // Wait a while for sync
        Thread.sleep(100);
        assertEquals(player1Color, gs2.players.get(gs.onlineId).color);
        assertEquals(player2Color, gs.players.get(gs2.onlineId).color);
        gs.disconnectFromOnline();
        Thread.sleep(100);
        gs2.hostGame("127.0.0.4");
        gs.joinGame("127.0.0.4");
        Thread.sleep(100);
        assertEquals(gs.players.get(gs2.onlineId).color, gs2.players.get(gs.onlineId).color);
        assertEquals(gs2.players.get(gs.onlineId).color, gs.players.get(gs2.onlineId).color);
    }
}
