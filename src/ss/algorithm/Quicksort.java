package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class Quicksort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Quicksort";
    }

    @Override
    public void sort(SArray array) {
        quickSort(array, 0, array.length() - 1);
    }

    private static void quickSort(SArray array, int startIndex, int endIndex) {
        if (startIndex < endIndex) {
            int pivotIndex = partition(array, startIndex, endIndex);
            quickSort(array, startIndex, pivotIndex);
            quickSort(array, pivotIndex + 1, endIndex);
        }
    }

    public static int partition(SArray array, int startIndex, int endIndex) {
        SInteger pivot = array.get((startIndex + endIndex) / 2);
        int leftIndex = startIndex;
        int rightIndex = endIndex;
        for (;;) {
            SInteger left = array.get(leftIndex);
            while (array.compare(left, pivot) < 0) {
                left = array.get(++leftIndex);
            }
            SInteger right = array.get(rightIndex);
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
