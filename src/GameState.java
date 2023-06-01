import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

public class GameState {
	
	//Size of game screen
	//Dimension SCREEN_SIZE = new Dimension(600,400);
	
	
	//game grid
	ArrayList<Hexagon> grid = new ArrayList<>();
	//ArrayList<Triangle> border = new ArrayList<>();
	ArrayList<Rectangle> border = new ArrayList<>();
	
	//Hexagon constants
	int numberOfHexagons =10;
	
	//Hexagon constants continued 

	int ids =0;

	double radius=(0.5773502717*(600-150))/(numberOfHexagons+1);
	double shift = 2*radius*0.8660254;
	int xOffSet= 100- (int) (radius*2);

	//Size of game screen depending on number of hexagones and radius
	//int widthScreen = (numberOfHexagons*(int)radius)+(int)shift+400;
	//int heightScreen = (numberOfHexagons*(int)radius)+(int)shift+200;

	int widthScreen = (numberOfHexagons*(int)Math.round(radius))+(int)Math.round(shift)+400;
	int heightScreen = (numberOfHexagons*(int)Math.round(radius))+(int)Math.round(shift)+200;

	Dimension SCREEN_SIZE = new Dimension(widthScreen,heightScreen);

	// Player names
	String player1Name = "Player 1";
	String player2Name = "Player 2";

	//Start point for grid
	Point startPoint = new Point(100,100);
	
	
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
