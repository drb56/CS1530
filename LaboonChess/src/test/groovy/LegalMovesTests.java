import entities.ChessBoard;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class LegalMovesTests {
    private ChessBoard chessBoard;
    private ChessBoard chessBoard2;

    /**
     * Creates a new chessboard object for the rest of the tests to use.
     */
    @Before
    public void beforeTests(){
        chessBoard = new ChessBoard();
        chessBoard2 = new ChessBoard("r1bqkb1r/ppppp2p/n4ppn/8/5B2/2PP1N2/PP2PPPP/RN1QKB1R w");
    }

    /**
     * Tests that when the user inputs an illegal move isLegal() method returns false
     */
    @Test
    public void testIsLegalReturnsFalse() {
        assertEquals(chessBoard.isLegal("b2", "c2"), 0);
    }

    /**
     * Tests that when the user inputs a legal move it returns true
     * This test in particular will move b2->b3
     */
    @Test
    public void testIsLegalPawnMoveOne() {
        assertEquals(chessBoard.isLegal("b2", "b3"), 1);
    }

    /**
     * Tests that when the user inputs a legal move it returns true
     * This test in particular will move b2->b4
     */
    @Test
    public void testIsLegalPawnMoveTwo() {
        assertEquals(chessBoard.isLegal("b2", "b4"), 1);
    }
}
