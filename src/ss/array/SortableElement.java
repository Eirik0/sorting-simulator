package ss.array;

import java.awt.Color;

import gt.gameentity.DrawingMethods;
import gt.gameloop.TimeConstants;

public class SortableElement {
    private static final long FADE_TIME = TimeConstants.NANOS_PER_SECOND;

    public static final Color ELEMENT_COLOR = Color.GREEN;
    public static final Color ACCESS_COLOR = Color.CYAN;
    public static final Color COMPARE_COLOR = Color.MAGENTA;
    public static final Color INSERT_COLOR = Color.RED;

    public final int value;
    long lastAccess;
    long lastCompare;
    long lastInsert;

    public SortableElement(int value) {
        this(value, 0, 0, 0);
    }

    private SortableElement(int value, long lastAccess, long lastCompare, long lastInsert) {
        this.value = value;
        this.lastAccess = lastAccess;
        this.lastCompare = lastCompare;
        this.lastInsert = lastInsert;
    }

    public SortableElement copy() {
        return new SortableElement(value, 0, 0, 0);
    }

    public Color getColor() {
        long timeNow = System.nanoTime();
        Color[] colors = new Color[3];
        int numColors = 0;
        double accessPercent = getColorPercent(timeNow, lastAccess);
        double comparePercent = getColorPercent(timeNow, lastCompare);
        double insertPercent = getColorPercent(timeNow, lastInsert);
        if (accessPercent < 1) {
            colors[numColors++] = DrawingMethods.fadeToColorS(ACCESS_COLOR, ELEMENT_COLOR, accessPercent);
        }
        if (comparePercent < 1) {
            colors[numColors++] = DrawingMethods.fadeToColorS(COMPARE_COLOR, ELEMENT_COLOR, comparePercent);
        }
        if (insertPercent < 1) {
            colors[numColors++] = DrawingMethods.fadeToColorS(INSERT_COLOR, ELEMENT_COLOR, insertPercent);
        }
        if (numColors == 0) {
            return ELEMENT_COLOR;
        } else if (numColors == 1) {
            return colors[0];
        } else {
            return colorAverage(colors, numColors);
        }
    }

    private static double getColorPercent(double timeNow, long timeThen) {
        return Math.min((timeNow - timeThen) / FADE_TIME, 1);
    }

    private static Color colorAverage(Color[] colors, int numColors) {
        double red = colors[0].getRed();
        double green = colors[0].getRed();
        double blue = colors[0].getRed();
        for (int i = 1; i < numColors; ++i) {
            red += colors[i].getRed();
            green += colors[i].getGreen();
            blue += colors[i].getBlue();
        }
        return new Color(DrawingMethods.roundS(red / numColors), DrawingMethods.roundS(green / numColors), DrawingMethods.roundS(blue / numColors));
    }
}
