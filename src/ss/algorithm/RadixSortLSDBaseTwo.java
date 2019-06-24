package ss.algorithm;

import ss.array.SArray;
import ss.array.SArray.ArrayType;
import ss.array.SInteger;

public class RadixSortLSDBaseTwo implements SortingAlgorithm {
    @Override
    public String getName() {
        return "Radix Sort LSD (Base 2)";
    }

    @Override
    public void sort(SArray array) {
        SArray workingArray = new SArray(ArrayType.EMPTY, array.length());
        int mask = 1;
        for (;;) {
            int zeroIndex = 0;
            int oneIndex = array.length() - 1;
            for (int i = 0; i < array.length(); ++i) {
                SInteger element = array.get(i);
                if ((element.value & mask) == 0) {
                    workingArray.set(zeroIndex++, element);
                } else {
                    workingArray.set(oneIndex--, element);
                }
            }
            if (oneIndex == array.length() - 1) {
                break;
            }
            int i = 0;
            for (; i < zeroIndex; ++i) {
                array.set(i, workingArray.get(i));
            }
            int copyFromIndex = array.length() - 1;
            for (; i < array.length(); ++i) {
                array.set(i, workingArray.get(copyFromIndex--));
            }
            mask <<= 1;
        }
    }
}
