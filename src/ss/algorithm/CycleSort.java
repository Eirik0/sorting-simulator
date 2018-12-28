package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class CycleSort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Cycle Sort";
    }

    @Override
    public void sort(SortableArray array) {
        for (int start = 0; start < array.length() - 1; ++start) {
            SortableElement item = array.get(start);

            int pos = start;
            for (int i = start + 1; i < array.length(); ++i) {
                if (array.compare(array.get(i), item) < 0) {
                    ++pos;
                }
            }

            if (pos == start) {
                continue;
            }

            SortableElement e1 = array.get(pos);
            while (array.compare(item, e1) == 0) {
                ++pos;
                e1 = array.get(pos);
            }
            array.set(pos, item);
            item = e1;

            while (pos != start) {
                pos = start;
                for (int i = start + 1; i < array.length(); ++i) {
                    if (array.compare(array.get(i), item) < 0) {
                        ++pos;
                    }
                }

                e1 = array.get(pos);
                while (array.compare(item, e1) == 0) {
                    ++pos;
                }
                array.set(pos, item);
                item = e1;
            }
        }
    }
}
