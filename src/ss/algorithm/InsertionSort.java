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
        for (int startIndex = 1; startIndex < array.length(); ++startIndex) {
            for (int i = startIndex; i > 0; --i) {
                checkStopRequested();
                SortableElement left = array.get(i - 1);
                SortableElement right = array.get(i);
                if (array.compare(left, right) <= 0) {
                    break;
                }
                array.set(i - 1, right);
                array.set(i, left);
            }
        }
    }
}
