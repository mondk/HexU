import java.util.HashMap;
import java.util.Map;

public interface Online {
	void start(boolean host, String ip);

	HashMap<Integer,Player> getPlayers();

	void changePlayer(Integer id, Player player);

	void startGame(int startingPlayer);

	void updateNumberOfHexagons(int numberOfHexagons);

	void disconnect(Integer onlineId);

	Integer numberOfHexagonsChanged(Integer onlineId);

	Map.Entry<Integer,Player> getNewPlayer(int id);

	Integer getPlayerLeft(Integer onlineId);

	Integer getStartGame(Integer onlineId);

	void makeMove(int moveId, Integer onlineId);

	void resetGame(Integer onlineId);

	Map.Entry<Integer, Integer> getMove(Integer onlineId);

	boolean getReset(Integer onlineId);
}
