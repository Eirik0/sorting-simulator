package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class BubbleSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public void sort(SArray array) {
        for (int endIndex = array.length() - 1; endIndex >= 0; --endIndex) {
            for (int i = 0; i < endIndex; ++i) {
                SInteger left = array.get(i);
                SInteger right = array.get(i + 1);
                if (array.compare(left, right) > 0) {
                    array.set(i, right);
                    array.set(i + 1, left);
                }
            }
        }
    }
}
