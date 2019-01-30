package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public interface SortingAlgorithm {
    String getName();

    void sort(SArray array);

    default SInteger[] sortPair(SArray array, int firstIndex, int secondIndex, boolean set) {
        SInteger first = array.get(firstIndex);
        SInteger second = array.get(secondIndex);
        if (array.compare(first, second) > 0) {
            if (set) {
                array.set(firstIndex, second);
                array.set(secondIndex, first);
            }
            SInteger temp = first;
            first = second;
            second = temp;
        }
        return new SInteger[] { first, second };
    }
}
