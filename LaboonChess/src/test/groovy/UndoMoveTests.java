import entities.ChessBoard;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;

public class UndoMoveTests {
    private ChessBoard chessBoard;
    ArrayList<String> history;

    /**
     * Creates a new chessboard object for the rest of the tests to use.
     */
    @Before
    public void beforeTests(){
        history = new ArrayList<>();
        history.add("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq");
        history.add("rnbqkbnr/pppppppp/8/8/2P5/8/PP1PPPPP/RNBQKBNR b KQkq");
        history.add("rnbqkbnr/ppppp1pp/8/5p2/2P5/8/PP1PPPPP/RNBQKBNR w KQkq");
        history.add("rnbqkbnr/ppppp1pp/8/5p2/2P5/5N2/PP1PPPPP/RNBQKB1R b KQkq");
        chessBoard = new ChessBoard(history);
    }

    /**
     * Tests that undo move works.
     */
    @Test
    public void testUndoMove(){
        chessBoard.undoMove(0);
        assertEquals(chessBoard.getFenList().size(), 3);
    }
}
