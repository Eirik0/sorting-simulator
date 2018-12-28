package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class CombSort extends AbstractStoppableSort {
    private static final double SHRINK = 1.3;

    @Override
    public String getName() {
        return "Comb Sort";
    }

    @Override
    public void sort(SortableArray array) {
        int gap = array.length();
        boolean sorted = false;

        while (!sorted) {
            gap = (int) (gap / SHRINK);
            if (gap <= 1) {
                gap = 1;
                sorted = true;
            }

            for (int i = 0; i < array.length() - gap; ++i) {
                checkStopRequested();
                SortableElement e1 = array.get(i);
                SortableElement e2 = array.get(i + gap);
                if (array.compare(e1, e2) > 0) {
                    array.set(i, e2);
                    array.set(i + gap, e1);
                    sorted = false;
                }
            }
        }
    }
}
