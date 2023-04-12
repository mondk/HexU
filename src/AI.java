import java.awt.Color;

public class AI {
	

	
	public static int nextMove(GameState gs1) {
		double[] move = minimax(gs1,2, true);
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
	    		return new double[] {  gs.evaluate(gs.winingState(gs.startP1, gs.colorP1, gs.winP1)), -1};
	    		
	    	case Player2:
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
	        	System.out.println("move :"+move);
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
		gs2.grid.get(move).clicked=true;
System.out.println(gs2.grid.get(move).clicked+" "+gs1.grid.get(move).clicked);
		switch(gs1.whosTurn) {
			case Player1:
				gs2.grid.get(move).color=gs2.colorP1;
				break;
			case Player2:
				gs2.grid.get(move).color=gs2.colorP2;
				break;
		}
	   gs2.nextTurn();
	   gs2.ids+=1;
       return gs2;
		
	}


}