package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;
import ss.interrupt.SortStopper;

public class BubbleSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public void sort(SortableArray array) {
        for (int endIndex = array.length() - 1; endIndex >= 0; --endIndex) {
            for (int i = 0; i < endIndex; ++i) {
                SortStopper.checkStopRequested();
                SortableElement left = array.get(i);
                SortableElement right = array.get(i + 1);
                if (array.compare(left, right) > 0) {
                    array.set(i, right);
                    array.set(i + 1, left);
                }
            }
        }
    }
}
