package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class Quicksort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Quicksort";
    }

    @Override
    public void sortImpl(SortableArray array) {
        quickSort(array, 0, array.length() - 1);
    }

    private void quickSort(SortableArray array, int startIndex, int endIndex) {
        checkStopRequested();
        if (startIndex < endIndex) {
            SortableElement pivot = array.get((startIndex + endIndex) / 2);
            int leftIndex = startIndex;
            int rightIndex = endIndex;
            while (leftIndex <= rightIndex) {
                SortableElement left = array.get(leftIndex);
                while (array.compare(left, pivot) < 0) {
                    ++leftIndex;
                    left = array.get(leftIndex);
                }
                SortableElement right = array.get(rightIndex);
                while (array.compare(right, pivot) > 0) {
                    --rightIndex;
                    right = array.get(rightIndex);
                }
                if (leftIndex <= rightIndex) {
                    array.set(leftIndex, right);
                    array.set(rightIndex, left);
                    ++leftIndex;
                    --rightIndex;
                }
            }
            quickSort(array, startIndex, rightIndex);
            quickSort(array, leftIndex, endIndex);
        }
    }
}
