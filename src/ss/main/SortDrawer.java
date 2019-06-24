package ss.main;

import gt.component.ComponentCreator;
import gt.gameentity.Drawable;
import gt.gameentity.IGraphics;
import gt.gameentity.Sizable;
import gt.util.EMath;
import ss.array.ComplexityCounter;
import ss.array.Memory;
import ss.array.SInteger;

public class SortDrawer implements Drawable, Sizable {
    private static final int TITLE_HEIGHT = 60;

    private int width;
    private int height;

    public SortDrawer() {
    }

    @Override
    public void drawOn(IGraphics g) {
        g.fillRect(0, 0, width, height, ComponentCreator.backgroundColor());

        String accessTime = String.format("access:  %.3fms", Double.valueOf(SortingSimulator.getAccessTime()));
        String compareTime = String.format("compare: %.3fms", Double.valueOf(SortingSimulator.getCompareTime()));
        String insertTime = String.format("insert:  %.3fms", Double.valueOf(SortingSimulator.getInsertTime()));
        g.setFont(SortingSimulator.SORT_FONT_SMALL);
        g.setColor(SInteger.ACCESS_COLOR);
        g.drawString(accessTime, 15, 15);
        g.setColor(SInteger.COMPARE_COLOR);
        g.drawString(compareTime, 15, 35);
        g.setColor(SInteger.INSERT_COLOR);
        g.drawString(insertTime, 15, 55);

        g.setColor(SInteger.ELEMENT_COLOR);
        g.drawCenteredString(SortingSimulator.getCurrentAlgorithmName(), width / 2.0, TITLE_HEIGHT * .25);

        String subTitle = String.format("accesses:%5d    comparisons:%5d    insertions:%5d",
                ComplexityCounter.getNumAccesses(), ComplexityCounter.getNumCompares(), ComplexityCounter.getNumInserts());
        g.setColor(ComponentCreator.foregroundColor());
        g.drawCenteredString(subTitle, width / 2.0, TITLE_HEIGHT * .75);

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
                int actualElementWidth = EMath.round((i + columnNum + 1) * elementWidth) - EMath.round(x0);
                g.fillRect(x0, y0 - elementHeight, actualElementWidth, elementHeight, element.getColor());
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
}
