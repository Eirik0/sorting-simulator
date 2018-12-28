package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class StoogeSort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Stooge Sort";
    }

    @Override
    public void sortImpl(SortableArray array) {
        stoogeSort(array, 0, array.length() - 1);
    }

    public void stoogeSort(SortableArray array, int start, int end) {
        checkStopRequested();
        SortableElement e1 = array.get(start);
        SortableElement e2 = array.get(end);
        if (array.compare(e1, e2) > 0) {
            array.set(start, e2);
            array.set(end, e1);
        }
        if (end - start > 1) {
            int third = (end + 1 - start) / 3;
            stoogeSort(array, start, end - third);
            stoogeSort(array, start + third, end);
            stoogeSort(array, start, end - third);
        }
    }
}
