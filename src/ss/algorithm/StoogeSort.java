package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class StoogeSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Stooge Sort";
    }

    @Override
    public void sort(SortableArray array) {
        stoogeSort(array, 0, array.length() - 1);
    }

    private static void stoogeSort(SortableArray array, int startIndex, int endIndex) {
        SortableElement start = array.get(startIndex);
        SortableElement end = array.get(endIndex);
        if (array.compare(start, end) > 0) {
            array.set(startIndex, end);
            array.set(endIndex, start);
        }
        if (endIndex - startIndex > 1) {
            int oneThird = (endIndex + 1 - startIndex) / 3;
            stoogeSort(array, startIndex, endIndex - oneThird);
            stoogeSort(array, startIndex + oneThird, endIndex);
            stoogeSort(array, startIndex, endIndex - oneThird);
        }
    }
}
