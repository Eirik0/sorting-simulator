package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class BubbleSort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public void sort(SortableArray array) {
        for (int max = array.length() - 1; max >= 0; --max) {
            for (int i = 0; i < max; ++i) {
                checkStopRequested();
                SortableElement e1 = array.get(i);
                SortableElement e2 = array.get(i + 1);
                if (array.compare(e1, e2) > 0) {
                    array.set(i, e2);
                    array.set(i + 1, e1);
                }
            }
        }
    }
}
