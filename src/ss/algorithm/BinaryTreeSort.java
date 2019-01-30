package ss.algorithm;

import ss.array.SortableArray;
import ss.array.SortableArray.ArrayType;
import ss.array.SortableElement;

public class BinaryTreeSort implements SortingAlgorithm {
    private static final int NULL_VALUE = 0;

    @Override
    public String getName() {
        return "Binary Tree Sort";
    }

    @Override
    public void sort(SortableArray array) {
        BinaryTree tree = new BinaryTree(array.length());
        for (int i = 0; i < array.length(); ++i) {
            tree.insert(array.get(i));
        }
        tree.fill(array);
    }

    private static class BinaryTree {
        private final SortableArray elements;
        private final SortableArray leftChildIndexes;
        private final SortableArray rightChildIndexes;

        private int position = 0;

        BinaryTree(int size) {
            elements = new SortableArray(ArrayType.EMPTY, size);
            leftChildIndexes = new SortableArray(ArrayType.EMPTY, size);
            rightChildIndexes = new SortableArray(ArrayType.EMPTY, size);
        }

        void insert(SortableElement element) {
            if (position > 0) {
                int rootIndex = 0;
                for (;;) {
                    SortableElement root = elements.get(rootIndex);
                    int childIndex;
                    if (elements.compare(element, root) < 0) {
                        childIndex = leftChildIndexes.get(rootIndex).value;
                        if (childIndex == NULL_VALUE) {
                            leftChildIndexes.set(rootIndex, new SortableElement(position));
                            break;
                        }
                    } else {
                        childIndex = rightChildIndexes.get(rootIndex).value;
                        if (childIndex == NULL_VALUE) {
                            rightChildIndexes.set(rootIndex, new SortableElement(position));
                            break;
                        }
                    }
                    rootIndex = childIndex;
                }
            }
            elements.copy(position++, element);
        }

        void fill(SortableArray array) {
            fill(array, 0, 0);
        }

        private int fill(SortableArray array, int getIndex, int setIndex) {
            int leftChildIndex = leftChildIndexes.get(getIndex).value;
            if (leftChildIndex != NULL_VALUE) {
                setIndex = fill(array, leftChildIndex, setIndex);
            }
            array.copy(setIndex++, elements.get(getIndex));
            int rightChildIndex = rightChildIndexes.get(getIndex).value;
            if (rightChildIndex != NULL_VALUE) {
                setIndex = fill(array, rightChildIndex, setIndex);
            }
            return setIndex;
        }
    }
}
