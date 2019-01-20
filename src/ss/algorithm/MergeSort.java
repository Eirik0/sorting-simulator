package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;
import ss.interrupt.SortStopper;

public class MergeSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public void sort(SortableArray array) {
        SortableArray workingArray = array.allocateNew();
        for (int i = 0; i < array.length(); ++i) {
            workingArray.set(i, array.get(i));
        }
        mergeSort(array, workingArray, 0, array.length());
    }

    private static void mergeSort(SortableArray array, SortableArray workingArray, int startIndex, int endIndex) {
        SortStopper.checkStopRequested();
        if (endIndex - startIndex >= 2) {
            int middleIndex = (startIndex + endIndex) / 2;

            mergeSort(workingArray, array, startIndex, middleIndex);
            mergeSort(workingArray, array, middleIndex, endIndex);

            int leftIndex = startIndex;
            int rightIndex = middleIndex;
            SortableElement left = workingArray.get(leftIndex);
            SortableElement right = workingArray.get(rightIndex);

            for (int i = startIndex; i < endIndex; ++i) {
                SortStopper.checkStopRequested();
                if (leftIndex < middleIndex && (rightIndex >= endIndex || array.compare(left, right) <= 0)) {
                    array.set(i, left);
                    ++leftIndex;
                    left = workingArray.get(leftIndex);
                } else {
                    array.set(i, right);
                    ++rightIndex;
                    if (rightIndex >= endIndex) {
                        continue;
                    }
                    right = workingArray.get(rightIndex);
                }
            }
        }
    }
}
