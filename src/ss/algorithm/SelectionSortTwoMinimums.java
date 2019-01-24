package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class SelectionSortTwoMinimums implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Selection Sort (Two Mins)";
    }

    @Override
    public void sort(SortableArray array) {
        int endIndex = array.length() - 1;
        for (int startIndex = 0; startIndex < endIndex - 1; startIndex += 2) {
            int firstIndex = startIndex;
            int secondIndex = startIndex + 1;
            SortableElement[] firstAndSecond = sortPair(array, firstIndex, secondIndex, true);
            SortableElement first = firstAndSecond[0];
            SortableElement second = firstAndSecond[1];
            for (int i = startIndex + 2; i <= endIndex; ++i) {
                SortableElement element = array.get(i);
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
        sortPair(array, array.length() - 2, array.length() - 1, true);
    }
}
