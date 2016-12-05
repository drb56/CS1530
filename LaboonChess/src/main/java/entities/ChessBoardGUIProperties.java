package entities;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ChessBoardGUIProperties {
    public ImageView getWhiteQueen() {
        ImageView piece = new ImageView(new Image(getClass().getResourceAsStream("/images/white_queen.png")));
        piece.setId("Q");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhiteKing() {
        ImageView piece = new ImageView(new Image(getClass().getResourceAsStream("/images/white_king.png")));
        piece.setId("K");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhiteBishop() {
        ImageView piece = new ImageView(new Image(getClass().getResourceAsStream("/images/white_bishop.png")));
        piece.setId("B");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhiteKnight() {
        ImageView piece = new ImageView(new Image(getClass().getResourceAsStream("/images/white_knight.png")));
        piece.setId("N");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhiteRook() {
        ImageView piece = new ImageView(new Image(getClass().getResourceAsStream("/images/white_rook.png")));
        piece.setId("R");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getWhitePawn() {
        ImageView piece = new ImageView(new Image(getClass().getResourceAsStream("/images/white_pawn.png")));
        piece.setId("P");
        piece.setFitHeight(44.0);
        piece.setFitWidth(31.0);
        piece.setLayoutX(12.0);
        piece.setPickOnBounds(true);
        piece.setPreserveRatio(true);

        return piece;
    }

    public ImageView getBlackQueen() {
        ImageView bqueen = new ImageView(new Image(getClass().getResourceAsStream("/images/black_queen.png")));
        bqueen.setId("q");
        bqueen.setFitHeight(44.0);
        bqueen.setFitWidth(31.0);
        bqueen.setLayoutX(12.0);
        bqueen.setPickOnBounds(true);
        bqueen.setPreserveRatio(true);

        return bqueen;
    }

    public ImageView getBlackKing() {
        ImageView bking = new ImageView(new Image(getClass().getResourceAsStream("/images/black_king.png")));
        bking.setId("k");
        bking.setFitHeight(44.0);
        bking.setFitWidth(31.0);
        bking.setLayoutX(12.0);
        bking.setPickOnBounds(true);
        bking.setPreserveRatio(true);

        return bking;
    }

    public ImageView getBlackKnight() {
        ImageView bknight = new ImageView(new Image(getClass().getResourceAsStream("/images/black_knight.png")));
        bknight.setId("n");
        bknight.setFitHeight(44.0);
        bknight.setFitWidth(31.0);
        bknight.setLayoutX(12.0);
        bknight.setPickOnBounds(true);
        bknight.setPreserveRatio(true);

        return bknight;
    }

    public ImageView getBlackBishop() {
        ImageView bbishop = new ImageView(new Image(getClass().getResourceAsStream("/images/black_bishop.png")));
        bbishop.setId("b");
        bbishop.setFitHeight(44.0);
        bbishop.setFitWidth(31.0);
        bbishop.setLayoutX(12.0);
        bbishop.setPickOnBounds(true);
        bbishop.setPreserveRatio(true);

        return bbishop;
    }

    public ImageView getBlackRook() {
        ImageView brook = new ImageView(new Image(getClass().getResourceAsStream("/images/black_rook.png")));
        brook.setId("r");
        brook.setFitHeight(44.0);
        brook.setFitWidth(31.0);
        brook.setLayoutX(12.0);
        brook.setPickOnBounds(true);
        brook.setPreserveRatio(true);

        return brook;
    }

    public ImageView getBlackPawn() {
        ImageView bpawn = new ImageView(new Image(getClass().getResourceAsStream("/images/black_pawn.png")));
        bpawn.setId("p");
        bpawn.setFitHeight(44.0);
        bpawn.setFitWidth(31.0);
        bpawn.setLayoutX(12.0);
        bpawn.setPickOnBounds(true);
        bpawn.setPreserveRatio(true);

        return bpawn;
    }
}
