package services;

/**
 * Created by davidbickford on 10/22/16.
 */
public class FENStringConversion {
    public static String chessboardToFEN(char[][] chessboard){
        char[] letterLocs = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
        char[] numberLocs = new char[]{'8', '7', '6', '5', '4', '3', '2', '1'};
        String currBoard = "";  //the current board in algebraic notation
        String fenBoard = "";   //the current board in FEN string notation

        for(int i=0; i<chessboard.length; i++){
            for(int j=0; j<chessboard.length; j++){
                if(chessboard[i][j] != 0){
                    currBoard = currBoard + chessboard[i][j] + letterLocs[j] + numberLocs[i] + " ";
                    fenBoard = fenBoard + chessboard[i][j];
                }
                else{
                    fenBoard = fenBoard + '0';
                }

            }
            if(i < 7){
                fenBoard = fenBoard + '/';
            }
        }
        char[] fenBoardArray = fenBoard.toCharArray();
        fenBoard = "";
        int onesNumber = 0;
        for(int i=0; i<fenBoardArray.length; i++){
            if(fenBoardArray[i] == '0'){
                onesNumber++;
                if(i == fenBoardArray.length-1){
                    fenBoard = fenBoard + onesNumber;
                }
            }
            else{
                if(onesNumber > 0){
                    fenBoard = fenBoard + onesNumber;
                }
                fenBoard = fenBoard + fenBoardArray[i];
                onesNumber = 0;
            }
        }
        return fenBoard;
    }
}
