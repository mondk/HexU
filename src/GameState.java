import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

public class GameState {
	
	//Size of game screen
	Dimension SCREEN_SIZE = new Dimension(600,600);
	
	//game grid
	ArrayList<Hexagon> grid = new ArrayList<>();
	int numberOfHexagons =9;
	double raidus=20;
	double shift = 2*raidus*0.8660254;
	
	//Start point for grid
	Point startPoint = new Point(200,200);
}
