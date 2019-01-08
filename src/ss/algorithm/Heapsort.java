package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableElement;

public class Heapsort extends AbstractStoppableSort {
    @Override
    public String getName() {
        return "Heapsort";
    }

    @Override
    public void sortImpl(SortableArray array) {
        heapify(array, array.length());

        int end = array.length() - 1;
        while (end > 0) {
            SortableElement e1 = array.get(0);
            SortableElement e2 = array.get(end);
            array.set(0, e2);
            array.set(end, e1);
            --end;
            siftDown(array, 0, end);
        }
    }

    private void heapify(SortableArray array, int length) {
        for (int start = (length - 2) / 2; start >= 0; --start) {
            siftDown(array, start, length - 1);
        }
    }

    private void siftDown(SortableArray array, int start, int end) {
        int root = start;
        while (root * 2 + 1 <= end) {
            checkStopRequested();
            int childIndex = root * 2 + 1;
            SortableElement largestChild = array.get(childIndex);
            if (childIndex + 1 <= end) {
                SortableElement right = array.get(childIndex + 1);
                if (array.compare(largestChild, right) < 0) {
                    largestChild = right;
                    ++childIndex;
                }
            }
            SortableElement parent = array.get(root);
            if (array.compare(parent, largestChild) < 0) {
                array.set(root, largestChild);
                array.set(childIndex, parent);
                root = childIndex;
            } else {
                break;
            }
        }
    }
}
