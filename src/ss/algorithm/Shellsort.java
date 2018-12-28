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
    public void sort(SortableArray array) {
        for (int gap : GAPS) {
            for (int i = gap; i < array.length(); ++i) {
                SortableElement temp = array.get(i);
                int j = i;
                for (; j >= gap; j -= gap) {
                    SortableElement e1 = array.get(j - gap);
                    if (array.compare(e1, temp) > 0) {
                        array.set(j, e1);
                    } else {
                        break;
                    }
                }
                array.set(j, temp);
            }
        }
    }
}
