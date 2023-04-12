import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AI {
	
	private GameState gs;
	static String player1 = "pink";
	static String player2 = "green";

	public AI(GameState gs){
		this.gs = gs;
	}


	public  int[] nextMove(String[][] matrix, String player) {
		int[] move = minimax(matrix,player,1, true);
		System.out.println("best move :"+move[1]);
		
		return new int[] {move[1],move[2]};
	}

	public int[] minimax(String[][] matrix, String player,int depth, boolean maximizing_player) {
	
	    if (depth == 0) {
	    	return new int[] {(int) evalMatrix(matrix, player),-1,-1};
	        
	    }

	    if (maximizing_player) {
	        int max_eval = Integer.MIN_VALUE;
	        int[] best_move = new int[] {-1,-1};
	        String currentPlayer = player;
	        String nextPlayer = nextTurn(player);
	        for (int[] move : getNullElements(matrix)) {
	    
	        	String[][] matrix_new=makeMove(move,currentPlayer,matrix);
	        	
	            int[] eval = minimax(matrix_new,nextPlayer, depth - 1, false);
				
	            if (eval[0] > max_eval) {
	                max_eval = eval[0];
	                best_move = move;
	            }
	        }
	        return new int[] {max_eval, best_move[0],best_move[1]};
	        
	    } else {
	    	int min_eval = Integer.MAX_VALUE;
	        int[] best_move = new int[] {-1,-1};
	        String currentPlayer = player;
	        String nextPlayer = nextTurn(player);
	        for (int[] move : getNullElements(matrix)) {
	    
	        	String[][] matrix_new=makeMove(move,currentPlayer,matrix);
	        	
	            int[] eval = minimax(matrix_new,nextPlayer, depth - 1, false);
				
	            if ( min_eval > eval[0]) {
	                min_eval = eval[0];
	                best_move = move;
	            }
	        }
	        return new int[] {min_eval, best_move[0],best_move[1]};
	    }
	}
	
	public static String[][] makeMove(int[] move,String player, String[][] matrix){
		String[][] m2 = matrix.clone();
		for (int i = 0; i < matrix.length; i++) {
	        m2[i] = matrix[i].clone();
	    }
		m2[move[0]][move[1]]=player;
		return m2;
		
	}

	public static String nextTurn(String player) {
		
		if(player.equals(player1)) {
			return player2;
		}
		else
			return player1;
		
	}

	public static String[][] gridToMatrix(ArrayList<Hexagon> grid){
		String[][] matrix = new String[4][4];
		for (int i =0; i < 4; i++){
			for (int j = 0; j < 4;j++){
				int hex = i*4+j;
				String color = grid.get(hex).color.toString();
				if (color.equals( Color.pink.toString())){
					matrix[i][j] = "pink";
				}else if ( color.equals( Color.green.toString())){
					matrix[i][j] = "green";
				}
			}
		}
		return matrix;
	}

	public static ArrayList<int[]> getNullElements(String[][] matrix) {
		ArrayList<int[]> validMoves = new ArrayList<>();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					validMoves.add(new int[]{i,j});
				}
			}
		}
		return validMoves;
	}

	public static void stringifyMatrix (String[][] matrix){
		for (int i =0; i < 4; i++){
			for (int j = 0; j < 4;j++){
				System.out.printf("%5s ", matrix[i][j]);
			}
			System.out.println();
		}
	}

	public double evalMatrix(String[][] matrix, String Player){
		ArrayList<Integer> seen = new ArrayList<>();

		double finalScore = 0.0;	
		double sum = 0;
		
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == Player) {
					int v = i*4+j;
					if (seen.contains(v)){
						continue;
					}
					ArrayList<Integer> cluster = new ArrayList<>();
					boolean visited[] = new boolean[gs.numberOfHexagons*gs.numberOfHexagons];
					LinkedList<Integer> queue = new LinkedList<Integer>();
					visited[v] = true;
					queue.add(v);
					cluster.add(v);
					
					while (queue.size()!=0) {
						int inter = queue.poll();
						Iterator<Integer> k = gs.grid.get(inter).adj.listIterator();
						while(k.hasNext()) {
							int n = k.next();
							if (Player.equals(player1)){
								if(visited[n] == false && matrix[n/gs.numberOfHexagons][n%gs.numberOfHexagons] == "pink") {
									visited[n] = true;
									queue.add(n);
									seen.add(n);
									cluster.add(n);
									if (!Collections.disjoint(cluster, gs.startP1) && !Collections.disjoint(cluster,gs.winP1)){
										return Double.MAX_VALUE;
									}
								}
							}else if (Player.equals(player2)){
								if(visited[n] == false && matrix[n/gs.numberOfHexagons][n%gs.numberOfHexagons] == "green") {
									visited[n] = true;
									queue.add(n);
									seen.add(n);
									cluster.add(n);
									if (!Collections.disjoint(cluster, gs.startP2) && !Collections.disjoint(cluster,gs.winP2)){
										return Double.MAX_VALUE;
									}
								}
							}
						}
				}
				System.out.println("cluster " + cluster.toString());
				sum = 0;
				double axis_counter = 0;
				for (int h : cluster){
					sum += gs.grid.get(h).score;
					if (Player.equals(player1)){
						axis_counter+= gs.grid.get(h).center.y;
					} else if (Player.equals(player2)){
						axis_counter+= gs.grid.get(h).center.x;
					}
					axis_counter = axis_counter/gs.numberOfHexagons;
				}
				sum = sum*(1+((cluster.size()-1)*0.25));//*(1+(axis_counter*0.6));
			}
			finalScore += sum;
		}
	}
	System.out.println(finalScore);
	return finalScore;
}
}
