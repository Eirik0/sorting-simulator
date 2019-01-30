package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

public class StoogeSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Stooge Sort";
    }

    @Override
    public void sort(SArray array) {
        stoogeSort(array, 0, array.length() - 1);
    }

    private static void stoogeSort(SArray array, int startIndex, int endIndex) {
        SInteger start = array.get(startIndex);
        SInteger end = array.get(endIndex);
        if (array.compare(start, end) > 0) {
            array.set(startIndex, end);
            array.set(endIndex, start);
        }
        if (endIndex - startIndex > 1) {
            int oneThird = (endIndex + 1 - startIndex) / 3;
            stoogeSort(array, startIndex, endIndex - oneThird);
            stoogeSort(array, startIndex + oneThird, endIndex);
            stoogeSort(array, startIndex, endIndex - oneThird);
        }
    }
}
