import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import java.io.IOException;
import entities.ChessBoard;
import static org.junit.Assert.assertEquals;

public class SANTests extends GuiTest{
    private ChessBoard chessBoard;
    private ChessBoard chessBoard2;
    public Parent getRootNode()
    {
        Parent parent = null;
        try {
            parent = FXMLLoader.load(getClass().getResource("/fxml/LaboonChessDocument.fxml"));
            return parent;

        } catch (IOException ex) {
            System.out.println(ex);
            System.exit(1);
        }
        return parent;
    }

    /**
     * Creates a new chessboard object for the rest of the tests to use.
     */
    @Before
    public void beforeTests(){
        chessBoard = new ChessBoard();
        chessBoard2 = new ChessBoard("r1bqkb1r/ppppp2p/n4ppn/8/5B2/2PP1N2/PP2PPPP/RN1QKB1R w");
    }

    /**
     * Tests valid input (algebraic notation) returns the proper row. "1" is the 7th row of a chessboard
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DRowLowerBound () { assertEquals(chessBoard.sanTo2DRow("a1"), 7); }

    /**
     * Tests valid input (algebraic notation) returns the proper row. "6" is the 2nd row of a chessboard
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DRowMiddleBound(){
        assertEquals(chessBoard.sanTo2DRow("g6"), 2);
    }

    /**
     * Tests valid input (algebraic notation) returns the proper row. "8" is the 0th row of a chessboard
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DRowUpperBound(){
        assertEquals(chessBoard.sanTo2DRow("h8"), 0);
    }

    /**
     * Tests invalid input (algebraic notation) returns -1 to catch invalid input. "9" is not a valid row
     * for the chessboard.
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DRowOutOfBoundsUpper(){
        assertEquals(chessBoard.sanTo2DRow("a9"), -1);
    }

    /**
     * Tests invalid input (algebraic notation) returns -1 to catch invalid input. "0" is not a valid row
     * for the chessboard.
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DRowOutOfBoundsLower(){ assertEquals(chessBoard.sanTo2DRow("a0"), -1); }

    /**
     * Tests valid input (algebraic notation) returns the proper column. "a" is the 0th column of a chessboard
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DColLowerBound(){
        assertEquals(chessBoard.sanTo2DCol("a1"), 0);
    }

    /**
     * Tests valid input (algebraic notation) returns the proper column. "b" is the 1st column of a chessboard
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DColMiddleBound(){
        assertEquals(chessBoard.sanTo2DCol("b3"), 1);
    }

    /**
     * Tests valid input (algebraic notation) returns the proper column. "e" is the 4th column of a chessboard
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DColMiddleBound2(){
        assertEquals(chessBoard.sanTo2DCol("e4"), 4);
    }

    /**
     * Tests valid input (algebraic notation) returns the proper column. "h" is the 7th column of a chessboard
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DColUpperBound(){
        assertEquals(chessBoard.sanTo2DCol("h4"), 7);
    }

    /**
     * Tests invalid input (algebraic notation) and returns -1 to catch invalid input. "I" is not a
     * valid column for the chessboard.
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DColOutOfBoundsUpper(){
        assertEquals(chessBoard.sanTo2DCol("I1"), -1);
    }

    /**
     * Tests invalid input (algebraic notation) and returns -1 to catch invalid input. "`" is not a
     * valid column for the chessboard.
     * The chessboard starts at a1 (a = column, 1 = row) in the lower left corner of the chessboard
     * and ends at h8 (h = column, 8 = row) in the upper right corner of the chessboard.
     */
    @Test
    public void testSanTo2DColOutOfBoundsLower(){
        assertEquals(chessBoard.sanTo2DCol("`1"), -1);
    }
}