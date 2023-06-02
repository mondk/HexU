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
        Thread.sleep(100);
        gs.online.makeMove(3,0);
        Thread.sleep(100);
        assertTrue(gs2.grid.get(3).clicked);
    }

    @Test
    public void resetGameTest() throws InterruptedException, IOException {
        gs.hostGame("127.0.0.2");
        gs2.joinGame("127.0.0.2");
        gs.online.startGame(0);
        Thread.sleep(100);
        gs.online.makeMove(3,0);
        Thread.sleep(100);
        assertTrue(gs2.grid.get(3).clicked);
        gs.online.resetGame(0,0);
        Thread.sleep(100);
        assertFalse(gs2.grid.get(3).clicked);
    }
}