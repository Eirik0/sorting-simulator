package ss.algorithm;

import ss.array.Memory;
import ss.array.SArray;
import ss.array.SArray.ArrayType;
import ss.array.SInteger;

public class RadixSortMSDBase16 implements SortingAlgorithm {
    private static final int NUM_BUCKETS = 16;
    private static final int INITIAL_MASK = 15; // = 1111
    private static final int SHIFT = 4;

    @Override
    public String getName() {
        return "Radix Sort MSD (Base 16)";
    }

    @Override
    public void sort(SArray array) {
        int shift = 0;
        int mask = INITIAL_MASK;
        int fullMask = INITIAL_MASK;
        for (int i = 0; i < array.length(); ++i) {
            SInteger elemenent = array.get(i);
            while ((elemenent.value & fullMask) != elemenent.value) {
                fullMask <<= SHIFT;
                fullMask |= INITIAL_MASK;
                mask <<= SHIFT;
                shift += SHIFT;
            }
        }
        radixSort(array, 0, array.length(), mask, shift);
    }

    private static void radixSort(SArray array, int startIndex, int endIndex, int mask, int shift) {
        if (mask == 0) {
            return;
        }

        Base16Buckets buckets = new Base16Buckets(endIndex - startIndex);
        for (int i = startIndex; i < endIndex; ++i) {
            SInteger element = array.get(i);
            int bucket = (element.value & mask) >> shift;
            buckets.insert(element, bucket);
        }
        buckets.copyInto(array, startIndex);
        buckets.deallocate();

        int nextStartIndex = startIndex;
        while (nextStartIndex < endIndex - 1) {
            int nextEndIndex = nextStartIndex + 1;
            int currentMask = array.get(nextStartIndex).value & mask;
            while (nextEndIndex < endIndex && (array.get(nextEndIndex).value & mask) == currentMask) {
                ++nextEndIndex;
            }
            radixSort(array, nextStartIndex, nextEndIndex, mask >> SHIFT, shift - SHIFT);
            nextStartIndex = nextEndIndex;
        }
    }

    private static class Base16Buckets {
        final SArray values;
        final SArray nextElementIndexes;
        final SArray bucketStarts;
        final SArray bucketEnds;

        int position = 0;

        Base16Buckets(int arrayLength) {
            values = new SArray(ArrayType.EMPTY, arrayLength);
            nextElementIndexes = new SArray(ArrayType.EMPTY, arrayLength);
            bucketStarts = new SArray(ArrayType.EMPTY, NUM_BUCKETS);
            bucketEnds = new SArray(ArrayType.EMPTY, NUM_BUCKETS);
            for (int i = 0; i < NUM_BUCKETS; ++i) {
                bucketStarts.set(i, new SInteger(-1));
                bucketEnds.set(i, new SInteger(-1));
            }
        }

        void insert(SInteger element, int bucket) {
            int bucketEnd = bucketEnds.get(bucket).value;
            if (bucketEnd != -1) {
                nextElementIndexes.set(bucketEnd, new SInteger(position));
            } else {
                int bucketStart = bucketStarts.get(bucket).value;
                if (bucketStart == -1) {
                    bucketStarts.set(bucket, new SInteger(position));
                } else {
                    int prevNextElementIndex = bucketStart;
                    int nextElementIndex = nextElementIndexes.get(bucketStart).value;
                    while (nextElementIndex != 0) {
                        prevNextElementIndex = nextElementIndex;
                        nextElementIndex = nextElementIndexes.get(nextElementIndex).value;
                    }
                    nextElementIndexes.set(prevNextElementIndex, new SInteger(position));
                }
            }
            bucketEnds.set(bucket, new SInteger(position));
            values.copy(position++, element);
        }

        void copyInto(SArray array, int startIndex) {
            int arrayIndex = 0;
            for (int i = 0; i < NUM_BUCKETS; ++i) {
                int nextElementIndex = bucketStarts.get(i).value;
                if (nextElementIndex != -1) {
                    do {
                        array.copy(startIndex + arrayIndex++, values.get(nextElementIndex));
                        nextElementIndex = nextElementIndexes.get(nextElementIndex).value;
                    } while (nextElementIndex != 0);
                }
            }
        }

        void deallocate() {
            Memory.deallocateLast();
            Memory.deallocateLast();
            Memory.deallocateLast();
            Memory.deallocateLast();
        }
    }
}
