package services;

/**
 * Created by davidbickford on 10/22/16.
 */
public class FENStringConversion
{
    public static String chessboardToFEN(char[][] chessboard){
        char[] letterLocs = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        char[] numberLocs = new char[]{'8', '7', '6', '5', '4', '3', '2', '1'};
        String currBoard = "";

        for(int i=0; i<chessboard.length; i++){
            for(int j=0; j<chessboard.length; j++){
                if(chessboard[i][j] != 0){
                    currBoard = currBoard + chessboard[i][j] + letterLocs[j] + numberLocs[i] + " ";
                }
            }
        }
        return currBoard;
    }
}
