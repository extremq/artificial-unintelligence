package game.util;

public class Time {
    static double secondsElapsed = 0.0f;
    static double deltaTimeSeconds = 0.0f;

    public static void updateSeconds(double newSeconds) {
        deltaTimeSeconds = newSeconds - secondsElapsed;
        secondsElapsed = newSeconds;
    }

    public static double getSecondsElapsed() {
        return secondsElapsed;
    }

    public static double getDeltaTimeSeconds() {
        return deltaTimeSeconds;
    }
}
