package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class InsertionSortBinarySearch implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Insertion Sort (Binary Search)";
    }

    @Override
    public void sort(SArray array) {
        for (int startIndex = 1; startIndex < array.length(); ++startIndex) {
            SInteger toInsert = array.get(startIndex);
            int pos = binarySearch(array, toInsert, startIndex);
            for (int i = startIndex; i > pos; --i) {
                SInteger element = array.get(i - 1);
                array.set(i, element);
            }
            array.set(pos, toInsert);
        }
    }

    private static int binarySearch(SArray array, SInteger toInsert, int elementIndex) {
        int leftIndex = 0;
        int rightIndex = elementIndex - 1;
        while (leftIndex <= rightIndex) {
            int middleIndex = (leftIndex + rightIndex) / 2;
            if (array.compare(array.get(middleIndex), toInsert) <= 0) {
                leftIndex = middleIndex + 1;
            } else {
                rightIndex = middleIndex - 1;
            }
        }
        return leftIndex;
    }
}
