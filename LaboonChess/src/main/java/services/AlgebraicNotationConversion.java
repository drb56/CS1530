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
            //System.out.println(algebraicNotation);
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
        return new Square(firstCoordinate, secondCoordinate);
        //return new Square(1,0);//can use .row or .column



    }


}

