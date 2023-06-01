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
        gs.waitingRoom.updateName(player1NewName);
        gs.waitingRoom.updateColor(player1NewColor);
        gs2.joinGame("127.0.0.1");
        assertEquals(player1NewName, gs2.onlinePlayers.get(gs.onlineId));
        assertEquals(player1NewColor, gs2.playerColors.get(gs.onlineId));
    }
    @Test
    public void updateNameAfterJoinTest() throws InterruptedException, IOException {
        gs.hostGame("127.0.0.1");
        gs2.joinGame("127.0.0.1");
        gs.waitingRoom.updateName("Jonas");
        gs2.waitingRoom.updateName("Wentzlau");
        // Wait a while for sync
        Thread.sleep(10);
        assertEquals("Jonas", gs2.onlinePlayers.get(gs.onlineId));
        assertEquals("Wentzlau", gs.onlinePlayers.get(gs2.onlineId));
    }
    @Test
    public void updateColourAfterJoinTest() throws IOException, InterruptedException {
        gs.joinGame("127.0.0.1");
        gs2.joinGame("127.0.0.1");
        Color player1Color = new Color(20,20,20);
        Color player2Color = new Color(20,30,40);
        gs.waitingRoom.updateColor(player1Color);
        gs2.waitingRoom.updateColor(player2Color);
        // Wait a while for sync
        Thread.sleep(10);
        assertEquals(player1Color, gs2.playerColors.get(gs.onlineId));
        assertEquals(player2Color, gs.playerColors.get(gs2.onlineId));
    }
}