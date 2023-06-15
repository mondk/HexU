/**
 * The interface for a class, which should listen to a WaitingRoom
 */
public interface WaitingRoomListener {
	void playerChanged(Integer id, Player player);
	void numberOfHexagonsChanged(Integer numberOfHexagons);
	void playerLeft(Integer id);
}
