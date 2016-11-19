//import java.awt.*;
//import java.util.Scanner;
//import java.util.Timer;
//import java.util.TimerTask;
//
//public class Something {
//    static Toolkit toolkit;
//    static int counter = 0;
//    static Timer timer;
//    static int seconds = 0;
//
////    public synchronized void Something(int seconds) {
////        toolkit = Toolkit.getDefaultToolkit();
////        timer = new Timer();
////        timer.schedule(new SomethingTask(), seconds * 5);
////    }
//
////    static class SomethingTask extends TimerTask {
////        public void run() {
////            System.out.println("Time's up!");
////            toolkit.beep();
////            counter++;
////            System.out.println("Counter is " + counter);
////            System.exit(0);
////        }
////    }
//
//    public static void main(String args[]) {
//        System.out.println("About to schedule task.");
//        Scanner sc = new Scanner(System.in);
//        String text= sc.nextLine();
//        while(!text.equals("q")){
//            toolkit = Toolkit.getDefaultToolkit();
//            timer = new Timer();
//            timer.schedule(beep(), 5, 5);
//
////            timer.schedule(new SomethingTask(), seconds * 5);
//        }
//
//
//        System.out.println("Task scheduled.");
//    }
//
//
//}
