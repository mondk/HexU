import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AI {
	

	static String player1 = "pink";
	static String player2 = "green";
	
	
	
	public static int[] nextMove(String[][] matrix, String player) {
		int[] move = minimax(matrix,player,2, true);
		System.out.println("best move :"+move[1]);
		
		return new int[] {move[1],move[2]};
	}

	public static int[] minimax(String[][] matrix, String player,int depth, boolean maximizing_player) {
	
	    if (depth == 0) {
	    	return new int[] {0,-1,-1};
	        
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
	
//	public static GameState makeMove(GameState gs1, int move, GameState.Turn currentPlayer) {
//		
//		GameState gs2 = null;
//		gs2=(GameState) gs1.clone();
//		for (Hexagon h : gs1.grid){
//			if ( h.clicked)
//				System.out.print(h.id + " ");
//		}
//		System.out.println("");
//		// for (Hexagon h : gs2.grid){
//		// 	if ( h.clicked = true)
//		// 		System.out.print(h.id + " ");
//		// }
//		gs2.grid.get(move).clicked=true;
//
//		System.out.println(gs2.grid.get(move).clicked+" "+gs1.grid.get(move).clicked);
//		switch(gs1.whosTurn) {
//			case Player1:
//				gs2.grid.get(move).color=gs2.colorP1;
//			case Player2:
//				gs2.grid.get(move).color=gs2.colorP2;
//		}
//	   gs2.nextTurn();
//	   gs2.ids+=1;
//       return gs2;
//		
//	}

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

	public static String nextTurn(String player) {
		
		if(player.equals(player1)) {
			return player2;
		}
		else
			return player1;
		
	}
	public static void stringifyMatrix (String[][] matrix){
		for (int i =0; i < 4; i++){
			for (int j = 0; j < 4;j++){
				System.out.printf("%5s ", matrix[i][j]);
			}
			System.out.println();
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
	
	
}