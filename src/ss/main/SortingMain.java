package ss.main;

import java.awt.Dimension;

import gt.component.ComponentCreator;
import gt.component.GamePanel;
import gt.component.MainFrame;
import gt.gamestate.GameStateManager;
import ss.algorithm.BinaryTreeSort;
import ss.algorithm.BubbleSort;
import ss.algorithm.CocktailShakerSort;
import ss.algorithm.CombSort;
import ss.algorithm.CycleSort;
import ss.algorithm.GnomeSort;
import ss.algorithm.Heapsort;
import ss.algorithm.InsertionSort;
import ss.algorithm.InsertionSortBinarySearch;
import ss.algorithm.InsertionSortPair;
import ss.algorithm.InsertionSortRootTuples;
import ss.algorithm.Introsort;
import ss.algorithm.MergeSort;
import ss.algorithm.MergeSortBottomUp;
import ss.algorithm.PancakeSort;
import ss.algorithm.Quicksort;
import ss.algorithm.QuicksortDualPivot;
import ss.algorithm.QuicksortMedianPivot;
import ss.algorithm.RadixSortLSDBaseTwo;
import ss.algorithm.RadixSortMSDBase16;
import ss.algorithm.SelectionSort;
import ss.algorithm.SelectionSortLeftRight;
import ss.algorithm.SelectionSortTwoMinimums;
import ss.algorithm.Shellsort;
import ss.algorithm.SortingAlgorithm;
import ss.algorithm.StoogeSort;

public class SortingMain {
    private static final String TITLE = "Sorting Simulator";

    private static final SortingAlgorithm[] SLOW_SORTING_ALGORITHMS = new SortingAlgorithm[] {
            new BubbleSort(),
            new CocktailShakerSort(),
            new CycleSort(),
            new GnomeSort(),
            new InsertionSort(),
            new InsertionSortBinarySearch(),
            new InsertionSortPair(),
            new SelectionSort(),
    };

    private static final SortingAlgorithm[] FAST_SORTING_ALGORITHMS = new SortingAlgorithm[] {
            new BinaryTreeSort(),
            new CombSort(),
            new Heapsort(),
            new Introsort(),
            new MergeSort(),
            new MergeSortBottomUp(),
            new Quicksort(),
            new QuicksortDualPivot(),
            new QuicksortMedianPivot(),
            new Shellsort(),
    };

    private static final SortingAlgorithm[] SLOWEST_SORTING_ALGORITHMS = new SortingAlgorithm[] {
            new PancakeSort(),
            new StoogeSort()
    };

    private static final SortingAlgorithm[] NON_COMPARISON_ALGORITHMS = new SortingAlgorithm[] {
            new RadixSortLSDBaseTwo(),
            new RadixSortMSDBase16()
    };

    private static final SortingAlgorithm[] CUSTOM_ALGORITHMS = new SortingAlgorithm[] {
            new InsertionSortRootTuples(),
            new SelectionSortLeftRight(),
            new SelectionSortTwoMinimums()
    };

    public static void main(String[] args) {
        SortingSimulator.registerSorts(SLOW_SORTING_ALGORITHMS);
        SortingSimulator.registerSorts(FAST_SORTING_ALGORITHMS);
        SortingSimulator.registerSorts(SLOWEST_SORTING_ALGORITHMS);
        SortingSimulator.registerSorts(NON_COMPARISON_ALGORITHMS);
        SortingSimulator.registerSorts(CUSTOM_ALGORITHMS);
        SortingSimulator.registerArrayTypes();

        ComponentCreator.setCrossPlatformLookAndFeel();

        GamePanel mainPanel = new GamePanel("Sort");
        mainPanel.setPreferredSize(new Dimension(ComponentCreator.DEFAULT_WIDTH, ComponentCreator.DEFAULT_HEIGHT));

        GameStateManager gameStateManager = mainPanel.getGameStateManager();
        gameStateManager.setGameState(new SortingSimulationState(gameStateManager));

        MainFrame mainFrame = new MainFrame(TITLE, mainPanel);
        mainFrame.show();
    }
}
