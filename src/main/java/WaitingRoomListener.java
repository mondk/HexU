import java.awt.*;

public interface WaitingRoomListener {
	void nameChanged(Integer id, String name, Color color);
	void numberOfHexagonsChanged(String numberOfHexagons);
}
