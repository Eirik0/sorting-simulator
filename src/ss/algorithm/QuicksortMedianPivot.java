package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class QuicksortMedianPivot implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Quicksort (Median Pivot)";
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
        SInteger pivot = getMedianPivot(array, startIndex, endIndex);
        int leftIndex = startIndex + 1;
        int rightIndex = endIndex - 1;
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

    private static SInteger getMedianPivot(SArray array, int startIndex, int endIndex) {
        int middleIndex = (startIndex + endIndex) / 2;
        SInteger low = array.get(startIndex);
        SInteger middle = array.get(middleIndex);
        SInteger high = array.get(endIndex);
        if (array.compare(low, high) > 0) {
            array.set(endIndex, low);
            array.set(startIndex, high);
            SInteger temp = high;
            high = low;
            low = temp;
        }
        if (array.compare(low, middle) > 0) {
            array.set(middleIndex, low);
            array.set(startIndex, middle);
            SInteger temp = middle;
            middle = low;
            low = temp;
        }
        if (array.compare(middle, high) > 0) {
            array.set(endIndex, middle);
            array.set(middleIndex, high);
            SInteger temp = high;
            high = middle;
            middle = temp;
        }
        return middle;
    }
}
