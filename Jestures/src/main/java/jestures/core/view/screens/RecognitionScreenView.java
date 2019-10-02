/*******************************************************************************
 * Copyright (c) 2018 Giulianini Luca Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 *******************************************************************************/

package jestures.core.view.screens;

import com.google.gson.JsonSyntaxException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTreeView;
import com.sun.javafx.application.PlatformImpl;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jestures.core.codification.GestureLength;
import jestures.core.file.FileManager;
import jestures.core.recognition.Recognition;
import jestures.core.recognition.UpdateRate;
import jestures.core.recognition.gesturedata.RecognitionSettings;
import jestures.core.view.enums.DialogsType.DimDialogs;
import jestures.core.view.enums.IconDim;
import jestures.core.view.enums.NotificationType;
import jestures.core.view.enums.NotificationType.Duration;
import jestures.core.view.utils.ViewUtilities;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.log4j.Logger;
import org.kordamp.ikonli.material.Material;

import java.io.IOException;
import java.util.List;

/**
 * The effective instance of the recognition view. This is a simple screen controller for a javafx fxml file.
 */

public class RecognitionScreenView extends AbstractRecognitionScreenView {
    private static final Logger LOG = Logger.getLogger(RecognitionScreenView.class);
    /**
     * Recognizer.
     */
    private final Recognition recognizer;
    /**
     * Gesture length.
     */
    private GestureLength gestureLength;

    // VIEW
    private Stage stage; // NOPMD
    private Scene scene; // NOPMD

    // TREE VIEW
    private TreeItem<String> root; // NOPMD

    // ########### ALL TABS #############
    @FXML
    private JFXProgressBar progressBar;
    @FXML
    private BorderPane recorderPane; // NOPMD
    @FXML
    private JFXButton startButton;
    @FXML
    private JFXScrollPane userScrollPane;
    @FXML
    private Label labelGestureLength;
    // ########### TAB 1 #############
    @FXML
    private JFXTreeView<String> treeView;
    @FXML
    private BorderPane userBorderPane; // NOPMD
    @FXML
    private JFXComboBox<String> selectUserCombo;

    // ########### TAB 2 #############

    // ########### TAB 3 #############

    // ########### TAB 4 #############
    @FXML
    private JFXComboBox<UpdateRate> samplingRateCombo;

    @FXML
    private JFXSlider sliderRadius;

    @FXML
    private JFXSlider sliderConfidence;

    @FXML
    private JFXSlider sliderTimeSeparation;

    @FXML
    private JFXSlider sliderK;
    @FXML
    private JFXButton saveSettingsButton;

    /**
     * @param recognizer the {@link RecognitionScreenView}
     */
    public RecognitionScreenView(final Recognition recognizer) {
        super(recognizer);
        this.recognizer = recognizer;
        this.gestureLength = recognizer.getFrameLength();

        // CREATE AND SET THE CONTROLLER. INIT THE BORDER PANE
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
        RecognitionScreenView.LOG.getClass();
    }

    @Override
    @FXML
    public final void initialize() { // NOPMD
        super.initialize();
        // CREATION OF STAGE SCENE AND PANE
        this.stage = new Stage();
        this.scene = new Scene(this.recorderPane);
        this.stage.setScene(this.scene);
        // SETTING EXIT ACTIONS
        this.stage.setOnCloseRequest(e -> {
            this.stopSensor();
            Platform.exit();
            System.exit(0);
        });
        // CHARGE THE CSS
        this.chargeSceneSheets(FXMLScreens.HOME);
        this.stage.show();

    }

    private void chargeSceneSheets(final FXMLScreens screen) {
        this.scene.getStylesheets().add(RecognitionScreenView.class.getResource(screen.getCssPath()).toString());
    }

    // ############################################## FROM TRACKER (VIEW OBSERVER) ###################################

    @Override
    public final void notifyOnFrameChange(final int frame, final Vector2D derivative, final Vector2D path) {
        Platform.runLater(() -> {
            // When it's the first frame it reset the canvas.
            if (frame == 0) {
                this.clearCanvasAndChart();
            }
            // fill the chart with data
            this.getxSeries().getData().add(new XYChart.Data<>(frame, (int) derivative.getX()));
            this.getySeries().getData().add(new XYChart.Data<>(frame, (int) derivative.getY()));
            // Draw oval on canvas
            this.getLiveContext().fillOval(-path.getX() + this.getLiveCanvas().getWidth() / 2,
                    path.getY() + this.getLiveCanvas().getHeight() / 2, 10, 10);
            // Move the progress bar
            this.progressBar.setProgress(frame / (this.gestureLength.getFrameNumber() + 0.0));
        });
    }

    @Override
    public void notifyOnFeatureVectorEvent() {
        // When a gesture is ready
    }

    @Override
    public final void refreshUsers() {
        Platform.runLater(() -> {
            try {
                // clear all users
                this.selectUserCombo.getItems().clear();
                // reload the users
                FileManager.getAllUserFolder().stream().forEachOrdered(t -> this.selectUserCombo.getItems().add(t));
            } catch (final IOException e) {
                ViewUtilities.showNotificationPopup("Exception", "Cannot read user data", Duration.MEDIUM,
                        NotificationType.WARNING, null);
            }
        });
    }

    // ############################## FROM RECOGNIZER (RECOGNITION VIEW OBSERVER )##########################
    // TAB 4
    @Override
    public final void updateSettings(final RecognitionSettings settings) {
        Platform.runLater(() -> {
            this.samplingRateCombo.getSelectionModel().select(settings.getSamplingRate());
            this.sliderRadius.setValue(settings.getDtwRadius() * 10);
            this.sliderConfidence.setValue(settings.getDTWConfidenceThreshold() * 100);
            this.sliderTimeSeparation.setValue(settings.getMinTimeSeparation());
            this.sliderK.setValue(settings.getK());
        });
    }

    @Override
    public final void onGestureRecognized(final String gestureName) {
        // Snack bar with the gesture name
        Platform.runLater(() -> {
            ViewUtilities.showSnackBar((Pane) this.recorderPane.getCenter(), gestureName, Duration.MEDIUM,
                    DimDialogs.MEDIUM, null);
        });
    }

    @Override
    public final void setGestureLengthLabel(final GestureLength length) {
        // Set the label with the gesture length
        this.labelGestureLength.setText("Gesture length: " + length.getFrameNumber());
    }

    // ############################################## TO TRACKER (VIEW) ###################################
    @Override
    public final void startSensor() {
        this.selectUserCombo.setDisable(true);
        this.recognizer.startSensor();
    }

    @Override
    public final void stopSensor() {
        this.clearCanvasAndChart();
        this.recognizer.stopSensor();
        this.selectUserCombo.setDisable(false);
    }

    // ######################################## TO RECOGNIZER (RECOGNITION VIEW) ###################################

    @Override
    public final void loadUserProfile(final String name) {
        try {
            // Load the actual user
            this.recognizer.loadUserProfile(name);
        } catch (final IOException e1) {
            ViewUtilities.showNotificationPopup("User Dataset not found", "Regenerating it", Duration.MEDIUM, // NOPMD
                    NotificationType.ERROR, t -> e1.printStackTrace());
        } catch (final JsonSyntaxException e2) {
            ViewUtilities.showNotificationPopup("Json file changed by human!", "Please click to se exception",
                    Duration.MEDIUM, // NOPMD
                    NotificationType.ERROR, t -> e2.printStackTrace());
        }
        // Set User name in scroll bar
        ((Label) this.userScrollPane.getBottomBar().getChildren().get(0)).setText(name);
        // Load user gestures
        ViewUtilities.showSnackBar((Pane) this.recorderPane.getCenter(), "Database loaded and Gesture updated!",
                Duration.MEDIUM, DimDialogs.SMALL, null);
        // Create the gesture tree representation.
        this.createGestureTreeView(this.recognizer.getUserName());
        // Initialize the gesture length.
        this.gestureLength = this.recognizer.getUserGestureLength();
        // Initialize Charts
        this.setChart(this.gestureLength.getFrameNumber(), this.gestureLength.getFrameNumber());
        this.startButton.setDisable(false);
    }

    @Override
    public final void setDtwRadius(final double radius) {
        this.recognizer.setDtwRadius(radius);
    }

    @Override
    public final void setConfidenceThreshold(final int confidenceThreshold) {
        this.recognizer.setConfidenceThreshold(confidenceThreshold / 100);
    }

    @Override
    public final void setSamplingRate(final UpdateRate samplingRate) {
        this.recognizer.setSamplingRate(samplingRate);
    }

    @Override
    public final void setMinTimeSeparation(final int minTimeSeparation) {
        this.recognizer.setMinTimeSeparation(minTimeSeparation);
    }

    @Override
    public final void setK(final int k) {
        this.recognizer.setK(k);
    }

    @Override
    public final void saveSettings() {
        try {
            this.recognizer.saveSettings();
        } catch (final IOException e) {
            ViewUtilities.showNotificationPopup("Exception", "Cannot serialize user data", Duration.MEDIUM,
                    NotificationType.WARNING, null);
        }
    }

    @Override
    public final void setSensorElevation(final int angle) {
        this.recognizer.setElevationAngle(angle);
    }

    // ################# TREE VIEW ###################
    // CANVAS GESTURE
    @Override
    public final void drawSavedGestureOnCanvas(final TreeItem<String> gestureItem, final int templateIndex) {
        // Get the specific gesture from dataset (list of 2D points)
        final List<Vector2D> template = this.recognizer.getGestureDataset(gestureItem.getValue()).get(templateIndex);
        // Reset the canvas
        this.getUserCanvasContext().clearRect(0, 0, this.getLiveCanvas().getWidth(), this.getLiveCanvas().getHeight());
        // Redraw oval for every point. Now it's a derivative representation
        for (final Vector2D path : template) {
            this.getUserCanvasContext().fillOval(-path.getX() + this.getLiveCanvas().getWidth() / 2,
                    path.getY() + this.getLiveCanvas().getHeight() / 2, 10, 10);
        }
        // Show the Canvas in a pop up
        this.getCnavasPopup().show(this.recorderPane);

    }

    // ###### TAB 4 ######

    // ############################################## INSTANCE METHODS ###################################

    // 2 THREAD, UI AND KINECT
    private synchronized void clearCanvasAndChart() {
        this.getLiveContext().clearRect(0, 0, this.getLiveCanvas().getWidth(), this.getLiveCanvas().getHeight());
        this.getxSeries().getData().clear();
        this.getySeries().getData().clear();
    }

    private void createGestureTreeView(final String root) {
        // Create the treeview in a recursive way
        this.root = new TreeItem<>(root);
        this.root.setExpanded(true);
        this.treeView.setRoot(this.root);
        final List<String> userGestures = this.recognizer.getAllUserGesture();
        for (int i = 0; i < userGestures.size(); i++) {
            this.makeGestureBranch(userGestures.get(i), this.root);
        }
    }

    // CREO UN TREEELEM PER OGNI GESTURE
    private TreeItem<String> makeGestureBranch(final String gestureName, final TreeItem<String> parent) {
        final TreeItem<String> item = new TreeItem<>(gestureName);
        item.setGraphic(ViewUtilities.iconSetter(Material.GESTURE, IconDim.SMALL));
        // RICORSIVA CREO TUTTO L'ALBERO
        final List<List<Vector2D>> gestureDataset = this.recognizer.getGestureDataset(gestureName);
        for (int i = 0; i < gestureDataset.size(); i++) {
            RecognitionScreenView.makeTemplateBranch("Template: " + (i + 1), item);
        }
        parent.getChildren().add(item);
        return item;
    }

    // CREO UN TREEELEM PER OGNI TEMPLATE
    private static TreeItem<String> makeTemplateBranch(final String gestureName, final TreeItem<String> parent) {
        final TreeItem<String> item = new TreeItem<>(gestureName);
        item.setGraphic(ViewUtilities.iconSetter(Material.SHOW_CHART, IconDim.SMALL));
        parent.getChildren().add(item);
        return item;
    }

    /**
     * Start fx thread.
     */
    public static void startFxThread() {
        PlatformImpl.startup(() -> {
        });
    }
}
