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
        String FEN = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";
        assertEquals(FENStringConversion.chessboardToFEN(chessboard), FEN);
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