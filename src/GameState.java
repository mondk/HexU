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

public class GameState implements Cloneable{
	
	//Size of game screen
	Dimension SCREEN_SIZE = new Dimension(600,400);
	
	//game grid
	ArrayList<Hexagon> grid = new ArrayList<>();
	ArrayList<Triangle> border = new ArrayList<>();
	
	//Hexagon constants
	int numberOfHexagons =4;
	
	int ids =0;

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
	Color colorP1 = Color.pink;
	Color colorP2 = Color.green;
	// Color colorP1 = Color.decode("#d032f0");
	// Color colorP2 = Color.decode("#247324");


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

	public ArrayList<ArrayList<Integer>> winingState(List<Integer> s, Color p, List<Integer> win) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<>(2);
		result.add(new ArrayList<>());
		ArrayList<ArrayList<Integer>> pCluster = new ArrayList<>();


		// Array der holder alle hexagon der er en del af en cluster
		ArrayList<Integer> seen = new ArrayList<>();
		//Loop over hele griddet, for at finde alle clusters
		for(Hexagon v : this.grid) {
			// Tjekker om den givne hexagon er den givne spillers farve 
			// eller om den allerede er en del af en cluster, i så fald skip!
			if (v.color != p || seen.contains(v.id)) {
				continue;
			}

			ArrayList<Integer> cluster = new ArrayList<>();
			
			// Standard BFS ting...
			boolean visited[] = new boolean[numberOfHexagons*numberOfHexagons];
			LinkedList<Integer> queue = new LinkedList<Integer>();
			visited[v.id] = true;
			queue.add(v.id);
			// Tilføjer hexagon til cluster
			cluster.add(v.id);

			while (queue.size()!=0) {
				int inter = queue.poll();
				Iterator<Integer> i = this.grid.get(inter).adj.listIterator();
				while(i.hasNext()) {
					int n = i.next();
					if(visited[n] == false && this.grid.get(n).color == p) {
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
		if (clusters.get(0).get(0) == 1){
			return Double.MAX_VALUE;
		}
		List<ArrayList<Integer>> only_clusters = clusters.subList(1, clusters.size());
		System.out.println("only cluster "+only_clusters.toString());
		double finalSum = 0;
		for (ArrayList<Integer> list : only_clusters){
			double sum =0;
			double axis_counter = 0;
			for (int i = 0; i <= list.size()-1; i++){
				if ( i > 0) {
					switch(whosTurn){
						case Player1:
							if (grid.get(list.get(i)).center.y != grid.get(list.get(i-1)).center.y){
								axis_counter += 1;
							}
						case Player2:
							if (grid.get(list.get(i)).center.x != grid.get(list.get(i-1)).center.x){
								axis_counter+=1;
							}
						case AI:
						}
				}
				sum += grid.get(list.get(i)).score;
			}
			sum = sum*(1+((list.size()-1)*0.25))*(1+(axis_counter*0.6));
			System.out.println(sum);
			finalSum += sum;
		}
		return finalSum;
	}

//	public double evaluate(ArrayList<ArrayList<Integer>> clusters) {
//		
//		//clears win indicator if not a winning state
//		System.out.println("clusters:= "+ clusters.toString());
//		if(clusters.get(0).get(0).equals(1)) {
//			return Double.MAX_VALUE;
//		}
//		clusters.remove(0);
//		double final_score =0;
//		double temp_score =0;
//		System.out.println("clusters:= "+ clusters.toString());
//		for(ArrayList<Integer> list: clusters) {
//			temp_score=0;
//			if(list.size()==1) {
//				final_score+=grid.get(list.get(0)).score;
//			}
//			else {
//				for(int i : list) {
//					temp_score+=grid.get(list.get(i)).score;
//				}
//				final_score+=temp_score*1.5;
//			}
//		}
//		
//		return final_score;
//		
//	}
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

	public ArrayList<Integer> getValidMoves() {
		ArrayList<Integer> validmoves = new ArrayList<>();
		
		for(Hexagon h: grid) {
			if(!h.clicked)
				validmoves.add(h.id);
		}
		return validmoves;
	}
	
	
	
	@Override
	public GameState clone() {
		GameState gs = new GameState();
		gs.whosTurn=this.whosTurn;
		
		gs.ids=this.ids;
		for(Hexagon h: this.grid )
			gs.grid.add(h.clone());
		gs.fillWinStateArrays();
		
		return gs;
		
	}
}

