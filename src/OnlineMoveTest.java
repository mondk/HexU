import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class OnlineMoveTest {
    GameState gs;
    GameState gs2;

    @BeforeEach
    void setUp() {
        gs = new GameState();
        gs2 = new GameState();
    }

    @Test
    public void makeMoveTest() throws InterruptedException, IOException {
        gs.hostGame("127.0.0.1");
        gs2.joinGame("127.0.0.1");
        gs.online.startGame(0);
        Thread.sleep(2000);
        gs.onlineMove.makeMove(3);
        Thread.sleep(2000);
        assertTrue(gs2.grid.get(3).clicked);
    }

    @Test
    public void resetGameTest() throws InterruptedException, IOException {
        gs.hostGame("127.0.0.2");
        gs2.joinGame("127.0.0.2");
        gs.online.startGame(0);
        Thread.sleep(2000);
        gs.onlineMove.makeMove(3);
        Thread.sleep(2000);
        assertTrue(gs2.grid.get(3).clicked);
        gs.online.resetGame(0,0);
        Thread.sleep(1000);
        assertFalse(gs2.grid.get(3).clicked);
    }
    @Test
    public void makeMoveSimultaneouslyTest() throws InterruptedException, IOException {
        gs.hostGame("127.0.0.3");
        gs2.joinGame("127.0.0.3");
        Player player = new Player("Jonas", new Color(10,10,10));
        gs2.online.changePlayer(gs2.onlineId, player);
        Thread.sleep(1000);
        gs.online.startGame(0);
        Thread.sleep(3000);
        gs.onlineMove.makeMove(3);
        gs2.onlineMove.makeMove(3);
        Thread.sleep(3000);
        assertTrue(gs2.grid.get(3).clicked);
        assertEquals(gs2.grid.get(3).color,gs.players.get(0).color);
        assertNotEquals(gs2.grid.get(3).color,gs2.players.get(1).color);
        assertEquals(gs.grid.get(3).color,Color.gray);
        assertNotEquals(gs.grid.get(3).color,gs2.players.get(1).color);
    }
}