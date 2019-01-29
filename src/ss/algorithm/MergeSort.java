package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableArray.ArrayType;
import ss.array.SortableElement;

public class MergeSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public void sort(SortableArray array) {
        SortableArray workingArray = new SortableArray(ArrayType.EMPTY, array.length());
        for (int i = 0; i < array.length(); ++i) {
            workingArray.copy(i, array.get(i));
        }
        mergeSort(array, workingArray, 0, array.length());
    }

    private static void mergeSort(SortableArray array, SortableArray workingArray, int startIndex, int endIndex) {
        if (endIndex - startIndex >= 2) {
            int middleIndex = (startIndex + endIndex) / 2;

            mergeSort(workingArray, array, startIndex, middleIndex);
            mergeSort(workingArray, array, middleIndex, endIndex);

            int leftIndex = startIndex;
            int rightIndex = middleIndex;
            SortableElement left = workingArray.get(leftIndex);
            SortableElement right = workingArray.get(rightIndex);

            for (int i = startIndex; i < endIndex; ++i) {
                if (leftIndex < middleIndex && (rightIndex >= endIndex || array.compare(left, right) <= 0)) {
                    array.copy(i, left);
                    ++leftIndex;
                    left = workingArray.get(leftIndex);
                } else {
                    array.copy(i, right);
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
