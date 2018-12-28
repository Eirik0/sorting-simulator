package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class Quicksort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Quicksort";
    }

    @Override
    public void sort(SortableArray array) {
        quickSort(array, 0, array.length() - 1);
    }

    private void quickSort(SortableArray array, int low, int high) {
        checkStopRequested();
        if (low < high) {
            SortableElement pivot = array.get((low + high) / 2);
            int left = low;
            int right = high;
            while (left <= right) {
                SortableElement e1 = array.get(left);
                while (array.compare(e1, pivot) < 0) {
                    ++left;
                    e1 = array.get(left);
                }
                SortableElement e2 = array.get(right);
                while (array.compare(e2, pivot) > 0) {
                    --right;
                    e2 = array.get(right);
                }
                if (left <= right) {
                    array.set(left, e2);
                    array.set(right, e1);
                    ++left;
                    --right;
                }
            }
            quickSort(array, low, right);
            quickSort(array, left, high);
        }
    }
}
