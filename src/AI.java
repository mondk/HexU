import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class AI {
	
	private GameState gs;
	static String player1;
	static String player2;

	public AI(GameState gs){
		this.gs = gs;
		this.player1=gs.players.get(0).color.toString();
		this.player2=gs.players.get(1).color.toString();
	}


	public  int[] nextMove(String[][] matrix, String player) {
		//System.out.println("\nSTART LOL"+player+"\n");
		int[] move = minimax(matrix,player,4, true);
		//System.out.println("best move :"+move[1]+" ; "+move[2]);

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
	        	

	        	//System.out.println("\ncurrent move :"+move[0]+" ; "+move[1]+"\n");

	        	String[][] matrix_new=makeMove(move,currentPlayer,matrix);

	        	if(depth==4) {
	        		if(evalMatrix(matrix_new, player)==Double.MAX_VALUE) {
		        		return new int[] {-1, move[0],move[1]};
		        	}
	        	}
	            int[] eval = minimax(matrix_new,nextPlayer, depth - 1, false);
				//System.out.println("eval score : "+eval[0]);
	            if (eval[0] > max_eval) {
	            	//System.out.println("inner score : "+eval[0]);
	                max_eval = eval[0];
	                best_move = move;

	            }
	            //System.out.println("best move inner :"+best_move[0]+" ; "+best_move[1]);
	        }
	        return new int[] {max_eval, best_move[0],best_move[1]};

	    } else {
	    	int min_eval = Integer.MAX_VALUE;
	        int[] best_move = new int[] {-1,-1};
	        String currentPlayer = player;
	        String nextPlayer = nextTurn(player);

	        for (int[] move : getNullElements(matrix)) {

	        	String[][] matrix_new=makeMove(move,currentPlayer,matrix);

	            int[] eval = minimax(matrix_new,nextPlayer, depth - 1, true);
				
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
		else if(player.equals(player2))
			return player1;
		else
			return null;

	}

	public static String[][] gridToMatrix(ArrayList<Hexagon> grid, int numberofHex){
		String[][] matrix = new String[numberofHex][numberofHex];
		for (int i =0; i < numberofHex; i++){
			for (int j = 0; j < numberofHex;j++){
				int hex = i*numberofHex+j;
				String color = grid.get(hex).color.toString();
				if (color.equals( player1)){
					matrix[i][j] = player1;
				}else if ( color.equals(player2)){
					matrix[i][j] = player2;
				}else{
					matrix[i][j] = "null";
				}
			}
		}
		return matrix;
	}

	public static ArrayList<int[]> getNullElements(String[][] matrix) {
		//stringifyMatrix(matrix,4);
		ArrayList<int[]> validMoves = new ArrayList<>();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j].equals("null")) {
					validMoves.add(new int[]{i,j});
				}
			}
		}
		return validMoves;
	}

	public static void stringifyMatrix (String[][] matrix,int numberofHex){
		for (int i =0; i < numberofHex; i++){
			for (int j = 0; j < numberofHex;j++){
				System.out.printf("%5s ", matrix[i][j]);
			}
			System.out.println();
		}
	}

	public double evalMatrix(String[][] matrix, String Player){
		ArrayList<Integer> seen = new ArrayList<>();

		double finalScore = 0.0;
		double sum = 0;
		for (int i = 0; i < gs.numberOfHexagons; i++) {
			for (int j = 0; j < gs.numberOfHexagons; j++) {
				if (matrix[i][j].equals(Player)) {
					int v = i*gs.numberOfHexagons+j;
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
								if(visited[n] == false && matrix[n/gs.numberOfHexagons][n%gs.numberOfHexagons].equals(player1)) {
									visited[n] = true;
									queue.add(n);
									seen.add(n);
									cluster.add(n);
									if (!Collections.disjoint(cluster, gs.startP1) && !Collections.disjoint(cluster,gs.winP1)){


										return Double.MAX_VALUE;
									}
								}
							}else if (Player.equals(player2)){
								if(visited[n] == false && matrix[n/gs.numberOfHexagons][n%gs.numberOfHexagons].equals(player2)) {
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
				sum = 0;
				double axis_counter = 0;
				if (cluster.size() > 0)
					sum +=gs.grid.get(cluster.get(0)).score;
				for (int h = 1; h<cluster.size();h ++){
					sum += gs.grid.get(cluster.get(h)).score;
					if (Player.equals(player1)){
						if (gs.grid.get(cluster.get(h)).center.y != gs.grid.get(cluster.get(h-1)).center.y)
							axis_counter += 1;
					} else if (Player.equals(player2)){
						if (gs.grid.get(cluster.get(h)).center.y == gs.grid.get(cluster.get(h-1)).center.y)
							axis_counter += 1;
					}
				}
				//System.out.println("sum chehck");
				//System.out.println(sum);
				sum = sum*(1+((cluster.size()-1)*0.25))*(1+(axis_counter*0.55));
				//System.out.println(sum);
			}
			finalScore += sum;
			sum=0;
		}
	}
	//System.out.println("final score : "+finalScore);
	return finalScore;
}
}
