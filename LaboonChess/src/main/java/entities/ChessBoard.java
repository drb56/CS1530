package entities;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.Move;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;
import java.security.InvalidParameterException;

public class ChessBoard {
    private char[][] chessboard;    /* holds the contents of the chessboard (pnrbkq | PNRBKQ | null) */
    private String lastFen;         /* holds the last FEN string representing the 2D chessboard array */
    private int turn;               /* holds which team is to go next (0=white 1=black) */

    /**
     * Creates a new ChessBoard instance. The chessboard is initialized to the "default"
     *      layout, the Turn is set to White, and the FEN string is the representation
     *      of the current "default" layout.
     */
    public ChessBoard(){
        /* build the chessboard */
        chessboard = new char[][]{
                { 'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r' },
                { 'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p' },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 0, 0, 0, 0, 0, 0, 0, 0 },
                { 'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P' },
                { 'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R' }
        };
        turn = 0;
        lastFen = toFEN();
    }

    public ChessBoard(String fen) {
        lastFen = fen;
    }


    /**
     * Determines if a chess move is a valid/legal move, given a
     *      starting and ending chess square in SAN notation (e.g. "a1")
     *
     * @param sanFrom The starting USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @param sanTo The starting USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'd4')
     * @return True if the move was valid; otherwise False.
     */
    public boolean isLegal(String sanFrom, String sanTo) {
        Board board = new Board();
        board.loadFromFEN(lastFen);
        MoveList moves = null;

        try {
            moves = MoveGenerator.getInstance().generateLegalMoves(board);
            String actualMove = sanFrom + sanTo;

            for (Move move : moves) {
                System.out.println(move.toString() + " " + actualMove);
                if (move.toString().equals(actualMove)) {
                    return true;
                }
            }

            return false;

        } catch (MoveGeneratorException e) {
            e.printStackTrace();
        }

        return false;
    }


    /**
     * Given a starting chess square and ending chess square, will determine if
     *      the move is a valid chess move. If valid, then the move is performed;
     *      otherwise the chess board stays the same.
     *
     * @param sanFrom The starting USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @param sanTo The starting USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'd4')
     * @return True if the move was valid; otherwise False.
     */
    public boolean move(String sanFrom, String sanTo) {
        if (!isLegal(sanFrom, sanTo)) {             //returns false if isn't a legal move
            System.out.println("ILLEGAL MOVE");
            return false;

        } else {                                    //changes the board if it is a legal move
            System.out.println("LEGAL MOVE");
            update2DArrayChessboard(sanFrom, sanTo);

            if (turn == 0) {
                turn = 1;
            } else {
                turn = 0;
            }

            lastFen = toFEN();
        }

        return true;
    }


    /**
     * Takes two SAN coordinates and moves the chess piece from the start square to
     *      the end square within the 2D chessboard array.
     *
     * @param sanFromSquare The SAN of the starting chess square. (e.g. 'a1')
     * @param sanToSquare The SAN of the ending chess square. (e.g. 'd6')
     */
    private void update2DArrayChessboard(final String sanFromSquare, final String sanToSquare) {
        /*
            Translate the SAN strings into 2D array coordinates, being careful due
                to the USCF column layouts and 2D array column layouts being reversed.
         */
        int fromRow = sanTo2DRow(sanFromSquare);
        int fromCol = sanTo2DCol(sanFromSquare);
        int toRow = sanTo2DRow(sanToSquare);
        int toCol = sanTo2DCol(sanToSquare);

        chessboard[toRow][toCol] = chessboard[fromRow][fromCol];        // move piece from start square to end square
        chessboard[fromRow][fromCol] = 0;                               // the start square should now be empty

        this.printBoard(); // DEBUG
    }


    /**
     * Transforms a given SAN string coordinate (e.g. 'a1') to its corresponding
     *      2D chessboard array ROW coordinate (e.g. '0'). Subtracted from (8) to
     *      give the proper USCF layout. This is a tricky translation, but is needed
     *      to go from USCF language to traditional programming array language.
     *
     * @param sanSquare A USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @return The given chess square ROW within the 2D chessboard array. (e.g. '7')
     */
    public int sanTo2DRow (final String sanSquare) {
        if (sanSquare == null || sanSquare.length() < 2) { throw new InvalidParameterException("sanSquare is null"); }

        try {
            int num = (8 - (int)sanSquare.charAt(1) % 48);  // ASCII char (48) is '0'

            if(num < 0 || num > 7){
                throw new InvalidParameterException(String.format("chessboard row is invalid (%d)", num));
            } else {
                return num;
            }
        } catch (InvalidParameterException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * Transforms a given SAN string coordinate (e.g. 'a1') to its corresponding
     *      2D chessboard array COLUMN coordinate (e.g. '0').
     *
     * @param sanSquare A USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @return The given chess square COLUMN within the 2D chessboard array. (e.g. '0')
     */
    public int sanTo2DCol (String sanSquare) {
        if (sanSquare == null) { throw new InvalidParameterException("sanSquare is null"); }

        try {
            sanSquare = sanSquare.toLowerCase();
            int num = (int)sanSquare.charAt(0) % 97;    // ASCII char (97) is 'a'

            if(num < 0 || num > 7){
                throw new InvalidParameterException(String.format("chessboard column is invalid (%d)", num));
            } else {
                return (num);
            }
        } catch (InvalidParameterException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * Given a SAN coordinate (e.g. 'a4'), will return the contents of the proper
     *      2D array location (chess square).
     *
     * @param sanSquare The chessboard square, in SAN, to get the 2D array coordinates of. (e.g. 'a1')
     * @return The contents of the given chess square; '0' if nothing exists on that square.
     */
    public char get2DArrayChessboardPiece(final String sanSquare) {
        return chessboard[sanTo2DRow(sanSquare)][sanTo2DCol(sanSquare)];
    }


    /**
     * Returns the current state of the 2D chessboard array into a proper
     *      Forsyth–Edwards Notation (FEN) formatted string.
     *
     * @return A formatted Forsyth-Edwards Notation string.
     */
    public String toFEN(){
        char[] letterLocs = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        char[] numberLocs = new char[]{'8', '7', '6', '5', '4', '3', '2', '1'};
        String curBoard = "";      //the current board in algebraic notation
        String fenBoard = "";       //the current board in FEN string notation

        for (int row=0; row<chessboard.length; row++) {         // rows
            for (int col=0; col<chessboard.length; col++) {     // columns
                if (chessboard[row][col] != 0) {
                    curBoard = curBoard + chessboard[row][col] + letterLocs[col] + numberLocs[row] + " ";
                    fenBoard = fenBoard + chessboard[row][col];
                } else {
                    fenBoard = fenBoard + '0';
                }
            }
            if (row < 7){
                fenBoard = fenBoard + '/';
            }
        }

        char[] fenBoardArray = fenBoard.toCharArray();
        fenBoard = "";
        int onesNumber = 0;

        for (int i=0; i<fenBoardArray.length; i++) {
            if (fenBoardArray[i] == '0') {
                onesNumber++;

                if (i == fenBoardArray.length-1) {
                    fenBoard = fenBoard + onesNumber;
                }

            } else {
                if (onesNumber > 0) {
                    fenBoard = fenBoard + onesNumber;
                }
                fenBoard = fenBoard + fenBoardArray[i];
                onesNumber = 0;
            }
        }
        if (turn == 0) {
            fenBoard = fenBoard + " w";
        } else {
            fenBoard = fenBoard + " b";
        }
        return fenBoard;
    }


    /**
     * Pretty-prints the current state of the 2D chessboard array.
     */
    public void printBoard() {
        System.out.println("   A   B   C   D   E   F   G   H ");

        int boardnum=8;
        for (int x = 0; x < chessboard.length; x++) {
            System.out.print(String.format("%d ", boardnum));

            char [] row = chessboard[x];
            for (int y = 0; y < row.length; y++) {
                char val = row[y];
                System.out.print(String.format("[%s] ", val==0 ? " " : val));
            }

            System.out.println(String.format("%d", boardnum--));
        }
        System.out.println("   A   B   C   D   E   F   G   H ");
    }
}
