package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class SelectionSort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public void sortImpl(SortableArray array) {
        for (int start = 0; start < array.length() - 1; ++start) {
            int min_i = start;
            SortableElement min = array.get(start);
            for (int i = start + 1; i < array.length(); ++i) {
                checkStopRequested();
                SortableElement e = array.get(i);
                if (array.compare(min, e) > 0) {
                    min_i = i;
                    min = e;
                }
            }
            if (min_i != start) {
                SortableElement temp = array.get(start);
                array.set(start, min);
                array.set(min_i, temp);
            }
        }
    }
}
