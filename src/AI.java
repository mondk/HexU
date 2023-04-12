import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class AI {
	

	
	public static int nextMove(GameState gs1) {
		double[] move = minimax(gs1,1, true);
		System.out.println("best move :"+move[1]);
		return (int) move[1];
	}

	public static double[] minimax(GameState gs,int depth, boolean maximizing_player) {
		System.out.println("id :"+gs.ids);
	    if (depth == 0) {
	    	System.out.println("final id "+gs.ids);
	    	for(Hexagon h: gs.grid) {
	    		if(h.clicked)
	    			System.out.print(h.id+" ");
	    	}
	    	gs.nextTurn();
	    	switch(gs.whosTurn) {
	    	case Player1:
	    	//	System.out.println(gs.winingState(gs.startP1, gs.colorP1, gs.winP1).toString());
				System.out.println("test winningState: " + gs.winingState(gs.startP1, gs.colorP1, gs.winP1).toString());
	    		return new double[] {  gs.evaluate(gs.winingState(gs.startP1, gs.colorP1, gs.winP1)), -1};
	    		
	    	case Player2:
				System.out.println("test winningState: " + gs.winingState(gs.startP2, gs.colorP2, gs.winP2).toString());
	    		return new double[] {  gs.evaluate(gs.winingState(gs.startP2, gs.colorP2, gs.winP2)), -1};
	    	}
	        
	    }

	    if (maximizing_player) {
	        double max_eval = Integer.MIN_VALUE;
	        double best_move = -1;
	        for (int move : gs.getValidMoves()) {
	        	double move1 = move;
	        	GameState new_state = null;
	        	new_state=makeMove(gs,move,gs.whosTurn);
	        	
	            double[] eval = minimax(new_state, depth - 1, false);
				System.out.println("score: "+eval[0]);
	            if (eval[0] > max_eval) {
	                max_eval = eval[0];
	                best_move = move1;
	            }
	        }
	        return new double[] {max_eval, best_move};
	    } else {
	        double min_eval = Integer.MAX_VALUE;
	        double best_move = -1;
	        for (int move : gs.getValidMoves()) {
	        	double move1 =move;
	        	GameState new_state = null;
	        	new_state=	makeMove(gs,move,gs.whosTurn);
	            double[] eval = minimax(new_state, depth - 1, true);
				
	            if (eval[0] < min_eval) {
	                min_eval = eval[0];
	                best_move = move1;
	            }
	        }
	        return new double[] {min_eval, best_move};
	    }
	}
	
	public static GameState makeMove(GameState gs1, int move, GameState.Turn currentPlayer) {
		
		GameState gs2 = null;
		gs2=(GameState) gs1.clone();
		for (Hexagon h : gs1.grid){
			if ( h.clicked)
				System.out.print(h.id + " ");
		}
		System.out.println("");
		// for (Hexagon h : gs2.grid){
		// 	if ( h.clicked = true)
		// 		System.out.print(h.id + " ");
		// }
		gs2.grid.get(move).clicked=true;

		System.out.println(gs2.grid.get(move).clicked+" "+gs1.grid.get(move).clicked);
		switch(gs1.whosTurn) {
			case Player1:
				gs2.grid.get(move).color=gs2.colorP1;
			case Player2:
				gs2.grid.get(move).color=gs2.colorP2;
		}
	   gs2.nextTurn();
	   gs2.ids+=1;
       return gs2;
		
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

	public static String getNullElements(String[][] matrix) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				if (matrix[i][j] == null) {
					sb.append("(" + i + "," + j + ")");
				}
			}
		}
		return sb.toString();
	}

	public static void stringifyMatrix (String[][] matrix){
		for (int i =0; i < 4; i++){
			for (int j = 0; j < 4;j++){
				System.out.printf("%5s ", matrix[i][j]);
			}
			System.out.println();
		}
	}
}