import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

/**
 * Created by david on 10/8/16.
 */


public class ChessLegalMoveTest {
    public static void main(String[] args) {
        String fen = "rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1";

        Board board = new Board();
        board.loadFromFEN(fen);
        MoveList moves = null;
        try {
            moves = MoveGenerator.getInstance().generateLegalMoves(board);
        } catch (MoveGeneratorException e) {
            e.printStackTrace();
        }
        System.out.println("Legal moves: " + moves);
    }


}
