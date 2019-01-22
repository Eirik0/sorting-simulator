package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class InsertionSortPair implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Insertion Sort (Pairs)";
    }

    @Override
    public void sort(SortableArray array) {
        int startIndex = 1;
        for (; startIndex <= array.length() - 2; startIndex += 2) {
            int i = startIndex;
            SortableElement toInsertLeft = array.get(i);
            SortableElement toInsertRight = array.get(i + 1);
            if (array.compare(toInsertLeft, toInsertRight) > 0) {
                SortableElement temp = toInsertLeft;
                toInsertLeft = toInsertRight;
                toInsertRight = temp;
            }
            for (; i > 0; --i) {
                SortableElement element = array.get(i - 1);
                if (array.compare(element, toInsertRight) < 0) {
                    break;
                }
                array.set(i + 1, element);
            }
            array.set(i + 1, toInsertRight);
            for (; i > 0; --i) {
                SortableElement element = array.get(i - 1);
                if (array.compare(element, toInsertLeft) < 0) {
                    break;
                }
                array.set(i, element);
            }
            array.set(i, toInsertLeft);
        }
        InsertionSort.insertionSort(array, array.length() - 2, array.length() - 1);
    }
}
