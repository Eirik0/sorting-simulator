package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class CombSort implements SortingAlgorithm {
    private static final double SHRINK = 1.3;

    @Override
    public String getName() {
        return "Comb Sort";
    }

    @Override
    public void sort(SArray array) {
        int gap = array.length();
        boolean sorted = false;

        while (!sorted) {
            gap = (int) (gap / SHRINK);
            if (gap <= 1) {
                gap = 1;
                sorted = true;
            }

            for (int i = 0; i < array.length() - gap; ++i) {
                SInteger left = array.get(i);
                SInteger right = array.get(i + gap);
                if (array.compare(left, right) > 0) {
                    array.set(i, right);
                    array.set(i + gap, left);
                    sorted = false;
                }
            }
        }
    }
}
