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
	int numberOfHexagons =6;


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
	String paneTurnString = "Player 1";
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
				paneTurnString = "Player 1";
				paneTColor = colorP1;
			}
		}
		//Multiplayer
		else {
			if(whosTurn.equals(Turn.Player1)) {
				whosTurn = Turn.Player2;
				paneTurnString = "Player 2";
				paneTColor = colorP2;
			}
			else {
				whosTurn = Turn.Player1;
				paneTurnString = "Player 1";
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

<<<<<<< Updated upstream
=======
	public boolean winingState(List<Hexagon> s, Color p, List<Hexagon> win, ArrayList<ArrayList<Hexagon>> pCluster) {
		// Array der holder alle hexagon der er en del af en cluster
		ArrayList<Integer> seen = new ArrayList<>();
		// Rydder listen med alle cluser for dne givne spiller
		pCluster.clear();
		//Loop over hele griddet, for at finde alle clusters
		for(Hexagon v : grid) {
			ArrayList<Hexagon> cluster = new ArrayList<>();

			// Tjekker om den givne hexagon er den givne spillers farve 
			// eller om den allerede er en del af en cluster, i så fald skip!
			if (v.color != p || seen.contains(v.id)) {
				continue;
			}
			
			// Standard BFS ting...
			boolean visited[] = new boolean[numberOfHexagons*numberOfHexagons];
			LinkedList<Integer> queue = new LinkedList<Integer>();
			visited[v.id] = true;
			queue.add(v.id);
			// Tilføjer hexagon til cluster
			cluster.add(v);

			while (queue.size()!=0) {
				int inter = queue.poll();
				Iterator<Integer> i = adj.get(inter).listIterator();
				while(i.hasNext()) {
					int n = i.next();
					if(visited[n] == false && grid.get(n).color == p) {
						visited[n] = true;
						queue.add(n);
						seen.add(n);
						cluster.add(grid.get(n));
						// Ser om den funde cluster ud fra v, indeholde en hexagon fra start og slut enden
						// af den givne spillers plade
						if (!Collections.disjoint(cluster, s) && !Collections.disjoint(cluster,win)){
							return true;
						}
					}
				}
			}
			pCluster.add(cluster);
		}
		System.out.println(pCluster.toString());
		return false;
	}

	public double evaluate(ArrayList<Hexagon> board){
		double finalSum = 0;
		
		switch(whosTurn){
			case Player1:
				for (ArrayList<Hexagon> list : p1Cluster){
					double sum =0;
					for (Hexagon hex : list){
						sum += hex.score;
					}
					if (sum>finalSum)
						finalSum = sum;
				}
			case Player2:
				for (ArrayList<Hexagon> list : p2Cluster){
					double sum =0;
					for (Hexagon hex : list){
						sum += hex.score;
					}
					if (sum>finalSum)
						finalSum = sum;
				}
			case AI:
				for (ArrayList<Hexagon> list : p2Cluster){
					double sum =0;
					for (Hexagon hex : list){
						sum += hex.score;
					}
					if (sum>finalSum)
						finalSum = sum;
				}	
		}
		return finalSum;
	}
	public ArrayList<Integer> getValidMoves(ArrayList<Hexagon> board) {
		// TODO Auto-generated method stub
		ArrayList<Integer> validMoves = new ArrayList<>();
		for(Hexagon h: board) {
			if(!h.clicked)
				validMoves.add(h.id);
		}
		return validMoves;
		
	}

	public ArrayList<Hexagon> makeMove(ArrayList<Hexagon> board, int id, GameState.Turn currentPlayer) {
		// TODO Auto-generated method stub
		ArrayList<Hexagon> grid2 = board;
		grid2.get(id).clicked=true;
		switch(currentPlayer) {
		case Player1:
			grid2.get(id).color=colorP1;
			break;
		case Player2:
				grid2.get(id).color=colorP2;
				break;
		}
	return grid2;
		
	}


>>>>>>> Stashed changes
	public void resetGame() {
		whosTurn = Turn.Player1;
		paneTColor = colorP1;
		paneTurnString = "Player 1";
		q.clear();
		for (Hexagon h : grid) {
			h.color = Color.gray;
			h.clicked=false;
		}
	}
}
