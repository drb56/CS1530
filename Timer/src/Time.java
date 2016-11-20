import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Craig on 11/15/2016.
 */
public class Time {
    static int counter = 0;
    static String[] s = {"Hurry up! You are taking too long!", "Yes, if you take longer, your IQ will go up",
            "God damn you are horrible", "Whatever you do, don't touch that rook!"};
    static Random ran = new Random();
    static TimerTask messageTask = new TimerTask() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            //Invoke your function here
            //counter++;
            String s_ran = s[ran.nextInt(s.length)];
//            System.out.println("The counter is " + counter);
            System.out.println(s_ran);
        }
    };

    static TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            counter++;
//            String s_ran = s[ran.nextInt(s.length)];
            System.out.println(counter);
//            System.out.println(s_ran);
        }
    };

    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 1*1000);
        timer.scheduleAtFixedRate(messageTask, 0, 5*1000);
    }
}
