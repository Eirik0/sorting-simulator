package ss.main;

import gt.component.ComponentCreator;
import gt.ecomponent.EBackground;
import gt.ecomponent.EComponentLocation;
import gt.ecomponent.EComponentPanel;
import gt.ecomponent.EComponentPanelBuilder;
import gt.ecomponent.ETextLabel;
import gt.ecomponent.button.EButton;
import gt.ecomponent.list.EComboBox;
import gt.ecomponent.location.EFixedLocation;
import gt.ecomponent.location.ESizableComponentLocation;
import gt.ecomponent.slider.ESlider;
import gt.gameentity.GameImageDrawer;
import gt.gameentity.IGameImage;
import gt.gameentity.IGraphics;
import gt.gameloop.TimeConstants;
import gt.gamestate.GameState;
import gt.gamestate.GameStateManager;
import gt.gamestate.UserInput;
import gt.settings.DoubleSetting;
import gt.settings.GameSettings;
import gt.util.EMath;
import ss.algorithm.SortingAlgorithm;
import ss.array.SArray;
import ss.array.SArray.ArrayType;
import ss.array.TimeManager;
import ss.interrupt.SortStopper;

public class SortingSimulationState implements GameState {
    private static final double UI_WIDTH_SCALE = GameSettings.getDouble("ss.uiscale.width", new DoubleSetting(Double.valueOf(1)));
    private static final double UI_HEIGHT_SCALE = GameSettings.getDouble("ss.uiscale.height", new DoubleSetting(Double.valueOf(1)));

    private static final double PADDING = 5;
    private static final double PADDING_2 = 3 * PADDING;
    private static final double CB_HEIGHT = 25;
    private static final double SELECTION_LABEL_WIDTH = 75;
    private static final double TS_HEIGHT = (2 * CB_HEIGHT - PADDING) / 3;

    private final GameImageDrawer imageDrawer;

    private final SortDrawer sortingState;
    private final IGameImage sortingStateImage;

    private final ESizableComponentLocation cpBackgroundLoc;
    private final EComponentPanel componentPanel;

    private SortingAlgorithm selectedAlgorithm;
    private ArrayType selectedType;
    private int selectedLength;
    private SArray array;
    private double uiHeight;
    private ESlider accessTimeSlider;
    private ESlider compareTimeSlider;
    private ESlider insertTimeSlider;

    public SortingSimulationState(GameStateManager gameStateManager) {
        String[] allAlgorithms = SortingSimulator.getAlgorithmNames();
        String[] allArrayTypes = SortingSimulator.getArrayTypeNames();
        selectedAlgorithm = SortingSimulator.getAlgorithm(allAlgorithms[0]);
        selectedType = SortingSimulator.getArrayType(allArrayTypes[0]);
        selectedLength = 25;
        array = new SArray(selectedType, selectedLength);

        sortingState = new SortDrawer();
        imageDrawer = gameStateManager.getImageDrawer();

        EComponentLocation algorithmLabelLoc = EFixedLocation.fromRect(PADDING, PADDING, SELECTION_LABEL_WIDTH, CB_HEIGHT);
        EComponentLocation algorithmCBLoc = EFixedLocation.fromRect(algorithmLabelLoc.getX1() + 1 + PADDING, PADDING, 200, CB_HEIGHT);
        EComponentLocation arrayLabelLoc = EFixedLocation.fromRect(PADDING, algorithmLabelLoc.getY1() + 1 + PADDING, SELECTION_LABEL_WIDTH, CB_HEIGHT);
        EComponentLocation arrayCBLoc = EFixedLocation.fromRect(arrayLabelLoc.getX1() + 1 + PADDING, algorithmLabelLoc.getY1() + 1 + PADDING, 100, CB_HEIGHT);

        EComponentLocation sizeLabelLoc = EFixedLocation.fromRect(algorithmCBLoc.getX1() + 1 + PADDING_2, (CB_HEIGHT + 3 * PADDING) / 2, 50, CB_HEIGHT);
        EComponentLocation sizeSliderLoc = EFixedLocation.fromRect(sizeLabelLoc.getX1() + 1 + PADDING_2, sizeLabelLoc.getY0(), 200, CB_HEIGHT);
        EComponentLocation sliderAmountLabelLoc = EFixedLocation.fromRect(sizeSliderLoc.getX0(), sizeSliderLoc.getY1() + 1 + PADDING, 200, TS_HEIGHT);
        ETextLabel sliderAmountLabel = new ETextLabel(sliderAmountLabelLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), Integer.toString(selectedLength), true);

        EComponentLocation accLabelLoc = EFixedLocation.fromRect(sizeSliderLoc.getX1() + 1 + PADDING_2, PADDING, 50, TS_HEIGHT);
        EComponentLocation accSliderLoc = EFixedLocation.fromRect(accLabelLoc.getX1() + 1 + PADDING_2, PADDING, 150, TS_HEIGHT);
        EComponentLocation comLabelLoc = EFixedLocation.fromRect(sizeSliderLoc.getX1() + 1 + PADDING_2, accLabelLoc.getY1() + 1 + PADDING, 50, TS_HEIGHT);
        EComponentLocation comSliderLoc = EFixedLocation.fromRect(accLabelLoc.getX1() + 1 + PADDING_2, accLabelLoc.getY1() + 1 + PADDING, 150, TS_HEIGHT);
        EComponentLocation insLabelLoc = EFixedLocation.fromRect(sizeSliderLoc.getX1() + 1 + PADDING_2, comSliderLoc.getY1() + 1 + PADDING, 50, TS_HEIGHT);
        EComponentLocation insSliderLoc = EFixedLocation.fromRect(accLabelLoc.getX1() + 1 + PADDING_2, comSliderLoc.getY1() + 1 + PADDING, 150, TS_HEIGHT);

        EComponentLocation startButtonLoc = EFixedLocation.fromRect(accSliderLoc.getX1() + 1 + PADDING_2, PADDING, 50, TS_HEIGHT);
        EComponentLocation stopButtonLoc = EFixedLocation.fromRect(accSliderLoc.getX1() + 1 + PADDING_2, startButtonLoc.getY1() + 1 + PADDING, 50, TS_HEIGHT);
        EComponentLocation resetButtonLoc = EFixedLocation.fromRect(accSliderLoc.getX1() + 1 + PADDING_2, stopButtonLoc.getY1() + 1 + PADDING, 50, TS_HEIGHT);

        uiHeight = resetButtonLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE).getY1() + PADDING;

        sortingStateImage = imageDrawer.newGameImage(ComponentCreator.DEFAULT_WIDTH, EMath.round(ComponentCreator.DEFAULT_HEIGHT - uiHeight));

        cpBackgroundLoc = new ESizableComponentLocation(0, 0, ComponentCreator.DEFAULT_WIDTH, ComponentCreator.DEFAULT_HEIGHT - uiHeight);

        accessTimeSlider = new ESlider(accSliderLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), 1, EMath.round(TimeConstants.NANOS_PER_MILLISECOND), 10000,
                time -> SortingSimulator.setAccessTime(time / 1000.0));
        compareTimeSlider = new ESlider(comSliderLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), 1, EMath.round(TimeConstants.NANOS_PER_MILLISECOND), 10000,
                time -> SortingSimulator.setCompareTime(time / 1000.0));
        insertTimeSlider = new ESlider(insSliderLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), 1, EMath.round(TimeConstants.NANOS_PER_MILLISECOND), 10000,
                time -> SortingSimulator.setInsertTime(time / 1000.0));

        componentPanel = new EComponentPanelBuilder(gameStateManager.getMouseTracker())
                .add(0, new EBackground(cpBackgroundLoc, ComponentCreator.backgroundColor()))
                .add(2, new ETextLabel(algorithmLabelLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), "Algorithm:", true))
                .add(2, new EComboBox(algorithmCBLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), imageDrawer, allAlgorithms, 15, 0, i -> {
                    selectedAlgorithm = SortingSimulator.getAlgorithm(allAlgorithms[i]);
                    SortingSimulator.stopSort(true);
                }))
                .add(1, new ETextLabel(arrayLabelLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), "Array Type:", true))
                .add(1, new EComboBox(arrayCBLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), imageDrawer, allArrayTypes, 5, 0, i -> {
                    selectedType = SortingSimulator.getArrayType(allArrayTypes[i]);
                    SortingSimulator.stopSort(false);
                    array = new SArray(selectedType, selectedLength);
                }))
                .add(1, new ETextLabel(sizeLabelLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), "Size:", true))
                .add(2, new ESlider(sizeSliderLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), 1, 5 * 4, 4, length -> {
                    int pow = length / 4;
                    int mod = length % 4;
                    selectedLength = EMath.round(Math.pow(5, pow + 1) * (mod + 1));
                    if (array.length() == selectedLength) {
                        return;
                    }
                    sliderAmountLabel.setText(Integer.toString(selectedLength));
                    SortingSimulator.stopSort(false);
                    array = new SArray(selectedType, selectedLength);
                }))
                .add(1, sliderAmountLabel)
                .add(1, new ETextLabel(accLabelLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), "Access:", true))
                .add(1, accessTimeSlider)
                .add(1, new ETextLabel(comLabelLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), "Compare:", true))
                .add(1, compareTimeSlider)
                .add(1, new ETextLabel(insLabelLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), "Insert:", true))
                .add(1, insertTimeSlider)
                .add(1, EButton.createTextButton(startButtonLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), "Start", () -> {
                    SortingSimulator.startSort(array, selectedAlgorithm);
                }))
                .add(1, EButton.createTextButton(stopButtonLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), "Stop", () -> {
                    SortingSimulator.stopSort(true);
                }))
                .add(1, EButton.createTextButton(resetButtonLoc.scale(UI_WIDTH_SCALE, UI_HEIGHT_SCALE), "Reset", () -> {
                    SortingSimulator.stopSort(false);
                    array = new SArray(selectedType, selectedLength);
                }))
                .build();
    }

    @Override
    public void update(double dt) {
        componentPanel.update(dt);
        if (SortStopper.isSorting()) {
            TimeManager.addTime(dt);
        }
    }

    @Override
    public void drawOn(IGraphics g) {
        sortingState.drawOn(sortingStateImage.getGraphics());
        imageDrawer.drawImage(g, sortingStateImage, 0, uiHeight);
        componentPanel.drawOn(g);
    }

    @Override
    public void setSize(int width, int height) {
        sortingState.setSize(width, EMath.round(height - uiHeight));
        sortingStateImage.setSize(width, EMath.round(height - uiHeight));
        cpBackgroundLoc.setSize(width, EMath.round(uiHeight));
    }

    @Override
    public void handleUserInput(UserInput input) {
        componentPanel.handleUserInput(input);
        switch (input) {
        case MINUS_KEY_PRESSED:
            SortingSimulator.setAccessTime(SortingSimulator.getAccessTime() * 1.25);
            SortingSimulator.setInsertTime(SortingSimulator.getInsertTime() * 1.25);
            SortingSimulator.setCompareTime(SortingSimulator.getCompareTime() * 1.25);
            setSliderValues();
            break;
        case EQUALS_KEY_PRESSED:
            SortingSimulator.setAccessTime(SortingSimulator.getAccessTime() / 1.25);
            SortingSimulator.setInsertTime(SortingSimulator.getInsertTime() / 1.25);
            SortingSimulator.setCompareTime(SortingSimulator.getCompareTime() / 1.25);
            setSliderValues();
            break;
        case R_KEY_PRESSED:
            SortingSimulator.setAccessTime(10);
            SortingSimulator.setInsertTime(10);
            SortingSimulator.setCompareTime(10);
            setSliderValues();
            break;
        case SPACE_KEY_PRESSED:
            SortingSimulator.startSort(array, selectedAlgorithm);
            break;
        case ESC_KEY_PRESSED:
            SortingSimulator.stopSort(true);
            break;
        }
    }

    private void setSliderValues() {
        accessTimeSlider.setCurrentValue(EMath.round(SortingSimulator.getAccessTime() * 1000));
        insertTimeSlider.setCurrentValue(EMath.round(SortingSimulator.getInsertTime() * 1000));
        compareTimeSlider.setCurrentValue(EMath.round(SortingSimulator.getCompareTime() * 1000));
    }
}
