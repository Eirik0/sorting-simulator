package ss.main;

import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;

import gt.async.ThreadWorker;
import ss.algorithm.SortingAlgorithm;
import ss.array.ComplexityCounter;
import ss.array.Memory;
import ss.array.SArray;
import ss.array.SArray.ArrayType;
import ss.array.TimeManager;
import ss.interrupt.SortStopException;
import ss.interrupt.SortStopper;
import ss.sound.SoundPlayer;

public class SortingSimulator {
    public static final Font SORT_FONT_LARGE = new Font(Font.MONOSPACED, Font.BOLD, 24);
    public static final Font SORT_FONT_SMALL = new Font(Font.MONOSPACED, Font.PLAIN, 16);

    private static double accessTime = 10;
    private static double insertTime = 10;
    private static double compareTime = 10;

    private static final ThreadWorker sortThreadWorker = new ThreadWorker();
    private static final SoundPlayer soundPlayer = new SoundPlayer();

    private static final Map<String, SortingAlgorithm> sortingAlgorithmMap = new LinkedHashMap<>();
    private static final Map<String, ArrayType> arrayTypeMap = new LinkedHashMap<>();

    private SortingSimulator() {
    }

    public static void registerSorts(SortingAlgorithm[] algorithms) {
        for (SortingAlgorithm sortingAlgorithm : algorithms) {
            sortingAlgorithmMap.put(sortingAlgorithm.getName(), sortingAlgorithm);
        }
    }

    public static String[] getAlgorithmNames() {
        return sortingAlgorithmMap.keySet().toArray(new String[0]);
    }

    public static SortingAlgorithm getAlgorithm(String name) {
        return sortingAlgorithmMap.get(name);
    }

    public static void registerArrayTypes() {
        arrayTypeMap.put("Shuffled", ArrayType.SHUFFLED);
        arrayTypeMap.put("In Order", ArrayType.IN_ORDER);
        arrayTypeMap.put("Reversed", ArrayType.REVERSED);
        arrayTypeMap.put("Duplicates", ArrayType.MANY_DUPLICATES);
    }

    public static String[] getArrayTypeNames() {
        return arrayTypeMap.keySet().toArray(new String[0]);
    }

    public static ArrayType getArrayType(String name) {
        return arrayTypeMap.get(name);
    }

    public static void stopSort(boolean keepFirst) {
        SortStopper.requestStop();
        TimeManager.requestStop();
        SortStopper.waitForStop();
        Memory.clear(keepFirst);
    }

    public static void startSort(SArray array, SortingAlgorithm algorithm) {
        stopSort(true);
        sortThreadWorker.workOn(() -> {
            try {
                ComplexityCounter.reset();
                TimeManager.reset();
                SortStopper.sortStarted();
                algorithm.sort(array);
            } catch (SortStopException e) {
            } finally {
                SortStopper.sortStopped();
            }
        });
        sortThreadWorker.waitForStart();
    }

    public static double getAccessTime() {
        return accessTime;
    }

    public static void setAccessTime(double time) {
        accessTime = time;
    }

    public static double getInsertTime() {
        return insertTime;
    }

    public static void setInsertTime(double time) {
        insertTime = time;
    }

    public static double getCompareTime() {
        return compareTime;
    }

    public static void setCompareTime(double time) {
        compareTime = time;
    }

    public static void playSound(double n, int numElements, double desiredDuration) {
        soundPlayer.play(n, numElements, desiredDuration);
    }
}
