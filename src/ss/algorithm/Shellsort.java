package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class Shellsort extends AbstractStoppableSort {
    public static final int[] GAPS = new int[] { 701, 301, 132, 57, 23, 10, 4, 1 };

    @Override
    public String getName() {
        return "Shellsort";
    }

    @Override
    public void sortImpl(SortableArray array) {
        for (int gap : GAPS) {
            for (int startIndex = gap; startIndex < array.length(); ++startIndex) {
                SortableElement toInsert = array.get(startIndex);
                int i = startIndex;
                for (; i >= gap; i -= gap) {
                    checkStopRequested();
                    SortableElement element = array.get(i - gap);
                    if (array.compare(element, toInsert) < 0) {
                        break;
                    }
                    array.set(i, element);
                }
                array.set(i, toInsert);
            }
        }
    }
}
