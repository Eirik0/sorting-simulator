package ss.array;

import java.awt.Color;
import java.util.Random;

import gt.gameentity.Updatable;
import gt.gameloop.TimeConstants;
import ss.interrupt.SortStopper;
import ss.main.SortingSimulator;
import ss.sound.SoundPlayer;

public class SortableArray implements Updatable {
    private static final Random RANDOM = new Random();

    private final SortableArray parentArray;
    private SortableArray childArray;

    private final SortableElement[] array;

    private long numCompares;
    private long numAccesses;
    private long numInserts;

    private volatile double timeAllotted = 0;

    private final SoundPlayer player;

    public SortableArray(int length) {
        parentArray = null;
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

    private SortableArray(SortableArray parentArray) {
        this.parentArray = parentArray;
        array = new SortableElement[parentArray.array.length];
        for (int i = 0; i < array.length; ++i) {
            array[i] = new SortableElement(1);
        }
        player = parentArray.player;
    }

    public SortableArray allocateNew() {
        childArray = new SortableArray(this);
        return childArray;
    }

    public void dellocate() {
        parentArray.childArray = null;
    }

    public SortableArray getChildArray() {
        return childArray;
    }

    @Override
    public synchronized void update(double dt) {
        timeAllotted += dt;
        notify();
    }

    public int length() {
        return array.length;
    }

    public int numChildren() {
        if (childArray == null) {
            return 0;
        } else {
            return 1 + childArray.numChildren();
        }
    }

    public SortableElement get(int i) {
        SortableElement element = array[i];
        player.play(element.value, SortingSimulator.getAccessTime());
        waitForTime(SortingSimulator.getAccessTime());
        element.lastAccess = System.nanoTime();
        incrementAccesses();
        return element;
    }

    public void set(int i, SortableElement elementToSet) {
        SortableElement element = elementToSet.copy();
        player.play(element.value, SortingSimulator.getInsertTime());
        waitForTime(SortingSimulator.getInsertTime());
        incrementInserts();
        element.lastInsert = System.nanoTime();
        array[i] = element;
    }

    public int compare(SortableElement e1, SortableElement e2) {
        player.play((e1.value + e2.value) / 2, SortingSimulator.getCompareTime());
        waitForTime(SortingSimulator.getCompareTime());
        incrementCompares();
        e1.lastCompare = System.nanoTime();
        e2.lastCompare = System.nanoTime();
        return Integer.compare(e1.value, e2.value);
    }

    private void incrementAccesses() {
        if (parentArray == null) {
            ++numAccesses;
        } else {
            parentArray.incrementAccesses();
        }
    }

    private void incrementInserts() {
        if (parentArray == null) {
            ++numInserts;
        } else {
            parentArray.incrementInserts();
        }
    }

    private void incrementCompares() {
        if (parentArray == null) {
            ++numCompares;
        } else {
            parentArray.incrementCompares();
        }
    }

    private void waitForTime(double timeUsed) {
        SortStopper.checkStopRequested();
        if (parentArray == null) {
            waitForTimeSynchronized(timeUsed);
        } else {
            parentArray.waitForTime(timeUsed);
        }
    }

    private synchronized void waitForTimeSynchronized(double timeUsed) {
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
