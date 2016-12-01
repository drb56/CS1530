import entities.ChessBoard;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by davidbickford on 11/30/16.
 */
public class SaveLoadGameTests {
    private ChessBoard chessBoard;
    private ChessBoard chessBoard2;


    /**
     * Creates a new chessboard object for the rest of the tests to use.
     */
    @Before
    public void beforeTests(){
        chessBoard = new ChessBoard();
        chessBoard2 = new ChessBoard("r1bqkb1r/ppppp2p/n4ppn/8/5B2/2PP1N2/PP2PPPP/RN1QKB1R w KQkq");
    }

    /**
     * Tests that save and load game works
     */
    @Test
    public void saveLoadGameTest(){
        File file = new File("test");
        if (file != null) {
            try {
                file.createNewFile();
                assertTrue(chessBoard.saveGame(file, 0, 0));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }

        int playerType = 1;
        if (file != null) {
            try {
                FileReader reader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line;
                line = bufferedReader.readLine();
                playerType = Integer.parseInt(line);
                reader.close();
            } catch (Exception e) {

            }
        }
        assertEquals(playerType, 0);
    }
}
