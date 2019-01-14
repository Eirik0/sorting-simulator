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
        for (int startIndex = 0; startIndex < array.length() - 1; ++startIndex) {
            int minIndex = startIndex;
            SortableElement minimum = array.get(startIndex);
            for (int i = startIndex + 1; i < array.length(); ++i) {
                checkStopRequested();
                SortableElement element = array.get(i);
                if (array.compare(minimum, element) > 0) {
                    minIndex = i;
                    minimum = element;
                }
            }
            if (minIndex != startIndex) {
                SortableElement temp = array.get(startIndex);
                array.set(startIndex, minimum);
                array.set(minIndex, temp);
            }
        }
    }
}
