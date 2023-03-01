import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GameState {
	
	//Size of game screen
	Dimension SCREEN_SIZE = new Dimension(600,600);
	
	//game grid
	ArrayList<Hexagon> grid = new ArrayList<>();
	
	//Hexagon constants
	int numberOfHexagons =3;
	double raidus=70;
	double shift = 2*raidus*0.8660254;

	// Player names
	String player1Name = "Player 1";
	String player2Name = "Player 2";

	//Start point for grid
	Point startPoint = new Point(100,100);

	// JPanel, which includes the different screens
	JPanel cards = new JPanel(new CardLayout());
}
