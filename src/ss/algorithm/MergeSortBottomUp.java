package ss.algorithm;

import ss.array.SArray;
import ss.array.SArray.ArrayType;

public class MergeSortBottomUp implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Merge Sort (Bottom Up)";
    }

    @Override
    public void sort(SArray array) {
        SArray arrayPtr = array;
        SArray workingArray = new SArray(ArrayType.EMPTY, array.length());
        for (int width = 1; width < array.length(); width = 2 * width) {
            for (int i = 0; i < array.length(); i = i + 2 * width) {
                int middleIndex = Math.min(i + width, array.length() - 1);
                int endIndex = Math.min(i + 2 * width, array.length());
                MergeSort.merge(workingArray, array, i, middleIndex, endIndex);
            }
            SArray tempArray = array;
            array = workingArray;
            workingArray = tempArray;
        }
        if (array != arrayPtr) {
            for (int i = 0; i < array.length(); ++i) {
                workingArray.copy(i, array.get(i));
            }
        }
    }
}
