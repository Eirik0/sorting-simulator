package ss.array;

import java.util.Random;

import ss.main.SortingSimulator;

public class SArray {
    public enum ArrayType {
        EMPTY,
        IN_ORDER,
        REVERSED,
        SHUFFLED,
        MANY_DUPLICATES
    }

    private static final Random RANDOM = new Random();

    private final SInteger[] array;

    public SArray(ArrayType type, int length) {
        array = Memory.allocate(length);
        switch (type) {
        case EMPTY:
            for (int i = 0; i < array.length; ++i) {
                array[i] = new SInteger(0);
            }
            break;
        case IN_ORDER:
            populateInOrder();
            break;
        case REVERSED:
            for (int i = 0; i < array.length; ++i) {
                array[i] = new SInteger(array.length - i);
            }
            break;
        case SHUFFLED:
            populateInOrder();
            shuffle();
            break;
        case MANY_DUPLICATES:
            int step = array.length / 10 + 1;
            int n = array.length;
            for (int i = 0; i < array.length; ++i) {
                array[i] = new SInteger(n);
                if ((i % step) == step - 1) {
                    n -= step;
                }
            }
            shuffle();
            break;
        }
    }

    private void populateInOrder() {
        for (int i = 0; i < array.length; ++i) {
            array[i] = new SInteger(i + 1);
        }
    }

    private void shuffle() {
        for (int i = array.length - 1; i >= 0; --i) {
            int rand = RANDOM.nextInt(i + 1);
            SInteger temp = array[i];
            array[i] = array[rand];
            array[rand] = temp;
        }
    }

    public int length() {
        return array.length;
    }

    public SInteger get(int i) {
        SInteger element = array[i];
        SortingSimulator.playSound(element.value, array.length, SortingSimulator.getAccessTime());
        TimeManager.waitForTime(SortingSimulator.getAccessTime());
        ComplexityCounter.incrementAccesses();
        element.lastAccess = System.nanoTime();
        return element;
    }

    public void copy(int i, SInteger element) {
        set(i, element.copy());
    }

    public void set(int i, SInteger element) {
        SortingSimulator.playSound(element.value, array.length, SortingSimulator.getInsertTime());
        TimeManager.waitForTime(SortingSimulator.getInsertTime());
        ComplexityCounter.incrementInserts();
        element.lastInsert = System.nanoTime();
        array[i] = element;
    }

    public int compare(SInteger e1, SInteger e2) {
        SortingSimulator.playSound((e1.value + e2.value) / 2.0, array.length, SortingSimulator.getCompareTime());
        TimeManager.waitForTime(SortingSimulator.getCompareTime());
        ComplexityCounter.incrementCompares();
        e1.lastCompare = System.nanoTime();
        e2.lastCompare = System.nanoTime();
        return Integer.compare(e1.value, e2.value);
    }
}
