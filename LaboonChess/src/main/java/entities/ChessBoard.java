package entities;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.move.MoveGenerator;
import com.github.bhlangonijr.chesslib.move.MoveGeneratorException;
import com.github.bhlangonijr.chesslib.move.MoveList;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;

public class ChessBoard {
    private char[][] chessboard;    /* holds the contents of the chessboard (pnrbkq | PNRBKQ | null) */
    private String lastFen;         /* holds the last FEN string representing the 2D chessboard array */
    private String boardFen;        /* holds the beginning portion of the FEN string that has the locations of each piece */
    private int turn;               /* holds which team is to go next (0=white 1=black) */
    private String castling = "";
    private ArrayList<String> allFenStrings;

    public enum returnStatus {
        INVALID, VALID, CHECKMATE, CASTLING, ENPASSANT, PAWNPROMOTION;
    }


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
        castling = " KQkq";
        turn = 0;
        allFenStrings = new ArrayList<>();
        lastFen = toFEN();
    }


    /**
     * returns the 2D chessboard array.
     *
     * @return A 2D representation of the state of the chessboard.
     */
    public char[][] getBoardState() {
        return chessboard;
    }

    /**
     * Gets the list of FEN string moves performed during
     *      the course of the chess game.
     *
     * @return A list of all FEN chess moves made during the course of the game
     */
    public ArrayList<String> getFenList() {
        return allFenStrings;
    }


    /**
     * Creates a ChessBoard from a FEN string. Used for loading
     *      a game; does not have a history of how the chess board
     *      came to be like this.
     *
     * @param fenString A valid FEN string.
     */
    public ChessBoard(String fenString) {
        ChessBoardConstructor(fenString);
    }


    /**
     * Undoes the previous move by the player by removing the last fen string
     *      from the allFenStrings ArrayList. If the size of the list is 0
     *      after removing the fen string then it resets the board and list.
     *
     * @param playerType '0' if user; otherwise CPU
     */
    public void undoMove(int playerType) {
        if(allFenStrings.size() != 0) {
            if (playerType > 0) {
                allFenStrings.remove(allFenStrings.size() - 1);
            }
            allFenStrings.remove(allFenStrings.size() - 1);

            if (allFenStrings.size() == 0) {
                chessboard = new char[][]{
                        {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'},
                        {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0},
                        {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'},
                        {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}
                };
                turn = 0;
                lastFen = toFEN();
            } else {
                ChessBoardConstructor(allFenStrings.get(allFenStrings.size() - 1));
                lastFen = allFenStrings.get(allFenStrings.size() - 1);
            }
        }
    }


    /**
     * Creates a ChessBoard from a FEN string history. Used for
     *      loading a game and maintaining the history associated
     *      with the loaded game.
     *
     * @param fenList an arrayList of fen strings.
     */
    public ChessBoard(ArrayList<String> fenList) {
        // first build the history of the chess board
        this.allFenStrings = new ArrayList<>();
        for(int i=0; i<fenList.size(); i++) {
            this.allFenStrings.add(fenList.get(i));
        }

        // grab the current state of the chess board
        String fenString = allFenStrings.get(allFenStrings.size()-1);

        // complete construction of class using current state of the board
        ChessBoardConstructor(fenString);
    }


    /**
     * Finalizes construction of the class; used if FEN string was given
     *      as an argument.
     *
     * @param fenString A valid FEN string representing the current state of the chess board.
     */
    private void ChessBoardConstructor(String fenString) {
        // passed-in string becomes the "last" FEN for the new board
        this.lastFen = fenString;

        // split the FEN string to get just the board information
        String[] fenArray = fenString.split(" ");
        String[] fenBeginning = fenArray[0].split("/");
        this.boardFen = fenArray[0];

        // populate the chess board
        this.chessboard = new char[8][8];
        populateBoard(fenBeginning);

        // obtain the castling information (if it exists)
        if (fenArray.length > 2) {
            castling = " " + fenArray[2];
        }

        // determine who's turn it is
        if (fenArray[1].equals("w")) {
            this.turn = 0;
        } else {
            this.turn = 1;
        }
    }


    /**
     * Saves all the fen strings since the beginning of the game to be able to repopulate the board
     *
     * @param file A file on disk to use for saving.
     * @param playerType If it is multiplayer, comp black, or comp white.
     * @param timer_count The timer that it was saved as.
     * @return true if save was successful; otherwise false.
     */
    public boolean saveGame(File file, int playerType, int timer_count) {
        try {
            PrintStream out = new PrintStream(file.getAbsoluteFile());
            out.println(playerType);
            out.println(timer_count);
            for(int i=0; i<allFenStrings.size(); i++) {
                out.println(allFenStrings.get(i));
            }
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }


    /**
     * Adds a fen string to the history
     * @param fen the string to add
     */
    public void addToHistory(String fen) {
        allFenStrings.add(fen);
    }


    /**
     * Populates the chessboard with the values that should be there
     *
     * @param fen array of each row in the board
     */
    private void populateBoard(String[] fen) {
        int rowNum;
        for(int i=0; i<fen.length; i++) {
            char[] row = fen[i].toCharArray();
            rowNum = 0;

            for(int j=0; j<row.length; j++) {
                if(tryParseInt(Character.toString(row[j]))) {
                    int numEmptySpaces = Integer.parseInt(Character.toString(row[j]));
                    for(int k=0; k<numEmptySpaces; k++) {
                        chessboard[i][rowNum] = 0;
                        rowNum++;
                    }
                }
                else {
                    chessboard[i][rowNum] = row[j];
                    rowNum++;
                }
            }
        }
    }


    /**
     * checking if the current character can be parsed into an int or not
     *
     * @param value the value to be parsed
     * @return true if it can be parsed, false otherwise
     */
    private boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    /**
     * Determines if a chess move is a valid/legal move, given a
     *      starting and ending chess square in SAN notation (e.g. "a1")
     *
     * @param sanFrom The starting USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @param sanTo The starting USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'd4')
     * @return 1 if the move was valid; 0 if the move was invalid; 2 if Checkmate.
     */
    public int isLegal(String sanFrom, String sanTo) {
        Board board = new Board();
        board.loadFromFEN(lastFen);
        MoveList moves = null;

        try {
            moves = MoveGenerator.getInstance().generateLegalMoves(board);
            String actualMove = sanFrom + sanTo;

            // see if checkmate has occurred
            if (moves.size() == 0) { return 2; }

            // performs lazy matching to see if the actualMove
            //      is contained within the list of legal moves
            if (moves.toString().contains(actualMove)) {
                return 1;
            } else {
                return 0;
            }
        } catch (MoveGeneratorException e) {
            e.printStackTrace();
        }

        return 0;
    }


    /**
     * Given a starting chess square and ending chess square, will determine if
     *      the move is a valid chess move. If valid, then the move is performed;
     *      otherwise the chess board stays the same.
     *
     * @param sanFrom The starting USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'a1')
     * @param sanTo The starting USCF chessboard coordinate that corresponds to a given chess square. (e.g. 'd4')
     * @return 0 if the move was invalid; 1+ if the move was valid; 2 if checkmate occurred; 3 if castling occurred; 4 if en passant occurred.
     */
    public returnStatus move(String sanFrom, String sanTo) {
        // keep status of the potential move
        returnStatus status = returnStatus.INVALID;
        int isLegal = isLegal(sanFrom, sanTo);
        if (isLegal == 0) {                              // returns false if isn't a legal move
            System.out.println("ILLEGAL MOVE");
            return status;

        } else if (isLegal == 1) {                       // changes the board if it is a legal move
            System.out.println("LEGAL MOVE");
            status = returnStatus.VALID;

            // remove castling if rooks or kings changed
            switch (sanFrom) {
                case "a1":
                    castling = castling.replaceAll("Q", "");
                    break;
                case "h1":
                    castling = castling.replaceAll("K", "");
                    break;
                case "a8":
                    castling = castling.replaceAll("q", "");
                    break;
                case "h8":
                    castling = castling.replaceAll("k", "");
                    break;
            }

            /**
             * Check if the KING has moved two spaces instead of
             *      one; this means a castling move was performed:
             *
             * Take the old location of the king and move the rook
             *      on that side to where the king used to be.
             */
            if (castling.matches(".*(K|Q|k|q).*")) {
                // check for moving of the rooks, and remove castling feature for that rook
                if (sanFrom.equals("a1") && chessboard[7][0] == 'R') {
                    // remove castling for white, queen-side
                    castling = castling.replaceAll("Q", "");
                } else if (sanFrom.equals("h1") && chessboard[7][7] == 'R') {
                    // remove castling for white, king-side
                    castling = castling.replaceAll("K", "");
                } else if (sanFrom.equals("a8") && chessboard[0][0] == 'r') {
                    // remove castling for black, queen-side
                    castling = castling.replaceAll("q", "");
                } else if (sanFrom.equals("h8") && chessboard[0][7] == 'r') {
                    // remove castling for black, king-side
                    castling = castling.replaceAll("k", "");
                }

                // check for moving of the king
                if ((sanFrom.equals("e1") && chessboard[7][4] == 'K') || (sanFrom.equals("e8") && chessboard[0][4] == 'k')) {
                    // a king is being moved. if it's moved two spaces
                    //      then its a castling action
                    if (sanTo.contains("c") || sanTo.contains("g")) {
                        // king is moved two spaces, complete the castling
                        //      by changing the FEN string to reflect
                        status = returnStatus.CASTLING;

                        // perform the king's move first
                        update2DArrayChessboard(sanFrom, sanTo);

                        // if the king moves then no more castling can occur
                        //      for that team
                        if (sanFrom.equals("e1")) {
                            castling = castling.replaceAll("(K|Q)", "");
                        } else {
                            castling = castling.replaceAll("(k|q)", "");
                        }

                        // castling for White
                        if (sanFrom.equals("e1") && sanTo.contains("g")) {  // king-side
                            sanFrom = "h1";
                            sanTo = "f1";
                        } else if (sanFrom.equals("e1") && sanTo.contains("c")) {  // queen-side
                            sanFrom = "a1";
                            sanTo = "d1";
                        }

                        // castling for Black
                        if (sanFrom.equals("e8") && sanTo.contains("g")) {   // king-side
                            sanFrom = "h8";
                            sanTo = "f8";
                        } else if (sanFrom.equals("e8") && sanTo.contains("c")) {   // queen-side
                            sanFrom = "a8";
                            sanTo = "h8";
                        }
                    } else if ((sanFrom.equals("e1") && chessboard[7][4] == 'K')) {
                        // remove white castling
                        castling = castling.replaceAll("[K|Q]", "");
                    } else {
                        // remove black castling
                        castling = castling.replaceAll("[k|q]", "");
                    }

                    // check if castling is complete for both sides; if so,
                    //      then mark it as '-' to show this in the FEN string.
                    if (castling.equals(" ")) {
                        castling = " -";
                    }
                }
            }

            // update the chessboard array
            update2DArrayChessboard(sanFrom, sanTo);

            /**
             * Check if a pawn has been moved all the way across
             *      the board; if it has, then Promote it to Queen.
             */
            if ((turn == 0) && (sanTo.contains("8") && chessboard[sanTo2DRow(sanTo)][sanTo2DCol(sanTo)] == 'P')
                    || (turn == 1) && (sanTo.contains("1") && chessboard[sanTo2DRow(sanTo)][sanTo2DCol(sanTo)] == 'p')) {

                // return that a pawn promotion has occurred
                status = returnStatus.PAWNPROMOTION;

                // update the board to reflect the change from pawn to Queen
                if (turn == 0) {
                    chessboard[sanTo2DRow(sanTo)][sanTo2DCol(sanTo)] = 'Q';     // white queen
                } else {
                    chessboard[sanTo2DRow(sanTo)][sanTo2DCol(sanTo)] = 'q';     // black queen
                }
            }

            if (turn == 0) {
                turn = 1;
            } else {
                turn = 0;
            }

            // DEBUG
            this.printBoard();

            // update the FEN string
            lastFen = toFEN();
        } else {
            // CHECKMATE
            status = returnStatus.CHECKMATE;
        }

        return status;
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
     * Reverses the beginning of the FEN string to allow for board reversal
     *
     * @return String representing the reversed board
     */
    public String reverseFEN() {
        return new StringBuilder(new String(boardFen.toCharArray())).reverse().toString();
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
        fenBoard = generateFEN(fenBoard.toCharArray());
        boardFen = fenBoard;
        fenBoard = fenBoard + " " + turn();
        fenBoard = fenBoard + "" + castling;

        return fenBoard;
    }


    /**
     * This method will take in an array which represents the board state and correctly format it
     *      into a FEN string.
     *
     * @param fenBoardArray - Array of a String which represents a board, will give a FEN string
     * @return Correctly formatted FEN String
     */
    private String generateFEN(char[] fenBoardArray){
        String fenBoard = "";
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
        return fenBoard;
    }


    /**
     * Returns, in character form, which team's turn it is to make a chess move.
     *
     * @return 'w' if it is white's turn; otherwise 'b' for black's turn.
     */
    public char turn(){
        if (turn == 0) {
            return 'w';
        } else {
            return 'b';
        }
    }

    /**
     * Sets the turn to a different player
     * @param turn
     */
    public void setTurn(int turn) {
        this.turn = turn;
    }


    /**
     * Pretty-prints the current state of the 2D chessboard array.
     */
    public void printBoard() {
        System.out.println("   A   B   C   D   E   F   G   H ");

        int boardNum=8;
        for (int x = 0; x < chessboard.length; x++) {
            System.out.print(String.format("%d ", boardNum));

            char [] row = chessboard[x];
            for (int y = 0; y < row.length; y++) {
                char val = row[y];
                System.out.print(String.format("[%s] ", val==0 ? " " : val));
            }

            System.out.println(String.format("%d", boardNum--));
        }
        System.out.println("   A   B   C   D   E   F   G   H ");
    }


    /**
     * Generates a standard algebraic notation (SAN) string given
     *      a row and column of the chess board.
     *
     * @param row A valid row number within the chess board.
     * @param col A valid column number within the chess board.
     *
     * @return The SAN string representing the row and column.
     */
    public String indexToSan(int row, int col) {
        row = (row + 7 - (2* row)) + 1;
        return "" + Character.toString((char)(col + 97)) +  Integer.toString(row);

    }
}
