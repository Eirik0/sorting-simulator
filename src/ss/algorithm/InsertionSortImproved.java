package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class InsertionSortImproved extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Insertion Sort (improved)";
    }

    @Override
    public void sortImpl(SortableArray array) {
        for (int start = 1; start < array.length(); ++start) {
            int i = start;
            SortableElement e2 = array.get(i);
            for (; i > 0; --i) {
                checkStopRequested();
                SortableElement e1 = array.get(i - 1);
                if (array.compare(e1, e2) < 0) {
                    break;
                }
                array.set(i, e1);
            }
            array.set(i, e2);
        }
    }
}
