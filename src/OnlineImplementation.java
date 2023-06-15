import org.jspace.*;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * An implementation of the Online interface, which utilizes the jSpace library
 */
public class OnlineImplementation implements Online{
	private Space space;
	SpaceRepository repository;

	/**
	 * Identifiers for knowing what the message received means.
	 */
	public final String PLAYERS_LIST_IDENTIFIER = "ALL_PLAYERS";
	private final String PLAYER_CHANGED_IDENTIFIER = "PLAYER_CHANGED";
	private final String START_GAME_IDENTIFIER = "START_GAME";
	private final String NUMBER_OF_HEXAGONS_IDENTIFIER = "NUMBER_OF_HEXAGONS";
	private final String NUMBER_OF_HEXAGONS_CHANGED_IDENTIFIER = "NUMBER_OF_HEXAGONS_CHANGED";
	private final String PLAYER_LEFT_IDENTIFIER = "LEAVE_GAME";
	private final String PLAYER_IDENTIFIER = "PLAYER";
	private final String MOVE_IDENTIFIER = "MOVE";
	private final String RESET_GAME_IDENTIFIER = "RESET";
	public OnlineImplementation(){
	}

	/**
	 * Creates or joins the online tuple space.
	 *
	 * @param host	Indicates whether the user is the host or a client
	 * @param ip	The ip that is connected to or hosted on
	 */
	@Override
	public void start(boolean host, String ip) {
		if(host){
			repository = new SpaceRepository();
			space = new SequentialSpace();
			repository.add("game",space);
			repository.addGate("tcp://" + ip + ":9001/?keep");
			try {
				space.put(PLAYERS_LIST_IDENTIFIER,new HashMapIntegerString());
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		} else {
			try {
				space = new RemoteSpace("tcp://" + ip + ":9001/game?keep");
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * A method to get all the players that are connected to the tuple space
	 * @return	The connected players
	 */

	@Override
	public HashMap<Integer, Player> getPlayers() {
		HashMap<Integer, Player> players = new HashMap<>();
		try {
			List<Object[]> playerList = space.queryAll(new ActualField(PLAYER_IDENTIFIER), new FormalField(Integer.class), new FormalField(String.class), new FormalField(Integer.class));
			for(Object[] player : playerList){
				players.put((Integer) player[1], new Player((String) player[2], new Color((Integer) player[3])));
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		return players;
	}

	/**
	 * Updates the player with the ID of onlineId to the player
	 * @param onlineId	The ID of the player to be updated
	 * @param player	The updated player information.
	 */
	@Override
	public void changePlayer(Integer onlineId, Player player) {
		try {
			space.getp(new ActualField(PLAYER_IDENTIFIER), new ActualField(onlineId), new FormalField(String.class), new FormalField(Integer.class));
			space.put(PLAYER_IDENTIFIER, onlineId, player.name, player.color.getRGB());
			for(Integer otherPlayer : getPlayers().keySet()) {
				if(onlineId.equals(otherPlayer)) continue;
				space.put(PLAYER_CHANGED_IDENTIFIER, onlineId, otherPlayer);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A method to signal the other players that the game is starting
	 * @param startingPlayer	The id of the player who should start.
	 */
	@Override
	public void startGame(int startingPlayer) {
		HashMap<Integer, Player> players = getPlayers();
		for(Map.Entry<Integer,Player> player : players.entrySet()){
			try {
				space.put(player.getKey(), START_GAME_IDENTIFIER, startingPlayer);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	/**
	 * A method to signal the other players the number of hexagons the game should have.
	 * @param numberOfHexagons	The updated number of hexagons
	 */
	@Override
	public void updateNumberOfHexagons(int numberOfHexagons) {
		try {
			space.getp(new ActualField(NUMBER_OF_HEXAGONS_IDENTIFIER), new FormalField(Integer.class));
			space.put(NUMBER_OF_HEXAGONS_IDENTIFIER, numberOfHexagons);
			HashMap<Integer, Player> players = getPlayers();
			for(Map.Entry<Integer,Player> player : players.entrySet()){
				space.put(player.getKey(), NUMBER_OF_HEXAGONS_CHANGED_IDENTIFIER , numberOfHexagons);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A method to signal the other players that a player has closed or left the game
	 * @param onlineId	The id of the disconnecting player
	 */
	@Override
	public void disconnect(Integer onlineId) {
		try {
			space.getp(new ActualField(PLAYER_IDENTIFIER), new ActualField(onlineId), new FormalField(String.class), new FormalField(Integer.class));
			for(Map.Entry<Integer,Player> player : getPlayers().entrySet()){
				space.put(PLAYER_LEFT_IDENTIFIER, onlineId, player.getKey());
			}
			while(repository != null) {
				if (getPlayers().size() == 0){
					repository.closeGates();
					break;
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A method to get the number of hexagons the game should have
	 * @param standardValue	A value, in case the number of hexagons has never been set
	 * @return				The number of hexagons the game should have
	 */
	@Override
	public Integer getInitialNumberOfHexagons(int standardValue) {
		try {
			Object[] value = space.queryp(new ActualField(NUMBER_OF_HEXAGONS_IDENTIFIER), new FormalField(Integer.class));
			return value != null ? (Integer) value[1] : standardValue;
		} catch (InterruptedException e) {
			return standardValue;
		}
	}

	/**
	 * A method to check if the number of hexagons has changed
	 * @param onlineId	The ID of the local player
	 * @return			The new number of hexagons
	 */
	@Override
	public Integer numberOfHexagonsChanged(Integer onlineId) {
		try {
			Object[] value = space.getp(new ActualField(onlineId),new ActualField(NUMBER_OF_HEXAGONS_CHANGED_IDENTIFIER), new FormalField(Integer.class));
			return value != null ? (Integer)value[2] : null;
		} catch (InterruptedException e) {
			return null;
		}
	}

	/**
	 * A method to check if a player has changed or if a new player has joined
	 * @param onlineId	The ID of the local player
	 * @return			The map entry with the ID as the key and the Player info as the value
	 */
	@Override
	public Map.Entry<Integer,Player> getNewPlayer(Integer onlineId) {
		try {
			Object[] newPlayer = space.getp(new ActualField(PLAYER_CHANGED_IDENTIFIER), new FormalField(Integer.class), new ActualField(onlineId));
			return newPlayer != null ? new AbstractMap.SimpleEntry<Integer,Player>((Integer)newPlayer[1], getPlayers().get((Integer)newPlayer[1])) : null;
		} catch (InterruptedException e) {
			return null;
		}
	}

	/**
	 * A method to check if a player has left
	 * @param onlineId	The ID of the local player
	 * @return			The ID of the player who left
	 */
	@Override
	public Integer getPlayerLeft(Integer onlineId) {
		try {
			Object[] value = space.getp(new ActualField(PLAYER_LEFT_IDENTIFIER), new FormalField(Integer.class), new ActualField(onlineId));
			return value != null ? (Integer)value[1] : null;
		} catch (InterruptedException e) {
			return null;
		}
	}

	/**
	 * A method to check if a player has signalled to start the game
	 * @param onlineId	The ID of the local player
	 * @return			The ID of the player, who should start
	 */
	@Override
	public Integer getStartGame(Integer onlineId) {
		try {
			Object[] value = space.getp(new ActualField(onlineId), new ActualField(START_GAME_IDENTIFIER), new FormalField(Integer.class));
			return value != null ? (Integer) value[2] : null;
		} catch (InterruptedException e) {
			return null;
		}
	}

	/**
	 * A method to signal the other players, that a move has been made
	 * @param moveId	The ID of the hexagon the player has clicked on
	 * @param onlineId	The ID of the player, who made the move
	 */
	@Override
	public void makeMove(int moveId, Integer onlineId) {
		try {
			for (Map.Entry<Integer, Player> player : getPlayers().entrySet()) {
				if (Objects.equals(onlineId, player.getKey())) continue;
				space.put(MOVE_IDENTIFIER, player.getKey(), onlineId, moveId);
			}
		} catch (InterruptedException e) {

		} catch (NullPointerException e) {}
	}

	/**
	 *	A method to signal the other players, that the game should be reset
	 * @param onlineId			The ID of the local player
	 * @param startingPlayer	The ID of the new starting player
	 */
	@Override
	public void resetGame(Integer onlineId, Integer startingPlayer) {
		try {
			for (Map.Entry<Integer, Player> player : getPlayers().entrySet()) {
				if (Objects.equals(onlineId, player.getKey())) continue;
				space.put(player.getKey(), RESET_GAME_IDENTIFIER, startingPlayer);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (NullPointerException e) {}
	}

	/**
	 * A method to get a move another player has made. Returns a Map.Entry, as java does not have native support for pairs
	 * @param onlineId	The id of the local player
	 * @return			A pair of integers, the key being the ID of the player who made the move, and the value being the ID of the hexagon.
	 */
	@Override
	public Map.Entry<Integer,Integer> getMove(Integer onlineId) {
		try {
			Object[] move = space.getp(new ActualField(MOVE_IDENTIFIER), new ActualField(onlineId), new FormalField(Integer.class), new FormalField(Integer.class));
			return move != null ? new AbstractMap.SimpleEntry<Integer, Integer>((Integer)move[2],(Integer)move[3]) : null;
		} catch (InterruptedException e) {
			return null;
		}
	}

	/**
	 * A method to see if a player has signalled to reset the game
	 * @param onlineId	The ID of the local player.
	 * @return			The ID of the starting player.
	 */
	@Override
	public Integer getReset(Integer onlineId) {
		try {
			Object[] reset = space.getp(new ActualField(onlineId), new ActualField(RESET_GAME_IDENTIFIER), new FormalField(Integer.class));
			return reset != null ? (Integer)reset[2] : null;
		} catch (InterruptedException e) {
			return null;
		}
	}
}
