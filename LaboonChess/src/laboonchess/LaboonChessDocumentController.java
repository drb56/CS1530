package laboonchess;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 *
 * @author Joe Meszar (jwm54@pitt.edu)
 */
public class LaboonChessDocumentController implements Initializable {
    
    @FXML
    private Label lblStatus;
    
    @FXML
    private void handleUndoMoveAction(ActionEvent event) {
        lblStatus.setText("Undo Move menu item clicked");
    }
    
    @FXML
    private void handleAboutAction(ActionEvent event) {
        lblStatus.setText("About menu item clicked");
    }
    
    @FXML
    private void handleLoadGameAction(ActionEvent event) {
        System.out.println("Load Game button clicked!");
        lblStatus.setText("Load Game clicked");
    }
    
    @FXML
    private void handleSaveGameAction(ActionEvent event) {
        System.out.println("Save Game button clicked!");
        lblStatus.setText("Save Game clicked");
    }
    
    @FXML
    private void handleExitAction(ActionEvent event) {
        System.out.println("Exit Game button clicked!");
        Platform.exit();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }       
}
