import java.util.HashMap;
import java.util.Map;

public interface Online {
	void start(boolean host, String ip);

	HashMap<Integer,Player> getPlayers();

	void changePlayer(Integer onlineId, Player player);

	void startGame(int startingPlayer);

	void updateNumberOfHexagons(int numberOfHexagons);

	void disconnect(Integer onlineId);
	Integer getInitialNumberOfHexagons(int standardValue);

	Integer numberOfHexagonsChanged(Integer onlineId);

	Map.Entry<Integer,Player> getNewPlayer(Integer onlineId);

	Integer getPlayerLeft(Integer onlineId);

	Integer getStartGame(Integer onlineId);

	void makeMove(int moveId, Integer onlineId);

	void resetGame(Integer onlineId, Integer startingPlayer);

	Map.Entry<Integer, Integer> getMove(Integer onlineId);

	Integer getReset(Integer onlineId);
}
