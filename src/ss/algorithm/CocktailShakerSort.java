package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class CocktailShakerSort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Cocktail Shaker Sort";
    }

    @Override
    public void sortImpl(SortableArray array) {
        int start = 0;
        int end = array.length() - 1;
        while (start <= end) {
            int nextEnd = start;
            for (int i = start; i < end; ++i) {
                checkStopRequested();
                SortableElement e1 = array.get(i);
                SortableElement e2 = array.get(i + 1);
                if (array.compare(e1, e2) > 0) {
                    array.set(i, e2);
                    array.set(i + 1, e1);
                    nextEnd = i + 1;
                }
            }
            end = nextEnd - 1;

            int nextStart = end;
            for (int i = end; i > start; --i) {
                checkStopRequested();
                SortableElement e1 = array.get(i - 1);
                SortableElement e2 = array.get(i);
                if (array.compare(e1, e2) > 0) {
                    array.set(i - 1, e2);
                    array.set(i, e1);
                    nextStart = i - 1;
                }
            }
            start = nextStart + 1;
        }
    }
}
