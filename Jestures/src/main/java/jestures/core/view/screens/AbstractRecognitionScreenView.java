/*******************************************************************************
 * Copyright (c) 2018 Giulianini Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package jestures.core.view.screens;

import java.util.Collections;

import org.kordamp.ikonli.material.Material;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTreeView;
import com.jfoenix.effects.JFXDepthManager;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jestures.core.codification.FrameLength;
import jestures.core.recognition.Recognition;
import jestures.core.recognition.UpdateRate;
import jestures.core.view.AbstractView;
import jestures.core.view.enums.IconDim;
import jestures.core.view.utils.RecordingFactory;
import jestures.core.view.utils.ScrollPaneFactory;
import jestures.core.view.utils.ViewUtilities;

/**
 * The abstract class of the recognition view. This is a simple abstract screen controller for a javafx fxml file. It is
 * used for initialization of buttons, cavnvas, list-view etc
 *
 */

public abstract class AbstractRecognitionScreenView extends AbstractView {
    // private static final Logger LOG = Logger.getLogger(AbstractRecorderScreenView.class);
    private final Recognition recognizer;

    // CHART
    private LineChart<Number, Number> lineChartX; // NOPMD
    private LineChart<Number, Number> lineChartY; // NOPMD
    private XYChart.Series<Number, Number> xSeries;
    private XYChart.Series<Number, Number> ySeries;
    private JFXPopup cnavasPopup;

    // ELEVATION
    private JFXPopup elevationPopup;
    private JFXSlider elevationSlider;

    // LIVE CANVAS
    private Canvas liveCanvas;
    private GraphicsContext liveCanvasContext;

    // USER CANVAS
    private Canvas userCanvas;
    private GraphicsContext userCanvasContext;

    // ########### ALL TABS #############
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private BorderPane recorderPane;
    @FXML
    private JFXButton startButton;
    @FXML
    private StackPane tabStackPane;
    @FXML
    private VBox vbox;
    @FXML
    private ComboBox<FrameLength> frameLengthCombo;

    // ########### TAB 1 #############
    @FXML
    private HBox userHBox;
    @FXML
    private BorderPane userBorderPane; // NOPMD
    @FXML
    private JFXComboBox<String> selectUserCombo;

    @FXML
    private JFXTreeView<String> treeView;
    @FXML
    private JFXScrollPane userScrollPane;

    // ########### TAB 2 #############

    @FXML
    private StackPane liveCanvasStackPane;
    // ########### TAB 3 #############

    // ########### TAB 4 #############
    @FXML
    private JFXComboBox<UpdateRate> udpateRateCombo;

    @FXML
    private JFXSlider sliderRadius;

    @FXML
    private JFXSlider sliderMinThreshold;

    @FXML
    private JFXSlider sliderMaxThreshold;

    @FXML
    private JFXSlider sliderTimeSeparation;

    @FXML
    private JFXSlider sliderMatchNumber;
    @FXML
    private JFXButton saveSettingsButton;

    /**
     * The constructor.
     *
     * @param recognizer
     *            the {@link Recognition} recognizer
     *
     */
    public AbstractRecognitionScreenView(final Recognition recognizer) {
        super(recognizer);
        this.recognizer = recognizer;
    }

    /**
     * Initialize method.
     */
    @FXML
    public void initialize() { // NOPMD
        this.initButtons();
        this.initCombos();
        this.initGraphic();
        this.initLiveCanvas();
        this.initUserCanvas();
        this.initChart();
        this.initScrollPane();
        this.initTreeView();
        this.setDisabled();
        this.initProgressBar();
        this.initSliders();
        this.initPopup();
    }

    private void initSliders() {
        for (final UpdateRate elem : UpdateRate.values()) {
            this.udpateRateCombo.getItems().add(elem);
        }
        this.udpateRateCombo.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> this.setUpdateRate(newValue));
        this.sliderRadius.valueProperty().addListener(
                (observable, oldValue, newValue) -> this.setDtwRadius(newValue.doubleValue() / 10));
        this.sliderMinThreshold.valueProperty().addListener(
                (observable, oldValue, newValue) -> this.setMinDtwThreashold(newValue.intValue()));
        this.sliderMaxThreshold.valueProperty().addListener(
                (observable, oldValue, newValue) -> this.setMaxDtwThreashold(newValue.intValue()));
        this.sliderTimeSeparation.valueProperty().addListener(
                (observable, oldValue, newValue) -> this.setMinTimeSeparation(newValue.intValue()));
        this.sliderMatchNumber.valueProperty().addListener(
                (observable, oldValue, newValue) -> this.setMatchNumber(newValue.intValue()));

        // CHECKSTYLE:OFF
        this.elevationSlider = new JFXSlider(0, 30, 10);
        this.elevationSlider.setMajorTickUnit(5);
        this.elevationSlider.setSnapToTicks(true);
        this.elevationSlider.setMinorTickCount(0);
        this.elevationSlider.setMinHeight(50);
        this.elevationSlider.valueProperty().addListener((oservable, oldValue, newValue) -> {
            new Thread(() -> {
                this.setSensorElevation(newValue.intValue());
            }).start();
        });
        // CHECKSTYLE:ON
    }

    private void setDisabled() {
        this.startButton.setDisable(true);
    }

    private void initPopup() {
        // CANVAS POPUP
        this.cnavasPopup = new JFXPopup(new StackPane(this.getUserCanvas()));
        this.getUserCanvas().setOnMouseClicked(t -> {
            this.cnavasPopup.hide();
        });

        // ELEVATION POPUP
        this.elevationPopup = new JFXPopup(this.elevationSlider);

    }

    private void initButtons() {
        // START AND STOP
        this.startButton.setOnMouseClicked(e -> {
            if (e.getButton().equals(MouseButton.SECONDARY)) {
                this.elevationPopup.show(this.startButton);
            } else {
                if (this.recognizer.state()) {
                    this.stopSensor();
                    this.startButton.setTooltip(new Tooltip("Start the sensor"));
                    this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY, IconDim.MEDIUM));
                } else {
                    this.startSensor();
                    this.startButton.setTooltip(new Tooltip("Start the sensor"));
                    this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY_OFF, IconDim.MEDIUM));
                }
            }
        });

        // SAVE BUTTON
        this.saveSettingsButton.setGraphic(ViewUtilities.iconSetter(Material.SAVE, IconDim.MEDIUM));
        JFXDepthManager.setDepth(this.saveSettingsButton, 4);
        this.saveSettingsButton.setOnAction(t -> this.saveSettings());

    }

    private void initLiveCanvas() {
        this.liveCanvas = new Canvas(this.recorderPane.getMinWidth(), this.recorderPane.getMinHeight());
        this.liveCanvas.widthProperty().bind(this.recorderPane.widthProperty());
        this.liveCanvas.heightProperty().bind(this.recorderPane.heightProperty());
        this.liveCanvasContext = this.liveCanvas.getGraphicsContext2D();
        this.liveCanvasStackPane.getChildren().setAll(this.liveCanvas);
    }

    private void initUserCanvas() {
        this.userCanvas = new Canvas(this.recorderPane.getMinWidth(), this.recorderPane.getMinHeight());
        this.userCanvas.widthProperty().bind(this.recorderPane.widthProperty());
        this.userCanvas.heightProperty().bind(this.recorderPane.heightProperty());
        this.userCanvasContext = this.userCanvas.getGraphicsContext2D();
    }

    private void initChart() {
        this.setChart(this.recognizer.getFrameLength().getFrameNumber(),
                this.recognizer.getFrameLength().getFrameNumber());
    }

    private void initProgressBar() {
        this.progressBar.minWidthProperty().bind(this.recorderPane.widthProperty());
    }

    private void initGraphic() {
        this.tabPane.getTabs().get(0).setGraphic(ViewUtilities.iconSetter(Material.PERSON, IconDim.MEDIUM));
        this.tabPane.getTabs().get(1).setGraphic(ViewUtilities.iconSetter(Material.BLUR_ON, IconDim.MEDIUM));
        this.tabPane.getTabs().get(2).setGraphic(ViewUtilities.iconSetter(Material.MULTILINE_CHART, IconDim.MEDIUM));
        this.tabPane.getTabs().get(3).setGraphic(ViewUtilities.iconSetter(Material.SETTINGS, IconDim.MEDIUM));
        this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY, IconDim.MEDIUM));
    }

    private void initScrollPane() {
        // CHECKSTYLE:OFF Magicnumber AH DI MI TOCCA
        ScrollPaneFactory.wrapNodeOnScrollPane(this.userScrollPane, this.userBorderPane, "Select User",
                "headerUserPane");
        this.treeView.maxHeightProperty().bind(this.tabStackPane.heightProperty().subtract(300));
        this.treeView.minHeightProperty().bind(this.tabStackPane.heightProperty().subtract(300));
        JFXDepthManager.setDepth(this.userHBox, 1);
        // CHECKSTYLE:ON Magicnumber AH DI MI TOCCA
    }

    private void initCombos() {
        this.frameLengthCombo.setOnAction(t -> this.setFrameLength(this.frameLengthCombo.getValue()));
        this.frameLengthCombo.getItems().add(FrameLength.FPS_30);
        this.frameLengthCombo.getItems().add(FrameLength.FPS_20);
        this.frameLengthCombo.getItems().add(FrameLength.FPS_10);
        this.frameLengthCombo.getSelectionModel().select(this.getFrameLength());
        JFXDepthManager.setDepth(this.frameLengthCombo, 4);

        // USER COMBOBOX
        Collections.sort(this.selectUserCombo.getItems());
        this.selectUserCombo.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue != null) {
                this.loadUserProfile(newValue);
            }
        });
    }

    private void initTreeView() {
        final TreeItem<String> root = new TreeItem<String>("Empty User");
        root.setGraphic(ViewUtilities.iconSetter(Material.PERSON, IconDim.SMALL));
        this.treeView.setRoot(root);
        this.treeView.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue != null && this.treeView.getTreeItemLevel(newValue) == 2) {
                this.drawSavedGestureOnCanvas(newValue.getParent(),
                        newValue.getParent().getChildren().indexOf(newValue));
            }
        });
    }

    /**
     * Set the chart with the selected length.
     *
     * @param xFrames
     *            the x frame length
     * @param yFrames
     *            the y frame length
     */
    public void setChart(final int xFrames, final int yFrames) {
        this.xSeries = new XYChart.Series<>();
        this.ySeries = new XYChart.Series<>();
        this.lineChartX = RecordingFactory.createDerivativeLineChart(xFrames);
        this.lineChartY = RecordingFactory.createDerivativeLineChart(yFrames);
        this.lineChartX.getData().add(this.xSeries);
        this.lineChartY.getData().add(this.ySeries);
        this.lineChartX.setTitle("Derivative: X");
        this.lineChartY.setTitle("Derivative: Y");
        HBox.setHgrow(this.lineChartX, Priority.ALWAYS);
        HBox.setHgrow(this.lineChartY, Priority.ALWAYS);
        this.vbox.getChildren().setAll(this.lineChartX, this.lineChartY);
    }

    /**
     * Draw saved gestures on canvas.
     *
     * @param parent
     *            the {@link TreeItem} parent
     * @param indexOf
     *            the feature vevector index for the selected gesture
     */
    public abstract void drawSavedGestureOnCanvas(TreeItem<String> parent, int indexOf);

    // ################################################ GETTER FOR INSTANCE CLASS #####################################
    /**
     * Get the xSerie.
     *
     * @return the {@link Series}
     */
    public XYChart.Series<Number, Number> getxSeries() {
        return this.xSeries;
    }

    /**
     * Get the ySerie.
     *
     * @return the {@link Series}
     */
    public XYChart.Series<Number, Number> getySeries() {
        return this.ySeries;
    }

    /**
     * Get the {@link Canvas}.
     *
     * @return the {@link Canvas}.
     */
    public Canvas getLiveCanvas() {
        return this.liveCanvas;
    }

    /**
     * Get the live {@link GraphicsContext}.
     *
     * @return the {@link GraphicsContext}
     */
    public GraphicsContext getLiveContext() {
        return this.liveCanvasContext;
    }

    /**
     * Get the userCanvas.
     *
     * @return the {@link Canvas}
     */
    public Canvas getUserCanvas() {
        return this.userCanvas;
    }

    /**
     * Get the user {@link GraphicsContext}.
     *
     * @return the {@link GraphicsContext}
     */
    public GraphicsContext getUserCanvasContext() {
        return this.userCanvasContext;
    }

    /**
     * Get the canvas popup.
     *
     * @return the {@link JFXPopup}
     */
    public JFXPopup getCnavasPopup() {
        return this.cnavasPopup;
    }
}
