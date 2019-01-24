package ss.main;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gt.async.ThreadWorker;
import gt.gamestate.GameState;
import gt.gamestate.GameStateManager;
import gt.gamestate.menu.ScaledMenuState;
import gt.util.Pair;
import ss.algorithm.SortingAlgorithm;

public class SortingSimulator {
    public static final Font SORT_FONT_LARGE = new Font(Font.MONOSPACED, Font.BOLD, 24);
    public static final Font SORT_FONT_SMALL = new Font(Font.MONOSPACED, Font.PLAIN, 16);

    private static double accessTime = 1;
    private static double insertTime = 1;
    private static double compareTime = 1;

    private static final ThreadWorker sortThreadWorker = new ThreadWorker();

    private static GameState sortSelctionMenuState;
    private static GameState sizeSelectionMenuState;

    private static SortingAlgorithm selectedAlgorithm;

    private SortingSimulator() {
    }

    public static void createSortSelectionMenuState(List<SortingAlgorithm[]> algorithmsList) {
        List<List<Pair<String, Runnable>>> menuActionsList = new ArrayList<>();
        for (SortingAlgorithm[] algorithms : algorithmsList) {
            menuActionsList.add(createSortSelectionActions(algorithms));
        }
        sortSelctionMenuState = new ScaledMenuState(GameStateManager.getMouseTracker(), SORT_FONT_SMALL, menuActionsList);
    }

    private static List<Pair<String, Runnable>> createSortSelectionActions(SortingAlgorithm[] algorithms) {
        List<Pair<String, Runnable>> menuActions = new ArrayList<>();
        for (SortingAlgorithm algorithm : algorithms) {
            menuActions.add(Pair.valueOf(algorithm.getName(), () -> {
                SortingSimulator.setSelectedAlgorithm(algorithm);
                GameStateManager.setGameState(getSizeSelectionMenuState());
            }));
        }
        return menuActions;
    }

    public static GameState getSortSelectionMenuState() {
        return sortSelctionMenuState;
    }

    public static void createSizeSelectionMenuState(int[] sizes) {
        List<Pair<String, Runnable>> menuActions = new ArrayList<>();
        for (int size : sizes) {
            Runnable action = () -> GameStateManager.setGameState(new SortingGameState(SortingSimulator.getSelectedAlgorithm(), size));
            menuActions.add(Pair.valueOf(Integer.toString(size), action));
        }
        sizeSelectionMenuState = new ScaledMenuState(GameStateManager.getMouseTracker(), SORT_FONT_LARGE, Collections.singletonList(menuActions));
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
