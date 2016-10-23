package services;

import entities.Square;

/**
 * Created by Craig on 10/22/2016.
 */
public class AlgebraicNotationConversion {
    private static char[] letterLocs = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private static char[] numberLocs = new char[]{'8', '7', '6', '5', '4', '3', '2', '1'};
    public static String getTranslate(int x, int y){
        if (x < 0 || y > 7){
            System.out.println("bad coordinates");
            return null;
        }
        else{

            String algebraicNotation = "";
            algebraicNotation = String.valueOf(letterLocs[x]) + String.valueOf(numberLocs[y]);
            System.out.println(algebraicNotation);
            return algebraicNotation;
        }
    }

    public static Square reverseTranslate(String an){//an = algebraic notation
        char x = an.charAt(0);//getting letter from algebraic notation
        char y = an.charAt(1);//getting number from algebraic notation
        int firstCoordinate = 0;
        int secondCoordinate = 0;
        for(int i = 0; i < letterLocs.length; i++){
            if(x == letterLocs[i]){
                firstCoordinate = i;
            }
            if(y == numberLocs[i]){
                secondCoordinate = i;
            }

        }
        System.out.println("first coordinate is: " + firstCoordinate);//97
        System.out.println("second coordinate is: " + secondCoordinate);//55
        // if a7 return 0,1
        return new Square(firstCoordinate, secondCoordinate);
        //return new Square(1,0);//can use .row or .column



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
    public static void update2DArrayChessboard(char[][] chessboard, final String sanFromSquare, final String sanToSquare) {
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
    public static int sanTo2DRow (final String sanSquare) {
        return (8 - ((int) sanSquare.charAt(1)) % 48);    // ASCII char (48) is '0'
    }

    /**
     * Transforms a given SAN string coordinate (e.g. 'a1') to its corresponding
     *      2D chessboard array COLUMN coordinate (e.g. '0').
     *
     * @param sanSquare A USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @return The given chess square COLUMN within the 2D chessboard array. (e.g. '0')
     */
    public static int sanTo2DCol (final String sanSquare) {
        return ((int) sanSquare.toLowerCase().charAt(0)) % 97;     // ASCII chart (97) is 'a'
    }

    /**
     * Given a SAN coordinate (e.g. 'a4'), will return the contents of the proper
     *      2D array location (chess square).
     *
     * @param sanSquare The chessboard square, in SAN, to get the 2D array coordinates of. (e.g. 'a1')
     * @return The contents of the given chess square; '0' if nothing exists on that square.
     */
    public static char get2DArrayChessboardPiece(char[][] chessboard, final String sanSquare) {

        return chessboard[sanTo2DRow(sanSquare)][sanTo2DCol(sanSquare)];
    }
}

