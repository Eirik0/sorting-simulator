package ss.array;

import java.util.Random;

import ss.main.SortingSimulator;
import ss.sound.SoundPlayer;

public class SArray {
    public enum ArrayType {
        EMPTY,
        SHUFFLED
    }

    private static final Random RANDOM = new Random();

    private final SInteger[] array;

    private final SoundPlayer player;

    public SArray(ArrayType type, int length) {
        array = Memory.allocate(length);
        switch (type) {
        case EMPTY:
            for (int i = 0; i < array.length; ++i) {
                array[i] = new SInteger(0);
            }
            break;
        case SHUFFLED:
            for (int i = 0; i < array.length; ++i) {
                array[i] = new SInteger(i + 1);
            }
            for (int i = array.length - 1; i >= 0; --i) {
                int rand = RANDOM.nextInt(i + 1);
                SInteger temp = array[i];
                array[i] = array[rand];
                array[rand] = temp;
            }
            break;
        }
        player = new SoundPlayer(length);
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
