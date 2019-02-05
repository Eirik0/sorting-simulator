package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class InsertionSortPair implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Insertion Sort (Pairs)";
    }

    @Override
    public void sort(SArray array) {
        int startIndex = 1;
        for (; startIndex <= array.length() - 2; startIndex += 2) {
            int i = startIndex;
            SInteger[] firstAndSecond = SortingAlgorithm.sortPair(array, i, i + 1, false);
            SInteger first = firstAndSecond[0];
            SInteger second = firstAndSecond[1];
            for (; i > 0; --i) {
                SInteger element = array.get(i - 1);
                if (array.compare(element, second) < 0) {
                    break;
                }
                array.set(i + 1, element);
            }
            array.set(i + 1, second);
            for (; i > 0; --i) {
                SInteger element = array.get(i - 1);
                if (array.compare(element, first) < 0) {
                    break;
                }
                array.set(i, element);
            }
            array.set(i, first);
        }
        InsertionSort.insertionSort(array, array.length() - 2, array.length() - 1);
    }
}
