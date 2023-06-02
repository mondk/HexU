public interface MoveListener {
    void performedMove(int moveId, int playerId) throws InterruptedException;
    void reset(int id) throws InterruptedException;
}
