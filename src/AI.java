import java.util.ArrayList;


public class AI {
	
	static GameState gs;
	
	AI(GameState gs){
		this.gs=gs;
	}
	
	
	public int nextMove() {
		GameState gs4 = new GameState();
		gs4.p1Cluster=gs.p1Cluster;
		gs4.p2Cluster=gs.p2Cluster;
		gs4.grid=gs.grid;
		
		int[] move = minimax(gs4,1, true);
		System.out.println("best move :"+move[1]);
		return move[1];
	}

	public static int[] minimax(GameState gs2,int depth, boolean maximizing_player) {
	    if (depth == 0) {
	    	System.out.println("suck my balls");
	        return new int[] { (int) gs2.evaluate(), -1};
	    }

	    if (maximizing_player) {
	        int max_eval = Integer.MIN_VALUE;
	        int best_move = -1;
	        for (int move : gs2.getValidMoves()) {
	        	System.out.println(move);
	        	GameState new_state = null;
	        	new_state=makeMove(gs2,move,gs2.whosTurn);
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
	        for (int move : gs2.getValidMoves()) {
	        	GameState new_state = null;
	        	new_state=	makeMove(gs2,move,gs2.whosTurn);
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
		GameState gs5 = new GameState();
		gs5.p1Cluster=gs1.p1Cluster;
		gs5.p2Cluster=gs1.p2Cluster;
		gs5.grid=gs1.grid;
		gs5.grid.get(move).clicked=true;
		
       return gs5;
		
	}

}
