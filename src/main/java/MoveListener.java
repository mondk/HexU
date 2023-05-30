public interface MoveListener {
    void performedMove(int id) throws InterruptedException;
    void reset(int id);
}
