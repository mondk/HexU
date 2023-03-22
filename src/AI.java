
public class AI {
	
	GameState gs;
	
	AI(GameState gs){
		this.gs=gs;
	}

	int id =0;
	public int nextMove() {
		id+=1;
		return id;
	}
	
//ublic static int[] minimax(Board board, int depth, boolean maximizing_player) {
//   if (depth == 0) {
//       return new int[] {board.evaluate(), -1};
//   }
//
//   if (maximizing_player) {
//       int max_eval = Integer.MIN_VALUE;
//       int best_move = -1;
//       for (int move : board.getValidMoves()) {
//           Board new_board = board.makeMove(move, board.getCurrentPlayer());
//           int[] eval = minimax(new_board, depth - 1, false);
//           if (eval[0] > max_eval) {
//               max_eval = eval[0];
//               best_move = move;
//           }
//       }
//       return new int[] {max_eval, best_move};
//   } else {
//       int min_eval = Integer.MAX_VALUE;
//       int best_move = -1;
//       for (int move : board.getValidMoves()) {
//           Board new_board = board.makeMove(move, board.getCurrentPlayer());
//           int[] eval = minimax(new_board, depth - 1, true);
//           if (eval[0] < min_eval) {
//               min_eval = eval[0];
//               best_move = move;
//           }
//       }
//       return new int[] {min_eval, best_move};
//   }
//
}
