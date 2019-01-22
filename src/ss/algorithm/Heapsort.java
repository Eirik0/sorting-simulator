package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class Heapsort implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Heapsort";
    }

    @Override
    public void sort(SortableArray array) {
        heapsort(array, 0, array.length() - 1);
    }

    public static void heapsort(SortableArray array, int startIndex, int endIndex) {
        for (int i = getParentIndex(endIndex, startIndex); i >= startIndex; --i) {
            reheapify(array, i, endIndex, startIndex);
        }

        while (endIndex > startIndex) {
            SortableElement largest = array.get(startIndex);
            SortableElement element = array.get(endIndex);
            array.set(startIndex, element);
            array.set(endIndex, largest);
            --endIndex;
            reheapify(array, startIndex, endIndex, startIndex);
        }
    }

    private static void reheapify(SortableArray array, int startIndex, int endIndex, int offset) {
        int rootIndex = startIndex;
        while (getChildIndex(rootIndex, offset) <= endIndex) {
            int childIndex = getChildIndex(rootIndex, offset);
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

    private static int getChildIndex(int parentIndex, int offset) {
        return offset + (parentIndex - offset) * 2 + 1;
    }

    private static int getParentIndex(int childIndex, int offset) {
        return offset + (childIndex - offset - 1) / 2;
    }
}
