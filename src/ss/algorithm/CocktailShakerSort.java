package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;
import ss.interrupt.SortStopper;

public class CocktailShakerSort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Cocktail Shaker Sort";
    }

    @Override
    public void sort(SortableArray array) {
        int startIndex = 0;
        int endIndex = array.length() - 1;

        while (startIndex <= endIndex) {
            int nextEndIndex = startIndex;
            for (int i = startIndex; i < endIndex; ++i) {
                SortStopper.checkStopRequested();
                SortableElement left = array.get(i);
                SortableElement right = array.get(i + 1);
                if (array.compare(left, right) > 0) {
                    array.set(i, right);
                    array.set(i + 1, left);
                    nextEndIndex = i + 1;
                }
            }
            endIndex = nextEndIndex - 1;

            int nextStartIndex = endIndex;
            for (int i = endIndex; i > startIndex; --i) {
                SortStopper.checkStopRequested();
                SortableElement left = array.get(i - 1);
                SortableElement right = array.get(i);
                if (array.compare(left, right) > 0) {
                    array.set(i - 1, right);
                    array.set(i, left);
                    nextStartIndex = i - 1;
                }
            }
            startIndex = nextStartIndex + 1;
        }
    }
}
