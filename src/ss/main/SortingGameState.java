package ss.main;

import java.awt.Graphics2D;

import gt.component.ComponentCreator;
import gt.gamestate.GameState;
import gt.gamestate.GameStateManager;
import gt.gamestate.UserInput;
import ss.algorithm.SortingAlgorithm;
import ss.array.SortableArray;
import ss.array.SortableElement;
import ss.interrupt.SortStopException;
import ss.interrupt.SortStopper;

public class SortingGameState implements GameState {
    private static final int TITLE_HEIGHT = 60;

    private final SortableArray array;
    private final String algorithmName;

    private int width;
    private int height;

    public SortingGameState(SortingAlgorithm algorithm, int arrayLength) {
        array = new SortableArray(arrayLength);
        algorithmName = algorithm.getName();
        SortingSimulator.getSortThreadWorker().workOn(() -> {
            try {
                SortStopper.sortStarted();
                algorithm.sort(array);
            } catch (SortStopException e) {
            } finally {
                array.searchStopped();
            }
        });
    }

    @Override
    public void update(double dt) {
        array.update(dt);
    }

    @Override
    public void drawOn(Graphics2D graphics) {
        fillRect(graphics, 0, 0, width, height, ComponentCreator.backgroundColor());

        String accessTime = String.format("access:  %.3fms", Double.valueOf(SortingSimulator.getAccessTime()));
        String compareTime = String.format("compare: %.3fms", Double.valueOf(SortingSimulator.getCompareTime()));
        String insertTime = String.format("insert:  %.3fms", Double.valueOf(SortingSimulator.getInsertTime()));
        graphics.setFont(SortingSimulator.SORT_FONT_SMALL);
        graphics.setColor(SortableElement.ACCESS_COLOR);
        graphics.drawString(accessTime, 15, 15);
        graphics.setColor(SortableElement.COMPARE_COLOR);
        graphics.drawString(compareTime, 15, 35);
        graphics.setColor(SortableElement.INSERT_COLOR);
        graphics.drawString(insertTime, 15, 55);

        graphics.setColor(SortableElement.ELEMENT_COLOR);
        drawCenteredString(graphics, SortingSimulator.SORT_FONT_LARGE, algorithmName, width / 2.0, TITLE_HEIGHT * .25);

        String subTitle = String.format("accesses:%5d    comparisons:%5d    inserts:%5d",
                Long.valueOf(array.getNumAccesses()), Long.valueOf(array.getNumCompares()), Long.valueOf(array.getNumInserts()));
        graphics.setColor(ComponentCreator.foregroundColor());
        drawCenteredString(graphics, SortingSimulator.SORT_FONT_SMALL, subTitle, width / 2.0, TITLE_HEIGHT * .75);

        int length = array.length();
        int numArrays = 1 + array.numChildren();
        double elementWidth = (double) width / length;
        double arrayHeight = (double) (height - TITLE_HEIGHT) / numArrays;
        double elementUnitHeight = arrayHeight / length;
        SortableArray arrayToDraw = array;
        double y0 = arrayHeight + TITLE_HEIGHT;
        for (int arrayNum = 0; arrayNum < numArrays; ++arrayNum) {
            for (int i = 0; i < length; ++i) {
                double elementHeight = arrayToDraw.getValue(i) * elementUnitHeight;
                double x0 = i * elementWidth;
                int actualElementWidth = round((i + 1) * elementWidth) - round(x0);
                fillRect(graphics, x0, y0 - elementHeight, actualElementWidth, elementHeight, arrayToDraw.getColor(i));
            }
            y0 += arrayHeight;
            arrayToDraw = arrayToDraw.getChildArray();
            if (arrayToDraw == null) {
                break;
            }
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
        case MINUS_KEY_PRESSED:
            SortingSimulator.setAccessTime(SortingSimulator.getAccessTime() * 1.25);
            SortingSimulator.setInsertTime(SortingSimulator.getInsertTime() * 1.25);
            SortingSimulator.setCompareTime(SortingSimulator.getCompareTime() * 1.25);
            break;
        case EQUALS_KEY_PRESSED:
            SortingSimulator.setAccessTime(SortingSimulator.getAccessTime() / 1.25);
            SortingSimulator.setInsertTime(SortingSimulator.getInsertTime() / 1.25);
            SortingSimulator.setCompareTime(SortingSimulator.getCompareTime() / 1.25);
            break;
        case ESC_KEY_PRESSED:
            SortingSimulator.getSortThreadWorker().waitForStart();
            SortStopper.requestStop();
            array.requestStop();
            GameStateManager.setGameState(SortingSimulator.getSortSelectionMenuState());
            break;
        }
    }
}
