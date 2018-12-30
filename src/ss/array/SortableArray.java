package ss.array;

import java.awt.Color;
import java.util.Random;

import ss.main.SortingSimulator;
import ss.sound.SoundPlayer;

public class SortableArray {
    private static final Random RANDOM = new Random();

    private final SortableElement[] array;

    private long numCompares;
    private long numAccesses;
    private long numInserts;

    private final SoundPlayer player;

    public SortableArray(int length) {
        array = new SortableElement[length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new SortableElement(i + 1);
        }
        for (int i = array.length - 1; i >= 0; --i) {
            int rand = RANDOM.nextInt(i + 1);
            SortableElement temp = array[i];
            array[i] = array[rand];
            array[rand] = temp;
        }
        player = new SoundPlayer(length);
    }

    public int length() {
        return array.length;
    }

    public SortableElement get(int i) {
        player.play(i, SortingSimulator.getAccessTime());
        sleep(SortingSimulator.getAccessTime());
        SortableElement element = array[i];
        element.lastAccess = System.nanoTime();
        ++numAccesses;
        return element;
    }

    public void set(int i, SortableElement element) {
        sleep(SortingSimulator.getInsertTime());
        ++numInserts;
        element.lastInsert = System.nanoTime();
        array[i] = element;
    }

    public int compare(SortableElement e1, SortableElement e2) {
        sleep(SortingSimulator.getCompareTime());
        ++numCompares;
        e1.lastCompare = System.nanoTime();
        e2.lastCompare = System.nanoTime();
        return Integer.compare(e1.value, e2.value);
    }

    public long getNumAccesses() {
        return numAccesses;
    }

    public long getNumInserts() {
        return numInserts;
    }

    public long getNumCompares() {
        return numCompares;
    }

    public int getValue(int i) {
        return array[i].value;
    }

    public Color getColor(int i) {
        return array[i].getColor();
    }

    public void closeSoundPlayer() {
        player.closeSDL();
    }

    private static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }
}
