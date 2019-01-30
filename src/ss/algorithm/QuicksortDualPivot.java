package ss.algorithm;

import ss.array.SArray;
import ss.array.SInteger;

/**
 * Vladimir Yaroslavskiy's Dual-Pivot Quicksort
 */
public class QuicksortDualPivot implements SortingAlgorithm {
    private static final int DIST_SIZE = 13;
    private static final int TINY_SIZE = 17;

    @Override
    public String getName() {
        return "Quicksort (Dual Pivot)";
    }

    @Override
    public void sort(SArray array) {
        dualPivotQuicksort(array, 0, array.length() - 1);
    }

    private static void dualPivotQuicksort(SArray array, int startIndex, int stopIndex) {
        int len = stopIndex - startIndex;
        if (len < TINY_SIZE) { // insertion sort on tiny array
            InsertionSort.insertionSort(array, startIndex, stopIndex);
            return;
        }
        SInteger x;
        // median indexes
        int sixth = len / 6;
        int m1 = startIndex + sixth;
        int m2 = m1 + sixth;
        int m3 = m2 + sixth;
        int m4 = m3 + sixth;
        int m5 = m4 + sixth;
        // 5-element sorting network
        if (array.compare(array.get(m1), array.get(m2)) > 0) {
            x = array.get(m1);
            array.set(m1, array.get(m2));
            array.set(m2, x);
        }
        if (array.compare(array.get(m4), array.get(m5)) > 0) {
            x = array.get(m4);
            array.set(m4, array.get(m5));
            array.set(m5, x);
        }
        if (array.compare(array.get(m1), array.get(m3)) > 0) {
            x = array.get(m1);
            array.set(m1, array.get(m3));
            array.set(m3, x);
        }
        if (array.compare(array.get(m2), array.get(m3)) > 0) {
            x = array.get(m2);
            array.set(m2, array.get(m3));
            array.set(m3, x);
        }
        if (array.compare(array.get(m1), array.get(m4)) > 0) {
            x = array.get(m1);
            array.set(m1, array.get(m4));
            array.set(m4, x);
        }
        if (array.compare(array.get(m3), array.get(m4)) > 0) {
            x = array.get(m3);
            array.set(m3, array.get(m4));
            array.set(m4, x);
        }
        if (array.compare(array.get(m2), array.get(m5)) > 0) {
            x = array.get(m2);
            array.set(m2, array.get(m5));
            array.set(m5, x);
        }
        if (array.compare(array.get(m2), array.get(m3)) > 0) {
            x = array.get(m2);
            array.set(m2, array.get(m3));
            array.set(m3, x);
        }
        if (array.compare(array.get(m4), array.get(m5)) > 0) {
            x = array.get(m4);
            array.set(m4, array.get(m5));
            array.set(m5, x);
        }
        // pivots: [ < pivot1 | pivot1 <= && <= pivot2 | > pivot2 ]
        SInteger pivot1 = array.get(m2);
        SInteger pivot2 = array.get(m4);
        boolean diffPivots = array.compare(pivot1, pivot2) != 0;
        array.set(m2, array.get(startIndex));
        array.set(m4, array.get(stopIndex));
        // center part pointers
        int less = startIndex + 1;
        int great = stopIndex - 1;
        // sorting
        if (diffPivots) {
            for (int k = less; k <= great; k++) {
                x = array.get(k);
                if (array.compare(x, pivot1) < 0) {
                    array.set(k, array.get(less));
                    array.set(less++, x);
                } else if (array.compare(x, pivot2) > 0) {
                    while (array.compare(array.get(great), pivot2) > 0 && k < great) {
                        great--;
                    }
                    array.set(k, array.get(great));
                    array.set(great--, x);
                    x = array.get(k);
                    if (array.compare(x, pivot1) < 0) {
                        array.set(k, array.get(less));
                        array.set(less++, x);
                    }
                }
            }
        } else {
            for (int k = less; k <= great; k++) {
                x = array.get(k);
                if (array.compare(x, pivot1) == 0) {
                    continue;
                }
                if (array.compare(x, pivot1) < 0) {
                    array.set(k, array.get(less));
                    array.set(less++, x);
                } else {
                    while (array.compare(array.get(great), pivot2) > 0 && k < great) {
                        great--;
                    }
                    array.set(k, array.get(great));
                    array.set(great--, x);
                    x = array.get(k);
                    if (array.compare(x, pivot1) < 0) {
                        array.set(k, array.get(less));
                        array.set(less++, x);
                    }
                }
            }
        }
        // swap
        array.set(startIndex, array.get(less - 1));
        array.set(less - 1, pivot1);
        array.set(stopIndex, array.get(great + 1));
        array.set(great + 1, pivot2);
        // left and right parts
        dualPivotQuicksort(array, startIndex, less - 2);
        dualPivotQuicksort(array, great + 2, stopIndex);
        // equal elements
        if (great - less > len - DIST_SIZE && diffPivots) {
            for (int k = less; k <= great; k++) {
                x = array.get(k);
                if (array.compare(x, pivot1) == 0) {
                    array.set(k, array.get(less));
                    array.set(less++, x);
                } else if (array.compare(x, pivot2) == 0) {
                    array.set(k, array.get(great));
                    array.set(great--, x);
                    x = array.get(k);
                    if (array.compare(x, pivot1) == 0) {
                        array.set(k, array.get(less));
                        array.set(less++, x);
                    }
                }
            }
        }
        // center part
        if (diffPivots) {
            dualPivotQuicksort(array, less, great);
        }
    }
}
