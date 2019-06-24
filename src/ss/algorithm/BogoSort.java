package ss.algorithm;

import java.util.Random;

import ss.array.SArray;
import ss.array.SInteger;

public class BogoSort implements SortingAlgorithm {
    private static final Random RANDOM = new Random();

    @Override
    public String getName() {
        return "Bogosort";
    }

    @Override
    public void sort(SArray array) {
        while (!isSorted(array)) {
            shuffle(array);
        }
    }

    private static boolean isSorted(SArray array) {
        for (int i = 0; i < array.length() - 1; ++i) {
            if (array.compare(array.get(i), array.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    private static void shuffle(SArray array) {
        for (int i = array.length() - 1; i >= 0; --i) {
            int rand = RANDOM.nextInt(i + 1);
            SInteger temp = array.get(i);
            array.set(i, array.get(rand));
            array.set(rand, temp);
        }
    }
}
