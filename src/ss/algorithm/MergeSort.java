package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class MergeSort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public void sortImpl(SortableArray array) {
        SortableArray workingArray = array.allocateNew();
        for (int i = 0; i < array.length(); ++i) {
            workingArray.set(i, array.get(i));
        }
        mergeSort(array, workingArray, 0, array.length());
    }

    public void mergeSort(SortableArray array, SortableArray workingArray, int start, int end) {
        checkStopRequested();
        if (end - start >= 2) {
            int middle = (start + end) / 2;

            mergeSort(workingArray, array, start, middle);
            mergeSort(workingArray, array, middle, end);

            int i = start;
            int j = middle;
            SortableElement left = workingArray.get(i);
            SortableElement right = workingArray.get(j);

            for (int k = start; k < end; ++k) {
                checkStopRequested();
                if (i < middle && (j >= end || array.compare(left, right) <= 0)) {
                    array.set(k, left);
                    ++i;
                    left = workingArray.get(i);
                } else {
                    array.set(k, right);
                    ++j;
                    if (j >= end) {
                        continue;
                    }
                    right = workingArray.get(j);
                }
            }
        }
    }
}
