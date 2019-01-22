package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class Quicksort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Quicksort";
    }

    @Override
    public void sort(SortableArray array) {
        quickSort(array, 0, array.length() - 1);
    }

    private static void quickSort(SortableArray array, int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            int pivotIndex = partition(array, startIndex, endIndex);
            quickSort(array, startIndex, pivotIndex);
            quickSort(array, pivotIndex + 1, endIndex);
        }
    }

    public static int partition(SortableArray array, int startIndex, int endIndex) {
        SortableElement pivot = array.get((startIndex + endIndex) / 2);
        int leftIndex = startIndex;
        int rightIndex = endIndex;
        for (;;) {
            SortableElement left = array.get(leftIndex);
            while (array.compare(left, pivot) < 0) {
                left = array.get(++leftIndex);
            }
            SortableElement right = array.get(rightIndex);
            while (array.compare(right, pivot) > 0) {
                right = array.get(--rightIndex);
            }
            if (leftIndex >= rightIndex) {
                return rightIndex;
            }
            array.set(leftIndex, right);
            array.set(rightIndex, left);
            ++leftIndex;
            --rightIndex;
        }
    }
}
