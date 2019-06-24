package ss.array;

import java.awt.Color;

import gt.gameentity.DrawingMethods;
import gt.gameloop.TimeConstants;
import gt.settings.ColorSetting;
import gt.settings.GameSettings;
import gt.util.EMath;
import ss.main.SortingSimulator;

public class SInteger {
    private static final long FADE_TIME = TimeConstants.NANOS_PER_SECOND;

    public static final Color ELEMENT_COLOR = GameSettings.getValue("ss.element", new ColorSetting(Color.GREEN));
    public static final Color ACCESS_COLOR = GameSettings.getValue("ss.access", new ColorSetting(Color.CYAN));
    public static final Color COMPARE_COLOR = GameSettings.getValue("ss.compare", new ColorSetting(Color.MAGENTA));
    public static final Color INSERT_COLOR = GameSettings.getValue("ss.insert", new ColorSetting(Color.RED));

    public final int value;

    long lastAccess;
    long lastCompare;
    long lastInsert;

    public SInteger(int value) {
        this(value, 0, 0, 0);
    }

    private SInteger(int value, long lastAccess, long lastCompare, long lastInsert) {
        this.value = value;
        this.lastAccess = lastAccess;
        this.lastCompare = lastCompare;
        this.lastInsert = lastInsert;
    }

    public SInteger copy() {
        return new SInteger(value, lastAccess, lastCompare, lastInsert);
    }

    public Color getColor() {
        long timeNow = System.nanoTime();
        Color[] colors = new Color[3];
        int numColors = 0;
        double accessPercent = getColorPercent(timeNow, SortingSimulator.getAccessTime() * TimeConstants.NANOS_PER_MILLISECOND, lastAccess);
        double comparePercent = getColorPercent(timeNow, SortingSimulator.getCompareTime() * TimeConstants.NANOS_PER_MILLISECOND, lastCompare);
        double insertPercent = getColorPercent(timeNow, SortingSimulator.getInsertTime() * TimeConstants.NANOS_PER_MILLISECOND, lastInsert);
        if (accessPercent < 1) {
            colors[numColors++] = DrawingMethods.fadeToColor(ACCESS_COLOR, ELEMENT_COLOR, accessPercent);
        }
        if (comparePercent < 1) {
            colors[numColors++] = DrawingMethods.fadeToColor(COMPARE_COLOR, ELEMENT_COLOR, comparePercent);
        }
        if (insertPercent < 1) {
            colors[numColors++] = DrawingMethods.fadeToColor(INSERT_COLOR, ELEMENT_COLOR, insertPercent);
        }
        if (numColors == 0) {
            return ELEMENT_COLOR;
        } else if (numColors == 1) {
            return colors[0];
        } else {
            return colorAverage(colors, numColors);
        }
    }

    private static double getColorPercent(double timeNow, double maybeFade, long timeThen) {
        return Math.min((timeNow - timeThen) / Math.max(FADE_TIME, maybeFade), 1);
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
        return new Color(EMath.round(red / numColors), EMath.round(green / numColors), EMath.round(blue / numColors));
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
