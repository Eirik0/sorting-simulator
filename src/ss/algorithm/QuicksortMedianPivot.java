package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class QuicksortMedianPivot implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Quicksort (Median Pivot)";
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
        SortableElement pivot = getMedianPivot(array, startIndex, endIndex);
        int leftIndex = startIndex + 1;
        int rightIndex = endIndex - 1;
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

    private static SortableElement getMedianPivot(SortableArray array, int startIndex, int endIndex) {
        int middleIndex = (startIndex + endIndex) / 2;
        SortableElement low = array.get(startIndex);
        SortableElement middle = array.get(middleIndex);
        SortableElement high = array.get(endIndex);
        if (array.compare(low, high) > 0) {
            array.set(endIndex, low);
            array.set(startIndex, high);
            SortableElement temp = high;
            high = low;
            low = temp;
        }
        if (array.compare(low, middle) > 0) {
            array.set(middleIndex, low);
            array.set(startIndex, middle);
            SortableElement temp = middle;
            middle = low;
            low = temp;
        }
        if (array.compare(middle, high) > 0) {
            array.set(endIndex, middle);
            array.set(middleIndex, high);
            SortableElement temp = high;
            high = middle;
            middle = temp;
        }
        return middle;
    }
}
