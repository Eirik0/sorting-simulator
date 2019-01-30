package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class CycleSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Cycle Sort";
    }

    @Override
    public void sort(SArray array) {
        for (int startIndex = 0; startIndex < array.length() - 1; ++startIndex) {
            SInteger item = array.get(startIndex);

            int pos = findPosition(array, startIndex, item);

            if (pos == startIndex) {
                continue;
            }

            item = swap(array, pos, item);

            while (pos != startIndex) {
                pos = findPosition(array, startIndex, item);
                item = swap(array, pos, item);
            }
        }
    }

    private static int findPosition(SArray array, int startIndex, SInteger item) {
        int pos = startIndex;
        for (int i = startIndex + 1; i < array.length(); ++i) {
            if (array.compare(array.get(i), item) < 0) {
                ++pos;
            }
        }
        return pos;
    }

    private static SInteger swap(SArray array, int pos, SInteger item) {
        SInteger element = array.get(pos);
        while (array.compare(item, element) == 0) {
            ++pos;
            element = array.get(pos);
        }
        array.set(pos, item);
        return element;
    }
}
