import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;
import org.testfx.api.FxRobot;
import org.testfx.robot.MouseRobot;
import stockfish.Stockfish;

import java.io.IOException;
import java.util.regex.Matcher;
import services.FENStringConversion;
import services.AlgebraicNotationConversion;
import services.Square;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.loadui.testfx.Assertions.assertNodeExists;
import static org.loadui.testfx.Assertions.verifyThat;

/**
 * Created by david on 9/28/16.
 */


public class LaboonChessTest extends GuiTest{
    public Parent getRootNode()
    {
        Parent parent = null;
        try {
//            /Users/davidbickford/Documents/github/CS1530/LaboonChess/src/main/resources/fxml/LaboonChessDocument.fxml
            parent = FXMLLoader.load(getClass().getResource("/fxml/LaboonChessDocument.fxml"));
            return parent;
        } catch (IOException ex) {
            // TODO ...
            System.out.println(ex);
            System.exit(1);
        }
        return parent;
    }

    @Test
    public void testLoadGame()
    {
        final Node b =  this.find("#btnLoadGame");
        this.clickOn(b);
        assertTrue(this.find("Load Game clicked").isVisible());
    }

    @Test
    public void testHandleSaveGameAction(){
        final Node b = this.find("#btnSaveGame");
        this.clickOn(b);
        assertTrue(this.find("Save Game clicked").isVisible());
    }

    @Test
    public void testChessboardToFEN(){
        char[][] chessboard = new char[][]{
                { 'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r' },
                { 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p' },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P' },
                { 'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R' }
        };
        String FEN = "ra8 nb8 bc8 qd8 ke8 bf8 ng8 rh8 pa7 pb7 pc7 pd7 pe7 pf7 pg7 ph7 Pa2 Pb2 Pc2 Pd2 Pe2 Pf2 Pg2 " +
                        "Ph2 Ra1 Nb1 Bc1 Qd1 Ke1 Bf1 Ng1 Rh1 ";
        assertEquals(FENStringConversion.chessboardToFEN(chessboard), FEN);
    }

    @Test
    public void testGetTranslate(){
        assertEquals(AlgebraicNotationConversion.getTranslate(0,1), "a7");
    }
    @Test
    public void testReverseTranslate(){
        Square sq = AlgebraicNotationConversion.reverseTranslate("a7");
        assertEquals(sq.column, 0);
        assertEquals(sq.row, 1);
       // assertEquals(AlgebraicNotationConversion.reverseTranslate("a7"), (01));
    }
//    @Test
//    public void testHandleUndoMoveAction(){
//        Node a = this.find("#btnSaveGame");
//        this.clickOn(a);
//        final Node b = this.find("#mnuUndoMove");
//        this.clickOn(b);
//        assertTrue(this.find("Undo Move menu item clicked").isVisible());
//    }

//    private void handleUndoMoveAction(ActionEvent event) {
//        lblStatus.setText("Undo Move menu item clicked");
//    }
//    private void handleSaveGameAction(ActionEvent event) {
//        lblStatus.setText("Save Game clicked");
//    }
}