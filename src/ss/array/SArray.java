package ss.array;

import java.util.Random;

import ss.main.SortingSimulator;
import ss.sound.SoundPlayer;

public class SArray {
    public enum ArrayType {
        EMPTY,
        IN_ORDER,
        REVERSED,
        SHUFFLED,
        MANY_DUPLICATES
    }

    private static final Random RANDOM = new Random();

    private SInteger[] array;

    private SoundPlayer player;

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
        player = new SoundPlayer(length);
    }

    public void reallocateMemory() {
        SInteger[] temp = array;
        array = Memory.allocate(array.length);
        System.arraycopy(temp, 0, array, 0, array.length);
        if (player.isStopped()) {
            player = new SoundPlayer(array.length);
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
        player.play(element.value, SortingSimulator.getAccessTime());
        TimeManager.waitForTime(SortingSimulator.getAccessTime());
        ComplexityCounter.incrementAccesses();
        element.lastAccess = System.nanoTime();
        return element;
    }

    public void copy(int i, SInteger element) {
        set(i, element.copy());
    }

    public void set(int i, SInteger element) {
        player.play(element.value, SortingSimulator.getInsertTime());
        TimeManager.waitForTime(SortingSimulator.getInsertTime());
        ComplexityCounter.incrementInserts();
        element.lastInsert = System.nanoTime();
        array[i] = element;
    }

    public int compare(SInteger e1, SInteger e2) {
        player.play((e1.value + e2.value) / 2, SortingSimulator.getCompareTime());
        TimeManager.waitForTime(SortingSimulator.getCompareTime());
        ComplexityCounter.incrementCompares();
        e1.lastCompare = System.nanoTime();
        e2.lastCompare = System.nanoTime();
        return Integer.compare(e1.value, e2.value);
    }

    public synchronized void searchStopped() {
        player.stop();
    }
}
