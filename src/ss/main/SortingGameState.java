package ss.main;

import java.awt.Graphics2D;

import gt.component.ComponentCreator;
import gt.gamestate.GameState;
import gt.gamestate.GameStateManager;
import gt.gamestate.UserInput;
import ss.algorithm.SortingAlgorithm;
import ss.array.SortStopException;
import ss.array.SortableArray;

public class SortingGameState implements GameState {
    private static final int TITLE_HEIGHT = 60;

    private final SortableArray array;
    private final SortingAlgorithm algorithm;

    private int width;
    private int height;

    public SortingGameState(SortingAlgorithm algorithm, int arrayLength) {
        array = new SortableArray(arrayLength);
        this.algorithm = algorithm;
        new Thread(() -> {
            try {
                algorithm.sort(array);
            } catch (SortStopException e) {
            }
        }, "Sort_Thread").start();
    }

    @Override
    public void update(double dt) {
        // Do nothing
    }

    @Override
    public void drawOn(Graphics2D graphics) {
        fillRect(graphics, 0, 0, width, height, ComponentCreator.backgroundColor());

        graphics.setColor(ComponentCreator.foregroundColor());
        drawCenteredString(graphics, SortingSimulator.SORT_FONT_LARGE, algorithm.getName(), width / 2.0, TITLE_HEIGHT * .25);
        String subTitle = String.format("accesses:%5d    comparisons:%5d    inserts:%5d",
                Integer.valueOf(array.getNumAccesses()), Integer.valueOf(array.getNumCompares()), Integer.valueOf(array.getNumInserts()));
        drawCenteredString(graphics, SortingSimulator.SORT_FONT_SMALL, subTitle, width / 2.0, TITLE_HEIGHT * .75);

        int length = array.length();
        double elementWidth = (double) width / length;
        double elementUnitHeight = (double) (height - TITLE_HEIGHT) / length;
        for (int i = 0; i < length; ++i) {
            double elementHeight = array.getValue(i) * elementUnitHeight;
            double x0 = i * elementWidth;
            int actualElementWidth = round((i + 1) * elementWidth) - round(x0);
            fillRect(graphics, x0, height - elementHeight, actualElementWidth, elementHeight, array.getColor(i));
        }
    }

    @Override
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public void handleUserInput(UserInput input) {
        switch (input) {
        case ESC_KEY_PRESSED:
            algorithm.requestStop();
            GameStateManager.setGameState(SortingSimulator.getSortSelectionMenuState());
        }
    }
}
