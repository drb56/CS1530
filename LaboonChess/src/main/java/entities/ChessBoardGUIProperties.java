package entities;


import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;

/**
 * Created by david on 11/22/16.
 */
public class ChessBoardGUIProperties {
    public ImageView getWhiteQueen() {
        String path2 = this.getClass().getClassLoader().getResource("images/white_queen.png").getPath();
        ImageView piece = new ImageView(new Image(new File(path2).toURI().toString()));
        piece.setId("Q");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhiteKing() {
        String path2 = this.getClass().getClassLoader().getResource("images/white_king.png").getPath();
        ImageView piece = new ImageView(new Image(new File(path2).toURI().toString()));
        piece.setId("K");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhiteBishop() {
        String path2 = this.getClass().getClassLoader().getResource("images/white_bishop.png").getPath();
        ImageView piece = new ImageView(new Image(new File(path2).toURI().toString()));
        piece.setId("B");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhiteKnight() {
        String path2 = this.getClass().getClassLoader().getResource("images/white_knight.png").getPath();
        ImageView piece = new ImageView(new Image(new File(path2).toURI().toString()));
        piece.setId("N");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhiteRook() {
        String path2 = this.getClass().getClassLoader().getResource("images/white_rook.png").getPath();
        ImageView piece = new ImageView(new Image(new File(path2).toURI().toString()));
        piece.setId("R");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhitePawn() {
        String path2 = this.getClass().getClassLoader().getResource("images/white_pawn.png").getPath();
        ImageView piece = new ImageView(new Image(new File(path2).toURI().toString()));
        piece.setId("P");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getBQueen() {
        String path2 = this.getClass().getClassLoader().getResource("images/black_queen.png").getPath();
        ImageView bqueen = new ImageView(new Image(new File(path2).toURI().toString()));
        bqueen.setId("q");
        bqueen.setFitHeight(44.0);
        bqueen.setFitWidth(31.0);
        bqueen.setLayoutX(12.0);
        bqueen.setPickOnBounds(true);
        bqueen.setPreserveRatio(true);

        return bqueen;
    }

    public ImageView getBKing() {
        String path2 = this.getClass().getClassLoader().getResource("images/black_king.png").getPath();
        ImageView bking = new ImageView(new Image(new File(path2).toURI().toString()));
        bking.setId("k");
        bking.setFitHeight(44.0);
        bking.setFitWidth(31.0);
        bking.setLayoutX(12.0);
        bking.setPickOnBounds(true);
        bking.setPreserveRatio(true);

        return bking;
    }

    public ImageView getBKnight() {
        String path2 = this.getClass().getClassLoader().getResource("images/black_knight.png").getPath();
        ImageView bknight = new ImageView(new Image(new File(path2).toURI().toString()));
        bknight.setId("n");
        bknight.setFitHeight(44.0);
        bknight.setFitWidth(31.0);
        bknight.setLayoutX(12.0);
        bknight.setPickOnBounds(true);
        bknight.setPreserveRatio(true);

        return bknight;
    }

    public ImageView getBBishop() {
        String path2 = this.getClass().getClassLoader().getResource("images/black_bishop.png").getPath();
        ImageView bbishop = new ImageView(new Image(new File(path2).toURI().toString()));
        bbishop.setId("b");
        bbishop.setFitHeight(44.0);
        bbishop.setFitWidth(31.0);
        bbishop.setLayoutX(12.0);
        bbishop.setPickOnBounds(true);
        bbishop.setPreserveRatio(true);

        return bbishop;
    }

    public ImageView getBrook() {
        String path2 = this.getClass().getClassLoader().getResource("images/black_rook.png").getPath();
        ImageView brook = new ImageView(new Image(new File(path2).toURI().toString()));
        brook.setId("r");
        brook.setFitHeight(44.0);
        brook.setFitWidth(31.0);
        brook.setLayoutX(12.0);
        brook.setPickOnBounds(true);
        brook.setPreserveRatio(true);

        return brook;
    }

    public ImageView getBPawn() {
        String path = this.getClass().getClassLoader().getResource("images/black_pawn.png").getPath();
        ImageView bpawn = new ImageView(new Image(new File(path).toURI().toString()));

        bpawn.setId("p");
        bpawn.setFitHeight(44.0);
        bpawn.setFitWidth(31.0);
        bpawn.setLayoutX(12.0);
        bpawn.setPickOnBounds(true);
        bpawn.setPreserveRatio(true);

        return bpawn;
    }
}
