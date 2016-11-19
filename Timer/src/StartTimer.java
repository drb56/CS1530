/*
import java.awt.*;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
* Created by Craig on 11/15/2016.
* /
public class StartTimer {
static void startTimer(int delaySeconds) {
Executors.newSingleThreadScheduledExecutor().schedule(
runnable,
delaySeconds,
TimeUnit.SECONDS);
}

static Runnable runnable = () -> System.out.println("Hello, world!");

public static void main(String args[]) {
System.out.println("About to schedule task.");
Scanner sc = new Scanner(System.in);
String text= sc.nextLine();
while(!text.equals("q")){
startTimer(5 * 1000);
//            toolkit = Toolkit.getDefaultToolkit();
//            timer = new Timer();
//            timer.schedule(beep(), 5, 5);

//            timer.schedule(new SomethingTask(), seconds * 5);
}


System.out.println("Task scheduled.");
}
}
*/
