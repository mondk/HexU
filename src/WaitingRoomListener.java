import java.awt.*;

public interface WaitingRoomListener {
	void nameChanged(Integer id, String name, Color color);
	void numberOfHexagonsChanged(Integer numberOfHexagons);
	void playerLeft(Integer id);
}
