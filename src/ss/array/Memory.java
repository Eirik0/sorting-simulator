package ss.array;

import java.util.ArrayList;
import java.util.List;

public class Memory {
    private final List<SortableElement[]> allocatedMemory = new ArrayList<>();

    private static final Memory instance = new Memory();

    private Memory() {
    }

    public static SortableElement[] allocate(int length) {
        SortableElement[] array = new SortableElement[length];
        instance.allocatedMemory.add(array);
        return array;
    }

    public static void clear() {
        instance.allocatedMemory.clear();
    }

    public static int numRows() {
        return instance.allocatedMemory.size();
    }

    public static SortableElement[] getRow(int row) {
        return instance.allocatedMemory.get(row);
    }
}
