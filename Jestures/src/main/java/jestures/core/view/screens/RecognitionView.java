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

import java.io.IOException;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.kordamp.ikonli.material.Material;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.effects.JFXDepthManager;
import com.sun.javafx.application.PlatformImpl;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jestures.core.codification.FrameLength;
import jestures.core.tracking.Tracker;
import jestures.core.tracking.Tracking;
import jestures.core.view.AbstractView;
import jestures.core.view.enums.IconDim;
import jestures.core.view.utils.RecordingFactory;
import jestures.core.view.utils.ViewUtilities;

/**
 *
 *
 */
@SuppressWarnings("restriction")
public class RecognitionView extends AbstractView {

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
    private ComboBox<FrameLength> frameLengthCombo;

    /**
     * @param tracker
     *            the {@link Tracker}
     */
    public RecognitionView(final Tracking tracker) {
        super(tracker);

        Platform.runLater(() -> {
            final FXMLLoader loader = new FXMLLoader();
            loader.setController(this);
            loader.setLocation(this.getClass().getResource(FXMLScreens.HOME.getPath()));
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
        this.stage = new Stage();
        this.scene = new Scene(this.recorderPane);
        this.stage.setScene(this.scene);
        this.stage.setOnCloseRequest(e -> {
            this.stopSensor();
            Platform.exit();
            System.exit(0);
        });
        this.chargeSceneSheets(FXMLScreens.HOME);
        this.stage.show();

    }

    private void initButtons() {
        this.startButton.setOnAction(e -> {
            if (this.getTracker().state()) {
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
        this.canvas.widthProperty().bind(this.recorderPane.widthProperty());
        this.canvas.heightProperty().bind(this.recorderPane.heightProperty());
        this.context = this.canvas.getGraphicsContext2D();
        this.canvasStackPane.getChildren().setAll(this.canvas);
    }

    private void initChart() {
        this.xSeries = new XYChart.Series<>();
        this.ySeries = new XYChart.Series<>();
        this.lineChartX = RecordingFactory.createDerivativeLineChart();
        this.lineChartY = RecordingFactory.createDerivativeLineChart();
        this.lineChartX.getData().add(this.xSeries);
        this.lineChartY.getData().add(this.ySeries);
        this.lineChartX.setTitle("Derivative: X");
        this.lineChartY.setTitle("Derivative: Y");
        HBox.setHgrow(this.lineChartX, Priority.ALWAYS);
        HBox.setHgrow(this.lineChartY, Priority.ALWAYS);
        this.vbox.getChildren().addAll(this.lineChartX, this.lineChartY);
    }

    private void initGraphic() {
        this.tabPane.getTabs().get(0).setGraphic(ViewUtilities.iconSetter(Material.BLUR_ON, IconDim.MEDIUM));
        this.tabPane.getTabs().get(1).setGraphic(ViewUtilities.iconSetter(Material.MULTILINE_CHART, IconDim.MEDIUM));
        this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY, IconDim.MEDIUM));

    }

    private void chargeSceneSheets(final FXMLScreens screen) {
        this.scene.getStylesheets().add(RecognitionView.class.getResource(screen.getCssPath()).toString());
    }

    private void initCombos() {
        this.frameLengthCombo.setOnAction(t -> this.setFrameLength(this.frameLengthCombo.getValue()));
        this.frameLengthCombo.getItems().add(FrameLength.ONE_SECOND);
        this.frameLengthCombo.getItems().add(FrameLength.TWO_SECONDS);
        this.frameLengthCombo.getItems().add(FrameLength.THREE_SECONDS);
        this.frameLengthCombo.getSelectionModel().select(this.getFrameLength());
        JFXDepthManager.setDepth(this.frameLengthCombo, 4);
    }

    @Override
    public void notifyOnFrameChange(final int frame, final Vector2D derivative, final Vector2D path) {
        Platform.runLater(() -> {
            if (frame > this.getFrameLength().getFrameNumber() - 1) {
                this.xSeries.getData().clear();
                this.ySeries.getData().clear();
            }
            this.xSeries.getData().add(new XYChart.Data<Number, Number>(frame, (int) derivative.getX()));
            this.ySeries.getData().add(new XYChart.Data<Number, Number>(frame, (int) derivative.getY()));

            this.context.fillOval(-path.getX() + this.canvas.getWidth() / 2, path.getY() + this.canvas.getHeight() / 2,
                    4, 4);
        });

    }

    @Override
    public void notifyOnFeatureVectorEvent() {
        Platform.runLater(() -> this.context.clearRect(0, 0, this.canvas.getWidth(), this.canvas.getHeight()));
    }

    /**
     * Start the Fx thread.
     */
    public static void startFxThread() {
        PlatformImpl.startup(() -> {
        });
    }

    @Override
    public void refreshUsers() {
        // TODO Auto-generated method stub

    }

    @Override
    public void loadUserProfile(final String name) {
        // TODO Auto-generated method stub

    }

}
