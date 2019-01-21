package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;
import ss.interrupt.SortStopper;

public class InsertionSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public void sort(SortableArray array) {
        for (int startIndex = 1; startIndex < array.length(); ++startIndex) {
            int i = startIndex;
            SortableElement toInsert = array.get(i);
            for (; i > 0; --i) {
                SortStopper.checkStopRequested();
                SortableElement element = array.get(i - 1);
                if (array.compare(element, toInsert) < 0) {
                    break;
                }
                array.set(i, element);
            }
            array.set(i, toInsert);
        }
    }
}
