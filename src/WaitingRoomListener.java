import java.awt.*;

public interface WaitingRoomListener {
	void playerChanged(Integer id, Player player);
	void numberOfHexagonsChanged(Integer numberOfHexagons);
	void playerLeft(Integer id);
}
