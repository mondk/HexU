import java.util.ArrayList;

public class AI {

	static GameState gs;
	
	AI(GameState gs){
		this.gs=gs;
	}
	
	
	public int nextMove() {
		
		int[] move = minimax(gs.grid,1, true);
		return move[1];
	}

	public static int[] minimax(ArrayList<Hexagon> board,int depth, boolean maximizing_player) {
	    if (depth == 0) {
	        return new int[] {(int) gs.evaluate(board), -1};
	    }

	    if (maximizing_player) {
	        int max_eval = Integer.MIN_VALUE;
	        int best_move = -1;
	        for (int move : gs.getValidMoves(board)) {
	        	ArrayList<Hexagon> new_board = gs.makeMove(board,move, gs.whosTurn);
	            int[] eval = minimax(new_board, depth - 1, false);
	            if (eval[0] > max_eval) {
	                max_eval = eval[0];
	                best_move = move;
	            }
	        }
	        return new int[] {max_eval, best_move};
	    } else {
	        int min_eval = Integer.MAX_VALUE;
	        int best_move = -1;
	        for (int move : gs.getValidMoves(board)) {
	        	ArrayList<Hexagon> new_board = gs.makeMove(board,move, gs.whosTurn);
	            int[] eval = minimax(new_board, depth - 1, true);
	            if (eval[0] < min_eval) {
	                min_eval = eval[0];
	                best_move = move;
	            }
	        }
	        return new int[] {min_eval, best_move};
	    }
	}
}
