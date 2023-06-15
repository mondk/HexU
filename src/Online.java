import java.util.HashMap;
import java.util.Map;

/**
 * An interface to implement online functionality
 */
public interface Online {
	/**
	 * Creates or joins an online room.
	 *
	 * @param host	Indicates whether the user is the host or a client
	 * @param ip	The ip that is connected to or hosted on
	 */
	void start(boolean host, String ip);

	/**
	 * A method to get all the players that are connected to game.
	 * @return	The connected players
	 */
	HashMap<Integer,Player> getPlayers();

	/**
	 * Updates the player with the ID of onlineId to the player
	 * @param onlineId	The ID of the player to be updated
	 * @param player	The updated player information.
	 */
	void changePlayer(Integer onlineId, Player player);

	/**
	 * A method to signal the other players that the game is starting
	 * @param startingPlayer	The id of the player who should start.
	 */
	void startGame(int startingPlayer);

	/**
	 * A method to signal the other players the number of hexagons the game should have.
	 * @param numberOfHexagons	The updated number of hexagons
	 */
	void updateNumberOfHexagons(int numberOfHexagons);

	/**
	 * A method to signal the other players that a player has closed or left the game
	 * @param onlineId	The id of the disconnecting player
	 */
	void disconnect(Integer onlineId);
	/**
	 * A method to get the number of hexagons the game should have
	 * @param standardValue	A value, in case the number of hexagons has never been set
	 * @return				The number of hexagons the game should have
	 */
	Integer getInitialNumberOfHexagons(int standardValue);

	/**
	 * A method to check if the number of hexagons has changed
	 * @param onlineId	The ID of the local player
	 * @return			The new number of hexagons
	 */
	Integer numberOfHexagonsChanged(Integer onlineId);

	/**
	 * A method to check if a player has changed or if a new player has joined
	 * @param onlineId	The ID of the local player
	 * @return			The map entry with the ID as the key and the Player info as the value
	 */
	Map.Entry<Integer,Player> getNewPlayer(Integer onlineId);

	/**
	 * A method to check if a player has left
	 * @param onlineId	The ID of the local player
	 * @return			The ID of the player who left
	 */
	Integer getPlayerLeft(Integer onlineId);

	/**
	 * A method to check if a player has signalled to start the game
	 * @param onlineId	The ID of the local player
	 * @return			The ID of the player, who should start
	 */
	Integer getStartGame(Integer onlineId);

	/**
	 * A method to signal the other players, that a move has been made
	 * @param moveId	The ID of the hexagon the player has clicked on
	 * @param onlineId	The ID of the player, who made the move
	 */
	void makeMove(int moveId, Integer onlineId);

	/**
	 *	A method to signal the other players, that the game should be reset
	 * @param onlineId			The ID of the local player
	 * @param startingPlayer	The ID of the new starting player
	 */
	void resetGame(Integer onlineId, Integer startingPlayer);

	/**
	 * A method to get a move another player has made. Returns a Map.Entry, as java does not have native support for pairs
	 * @param onlineId	The id of the local player
	 * @return			A pair of integers, the key being the ID of the player who made the move, and the value being the ID of the hexagon.
	 */
	Map.Entry<Integer, Integer> getMove(Integer onlineId);

	/**
	 * A method to see if a player has signalled to reset the game
	 * @param onlineId	The ID of the local player.
	 * @return			The ID of the starting player.
	 */
	Integer getReset(Integer onlineId);
}
