package ss.array;

public class ComplexityCounter {
    private long numCompares;
    private long numAccesses;
    private long numInserts;

    private static final ComplexityCounter instance = new ComplexityCounter();

    private ComplexityCounter() {
    }

    public static Long getNumAccesses() {
        return Long.valueOf(instance.numAccesses);
    }

    public static void incrementAccesses() {
        ++instance.numAccesses;
    }

    public static Long getNumInserts() {
        return Long.valueOf(instance.numInserts);
    }

    public static void incrementInserts() {
        ++instance.numInserts;
    }

    public static Long getNumCompares() {
        return Long.valueOf(instance.numCompares);
    }

    public static void incrementCompares() {
        ++instance.numCompares;
    }

    public static void reset() {
        instance.numCompares = 0;
        instance.numAccesses = 0;
        instance.numInserts = 0;
    }
}
