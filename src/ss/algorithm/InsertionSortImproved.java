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
        for (int startIndex = 1; startIndex < array.length(); ++startIndex) {
            int i = startIndex;
            SortableElement toInsert = array.get(i);
            for (; i > 0; --i) {
                checkStopRequested();
                SortableElement element = array.get(i - 1);
                if (array.compare(element, toInsert) < 0) {
                    break;
                }
                array.set(i, element);
            }
            array.set(i, toInsert);
        }
    }
}
