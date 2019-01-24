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
            SortableElement[] firstAndSecond = sortPair(array, i, i + 1, false);
            SortableElement first = firstAndSecond[0];
            SortableElement second = firstAndSecond[1];
            for (; i > 0; --i) {
                SortableElement element = array.get(i - 1);
                if (array.compare(element, second) < 0) {
                    break;
                }
                array.set(i + 1, element);
            }
            array.set(i + 1, second);
            for (; i > 0; --i) {
                SortableElement element = array.get(i - 1);
                if (array.compare(element, first) < 0) {
                    break;
                }
                array.set(i, element);
            }
            array.set(i, first);
        }
        InsertionSort.insertionSort(array, array.length() - 2, array.length() - 1);
    }
}
