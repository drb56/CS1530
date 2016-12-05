import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertTrue;


public class KibitzerTests extends ApplicationTest {
    Parent mainNode;
    Stage stage;
    Label label;

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

    @Before
    public void setUp() {
        label = find("#comments");
    }

    @Test
    public void testKibitzerCommentVisible() {
        assertTrue(label.isVisible());
    }

    @Test
    public void testKibitzerMessage() {
        sleep(1000);
        assertTrue(label.getText().length() > 0);
    }


}
