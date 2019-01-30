package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class GnomeSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Gnome Sort";
    }

    @Override
    public void sort(SArray array) {
        int firstIndex = 1;
        while (firstIndex < array.length()) {
            SInteger left = array.get(firstIndex - 1);
            SInteger right = array.get(firstIndex);
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
