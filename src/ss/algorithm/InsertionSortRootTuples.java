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
        insertionSort(array);
    }

    private static void insertionSort(SArray array) {
        if (array.length() < 5) {
            InsertionSort.insertionSort(array, 0, array.length() - 1);
            return;
        }
        int sqrtLen = (int) Math.sqrt(array.length());
        SArray workingArray = new SArray(ArrayType.EMPTY, sqrtLen);
        int startIndex = 0;
        while (startIndex < array.length()) {
            int numToInsert = 0;
            for (; numToInsert < sqrtLen && startIndex + numToInsert < array.length(); ++numToInsert) {
                workingArray.copy(numToInsert, array.get(startIndex + numToInsert));
            }
            insertionSort(workingArray);
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
                array.copy(i + numToInsert, toInsert);
                ++startIndex;
            } while (numToInsert > 0);
        }
        Memory.deallocateLast();
    }
}
