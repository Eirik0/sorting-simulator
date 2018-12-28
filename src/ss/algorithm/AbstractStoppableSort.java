package ss.algorithm;

import ss.array.SortStopException;

public abstract class AbstractStoppableSort implements SortingAlgorithm {
    private volatile boolean stopRequested = false;

    protected void checkStopRequested() {
        if (stopRequested) {
            stopRequested = false;
            throw new SortStopException();
        }
    }

    @Override
    public void requestStop() {
        stopRequested = true;
    }
}
