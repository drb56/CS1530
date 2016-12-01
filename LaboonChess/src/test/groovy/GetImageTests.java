import entities.ChessBoardGUIProperties;
import javafx.scene.image.ImageView;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 11/30/2016.
 */
public class GetImageTests {
    @Test
    public void testGetWhiteBishop() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getWhiteBishop();
        assertEquals(i.getId(), "B");
    }
    @Test
    public void testGetWhitePawn() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getWhitePawn();
        assertEquals(i.getId(), "P");
    }
    @Test
    public void testGetWhiteRook() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getWhiteRook();
        assertEquals(i.getId(), "R");
    }
    @Test
    public void testGetWhiteKnight() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getWhiteKnight();
        assertEquals(i.getId(), "N");
    }
    @Test
    public void testGetWhiteKing() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getWhiteKing();
        assertEquals(i.getId(), "K");
    }
    @Test
    public void testGetWhiteQueen() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getWhiteQueen();
        assertEquals(i.getId(), "Q");
    }
    @Test
    public void testGetBlackPawn() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getBlackPawn();
        assertEquals(i.getId(), "p");
    }
    @Test
    public void testGetBlackRook() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getBlackRook();
        assertEquals(i.getId(), "r");
    }
    @Test
    public void testGetBlackKnight() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getBlackKnight();
        assertEquals(i.getId(), "n");
    }
    @Test
    public void testGetBlackBishop() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getBlackBishop();
        assertEquals(i.getId(), "b");
    }
    @Test
    public void testGetBlackKing() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getBlackKing();
        assertEquals(i.getId(), "k");
    }
    @Test
    public void testGetBlackQueen() {
        ChessBoardGUIProperties g = new ChessBoardGUIProperties();
        ImageView i = g.getBlackQueen();
        assertEquals(i.getId(), "q");
    }
}
