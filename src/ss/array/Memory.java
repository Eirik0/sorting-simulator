package ss.array;

import java.util.ArrayList;
import java.util.List;

public class Memory {
    private final List<SInteger[]> allocatedMemory = new ArrayList<>();

    private static final Memory instance = new Memory();

    private Memory() {
    }

    public static SInteger[] allocate(int length) {
        SInteger[] array = new SInteger[length];
        instance.allocatedMemory.add(array);
        return array;
    }

    public static void clear() {
        instance.allocatedMemory.clear();
    }

    public static int numRows() {
        return instance.allocatedMemory.size();
    }

    public static SInteger[] getRow(int row) {
        return instance.allocatedMemory.get(row);
    }
}
