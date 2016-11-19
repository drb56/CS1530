package entities;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import java.security.InvalidParameterException;

/**
 * Created by Craig on 11/15/2016.
 */
public class SpecialMovesChecker {
    ChessBoard chessboard = new ChessBoard();
    //private char[][] chessboard;    /* holds the contents of the chessboard (pnrbkq | PNRBKQ | null) */
    private String lastFen;         /* holds the last FEN string representing the 2D chessboard array */
    private String boardFen;        /* holds the beginning portion of the FEN string that has the locations of each piece */
    private int turn;               /* holds which team is to go next (0=white 1=black) */
    private boolean hasWhiteKingBeenMoved = false;
    private boolean hasBlackKingBeenMoved = false;
    private boolean hasRook00BeenMoved = false;
    private boolean hasRook07BeenMoved = false;
    private boolean hasRook70BeenMoved = false;
    private boolean hasRook77BeenMoved = false;
    private String castling = "";
}
