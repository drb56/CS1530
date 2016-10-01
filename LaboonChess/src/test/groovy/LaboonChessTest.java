import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.loadui.testfx.GuiTest;
import org.loadui.testfx.utils.FXTestUtils;
import org.testfx.api.FxRobot;
import org.testfx.robot.MouseRobot;

import java.io.IOException;
import java.util.regex.Matcher;

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
            parent = FXMLLoader.load(getClass().getResource("/fxml/LaboonChessDocument.fxml"));
            return parent;
        } catch (IOException ex) {
            // TODO ...
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
}