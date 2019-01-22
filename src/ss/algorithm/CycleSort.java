package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class CycleSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Cycle Sort";
    }

    @Override
    public void sort(SortableArray array) {
        for (int startIndex = 0; startIndex < array.length() - 1; ++startIndex) {
            SortableElement item = array.get(startIndex);

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

    private static int findPosition(SortableArray array, int startIndex, SortableElement item) {
        int pos = startIndex;
        for (int i = startIndex + 1; i < array.length(); ++i) {
            if (array.compare(array.get(i), item) < 0) {
                ++pos;
            }
        }
        return pos;
    }

    private static SortableElement swap(SortableArray array, int pos, SortableElement item) {
        SortableElement element = array.get(pos);
        while (array.compare(item, element) == 0) {
            ++pos;
            element = array.get(pos);
        }
        array.set(pos, item);
        return element;
    }
}
