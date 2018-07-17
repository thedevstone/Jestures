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

package recorder.view;

import java.io.IOException;
import java.util.Queue;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.kordamp.ikonli.material.Material;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.effects.JFXDepthManager;
import com.sun.javafx.application.PlatformImpl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;
import jestures.core.codification.FrameLenght;
import jestures.core.view.IconDim;
import jestures.core.view.ListViewFactory;
import jestures.core.view.NotificationType;
import jestures.core.view.NotificationType.Duration;
import jestures.core.view.RecordingFactory;
import jestures.core.view.ScrollPaneFactory;
import jestures.core.view.ViewUtilities;

/**
 *
 *
 */
@SuppressWarnings("restriction")
public class RecorderScreenView implements RecView {
    private final Recording recorder;
    private FrameLenght frameLength;

    // VIEW
    private Stage stage; // NOPMD
    private Scene scene; // NOPMD
    // CHART
    private LineChart<Number, Number> lineChartX; // NOPMD
    private LineChart<Number, Number> lineChartY; // NOPMD
    private XYChart.Series<Number, Number> xSeries;
    private XYChart.Series<Number, Number> ySeries;
    // CANVAS
    private Canvas canvas;
    private GraphicsContext context;

    @FXML
    private JFXTabPane tabPane;
    @FXML
    private BorderPane recorderPane; // NOPMD
    @FXML
    private JFXButton startButton;

    @FXML
    private VBox vbox;
    @FXML
    private StackPane canvasStackPane;
    @FXML
    private ComboBox<FrameLenght> frameLengthCombo;

    @FXML
    private JFXScrollPane scrollPane;
    @FXML
    private JFXListView<BorderPane> listView;

    /**
     * @param recorder
     *            the {@link AbstractTracker}
     */
    public RecorderScreenView(final Recording recorder) {
        this.recorder = recorder;
        Platform.runLater(() -> {
            final FXMLLoader loader = new FXMLLoader();
            loader.setController(this);
            loader.setLocation(this.getClass()
                                   .getResource(FXMLScreens.HOME.getPath()));
            try {
                this.recorderPane = (BorderPane) loader.load();
            } catch (final IOException e1) {
                e1.printStackTrace();
            }
        });
    }

    @FXML
    private void initialize() { // NOPMD
        this.initButtons();
        this.initCombos();
        this.initGraphic();
        this.initCanvas();
        this.initChart();
        this.initListView();
        this.stage = new Stage();
        this.scene = new Scene(this.recorderPane);
        this.stage.setScene(this.scene);
        this.stage.setOnCloseRequest(e -> this.stopSensor());
        this.chargeSceneSheets(FXMLScreens.HOME);
        this.stage.show();

    }

    private void initButtons() {
        this.startButton.setOnAction(e -> {
            if (this.getTracker()
                    .state()) {
                this.stopSensor();
                this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY, IconDim.MEDIUM));
            } else {
                this.startSensor();
                this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY_OFF, IconDim.MEDIUM));
            }
        });

    }

    private void initCanvas() {
        this.canvas = new Canvas(this.recorderPane.getMinWidth(), this.recorderPane.getMinHeight());
        this.canvas.widthProperty()
                   .bind(this.recorderPane.widthProperty());
        this.canvas.heightProperty()
                   .bind(this.recorderPane.heightProperty());
        this.context = this.canvas.getGraphicsContext2D();
        this.canvasStackPane.getChildren()
                            .setAll(this.canvas);
    }

    private void initChart() {
        this.xSeries = new XYChart.Series<>();
        this.ySeries = new XYChart.Series<>();
        this.lineChartX = RecordingFactory.createDerivativeLineChart();
        this.lineChartY = RecordingFactory.createDerivativeLineChart();
        this.lineChartX.getData()
                       .add(this.xSeries);
        this.lineChartY.getData()
                       .add(this.ySeries);
        this.lineChartX.setTitle("Derivative: X");
        this.lineChartY.setTitle("Derivative: Y");
        HBox.setHgrow(this.lineChartX, Priority.ALWAYS);
        HBox.setHgrow(this.lineChartY, Priority.ALWAYS);
        this.vbox.getChildren()
                 .addAll(this.lineChartX, this.lineChartY);
    }

    private void initGraphic() {
        this.tabPane.getTabs()
                    .get(0)
                    .setGraphic(ViewUtilities.iconSetter(Material.BLUR_ON, IconDim.MEDIUM));
        this.tabPane.getTabs()
                    .get(1)
                    .setGraphic(ViewUtilities.iconSetter(Material.MULTILINE_CHART, IconDim.MEDIUM));
        this.tabPane.getTabs()
                    .get(2)
                    .setGraphic(ViewUtilities.iconSetter(Material.VIEW_LIST, IconDim.MEDIUM));
        this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY, IconDim.MEDIUM));

    }

    private void chargeSceneSheets(final FXMLScreens screen) {
        this.scene.getStylesheets()
                  .add(RecorderScreenView.class.getResource(screen.getCssPath())
                                               .toString());
    }

    private void initListView() {
        ScrollPaneFactory.wrapListViewOnScrollPane(this.scrollPane, this.listView, "Feature Vectors",
                "headerFeatureVector");
    }

    private void initCombos() {
        this.frameLengthCombo.setOnAction(t -> this.setFrameLength(this.frameLengthCombo.getValue()));
        this.frameLengthCombo.getItems()
                             .add(FrameLenght.ONE_SECOND);
        this.frameLengthCombo.getItems()
                             .add(FrameLenght.TWO_SECONDS);
        this.frameLengthCombo.getItems()
                             .add(FrameLenght.THREE_SECONDS);
        this.frameLengthCombo.getSelectionModel()
                             .select(this.getFrameLength());
        JFXDepthManager.setDepth(this.frameLengthCombo, 4);
    }

    // ############################################## FROM RECORDER ###################################
    @Override
    public void notifyOnFrameChange(final int frame, final Vector2D derivative, final Vector2D path) {
        Platform.runLater(() -> {
            if (frame > this.getFrameLength()
                            .getFrameNumber()
                    - 1 || frame == 0) {
                this.context.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight());
                this.xSeries.getData()
                            .clear();
                this.ySeries.getData()
                            .clear();
            }
            this.xSeries.getData()
                        .add(new XYChart.Data<Number, Number>(frame, (int) derivative.getX()));
            this.ySeries.getData()
                        .add(new XYChart.Data<Number, Number>(frame, (int) derivative.getY()));

            this.context.fillOval(-path.getX() + this.canvas.getWidth() / 2, path.getY() + this.canvas.getHeight() / 2,
                    10, 10);
        });

    }

    @Override
    public void notifyOnFeatureVectorEvent(final Queue<Vector2D> featureVector) {
        Platform.runLater(() -> {
            this.createUserInListView("Feature Vector", this.canvas.snapshot(new SnapshotParameters(), null));
        });
    }

    private void createUserInListView(final String nickname, final Image image) {
        ListViewFactory.addVectorToListView(this.listView, image, nickname);
        this.initPlayerDeleteListViewListeners();
        this.scrollPane.setContent(this.listView);
    }

    public void clearListView() {
        this.listView.getItems()
                     .clear();
    }

    private void initPlayerDeleteListViewListeners() {
        this.listView.getItems()
                     .stream()
                     .map(t -> new Pair<>((Label) t.getCenter(), (JFXButton) t.getRight()))
                     .forEach(j -> {
                         j.getValue()
                          .setOnMouseClicked(k -> {
                              this.recorder.deleteFeatureVector(3);
                          });
                     });
    }

    @Override
    public void setRecording(final boolean isRecording) {
        if (isRecording) {
            Platform.runLater(() -> ViewUtilities.showNotificationPopup("START RECORDING", "Record is started",
                    Duration.MEDIUM, NotificationType.WARNING, null));
        } else {
            Platform.runLater(() -> ViewUtilities.showNotificationPopup("STOP RECORDING", "Record is stopped",
                    Duration.MEDIUM, NotificationType.WARNING, null));
        }
    }

    // ############################################## INSTANCE METHODS ###################################
    /**
     * Start the Fx thread.
     */
    public static void startFxThread() {
        PlatformImpl.startup(() -> {
        });
    }

    @Override
    public FrameLenght getFrameLength() {
        return this.frameLength;
    }

    @Override
    public void setFrameLength(final FrameLenght length) {
        this.frameLength = length;
        this.recorder.setFrameLength(length);
        System.out.println(length);
    }

    @Override
    public void startSensor() {
        this.recorder.startSensor();
    }

    @Override
    public void stopSensor() {
        this.recorder.stopSensor();
    }

    @Override
    public Recording getTracker() {
        return this.recorder;
    }

}
