package ss.algorithm;

import ss.array.Memory;
import ss.array.SArray;
import ss.array.SArray.ArrayType;
import ss.array.SInteger;

public class InsertionSortRootTuples implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Insertion Sort (Root Tuples)";
    }

    @Override
    public void sort(SArray array) {
        insertionSort(array, array.length());
    }

    private static void insertionSort(SArray array, int length) {
        if (length < 5) {
            InsertionSort.insertionSort(array, 0, length - 1);
            return;
        }
        int sqrtLen = (int) Math.sqrt(length);
        SArray workingArray = new SArray(ArrayType.EMPTY, sqrtLen);
        int startIndex = 0;
        while (startIndex < length) {
            int numToInsert = 0;
            for (; numToInsert < sqrtLen && startIndex + numToInsert < length; ++numToInsert) {
                workingArray.set(numToInsert, array.get(startIndex + numToInsert));
            }
            insertionSort(workingArray, numToInsert);
            int i = startIndex;
            do {
                SInteger toInsert = workingArray.get(--numToInsert);
                for (; i > 0; --i) {
                    SInteger element = array.get(i - 1);
                    if (array.compare(element, toInsert) < 0) {
                        break;
                    }
                    array.set(i + numToInsert, element);
                }
                array.set(i + numToInsert, toInsert);
                ++startIndex;
            } while (numToInsert > 0);
        }
        Memory.deallocateLast();
    }
}
