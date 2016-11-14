import entities.ChessBoard;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Created by davidbickford on 11/14/16.
 */
public class LegalMovesTests extends GuiTest{
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
     * Tests that when the user inputs an illegal move isLegal() method returns false
     */
    @Test
    public void testIsLegalReturnsFalse() {
        assertEquals(chessBoard.isLegal("b2", "c2"), false);
    }

    /**
     * Tests that when the user inputs a legal move it returns true
     * This test in particular will move b2->b3
     */
    @Test
    public void testIsLegalPawnMoveOne() {
        assertEquals(chessBoard.isLegal("b2", "b3"), true);
    }

    /**
     * Tests that when the user inputs a legal move it returns true
     * This test in particular will move b2->b4
     */
    @Test
    public void testIsLegalPawnMoveTwo() {
        assertEquals(chessBoard.isLegal("b2", "b4"), true);
    }
}
