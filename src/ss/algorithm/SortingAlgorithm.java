package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public interface SortingAlgorithm {
    String getName();

    void sort(SortableArray array);

    default SortableElement[] sortPair(SortableArray array, int firstIndex, int secondIndex, boolean set) {
        SortableElement first = array.get(firstIndex);
        SortableElement second = array.get(secondIndex);
        if (array.compare(first, second) > 0) {
            if (set) {
                array.set(firstIndex, second);
                array.set(secondIndex, first);
            }
            SortableElement temp = first;
            first = second;
            second = temp;
        }
        return new SortableElement[] { first, second };
    }
}
