package ss.main;

import gt.component.ComponentCreator;
import gt.ecomponent.EBackground;
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
import gt.util.EMath;
import ss.algorithm.SortingAlgorithm;
import ss.array.SArray;
import ss.array.SArray.ArrayType;

public class SortingSimulationState implements GameState {
    private static final double PADDING = 5;
    private static final double PADDING_2 = 3 * PADDING;
    private static final double COMBO_BOX_HEIGHT = 25;
    private static final double SELECTION_LABEL_WIDTH = 75;
    private static final double UI_HEIGHT = 2 * COMBO_BOX_HEIGHT + 3 * PADDING;
    private static final double TIME_SLIDER_HEIGHT = (UI_HEIGHT - 4 * PADDING) / 3;

    private final GameImageDrawer imageDrawer;

    private final SortingState sortingState;
    private final IGameImage sortingStateImage;

    private final ESizableComponentLocation cpBackgroundLoc;
    private final EComponentPanel componentPanel;

    private SortingAlgorithm selectedAlgorithm;
    private ArrayType selectedType;
    private int selectedLength;
    private SArray array;

    public SortingSimulationState(GameStateManager gameStateManager) {
        String[] allAlgorithms = SortingSimulator.getAlgorithmNames();
        String[] allArrayTypes = SortingSimulator.getArrayTypeNames();
        selectedAlgorithm = SortingSimulator.getAlgorithm(allAlgorithms[0]);
        selectedType = SortingSimulator.getArrayType(allArrayTypes[0]);
        selectedLength = 25;
        array = new SArray(selectedType, selectedLength);

        sortingState = new SortingState(selectedAlgorithm.getName());
        imageDrawer = gameStateManager.getImageDrawer();
        sortingStateImage = imageDrawer.newGameImage(ComponentCreator.DEFAULT_WIDTH, EMath.round(ComponentCreator.DEFAULT_HEIGHT - UI_HEIGHT));

        cpBackgroundLoc = new ESizableComponentLocation(0, 0, ComponentCreator.DEFAULT_WIDTH, ComponentCreator.DEFAULT_HEIGHT - UI_HEIGHT);

        EFixedLocation algorithmLabelLoc = EFixedLocation.fromRect(PADDING, PADDING, SELECTION_LABEL_WIDTH, COMBO_BOX_HEIGHT);
        EFixedLocation algorithmCBLoc = EFixedLocation.fromRect(algorithmLabelLoc.getX1() + 1 + PADDING, PADDING, 200, COMBO_BOX_HEIGHT);
        EFixedLocation arrayLabelLoc = EFixedLocation.fromRect(PADDING, algorithmLabelLoc.getY1() + 1 + PADDING, SELECTION_LABEL_WIDTH, COMBO_BOX_HEIGHT);
        EFixedLocation arrayCBLoc = EFixedLocation.fromRect(arrayLabelLoc.getX1() + 1 + PADDING, algorithmLabelLoc.getY1() + 1 + PADDING, 100,
                COMBO_BOX_HEIGHT);

        EFixedLocation sizeLabelLoc = EFixedLocation.fromRect(algorithmCBLoc.getX1() + 1 + PADDING_2, (UI_HEIGHT - COMBO_BOX_HEIGHT) / 2, 50,
                COMBO_BOX_HEIGHT);
        EFixedLocation sizeSliderLoc = EFixedLocation.fromRect(sizeLabelLoc.getX1() + 1 + PADDING_2, sizeLabelLoc.getY0(), 200, COMBO_BOX_HEIGHT);
        EFixedLocation sliderAmountLabelLoc = EFixedLocation.fromRect(sizeSliderLoc.getX0(), sizeSliderLoc.getY1() + 1 + PADDING, 200, TIME_SLIDER_HEIGHT);
        ETextLabel sliderAmountLabel = new ETextLabel(sliderAmountLabelLoc, Integer.toString(selectedLength), true);

        EFixedLocation accessLabelLoc = EFixedLocation.fromRect(sizeSliderLoc.getX1() + 1 + PADDING_2, PADDING, 50, TIME_SLIDER_HEIGHT);
        EFixedLocation accessSliderLoc = EFixedLocation.fromRect(accessLabelLoc.getX1() + 1 + PADDING_2, PADDING, 150, TIME_SLIDER_HEIGHT);
        EFixedLocation compareLabelLoc = EFixedLocation.fromRect(sizeSliderLoc.getX1() + 1 + PADDING_2, accessLabelLoc.getY1() + 1 + PADDING, 50,
                TIME_SLIDER_HEIGHT);
        EFixedLocation compareSliderLoc = EFixedLocation.fromRect(accessLabelLoc.getX1() + 1 + PADDING_2, accessLabelLoc.getY1() + 1 + PADDING, 150,
                TIME_SLIDER_HEIGHT);
        EFixedLocation insertLabelLoc = EFixedLocation.fromRect(sizeSliderLoc.getX1() + 1 + PADDING_2, compareSliderLoc.getY1() + 1 + PADDING, 50,
                TIME_SLIDER_HEIGHT);
        EFixedLocation insertSliderLoc = EFixedLocation.fromRect(accessLabelLoc.getX1() + 1 + PADDING_2, compareSliderLoc.getY1() + 1 + PADDING, 150,
                TIME_SLIDER_HEIGHT);

        EFixedLocation startButtonLoc = EFixedLocation.fromRect(accessSliderLoc.getX1() + 1 + PADDING_2, PADDING, 50, TIME_SLIDER_HEIGHT);
        EFixedLocation stopButtonLoc = EFixedLocation.fromRect(accessSliderLoc.getX1() + 1 + PADDING_2, startButtonLoc.getY1() + 1 + PADDING, 50,
                TIME_SLIDER_HEIGHT);
        EFixedLocation resetButtonLoc = EFixedLocation.fromRect(accessSliderLoc.getX1() + 1 + PADDING_2, stopButtonLoc.getY1() + 1 + PADDING, 50,
                TIME_SLIDER_HEIGHT);

        componentPanel = new EComponentPanelBuilder(gameStateManager.getMouseTracker())
                .add(0, new EBackground(cpBackgroundLoc, ComponentCreator.backgroundColor()))
                .add(2, new ETextLabel(algorithmLabelLoc, "Algorithm:", true))
                .add(2, new EComboBox(algorithmCBLoc, imageDrawer, allAlgorithms, 15, 0, i -> {
                    selectedAlgorithm = SortingSimulator.getAlgorithm(allAlgorithms[i]);
                    sortingState.stopSort();
                    array.reallocateMemory();
                }))
                .add(1, new ETextLabel(arrayLabelLoc, "Array Type:", true))
                .add(1, new EComboBox(arrayCBLoc, imageDrawer, allArrayTypes, 5, 0, i -> {
                    selectedType = SortingSimulator.getArrayType(allArrayTypes[i]);
                    resetState();
                }))
                .add(1, new ETextLabel(sizeLabelLoc, "Size:", true))
                .add(2, new ESlider(sizeSliderLoc, 1, 5 * 4, 4, length -> {
                    int pow = length / 4;
                    int mod = length % 4;
                    selectedLength = EMath.round(Math.pow(5, pow + 1) * (mod + 1));
                    if (array.length() == selectedLength) {
                        return;
                    }
                    sliderAmountLabel.setText(Integer.toString(selectedLength));
                    resetState();
                }))
                .add(1, sliderAmountLabel)
                .add(1, new ETextLabel(accessLabelLoc, "Access:", true))
                .add(1, new ESlider(accessSliderLoc, 1, EMath.round(TimeConstants.NANOS_PER_MILLISECOND), 10000, time -> {
                    SortingSimulator.setAccessTime(time / 1000.0);
                }))
                .add(1, new ETextLabel(compareLabelLoc, "Compare:", true))
                .add(1, new ESlider(compareSliderLoc, 1, EMath.round(TimeConstants.NANOS_PER_MILLISECOND), 10000, time -> {
                    SortingSimulator.setCompareTime(time / 1000.0);
                }))
                .add(1, new ETextLabel(insertLabelLoc, "Insert:", true))
                .add(1, new ESlider(insertSliderLoc, 1, EMath.round(TimeConstants.NANOS_PER_MILLISECOND), 10000, time -> {
                    SortingSimulator.setInsertTime(time / 1000.0);
                }))
                .add(1, EButton.createTextButton(startButtonLoc, "Start", () -> {
                    sortingState.startSort(array, selectedAlgorithm);
                }))
                .add(1, EButton.createTextButton(stopButtonLoc, "Stop", () -> {
                    sortingState.stopSort();
                    array.reallocateMemory();
                }))
                .add(1, EButton.createTextButton(resetButtonLoc, "Reset", () -> {
                    resetState();
                }))
                .build();
    }

    private void resetState() {
        sortingState.stopSort();
        array = new SArray(selectedType, selectedLength);
    }

    @Override
    public void update(double dt) {
        componentPanel.update(dt);
        sortingState.update(dt);
    }

    @Override
    public void drawOn(IGraphics g) {
        sortingState.drawOn(sortingStateImage.getGraphics());
        imageDrawer.drawImage(g, sortingStateImage, 0, UI_HEIGHT);
        componentPanel.drawOn(g);
    }

    @Override
    public void setSize(int width, int height) {
        sortingState.setSize(width, EMath.round(height - UI_HEIGHT));
        sortingStateImage.setSize(width, EMath.round(height - UI_HEIGHT));
        cpBackgroundLoc.setSize(width, EMath.round(UI_HEIGHT));
    }

    @Override
    public void handleUserInput(UserInput input) {
        componentPanel.handleUserInput(input);
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
        case R_KEY_PRESSED:
            SortingSimulator.setAccessTime(10);
            SortingSimulator.setInsertTime(10);
            SortingSimulator.setCompareTime(10);
            break;
        case ESC_KEY_PRESSED:
            sortingState.stopSort();
            break;
        }
    }
}
