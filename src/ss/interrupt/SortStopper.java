package ss.interrupt;

public class SortStopper {
    private volatile boolean stopRequested = false;
    private volatile boolean isSorting = false;

    private static final SortStopper instance = new SortStopper();

    private SortStopper() {
    }

    public static void sortStarted() {
        instance.stopRequested = false;
        instance.isSorting = true;
    }

    public static void sortStopped() {
        synchronized (instance) {
            instance.isSorting = false;
            instance.notify();
        }
    }

    public static void checkStopRequested() {
        if (instance.stopRequested) {
            throw new SortStopException();
        }
    }

    public static void requestStop() {
        instance.stopRequested = true;
    }

    public static void waitForStop() {
        instance.waitForStopSynchronized();
    }

    private synchronized void waitForStopSynchronized() {
        while (isSorting) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean isSorting() {
        return instance.isSorting;
    }
}
