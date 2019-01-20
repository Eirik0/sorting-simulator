package ss.interrupt;

public class SortStopper {
    private volatile boolean stopRequested = false;
    private volatile boolean complete = false;

    private static final SortStopper instance = new SortStopper();

    private SortStopper() {
    }

    public static void sortStarted() {
        instance.stopRequested = false;
        instance.complete = false;
    }

    public static void sortFinished() {
        instance.complete = true;
    }

    public static void checkStopRequested() {
        if (instance.stopRequested) {
            throw new SortStopException();
        }
    }

    public static void requestStop() {
        if (!instance.complete) {
            instance.stopRequested = true;
        }
    }
}
