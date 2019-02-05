package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class SelectionSortTwoMinimums implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Selection Sort (Two Mins)";
    }

    @Override
    public void sort(SArray array) {
        int endIndex = array.length() - 1;
        for (int startIndex = 0; startIndex < endIndex - 1; startIndex += 2) {
            int firstIndex = startIndex;
            int secondIndex = startIndex + 1;
            SInteger[] firstAndSecond = SortingAlgorithm.sortPair(array, firstIndex, secondIndex, true);
            SInteger first = firstAndSecond[0];
            SInteger second = firstAndSecond[1];
            for (int i = startIndex + 2; i <= endIndex; ++i) {
                SInteger element = array.get(i);
                if (array.compare(second, element) > 0) {
                    if (array.compare(first, element) > 0) {
                        secondIndex = firstIndex;
                        firstIndex = i;
                        second = first;
                        first = element;
                    } else {
                        secondIndex = i;
                        second = element;
                    }
                }
            }
            if (secondIndex != startIndex + 1) {
                array.set(secondIndex, array.get(startIndex + 1));
                array.set(startIndex + 1, second);
                if (firstIndex != startIndex) {
                    array.set(firstIndex, array.get(startIndex));
                    array.set(startIndex, first);
                }
            }
        }
        SortingAlgorithm.sortPair(array, array.length() - 2, array.length() - 1, true);
    }
}
