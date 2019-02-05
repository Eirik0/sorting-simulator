package ss.main;

import java.awt.Graphics2D;

import gt.component.ComponentCreator;
import gt.gamestate.GameState;
import gt.gamestate.GameStateManager;
import gt.gamestate.UserInput;
import ss.algorithm.SortingAlgorithm;
import ss.array.ComplexityCounter;
import ss.array.Memory;
import ss.array.SArray;
import ss.array.SArray.ArrayType;
import ss.array.SInteger;
import ss.array.TimeManager;
import ss.interrupt.SortStopException;
import ss.interrupt.SortStopper;

public class SortingGameState implements GameState {
    private static final int TITLE_HEIGHT = 60;

    private final SArray array;
    private final String algorithmName;

    private int width;
    private int height;

    public SortingGameState(SortingAlgorithm algorithm, ArrayType arrayType, int arrayLength) {
        Memory.clear();
        ComplexityCounter.reset();
        TimeManager.reset();
        array = new SArray(arrayType, arrayLength);
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
        graphics.setColor(SInteger.ACCESS_COLOR);
        graphics.drawString(accessTime, 15, 15);
        graphics.setColor(SInteger.COMPARE_COLOR);
        graphics.drawString(compareTime, 15, 35);
        graphics.setColor(SInteger.INSERT_COLOR);
        graphics.drawString(insertTime, 15, 55);

        graphics.setColor(SInteger.ELEMENT_COLOR);
        drawCenteredString(graphics, SortingSimulator.SORT_FONT_LARGE, algorithmName, width / 2.0, TITLE_HEIGHT * .25);

        String subTitle = String.format("accesses:%5d    comparisons:%5d    inserts:%5d",
                ComplexityCounter.getNumAccesses(), ComplexityCounter.getNumCompares(), ComplexityCounter.getNumInserts());
        graphics.setColor(ComponentCreator.foregroundColor());
        drawCenteredString(graphics, SortingSimulator.SORT_FONT_SMALL, subTitle, width / 2.0, TITLE_HEIGHT * .75);

        int[] colsAndRows = calculateColsAndRows();
        int colsToDraw = colsAndRows[0];
        int rowsToDraw = colsAndRows[1];

        double arrayHeight = (double) (height - TITLE_HEIGHT) / rowsToDraw;
        double elementWidth = (double) width / colsToDraw;
        double elementUnitHeight = arrayHeight / colsToDraw;
        double y0 = arrayHeight + TITLE_HEIGHT;
        int columnNum = 0;
        for (int rowNum = 0; rowNum < Memory.numRows(); ++rowNum) {
            SInteger[] row = Memory.getRow(rowNum);
            for (int i = 0; i < row.length; ++i) {
                SInteger element = row[i];
                double elementHeight = element.value * elementUnitHeight;
                double x0 = (i + columnNum) * elementWidth;
                int actualElementWidth = round((i + columnNum + 1) * elementWidth) - round(x0);
                fillRect(graphics, x0, y0 - elementHeight, actualElementWidth, elementHeight, element.getColor());
            }

            if (rowNum < Memory.numRows() - 1 && columnNum + row.length + Memory.getRow(rowNum + 1).length < colsToDraw) {
                columnNum += row.length;
            } else {
                y0 += arrayHeight;
                columnNum = 0;
            }
        }
    }

    private static int[] calculateColsAndRows() {
        int memoryCols = 0;
        for (int row = 0; row < Memory.numRows(); ++row) {
            memoryCols = Math.max(memoryCols, Memory.getRow(row).length);
        }
        int columnNum = 0;
        int memoryRows = 0;
        for (int rowNum = 0; rowNum < Memory.numRows(); ++rowNum) {
            SInteger[] row = Memory.getRow(rowNum);
            if (rowNum < Memory.numRows() - 1 && columnNum + row.length + Memory.getRow(rowNum + 1).length < memoryCols) {
                columnNum += row.length;
            } else {
                ++memoryRows;
                columnNum = 0;
            }
        }
        return new int[] { memoryCols, memoryRows };
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
