package ss.interrupt;

public class SortStopper {
    private volatile boolean stopRequested = false;

    private static final SortStopper instance = new SortStopper();

    private SortStopper() {
    }

    public static void sortStarted() {
        instance.stopRequested = false;
    }

    public static void checkStopRequested() {
        if (instance.stopRequested) {
            throw new SortStopException();
        }
    }

    public static void requestStop() {
        instance.stopRequested = true;
    }
}
