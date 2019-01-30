package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class SelectionSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public void sort(SArray array) {
        selectionSort(array, 0, array.length() - 1);
    }

    public static void selectionSort(SArray array, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; ++i) {
            selectMin(array, i, endIndex);
        }
    }

    public static SInteger selectMin(SArray array, int startIndex, int endIndex) {
        int minIndex = startIndex;
        SInteger minimum = array.get(startIndex);
        for (int i = startIndex + 1; i <= endIndex; ++i) {
            SInteger element = array.get(i);
            if (array.compare(minimum, element) > 0) {
                minIndex = i;
                minimum = element;
            }
        }
        if (minIndex != startIndex) {
            array.set(minIndex, array.get(startIndex));
            array.set(startIndex, minimum);
        }
        return minimum;
    }
}
