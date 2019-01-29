package ss.main;

import java.awt.Graphics2D;

import gt.component.ComponentCreator;
import gt.gamestate.GameState;
import gt.gamestate.GameStateManager;
import gt.gamestate.UserInput;
import ss.algorithm.SortingAlgorithm;
import ss.array.ComplexityCounter;
import ss.array.Memory;
import ss.array.SortableArray;
import ss.array.SortableArray.ArrayType;
import ss.array.SortableElement;
import ss.array.TimeManager;
import ss.interrupt.SortStopException;
import ss.interrupt.SortStopper;

public class SortingGameState implements GameState {
    private static final int TITLE_HEIGHT = 60;

    private final SortableArray array;
    private final String algorithmName;

    private int width;
    private int height;

    public SortingGameState(SortingAlgorithm algorithm, int arrayLength) {
        Memory.clear();
        ComplexityCounter.reset();
        TimeManager.reset();
        array = new SortableArray(ArrayType.SHUFFLED, arrayLength);
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
        TimeManager.addTime(dt);
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
                ComplexityCounter.getNumAccesses(), ComplexityCounter.getNumCompares(), ComplexityCounter.getNumInserts());
        graphics.setColor(ComponentCreator.foregroundColor());
        drawCenteredString(graphics, SortingSimulator.SORT_FONT_SMALL, subTitle, width / 2.0, TITLE_HEIGHT * .75);

        int memoryRows = Memory.numRows();
        int memoryCols = 0;
        for (int row = 0; row < memoryRows; ++row) {
            memoryCols = Math.max(memoryCols, Memory.getRow(row).length);
        }
        double arrayHeight = (double) (height - TITLE_HEIGHT) / memoryRows;
        double elementWidth = (double) width / memoryCols;
        double elementUnitHeight = arrayHeight / memoryCols;
        double y0 = arrayHeight + TITLE_HEIGHT;
        for (int rowNum = 0; rowNum < memoryRows; ++rowNum) {
            SortableElement[] row = Memory.getRow(rowNum);
            for (int i = 0; i < row.length; ++i) {
                SortableElement element = row[i];
                double elementHeight = element.value * elementUnitHeight;
                double x0 = i * elementWidth;
                int actualElementWidth = round((i + 1) * elementWidth) - round(x0);
                fillRect(graphics, x0, y0 - elementHeight, actualElementWidth, elementHeight, element.getColor());
            }
            y0 += arrayHeight;
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
            TimeManager.requestStop();
            GameStateManager.setGameState(SortingSimulator.getSortSelectionMenuState());
            break;
        }
    }
}
