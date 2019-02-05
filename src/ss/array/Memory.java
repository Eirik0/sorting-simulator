package ss.array;

import java.util.ArrayList;
import java.util.List;

public class Memory {
    private final List<SInteger[]> allocatedMemory = new ArrayList<>();
    private final List<SInteger[]> deallocatedMemory = new ArrayList<>();
    private int numRows = 0;

    private static final Memory instance = new Memory();

    private Memory() {
    }

    public static synchronized SInteger[] allocate(int length) {
        SInteger[] array = new SInteger[length];
        instance.allocatedMemory.add(array);
        instance.numRows = Math.max(instance.numRows, instance.allocatedMemory.size());
        if (instance.deallocatedMemory.size() < instance.allocatedMemory.size()) {
            instance.deallocatedMemory.add(array);
        }
        return array;
    }

    public static synchronized void deallocateLast() {
        int rowIndex = instance.allocatedMemory.size() - 1;
        instance.deallocatedMemory.set(rowIndex, instance.allocatedMemory.get(rowIndex));
        instance.allocatedMemory.remove(rowIndex);
    }

    public static synchronized void clear() {
        instance.allocatedMemory.clear();
        instance.numRows = 0;
    }

    public static synchronized int numRows() {
        return instance.numRows;
    }

    public static synchronized SInteger[] getRow(int row) {
        if (row >= instance.allocatedMemory.size()) {
            return instance.deallocatedMemory.get(row);
        }
        return instance.allocatedMemory.get(row);
    }
}
