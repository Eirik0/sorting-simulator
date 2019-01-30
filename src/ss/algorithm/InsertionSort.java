package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class InsertionSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public void sort(SArray array) {
        insertionSort(array, 0, array.length() - 1);
    }

    public static void insertionSort(SArray array, int low, int high) {
        for (int startIndex = low + 1; startIndex <= high; ++startIndex) {
            int i = startIndex;
            SInteger toInsert = array.get(i);
            for (; i > 0; --i) {
                SInteger element = array.get(i - 1);
                if (array.compare(element, toInsert) < 0) {
                    break;
                }
                array.set(i, element);
            }
            array.set(i, toInsert);
        }
    }
}
