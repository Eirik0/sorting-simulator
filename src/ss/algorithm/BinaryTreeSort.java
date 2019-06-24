package ss.algorithm;

import ss.array.SArray;
import ss.array.SArray.ArrayType;
import ss.array.SInteger;

public class BinaryTreeSort implements SortingAlgorithm {
    private static final int NULL_VALUE = 0;

    @Override
    public String getName() {
        return "Binary Tree Sort";
    }

    @Override
    public void sort(SArray array) {
        BinaryTree tree = new BinaryTree(array.length());
        for (int i = 0; i < array.length(); ++i) {
            tree.insert(array.get(i));
        }
        tree.fill(array);
    }

    private static class BinaryTree {
        private final SArray elements;
        private final SArray leftChildIndexes;
        private final SArray rightChildIndexes;

        private int position = 0;

        BinaryTree(int size) {
            elements = new SArray(ArrayType.EMPTY, size);
            leftChildIndexes = new SArray(ArrayType.EMPTY, size);
            rightChildIndexes = new SArray(ArrayType.EMPTY, size);
        }

        void insert(SInteger element) {
            if (position > 0) {
                int rootIndex = 0;
                for (;;) {
                    SInteger root = elements.get(rootIndex);
                    int childIndex;
                    if (elements.compare(element, root) < 0) {
                        childIndex = leftChildIndexes.get(rootIndex).value;
                        if (childIndex == NULL_VALUE) {
                            leftChildIndexes.set(rootIndex, new SInteger(position));
                            break;
                        }
                    } else {
                        childIndex = rightChildIndexes.get(rootIndex).value;
                        if (childIndex == NULL_VALUE) {
                            rightChildIndexes.set(rootIndex, new SInteger(position));
                            break;
                        }
                    }
                    rootIndex = childIndex;
                }
            }
            elements.set(position++, element);
        }

        void fill(SArray array) {
            fill(array, 0, 0);
        }

        private int fill(SArray array, int getIndex, int setIndex) {
            int leftChildIndex = leftChildIndexes.get(getIndex).value;
            if (leftChildIndex != NULL_VALUE) {
                setIndex = fill(array, leftChildIndex, setIndex);
            }
            array.set(setIndex++, elements.get(getIndex));
            int rightChildIndex = rightChildIndexes.get(getIndex).value;
            if (rightChildIndex != NULL_VALUE) {
                setIndex = fill(array, rightChildIndex, setIndex);
            }
            return setIndex;
        }
    }
}
