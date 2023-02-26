import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

public class GameState {
	
	//Size of game screen
	Dimension SCREEN_SIZE = new Dimension(600,400);
	
	//game grid
	ArrayList<Hexagon> grid = new ArrayList<>();
	
	//Hexagon constants
	int numberOfHexagons =4;
	
	
	double raidus=(0.5773502717*(600-150))/(numberOfHexagons+1);
	double shift = 2*raidus*0.8660254;
	
	//Start point for grid
	Point startPoint = new Point(50,50);
	
	
	//Playerstate
	boolean singlePlayer = false;
	
	//Show which player turn it is
	Turn whosTurn = Turn.Player1;
	
	
	public enum Turn{
		Player1,
		Player2,
		AI
	}
	
	//change player turn
	public void nextTurn() {
		
		//if true it either player 1 or the ai which plays
		if(singlePlayer) {
			if(whosTurn.equals(Turn.Player1)) {
				whosTurn = Turn.AI;
			}
			else {
				whosTurn = Turn.Player1;
			}
		}
		//Multiplayer
		else {
			if(whosTurn.equals(Turn.Player1)) {
				whosTurn = Turn.Player2;
			}
			else {
				whosTurn = Turn.Player1;
			}
		}
	}
}
