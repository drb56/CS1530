package entities;

/**
 * Created by Craig on 10/23/2016.
 */
public class ChessBoard {
    private char[][] chessboard;

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
    }

    public ChessBoard(String fen){

    }

    /**
     * Takes two SAN coordinates and moves the chess piece from the start square to
     *      the end square within the 2D chessboard array. Because of the way a chessboard
     *      is "called", things get tricky due to [0][0] actually being referenced
     *      as [a][8] in USCF terms.
     *
     * @param sanFromSquare The ID or SAN of the starting chess square. (e.g. 'a1')
     * @param sanToSquare The ID or SAN of the ending chess square. (e.g. 'd6')
     */
    public void update2DArrayChessboard(final String sanFromSquare, final String sanToSquare) {
        /*
            Transform the SAN strings into 2D array coordinates, being careful due
                to the USCF column layouts and 2D array column layouts are reversed.
         */
        int fromRow = sanTo2DRow(sanFromSquare);
        int fromCol = sanTo2DCol(sanFromSquare);
        int toRow = sanTo2DRow(sanToSquare);
        int toCol = sanTo2DCol(sanToSquare);

        chessboard[toRow][toCol] = chessboard[fromRow][fromCol];        // move piece from start to end square
        chessboard[fromRow][fromCol] = 0;                               // the start square should now be empty
    }

    /**
     * Transforms a given SAN string coordinate (e.g. 'a1') to its corresponding
     *      2D chessboard array ROW coordinate (e.g. '0'). Subtracted from (8) to
     *      give the proper USCF layout.
     *
     * @param sanSquare A USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @return The given chess square ROW within the 2D chessboard array. (e.g. '7')
     */
    public int sanTo2DRow (final String sanSquare) {
        return (8 - ((int) sanSquare.charAt(1)) % 48);    // ASCII char (48) is '0'
    }

    /**
     * Transforms a given SAN string coordinate (e.g. 'a1') to its corresponding
     *      2D chessboard array COLUMN coordinate (e.g. '0').
     *
     * @param sanSquare A USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @return The given chess square COLUMN within the 2D chessboard array. (e.g. '0')
     */
    public int sanTo2DCol (final String sanSquare) {
        return ((int) sanSquare.toLowerCase().charAt(0)) % 97;     // ASCII chart (97) is 'a'
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
}
