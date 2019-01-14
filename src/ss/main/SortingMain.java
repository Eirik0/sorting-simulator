package ss.main;

import java.awt.Dimension;

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
import ss.algorithm.InsertionSortImproved;
import ss.algorithm.MergeSort;
import ss.algorithm.Quicksort;
import ss.algorithm.SelectionSort;
import ss.algorithm.Shellsort;
import ss.algorithm.SortingAlgorithm;
import ss.algorithm.StoogeSort;

public class SortingMain {
    private static final String TITLE = "Sorting Simulator";

    private static final SortingAlgorithm[] SORTING_ALGORITHMS = new SortingAlgorithm[] {
            new BubbleSort(),
            new CocktailShakerSort(),
            new CombSort(),
            new CycleSort(),
            new GnomeSort(),
            new Heapsort(),
            new InsertionSort(),
            new InsertionSortImproved(),
            new MergeSort(),
            new Quicksort(),
            new SelectionSort(),
            new Shellsort(),
            new StoogeSort()
    };

    private static final int[] ARRAY_SIZES = new int[] { 50, 100, 250, 500, 1000, 2500, 5000 };

    public static void main(String[] args) {
        ComponentCreator.setCrossPlatformLookAndFeel();

        GamePanel mainPanel = new GamePanel("Sort");
        mainPanel.setPreferredSize(new Dimension(ComponentCreator.DEFAULT_WIDTH, ComponentCreator.DEFAULT_HEIGHT));

        GameStateManager.setMainPanel(mainPanel);

        SortingSimulator.createSortSelectionMenuState(SORTING_ALGORITHMS);
        SortingSimulator.createSizeSelectionMenuState(ARRAY_SIZES);

        GameStateManager.setGameState(SortingSimulator.getSortSelectionMenuState());

        MainFrame mainFrame = new MainFrame(TITLE, mainPanel);

        mainFrame.show();
    }
}
