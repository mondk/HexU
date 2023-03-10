import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class GameState {
	
	//Size of game screen
	Dimension SCREEN_SIZE = new Dimension(600,400);
	
	//game grid
	ArrayList<Hexagon> grid = new ArrayList<>();
	
	//Hexagon constants
	int numberOfHexagons =3;


	double raidus=(0.5773502717*(600-150))/(numberOfHexagons+1);
	double shift = 2*raidus*0.8660254;

	// Player names
	String player1Name = "Player 1";
	String player2Name = "Player 2";

	//Start point for grid
	Point startPoint = new Point(50,75);
	

	// JPanel, which includes the different screens
	JPanel cards = new JPanel(new CardLayout());


	//Playerstate
	boolean singlePlayer = false;
	Color colorP1 = Color.decode("#d032f0");
	Color colorP2 = Color.decode("#247324");


	//Show which player turn it is
	Turn whosTurn = Turn.Player1;
	String paneTurnString = player1Name;
	Color paneTColor = colorP1;
	
	public enum Turn{
		Player1,
		Player2,
		AI
	}

	//Lists containing start arrays for players
	List<Hexagon> startP1 = new ArrayList<>();
	List<Hexagon> startP2 = new ArrayList<>();
	List<Hexagon> startAI = new ArrayList<>();
	List<Hexagon> winP1 = new ArrayList<>();
	List<Hexagon> winP2 = new ArrayList<>();
	List<Hexagon> winAI = new ArrayList<>();

	// Adjaceny matrix for the BFS
	ArrayList<ArrayList<Integer>> adj = new ArrayList<>();

	// Linked list containing moves made
	LinkedList<Integer> q = new LinkedList<>();

	//change player turn
	public void nextTurn() {
		if(singlePlayer) {
			if(whosTurn.equals(Turn.Player1)) {
				whosTurn = Turn.AI;
				paneTurnString = "AI";
				paneTColor = colorP2;
			}
			else {
				whosTurn = Turn.Player1;
				paneTurnString = player1Name;
				paneTColor = colorP1;
			}
		}
		//Multiplayer
		else {
			if(whosTurn.equals(Turn.Player1)) {
				whosTurn = Turn.Player2;
				paneTurnString = player2Name;
				paneTColor = colorP2;
			}
			else {
				whosTurn = Turn.Player1;
				paneTurnString = player1Name;
				paneTColor = colorP1;
			}
		}
	}

	public void fillWinStateArrays() {
		startP1.addAll(grid.subList(0, numberOfHexagons));
		winP1.addAll(grid.subList(numberOfHexagons*(numberOfHexagons-1), numberOfHexagons*numberOfHexagons));
		for(int i=0;i<numberOfHexagons;i++) {
			int s = i*numberOfHexagons;
			int a = i*numberOfHexagons+numberOfHexagons-1;
			startP2.add(grid.get(s));
			winP2.add(grid.get(a));
		}
	}

	public void createAdjacenyMatrix() {
		for (int i = 0; i<numberOfHexagons; i++) {
			for (int j = 0; j<numberOfHexagons;j++) {
				adj.add(new ArrayList<Integer>());
				int hex = i*numberOfHexagons+j;

				if (i==0 && j==0) {												//First hexagon
					adj.get(hex).add(1);
					adj.get(hex).add(numberOfHexagons);
				}
				else if (i==numberOfHexagons-1 && j ==numberOfHexagons-1) {		//Last Hexagon
					adj.get(hex).add(hex-1);
					adj.get(hex).add(hex-numberOfHexagons);
				}
				else if(i==0 & j==numberOfHexagons-1) {							//Last hexagon first row
					adj.get(hex).add(hex-1);
					adj.get(hex).add(hex+numberOfHexagons);
					adj.get(hex).add(hex+numberOfHexagons-1);
				}
				else if (i == numberOfHexagons-1 && j ==0) {					//First hexagon last row
					adj.get(hex).add(hex-numberOfHexagons);
					adj.get(hex).add(hex-numberOfHexagons+1);
					adj.get(hex).add(hex+1);
				}
				else if (i==0) {												//Rest of first row
					adj.get(hex).add(hex+numberOfHexagons);
					adj.get(hex).add(hex+numberOfHexagons-1);
					adj.get(hex).add(hex+1);
					adj.get(hex).add(hex-1);
				}
				else if (i==numberOfHexagons-1) {								//Rest of last row
					adj.get(hex).add(hex-1);
					adj.get(hex).add(hex-numberOfHexagons);
					adj.get(hex).add(hex-numberOfHexagons+1);
					adj.get(hex).add(hex+1);
				}
				else if (j==0) {												//Rest of first column
					adj.get(hex).add(hex-numberOfHexagons);
					adj.get(hex).add(hex-numberOfHexagons+1);
					adj.get(hex).add(hex+1);
					adj.get(hex).add(hex+numberOfHexagons);
				}
				else if(j==numberOfHexagons-1) {								//Rest of last column
					adj.get(hex).add(hex-numberOfHexagons);
					adj.get(hex).add(hex-1);
					adj.get(hex).add(hex+numberOfHexagons-1);
					adj.get(hex).add(hex+numberOfHexagons);
				}
				else {															//Everything in between
					adj.get(hex).add(hex-1);
					adj.get(hex).add(hex+1);
					adj.get(hex).add(hex-numberOfHexagons);
					adj.get(hex).add(hex-numberOfHexagons+1);
					adj.get(hex).add(hex+numberOfHexagons);
					adj.get(hex).add(hex+numberOfHexagons-1);
				}
			}
		}
	}

	public void resetGame() {
		whosTurn = Turn.Player1;
		paneTColor = colorP1;
		paneTurnString = player1Name;
		q.clear();
		for (Hexagon h : grid) {
			h.color = Color.gray;
			h.clicked=false;
		}
	}
}
