package ss.array;

import java.awt.Color;
import java.util.Random;

import gt.gameentity.Updatable;
import gt.gameloop.TimeConstants;
import ss.main.SortingSimulator;
import ss.sound.SoundPlayer;

public class SortableArray implements Updatable {
    private static final Random RANDOM = new Random();

    private final SortableElement[] array;

    private long numCompares;
    private long numAccesses;
    private long numInserts;

    private volatile double timeAllotted = 0;

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

    @Override
    public synchronized void update(double dt) {
        timeAllotted += dt;
        notify();
    }

    public int length() {
        return array.length;
    }

    public SortableElement get(int i) {
        SortableElement element = array[i];
        player.play(element.value, SortingSimulator.getAccessTime());
        waitForTime(SortingSimulator.getAccessTime());
        element.lastAccess = System.nanoTime();
        ++numAccesses;
        return element;
    }

    public void set(int i, SortableElement elementToSet) {
        SortableElement element = elementToSet.copy();
        player.play(element.value, SortingSimulator.getInsertTime());
        waitForTime(SortingSimulator.getInsertTime());
        ++numInserts;
        element.lastInsert = System.nanoTime();
        array[i] = element;
    }

    public int compare(SortableElement e1, SortableElement e2) {
        player.play((e1.value + e2.value) / 2, SortingSimulator.getCompareTime());
        waitForTime(SortingSimulator.getCompareTime());
        ++numCompares;
        e1.lastCompare = System.nanoTime();
        e2.lastCompare = System.nanoTime();
        return Integer.compare(e1.value, e2.value);
    }

    private synchronized void waitForTime(double timeUsed) {
        while (timeAllotted <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        timeAllotted -= timeUsed * TimeConstants.NANOS_PER_MILLISECOND;
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

    public void requestStop() {
        update(Double.MAX_VALUE);
    }

    public synchronized void searchStopped() {
        player.stop();
    }
}
