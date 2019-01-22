package ss.algorithm;

import ss.array.SortableArray;

public class Introsort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Introsort";
    }

    @Override
    public void sort(SortableArray array) {
        int maxDepth = (int) Math.round(Math.log(array.length()) * 2);
        introsort(array, 0, array.length() - 1, maxDepth);
    }

    private static void introsort(SortableArray array, int startIndex, int endIndex, int maxDepth) {
        if (startIndex < endIndex) {
            if (maxDepth == 0) {
                Heapsort.heapsort(array, startIndex, endIndex);
            } else {
                int pivotIndex = Quicksort.partition(array, startIndex, endIndex);
                introsort(array, startIndex, pivotIndex, maxDepth - 1);
                introsort(array, pivotIndex + 1, endIndex, maxDepth - 1);
            }
        }
    }
}
