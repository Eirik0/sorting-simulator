package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class InsertionSortBinarySearch extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Insertion Sort (w/ Binary Search)";
    }

    @Override
    public void sortImpl(SortableArray array) {
        for (int startIndex = 1; startIndex < array.length(); ++startIndex) {
            SortableElement toInsert = array.get(startIndex);
            int pos = binarySearch(array, toInsert, startIndex);
            for (int i = startIndex; i > pos; --i) {
                checkStopRequested();
                SortableElement element = array.get(i - 1);
                array.set(i, element);
            }
            array.set(pos, toInsert);
        }
    }

    private int binarySearch(SortableArray array, SortableElement toInsert, int elementIndex) {
        int leftIndex = 0;
        int rightIndex = elementIndex - 1;
        while (leftIndex <= rightIndex) {
            checkStopRequested();
            int middleIndex = (leftIndex + rightIndex) / 2;
            if (array.compare(array.get(middleIndex), toInsert) <= 0) {
                leftIndex = middleIndex + 1;
            } else {
                rightIndex = middleIndex - 1;
            }
        }
        return leftIndex;
    }
}
