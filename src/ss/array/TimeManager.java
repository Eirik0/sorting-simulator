package ss.array;

import gt.gameloop.TimeConstants;
import ss.interrupt.SortStopper;

public class TimeManager {
    private volatile double timeAllotted = 0;

    private static final TimeManager instance = new TimeManager();

    private TimeManager() {
    }

    public static void addTime(double dt) {
        instance.timeAllotted += dt;
        synchronized (instance) {
            instance.notify();
        }
    }

    public static void waitForTime(double timeUsed) {
        SortStopper.checkStopRequested();
        instance.waitForTimeSynchronized(timeUsed);
    }

    private synchronized void waitForTimeSynchronized(double timeUsed) {
        while (timeAllotted <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        timeAllotted -= timeUsed * TimeConstants.NANOS_PER_MILLISECOND;
    }

    public static void requestStop() {
        addTime(Double.MAX_VALUE);
    }

    public static void reset() {
        instance.timeAllotted = 0;
    }
}
