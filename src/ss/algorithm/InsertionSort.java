package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class InsertionSort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Insertion Sort";
    }

    @Override
    public void sortImpl(SortableArray array) {
        for (int start = 1; start < array.length(); ++start) {
            for (int i = start; i > 0; --i) {
                checkStopRequested();
                SortableElement e1 = array.get(i - 1);
                SortableElement e2 = array.get(i);
                if (array.compare(e1, e2) > 0) {
                    SortableElement temp = e1;
                    array.set(i - 1, e2);
                    array.set(i, temp);
                } else {
                    break;
                }
            }
        }
    }
}
