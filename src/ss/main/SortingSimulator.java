package ss.main;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gt.async.ThreadWorker;
import gt.gamestate.GameState;
import gt.gamestate.GameStateManager;
import gt.gamestate.menu.ScaledMenuState;
import gt.util.Pair;
import ss.algorithm.SortingAlgorithm;
import ss.array.SArray.ArrayType;

public class SortingSimulator {
    public static final Font SORT_FONT_LARGE = new Font(Font.MONOSPACED, Font.BOLD, 24);
    public static final Font SORT_FONT_SMALL = new Font(Font.MONOSPACED, Font.PLAIN, 16);

    private static double accessTime = 10;
    private static double insertTime = 10;
    private static double compareTime = 10;

    private static final ThreadWorker sortThreadWorker = new ThreadWorker();

    private static GameState sortSelctionMenuState;
    private static GameState sizeSelectionMenuState;

    private static SortingAlgorithm selectedAlgorithm;

    private SortingSimulator() {
    }

    public static void createSortSelectionMenuState(GameStateManager gameStateManager, List<SortingAlgorithm[]> algorithmsList) {
        List<List<Pair<String, Runnable>>> menuActionsList = new ArrayList<>();
        for (SortingAlgorithm[] algorithms : algorithmsList) {
            menuActionsList.add(createSortSelectionActions(gameStateManager, algorithms));
        }
        sortSelctionMenuState = new ScaledMenuState(gameStateManager.getMouseTracker(), SORT_FONT_SMALL, 0, menuActionsList);
    }

    private static List<Pair<String, Runnable>> createSortSelectionActions(GameStateManager gameStateManager, SortingAlgorithm[] algorithms) {
        List<Pair<String, Runnable>> menuActions = new ArrayList<>();
        for (SortingAlgorithm algorithm : algorithms) {
            menuActions.add(Pair.valueOf(algorithm.getName(), () -> {
                SortingSimulator.setSelectedAlgorithm(algorithm);
                gameStateManager.setGameState(getSizeSelectionMenuState());
            }));
        }
        return menuActions;
    }

    public static GameState getSortSelectionMenuState() {
        return sortSelctionMenuState;
    }

    public static void createSizeSelectionMenuState(GameStateManager gameStateManager, int[] sizes) {
        List<Pair<String, ArrayType>> namedTypes = Arrays.asList(Pair.valueOf("Shuffled", ArrayType.SHUFFLED),
                Pair.valueOf("In Order", ArrayType.IN_ORDER),
                Pair.valueOf("Reversed", ArrayType.REVERSED),
                Pair.valueOf("Many Duplicates", ArrayType.MANY_DUPLICATES));
        List<List<Pair<String, Runnable>>> menuActionsList = new ArrayList<>();
        for (Pair<String, ArrayType> namedType : namedTypes) {
            List<Pair<String, Runnable>> menuActions = new ArrayList<>();
            menuActions.add(Pair.valueOf(namedType.getFirst(), () -> {
            }));
            for (int size : sizes) {
                menuActions.add(Pair.valueOf(Integer.toString(size), () -> gameStateManager.setGameState(
                        new SortingGameState(gameStateManager, SortingSimulator.getSelectedAlgorithm(), namedType.getSecond(), size))));
            }
            menuActionsList.add(menuActions);
        }
        sizeSelectionMenuState = new ScaledMenuState(gameStateManager.getMouseTracker(), SORT_FONT_LARGE, 0.1, menuActionsList);
    }

    public static GameState getSizeSelectionMenuState() {
        return sizeSelectionMenuState;
    }

    public static SortingAlgorithm getSelectedAlgorithm() {
        return selectedAlgorithm;
    }

    public static void setSelectedAlgorithm(SortingAlgorithm algorithm) {
        selectedAlgorithm = algorithm;
    }

    public static ThreadWorker getSortThreadWorker() {
        return sortThreadWorker;
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
}
