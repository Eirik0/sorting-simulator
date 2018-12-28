package ss.main;

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import gt.gamestate.GameState;
import gt.gamestate.GameStateManager;
import gt.gamestate.menu.ScaledMenuState;
import gt.util.Pair;
import ss.algorithm.SortingAlgorithm;

public class SortingSimulator {
    public static final Font SORT_FONT_LARGE = new Font(Font.MONOSPACED, Font.BOLD, 24);
    public static final Font SORT_FONT_SMALL = new Font(Font.MONOSPACED, Font.PLAIN, 16);

    private static GameState sortSelctionMenuState;
    private static GameState sizeSelectionMenuState;

    private static SortingAlgorithm selectedAlgorithm;

    private SortingSimulator() {
    }

    public static void createSortSelectionMenuState(SortingAlgorithm[] algorithms) {
        List<Pair<String, Runnable>> menuActions = new ArrayList<>();
        for (SortingAlgorithm algorithm : algorithms) {
            menuActions.add(Pair.valueOf(algorithm.getName(), () -> {
                SortingSimulator.setSelectedAlgorithm(algorithm);
                GameStateManager.setGameState(getSizeSelectionMenuState());
            }));
        }
        sortSelctionMenuState = new ScaledMenuState(GameStateManager.getMouseTracker(), SORT_FONT_LARGE, menuActions);
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
        sizeSelectionMenuState = new ScaledMenuState(GameStateManager.getMouseTracker(), SORT_FONT_LARGE, menuActions);
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

    public static int getAccessTime() {
        return 1;
    }

    public static int getInsertTime() {
        return 1;
    }

    public static int getCompareTime() {
        return 1;
    }
}
