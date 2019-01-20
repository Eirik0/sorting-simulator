package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;
import ss.interrupt.SortStopper;

public class Heapsort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Heapsort";
    }

    @Override
    public void sort(SortableArray array) {
        for (int startIndex = (array.length() - 2) / 2; startIndex >= 0; --startIndex) {
            reheapify(array, startIndex, array.length() - 1);
        }

        int endIndex = array.length() - 1;
        while (endIndex > 0) {
            SortableElement largest = array.get(0);
            SortableElement element = array.get(endIndex);
            array.set(0, element);
            array.set(endIndex, largest);
            --endIndex;
            reheapify(array, 0, endIndex);
        }
    }

    private static void reheapify(SortableArray array, int startIndex, int endIndex) {
        int rootIndex = startIndex;
        while (rootIndex * 2 + 1 <= endIndex) {
            SortStopper.checkStopRequested();
            int childIndex = rootIndex * 2 + 1;
            SortableElement largestChild = array.get(childIndex);
            if (childIndex + 1 <= endIndex) {
                SortableElement right = array.get(childIndex + 1);
                if (array.compare(largestChild, right) < 0) {
                    largestChild = right;
                    ++childIndex;
                }
            }
            SortableElement parent = array.get(rootIndex);
            if (array.compare(parent, largestChild) >= 0) {
                break;
            }
            array.set(rootIndex, largestChild);
            array.set(childIndex, parent);
            rootIndex = childIndex;
        }
    }
}
