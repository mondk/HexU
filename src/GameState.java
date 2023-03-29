import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Iterator;

public class GameState {
	
	//Size of game screen
	Dimension SCREEN_SIZE = new Dimension(600,400);
	
	//game grid
	ArrayList<Hexagon> grid = new ArrayList<>();
	ArrayList<Triangle> border = new ArrayList<>();
	
	//Hexagon constants
	int numberOfHexagons =4;


	double radius=(0.5773502717*(600-150))/(numberOfHexagons+1);
	double shift = 2*radius*0.8660254;
	int xOffSet= 100- (int) (radius*2);

	// Player names
	String player1Name = "Player 1";
	String player2Name = "Player 2";

	//Start point for grid
	Point startPoint = new Point((int) radius+50,(int) radius+50);
	

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
	List<Integer> startP1 = new ArrayList<>();
	List<Integer> startP2 = new ArrayList<>();
	List<Integer> startAI = new ArrayList<>();
	List<Integer> winP1 = new ArrayList<>();
	List<Integer> winP2 = new ArrayList<>();
	List<Integer> winAI = new ArrayList<>();

	// // Adjaceny matrix for the BFS
	// ArrayList<ArrayList<Integer>> adj = new ArrayList<>();

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
		for (Hexagon hexes: grid.subList(0, numberOfHexagons)){
			startP1.add(hexes.id);
		}
		for (Hexagon hexes: grid.subList(numberOfHexagons*(numberOfHexagons-1), numberOfHexagons*numberOfHexagons)){
			winP1.add(hexes.id);
		}
		for(int i=0;i<numberOfHexagons;i++) {
			int s = i*numberOfHexagons;
			int a = i*numberOfHexagons+numberOfHexagons-1;
			startP2.add(grid.get(s).id);
			winP2.add(grid.get(a).id);
		}
	}

	// public void createAdjacenyMatrix() {
	// 	for (int i = 0; i<numberOfHexagons; i++) {
	// 		for (int j = 0; j<numberOfHexagons;j++) {
	// 			adj.add(new ArrayList<Integer>());
	// 			int hex = i*numberOfHexagons+j;

	// 			if (i==0 && j==0) {												//First hexagon
	// 				adj.get(hex).add(1);
	// 				adj.get(hex).add(numberOfHexagons);
	// 			}
	// 			else if (i==numberOfHexagons-1 && j ==numberOfHexagons-1) {		//Last Hexagon
	// 				adj.get(hex).add(hex-1);
	// 				adj.get(hex).add(hex-numberOfHexagons);
	// 			}
	// 			else if(i==0 & j==numberOfHexagons-1) {							//Last hexagon first row
	// 				adj.get(hex).add(hex-1);
	// 				adj.get(hex).add(hex+numberOfHexagons);
	// 				adj.get(hex).add(hex+numberOfHexagons-1);
	// 			}
	// 			else if (i == numberOfHexagons-1 && j ==0) {					//First hexagon last row
	// 				adj.get(hex).add(hex-numberOfHexagons);
	// 				adj.get(hex).add(hex-numberOfHexagons+1);
	// 				adj.get(hex).add(hex+1);
	// 			}
	// 			else if (i==0) {												//Rest of first row
	// 				adj.get(hex).add(hex+numberOfHexagons);
	// 				adj.get(hex).add(hex+numberOfHexagons-1);
	// 				adj.get(hex).add(hex+1);
	// 				adj.get(hex).add(hex-1);
	// 			}
	// 			else if (i==numberOfHexagons-1) {								//Rest of last row
	// 				adj.get(hex).add(hex-1);
	// 				adj.get(hex).add(hex-numberOfHexagons);
	// 				adj.get(hex).add(hex-numberOfHexagons+1);
	// 				adj.get(hex).add(hex+1);
	// 			}
	// 			else if (j==0) {												//Rest of first column
	// 				adj.get(hex).add(hex-numberOfHexagons);
	// 				adj.get(hex).add(hex-numberOfHexagons+1);
	// 				adj.get(hex).add(hex+1);
	// 				adj.get(hex).add(hex+numberOfHexagons);
	// 			}
	// 			else if(j==numberOfHexagons-1) {								//Rest of last column
	// 				adj.get(hex).add(hex-numberOfHexagons);
	// 				adj.get(hex).add(hex-1);
	// 				adj.get(hex).add(hex+numberOfHexagons-1);
	// 				adj.get(hex).add(hex+numberOfHexagons);
	// 			}
	// 			else {															//Everything in between
	// 				adj.get(hex).add(hex-1);
	// 				adj.get(hex).add(hex+1);
	// 				adj.get(hex).add(hex-numberOfHexagons);
	// 				adj.get(hex).add(hex-numberOfHexagons+1);
	// 				adj.get(hex).add(hex+numberOfHexagons);
	// 				adj.get(hex).add(hex+numberOfHexagons-1);
	// 			}
	// 		}
	// 	}
	// }

	public ArrayList<ArrayList<Integer>> winingState(List<Integer> s, Color p, List<Integer> win) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<>(2);
		result.add(new ArrayList<>());
		ArrayList<ArrayList<Integer>> pCluster = new ArrayList<>();


		// Array der holder alle hexagon der er en del af en cluster
		ArrayList<Integer> seen = new ArrayList<>();
		//Loop over hele griddet, for at finde alle clusters
		for(Hexagon v : grid) {
			ArrayList<Integer> cluster = new ArrayList<>();

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
			cluster.add(v.id);

			while (queue.size()!=0) {
				int inter = queue.poll();
				Iterator<Integer> i = grid.get(inter).adj.listIterator();
				while(i.hasNext()) {
					int n = i.next();
					if(visited[n] == false && grid.get(n).color == p) {
						visited[n] = true;
						queue.add(n);
						seen.add(n);
						cluster.add(n);
						// Ser om den funde cluster ud fra v, indeholde en hexagon fra start og slut enden
						// af den givne spillers plade
						if (!Collections.disjoint(cluster, s) && !Collections.disjoint(cluster,win)){
							result.get(0).add(1);
							return result;
						}
					}
				}
			}
			pCluster.add(cluster);
		}	
		result.get(0).add(0);
		result.addAll(pCluster);
		return result;
	}

	public double evaluate(ArrayList<ArrayList<Integer>> clusters){
		double finalSum = 0;
				for (ArrayList<Integer> list : clusters){
					double sum =0;
					for (Integer hex : list){
						sum += grid.get(hex).score;
					}
					if (sum>finalSum)
						finalSum = sum;
				}
		return finalSum;
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

	public void updateNumberOfHexagons(int numberOfHexagons){
		this.numberOfHexagons = numberOfHexagons;
		this.radius=(0.5773502717*(600-150))/(numberOfHexagons+1);
		this.shift = 2*radius*0.8660254;
	}
}

