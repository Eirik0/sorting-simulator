package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class SelectionSortLeftRight implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Selection Sort (Left/Right)";
    }

    @Override
    public void sort(SortableArray array) {
        int leftIndex = 0;
        int rightIndex = array.length() / 2;
        int endIndex = array.length() - 1;

        SelectionSort.selectMin(array, 0, rightIndex - 1);
        SelectionSort.selectMin(array, rightIndex, endIndex);

        SortableElement left = array.get(leftIndex);
        SortableElement right = array.get(rightIndex);

        boolean even = false;
        for (;;) {
            boolean minRight = array.compare(left, right) > 0;
            if (minRight) {
                SortableElement temp = array.get(leftIndex + 1);
                array.set(leftIndex, right);
                array.set(leftIndex + 1, left);
                array.set(rightIndex, temp);
            } else if (even) {
                array.set(rightIndex, array.get(rightIndex + 1));
                array.set(rightIndex + 1, right);
            }
            ++leftIndex;
            if (leftIndex == rightIndex - 1) {
                break;
            }
            if (even) {
                ++rightIndex;
            }
            if (minRight) {
                right = SelectionSort.selectMin(array, rightIndex, endIndex);
            } else {
                left = SelectionSort.selectMin(array, leftIndex, rightIndex - 1);
            }
            even = !even;
        }

        SelectionSort.selectionSort(array, leftIndex, endIndex);
    }
}
