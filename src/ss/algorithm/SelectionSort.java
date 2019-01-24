package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class SelectionSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public void sort(SortableArray array) {
        selectionSort(array, 0, array.length() - 1);
    }

    public static void selectionSort(SortableArray array, int startIndex, int endIndex) {
        for (int i = startIndex; i < endIndex; ++i) {
            selectMin(array, i, endIndex);
        }
    }

    public static SortableElement selectMin(SortableArray array, int startIndex, int endIndex) {
        int minIndex = startIndex;
        SortableElement minimum = array.get(startIndex);
        for (int i = startIndex + 1; i <= endIndex; ++i) {
            SortableElement element = array.get(i);
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
