package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class InsertionSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public void sort(SortableArray array) {
        insertionSort(array, 0, array.length() - 1);
    }

    public static void insertionSort(SortableArray array, int low, int high) {
        for (int startIndex = low + 1; startIndex <= high; ++startIndex) {
            int i = startIndex;
            SortableElement toInsert = array.get(i);
            for (; i > 0; --i) {
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
