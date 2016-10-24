import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;

import java.io.IOException;

import entities.ChessBoard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.loadui.testfx.Assertions.assertNodeExists;
import static org.loadui.testfx.Assertions.verifyThat;

public class LaboonChessTest extends GuiTest{
    private ChessBoard chessBoard;
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

    @Before
    public void beforeTests(){
        chessBoard = new ChessBoard();
    }

    @Test
    public void testChessboardToFENGameStart(){ assertEquals(chessBoard.toFEN(), "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w"); }

    @Test
    //
    public void testSanTo2DRowLowerBound () { assertEquals(chessBoard.sanTo2DRow("a1"), 7); }

    @Test
    public void testSanTo2DRowMiddleBound(){
        assertEquals(chessBoard.sanTo2DRow("g6"), 2);
    }

    @Test
    public void testSanTo2DRowUpperBound(){
        assertEquals(chessBoard.sanTo2DRow("h8"), 0);
    }

    @Test
    public void testSanTo2DRowOutOfBoundsUpper(){
        assertEquals(chessBoard.sanTo2DRow("a9"), -1);
    }

    @Test
    public void testSanTo2DRowOutOfBoundsLower(){ assertEquals(chessBoard.sanTo2DRow("a0"), -1); }

    @Test
    public void testSanTo2DColLowerBound(){
        assertEquals(chessBoard.sanTo2DCol("a1"), 0);
    }

    @Test
    public void testSanTo2DColMiddleBound(){
        assertEquals(chessBoard.sanTo2DCol("b3"), 1);
    }

    @Test
    public void testSanTo2DColMiddleBound2(){
        assertEquals(chessBoard.sanTo2DCol("e4"), 4);
    }

    @Test
    public void testSanTo2DColUpperBound(){
        assertEquals(chessBoard.sanTo2DCol("h4"), 7);
    }

    @Test
    public void testSanTo2DColOutOfBoundsUpper(){
        assertEquals(chessBoard.sanTo2DCol("I1"), -1);
    }

    @Test
    public void testSanTo2DColOutOfBoundsLower(){
        assertEquals(chessBoard.sanTo2DCol("`1"), -1);
    }


//    @Test
//    public void testGetTranslate(){
//        assertEquals(AlgebraicNotationConversion.getTranslate(0,1), "a7");
//    }
//    @Test
//    public void testReverseTranslate(){
//        Square sq = AlgebraicNotationConversion.reverseTranslate("a7");
//        assertEquals(sq.column, 0);
//        assertEquals(sq.row, 1);
//    }
//    @Test
//    public void testReverseTranslate2(){
//        Square sq = AlgebraicNotationConversion.reverseTranslate("H8");
//        assertEquals(sq.column, 7);
//        assertEquals(sq.row, 0);
//    }
//    @Test
//    public void testHandleUndoMoveAction(){
//        Node a = this.find("#btnSaveGame");
//        this.clickOn(a);
//        final Node b = this.find("#mnuUndoMove");
//        this.clickOn(b);
//        assertTrue(this.find("Undo Move menu item clicked").isVisible());
//    }
//
//    @Test
//    public void testLoadGame()
//    {
//        final Node b =  this.find("#btnLoadGame");
//        this.clickOn(b);
//        assertTrue(this.find("Load Game clicked").isVisible());
//    }
//
//    @Test
//    public void testHandleSaveGameAction(){
//        final Node b = this.find("#btnSaveGame");
//        this.clickOn(b);
//        assertTrue(this.find("Save Game clicked").isVisible());
//    }
}