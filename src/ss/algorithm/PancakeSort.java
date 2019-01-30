package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class PancakeSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Pancake Sort";
    }

    @Override
    public void sort(SArray array) {
        for (int startIndex = 0; startIndex < array.length(); ++startIndex) {
            int minIndex = startIndex;
            SInteger minimum = array.get(startIndex);
            for (int i = startIndex + 1; i <= array.length() - 1; ++i) {
                SInteger element = array.get(i);
                if (array.compare(minimum, element) > 0) {
                    minIndex = i;
                    minimum = element;
                }
            }
            if (minIndex != startIndex) {
                flip(array, minIndex);
                flip(array, startIndex);
            }
        }
    }

    private static void flip(SArray array, int n) {
        int leftIndex = n;
        int rightIndex = array.length() - 1;
        while (leftIndex < rightIndex) {
            SInteger left = array.get(leftIndex);
            SInteger right = array.get(rightIndex);
            array.set(leftIndex, right);
            array.set(rightIndex, left);
            ++leftIndex;
            --rightIndex;
        }
    }
}
