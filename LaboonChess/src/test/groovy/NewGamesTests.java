import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import java.util.List;

import static org.junit.Assert.*;


public class NewGamesTests extends ApplicationTest {
    Parent mainNode;
    Stage stage;
    MenuBar menubar;
    Menu file;
    Menu newGame;
    MenuItem multiplayer;
    MenuItem blackAI;
    MenuItem whiteAI;
    GridPane guiBoard;

    @Override
    public void start(Stage stage) throws Exception {
        mainNode = FXMLLoader.load(getClass().getResource("/fxml/LaboonChessDocument.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();

    }

    public <T extends Node> T find(final String query) {
        /* TestFX provides many operations to retrieve elements from the loaded GUI. */
        return lookup(query).query();
    }

    public boolean isNewGame() {
        List children = guiBoard.getChildren();
        for(int i = 0; i < children.size(); i++) {
            Pane child = (Pane) children.get(i);

            List pane_properties = child.getChildren();

            if(pane_properties.size() > 0 && pane_properties.get(0).getClass().toString().equals("class javafx.scene.image.ImageView")) {
                if (child.getId().equals("b8") || child.getId().equals("c8") || child.getId().equals("d8") ||
                        child.getId().equals("e8") || child.getId().equals("f8") || child.getId().equals("g8") || child.getId().equals("h8") || child.getId().equals("a8") ||
                        child.getId().equals("a7") || child.getId().equals("b7") || child.getId().equals("c7") || child.getId().equals("d7") || child.getId().equals("e7") ||
                        child.getId().equals("f7") || child.getId().equals("g7") || child.getId().equals("a1") || child.getId().equals("b1") || child.getId().equals("c1") ||
                        child.getId().equals("d1") || child.getId().equals("e1") || child.getId().equals("f1") || child.getId().equals("g1") || child.getId().equals("h1") ||
                        child.getId().equals("a2") || child.getId().equals("b2") || child.getId().equals("c2") || child.getId().equals("d2") || child.getId().equals("e2") ||
                        child.getId().equals("f2") || child.getId().equals("g2")
                        ) {
                    if (pane_properties.size() != 1) {
                        return false;
                    }
                }

            }

        }
        return true;
    }


    @Before
    public void setUp() {
        /* Just retrieving the tested widgets from the GUI. */
        guiBoard = find("#guiChessboard");
        menubar = find("#mnuMain");
        file = menubar.getMenus().get(0);
        newGame = (Menu) file.getItems().get(0);
        multiplayer = newGame.getItems().get(0);
        blackAI = newGame.getItems().get(1);
        whiteAI = newGame.getItems().get(2);
    }

    @Test
    public void testInitializeBoard() {
        assertFalse(guiBoard == null);
    }

    @Test
    public void testInitializeMultiplayer() {
        assertEquals(multiplayer.getId(), "multiplayer");
    }

    @Test
    public void testInitializeBlackAI() {
        assertEquals(blackAI.getId(), "cpublack");
    }

    @Test
    public void testInitializeWhiteAI() {
        assertEquals(whiteAI.getId(), "cpuwhite");
    }

    @Test
    public void testNewBoardMultiplayer() {
        clickOn(menubar).clickOn("#"+file.getId()).clickOn("#"+newGame.getId()).clickOn("#"+multiplayer.getId());
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(isNewGame());
    }

    @Test
    public void testNewBoardBlackAI() {
        clickOn(menubar).clickOn("#"+file.getId()).clickOn("#"+newGame.getId()).clickOn("#"+blackAI.getId());
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(isNewGame());
    }

    @Test
    public void testNewBoardWhiteAI() {
        clickOn(menubar).clickOn("#"+file.getId()).clickOn("#"+newGame.getId()).clickOn("#"+blackAI.getId());
        WaitForAsyncUtils.waitForFxEvents();
        assertTrue(isNewGame());
    }


}