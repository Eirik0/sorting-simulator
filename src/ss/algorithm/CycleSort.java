package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class CycleSort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Cycle Sort";
    }

    @Override
    public void sortImpl(SortableArray array) {
        for (int start = 0; start < array.length() - 1; ++start) {
            SortableElement item = array.get(start);

            int pos = findPosition(array, start, item);

            if (pos == start) {
                continue;
            }

            item = swap(array, pos, item);

            while (pos != start) {
                pos = findPosition(array, start, item);
                item = swap(array, pos, item);
            }
        }
    }

    private int findPosition(SortableArray array, int start, SortableElement item) {
        int pos = start;
        for (int i = start + 1; i < array.length(); ++i) {
            checkStopRequested();
            if (array.compare(array.get(i), item) < 0) {
                ++pos;
            }
        }
        return pos;
    }

    private static SortableElement swap(SortableArray array, int pos, SortableElement item) {
        SortableElement e1 = array.get(pos);
        while (array.compare(item, e1) == 0) {
            ++pos;
            e1 = array.get(pos);
        }
        array.set(pos, item);
        return e1;
    }
}
