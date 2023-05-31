public interface MoveListener {
    void performedMove(int playerId, int moveId) throws InterruptedException;
    void reset(int id) throws InterruptedException;
}
