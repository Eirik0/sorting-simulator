package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class GnomeSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Gnome Sort";
    }

    @Override
    public void sort(SortableArray array) {
        int firstIndex = 1;
        while (firstIndex < array.length()) {
            SortableElement left = array.get(firstIndex - 1);
            SortableElement right = array.get(firstIndex);
            if (array.compare(left, right) <= 0) {
                ++firstIndex;
            } else {
                array.set(firstIndex - 1, right);
                array.set(firstIndex, left);
                firstIndex = Math.max(firstIndex - 1, 1);
            }
        }
    }
}
