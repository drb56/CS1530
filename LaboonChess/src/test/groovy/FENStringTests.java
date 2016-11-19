import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

import entities.ChessBoard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.loadui.testfx.Assertions.assertNodeExists;
import static org.loadui.testfx.Assertions.verifyThat;

/**
 * Created by davidbickford on 11/14/16.
 */
public class FENStringTests extends GuiTest {
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
     * Tests that when the game starts, the correct Fen string is created.
     */
    @Test
    public void testChessboardToFENGameStart() {
        assertEquals(chessBoard.toFEN(), "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w kqKQ");
    }

    /**
     * Tests that the reverse of the original board is as it should be
     */
    @Test
    public void testReverseFEN() {
        assertEquals(chessBoard.reverseFEN(), "RNBKQBNR/PPPPPPPP/8/8/8/8/pppppppp/rnbkqbnr");
    }

    /**
     * Tests that the reverse of a complicated board is as it should be
     */
    @Test
    public void testReverseFENComplicatedBoard() {
        assertEquals(chessBoard2.reverseFEN(), "R1BKQ1NR/PPPP2PP/2N1PP2/2B5/8/npp4n/p2ppppp/r1bkqb1r");
    }

    /**
     * Tests that the creation of a ChessBoard object creates the correct chessboard
     */
    @Test
    public void testChessBoardGivenFEN() {
        assertEquals(chessBoard2.toFEN(), "r1bqkb1r/ppppp2p/n4ppn/8/5B2/2PP1N2/PP2PPPP/RN1QKB1R w kqKQ");
    }

}
