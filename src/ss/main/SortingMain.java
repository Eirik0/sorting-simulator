package ss.main;

import java.awt.Dimension;
import java.util.Arrays;

import gt.component.ComponentCreator;
import gt.component.GamePanel;
import gt.component.MainFrame;
import gt.gamestate.GameStateManager;
import ss.algorithm.BubbleSort;
import ss.algorithm.CocktailShakerSort;
import ss.algorithm.CombSort;
import ss.algorithm.CycleSort;
import ss.algorithm.GnomeSort;
import ss.algorithm.Heapsort;
import ss.algorithm.InsertionSort;
import ss.algorithm.InsertionSortBinarySearch;
import ss.algorithm.InsertionSortImproved;
import ss.algorithm.MergeSort;
import ss.algorithm.Quicksort;
import ss.algorithm.SelectionSort;
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
            new InsertionSortImproved(),
            new SelectionSort(),
    };
    private static final SortingAlgorithm[] FAST_SORTING_ALGORITHMS = new SortingAlgorithm[] {
            new CombSort(),
            new Heapsort(),
            new MergeSort(),
            new Quicksort(),
            new Shellsort(),
    };
    private static final SortingAlgorithm[] SLOWEST_SORTING_ALGORITHMS = new SortingAlgorithm[] {
            new StoogeSort()
    };

    private static final int[] ARRAY_SIZES = new int[] { 50, 100, 250, 500, 1000, 2500, 5000 };

    public static void main(String[] args) {
        ComponentCreator.setCrossPlatformLookAndFeel();

        GamePanel mainPanel = new GamePanel("Sort");
        mainPanel.setPreferredSize(new Dimension(ComponentCreator.DEFAULT_WIDTH, ComponentCreator.DEFAULT_HEIGHT));

        GameStateManager.setMainPanel(mainPanel);

        SortingSimulator.createSortSelectionMenuState(Arrays.asList(SLOW_SORTING_ALGORITHMS, FAST_SORTING_ALGORITHMS, SLOWEST_SORTING_ALGORITHMS));
        SortingSimulator.createSizeSelectionMenuState(ARRAY_SIZES);

        GameStateManager.setGameState(SortingSimulator.getSortSelectionMenuState());

        MainFrame mainFrame = new MainFrame(TITLE, mainPanel);

        mainFrame.show();
    }
}
