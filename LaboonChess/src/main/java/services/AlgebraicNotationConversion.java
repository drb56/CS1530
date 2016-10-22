package services;

import static java.lang.String.valueOf;

/**
 * Created by Craig on 10/22/2016.
 */
public class AlgebraicNotationConversion {
    public static String getTranslate(int x, int y){
        if (x < 0 || y > 7){
            System.out.println("bad coordinates");
            return null;
        }
        else{
            char[] letterLocs = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
            char[] numberLocs = new char[]{'8', '7', '6', '5', '4', '3', '2', '1'};
            String algebraicNotation = "";
            algebraicNotation = String.valueOf(letterLocs[x]) + String.valueOf(numberLocs[y]);
            System.out.println(algebraicNotation);
            return algebraicNotation;
        }
    }
}

