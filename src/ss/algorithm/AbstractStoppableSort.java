package ss.algorithm;

import ss.array.SortStopException;
import ss.array.SortableArray;

public abstract class AbstractStoppableSort implements SortingAlgorithm {
    private volatile boolean stopRequested = false;
    private volatile boolean complete = false;

    @Override
    public void sort(SortableArray array) {
        stopRequested = false;
        complete = false;
        sortImpl(array);
    }

    public abstract void sortImpl(SortableArray array);

    protected void checkStopRequested() {
        if (stopRequested) {
            stopRequested = false;
            throw new SortStopException();
        }
    }

    @Override
    public void requestStop() {
        if (!complete) {
            stopRequested = true;
        }
    }
}
