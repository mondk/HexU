import java.awt.Color;

public class AI {
	

	
	public int nextMove(GameState gs1) {
		int[] move = minimax(gs1,1, true);
		System.out.println("best move :"+move[1]);
		return move[1];
	}

	public static int[] minimax(GameState gs,int depth, boolean maximizing_player) {
	    if (depth == 0) {
	    	gs.nextTurn();
	    	switch(gs.whosTurn) {
	    	case Player1:
	    	//	System.out.println(gs.winingState(gs.startP1, gs.colorP1, gs.winP1).toString());
	    		return new int[] { (int) gs.evaluate(gs.winingState(gs.startP1, gs.colorP1, gs.winP1)), -1};
	    		
	    	case Player2:
	    		return new int[] { (int) gs.evaluate(gs.winingState(gs.startP2, gs.colorP2, gs.winP2)), -1};
	    	}
	        
	    }

	    if (maximizing_player) {
	        int max_eval = Integer.MIN_VALUE;
	        int best_move = -1;
	        for (int move : gs.getValidMoves()) {
	        	GameState new_state = null;
	        	new_state=makeMove(gs,move,gs.whosTurn);
	        	
	            int[] eval = minimax(new_state, depth - 1, false);
	            if (eval[0] > max_eval) {
	                max_eval = eval[0];
	                best_move = move;
	            }
	        }
	        return new int[] {max_eval, best_move};
	    } else {
	        int min_eval = Integer.MAX_VALUE;
	        int best_move = -1;
	        for (int move : gs.getValidMoves()) {
	        	
	        	GameState new_state = null;
	        	new_state=	makeMove(gs,move,gs.whosTurn);
	            int[] eval = minimax(new_state, depth - 1, true);
	            if (eval[0] < min_eval) {
	                min_eval = eval[0];
	                best_move = move;
	            }
	        }
	        return new int[] {min_eval, best_move};
	    }
	}
	
	public static GameState makeMove(GameState gs1, int move, GameState.Turn currentPlayer) {
		
		GameState gs2 = null;
		gs2=(GameState) gs1.clone();
		gs2.grid.get(move).clicked=true;
				switch(currentPlayer) {
				case Player1:
					gs2.grid.get(move).color=gs2.colorP1;
					break;
				case Player2:
					gs2.grid.get(move).color=gs2.colorP2;
					break;
				}
	   gs2.nextTurn();
       return gs2;
		
	}


}