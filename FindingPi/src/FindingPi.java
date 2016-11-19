import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Craig on 11/17/2016.
 */
public class FindingPi {


    public static Double generateRand(){
        return null;
    }

    public static void main(String[] args) {

        //System.out.println(num);
        int thread = 4;
        int iterations = 100;

        int loop = iterations/thread;
        //random x and random y
        //double variable for sum of inside and sum of outside
        double sumInside = 0;
        for(int i = 0; i < loop; i++){
            //find random/test if inside circle...increment the particular sum
            double x = ThreadLocalRandom.current().nextDouble(0, 1);
            double y = ThreadLocalRandom.current().nextDouble(0, 1);
            if((Math.pow(x,2) + Math.pow(y, 2)) < 1){
                sumInside++;
                System.out.println(sumInside);
            }


        }
        double pi = sumInside/loop * 4;
        System.out.println("pi is " + pi);

    }
}
