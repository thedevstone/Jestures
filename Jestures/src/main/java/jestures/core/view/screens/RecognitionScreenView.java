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
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.log4j.Logger;
import org.kordamp.ikonli.material.Material;

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
import jestures.core.codification.FrameLength;
import jestures.core.file.FileManager;
import jestures.core.recognition.Recognition;
import jestures.core.recognition.UpdateRate;
import jestures.core.recognition.gesturedata.RecognitionSettingsImpl;
import jestures.core.view.enums.DialogsType.DimDialogs;
import jestures.core.view.enums.IconDim;
import jestures.core.view.enums.NotificationType;
import jestures.core.view.enums.NotificationType.Duration;
import jestures.core.view.utils.ViewUtilities;

/**
 * The effective instance of the recognition view. This is a simple screen controller for a javafx fxml file.
 *
 */

@SuppressWarnings("restriction")
public class RecognitionScreenView extends AbstractRecognitionScreenView {
    private static final Logger LOG = Logger.getLogger(RecognitionScreenView.class);
    private final Recognition recognizer;
    private int frameLength;

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

    /**
     * @param recognizer
     *            the {@link RecognitionScreenView}
     */
    public RecognitionScreenView(final Recognition recognizer) {
        super(recognizer);
        this.recognizer = recognizer;
        this.frameLength = recognizer.getFrameLength().getFrameNumber();
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
    public void initialize() { // NOPMD
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
    public void notifyOnFrameChange(final int frame, final Vector2D derivative, final Vector2D path) {
        Platform.runLater(() -> {
            if (frame == 0) {
                this.clearCanvasAndChart();
            }
            this.getxSeries().getData().add(new XYChart.Data<Number, Number>(frame, (int) derivative.getX()));
            this.getySeries().getData().add(new XYChart.Data<Number, Number>(frame, (int) derivative.getY()));

            this.getLiveContext().fillOval(-path.getX() + this.getLiveCanvas().getWidth() / 2,
                    path.getY() + this.getLiveCanvas().getHeight() / 2, 10, 10);
            this.progressBar.setProgress(frame / (this.frameLength + 0.0));
        });
    }

    @Override
    public void notifyOnFeatureVectorEvent() {

    }

    @Override
    public void refreshUsers() {
        Platform.runLater(() -> {
            try {
                this.selectUserCombo.getItems().clear();
                FileManager.getAllUserFolder().stream().forEachOrdered(t -> this.selectUserCombo.getItems().add(t));
            } catch (final IOException e) {
                ViewUtilities.showNotificationPopup("Exception", "Cannot read user data", Duration.MEDIUM,
                        NotificationType.WARNING, null);
            }
        });
    }

    @Override
    public FrameLength getFrameLength() {
        return this.recognizer.getFrameLength();
    }

    // ############################## FROM RECOGNIZER (RECOGNITION VIEW OBSERVER )##########################
    // TAB 4
    @Override
    public void updateSettings(final RecognitionSettingsImpl settings) {
        Platform.runLater(() -> {
            this.udpateRateCombo.getSelectionModel().select(settings.getUpdateRate());
            this.sliderRadius.setValue(settings.getDtwRadius() * 10);
            this.sliderMinThreshold.setValue(settings.getMinDtwThreashold());
            this.sliderMaxThreshold.setValue(settings.getMaxDTWThreashold());
            this.sliderTimeSeparation.setValue(settings.getMinTimeSeparation());
            this.sliderMatchNumber.setValue(settings.getMatchNumber());
        });
    }

    // ############################################## TO TRACKER (VIEW) ###################################
    @Override
    public void startSensor() {
        this.selectUserCombo.setDisable(true);
        this.recognizer.startSensor();
    }

    @Override
    public void stopSensor() {
        this.clearCanvasAndChart();
        this.recognizer.stopSensor();
        this.selectUserCombo.setDisable(false);
    }

    @Override
    public void setFrameLength(final FrameLength length) {
        this.recognizer.setFrameLength(length);
        this.setChart(length.getFrameNumber(), length.getFrameNumber());
        this.frameLength = length.getFrameNumber();
    }

    // ######################################## TO RECOGNIZER (RECOGNITION VIEW) ###################################

    @Override
    public void loadUserProfile(final String name) {
        try {
            this.recognizer.loadUserProfile(name);
        } catch (final IOException e1) {
            ViewUtilities.showNotificationPopup("User Dataset not found", "Regenerating it", Duration.MEDIUM, // NOPMD
                    NotificationType.ERROR, t -> e1.printStackTrace());
        } catch (final JsonSyntaxException e2) {
            ViewUtilities.showNotificationPopup("Json file changed by human!", "Please click to se exception",
                    Duration.MEDIUM, // NOPMD
                    NotificationType.ERROR, t -> e2.printStackTrace());
        }
        ((Label) this.userScrollPane.getBottomBar().getChildren().get(0)).setText(name);
        // LOAD USER GESTURES
        ViewUtilities.showSnackBar((Pane) this.recorderPane.getCenter(), "Database loaded and Gesture updated!",
                Duration.MEDIUM, DimDialogs.SMALL, null);
        this.createGestureTreeView(this.recognizer.getUserName());
        this.startButton.setDisable(false);
    }

    @Override
    public void setDtwRadius(final double radius) {
        this.recognizer.setDtwRadius(radius);
    }

    @Override
    public void setMinDtwThreashold(final int minDtwThreashold) {
        this.recognizer.setMinDtwThreashold(minDtwThreashold);
    }

    @Override
    public void setMaxDtwThreashold(final int maxDtwThreashold) {
        this.recognizer.setMaxDtwThreashold(maxDtwThreashold);
    }

    @Override
    public void setUpdateRate(final UpdateRate updateRate) {
        this.recognizer.setUpdateRate(updateRate);
    }

    @Override
    public void setMinTimeSeparation(final int minTimeSeparation) {
        this.recognizer.setMinTimeSeparation(minTimeSeparation);
    }

    @Override
    public void setMatchNumber(final int matchNumber) {
        this.recognizer.setMatchNumber(matchNumber);
    }

    @Override
    public void saveSettings() {
        try {
            this.recognizer.saveSettings();
        } catch (final IOException e) {
            ViewUtilities.showNotificationPopup("Exception", "Cannot serialize user data", Duration.MEDIUM,
                    NotificationType.WARNING, null);
        }
    }

    // ################# TREE VIEW ###################
    // CANVAS GESTURE
    @Override
    public void drawSavedGestureOnCanvas(final TreeItem<String> gestureItem, final int templateIndex) {
        final List<Vector2D> template = this.recognizer.getGestureDataset(gestureItem.getValue()).get(templateIndex);
        this.getUserCanvasContext().clearRect(0, 0, this.getLiveCanvas().getWidth(), this.getLiveCanvas().getHeight());
        for (final Vector2D path : template) {
            this.getUserCanvasContext().fillOval(-path.getX() + this.getLiveCanvas().getWidth() / 2,
                    path.getY() + this.getLiveCanvas().getHeight() / 2, 10, 10);
        }
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
        // REGENERATE TREVIEW FOR USER
        this.root = new TreeItem<String>(root);
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
            this.makeTemplateBranch("Template: " + (i + 1), item);
        }
        parent.getChildren().add(item);
        return item;
    }

    // CREO UN TREEELEM PER OGNI TEMPLATE
    private TreeItem<String> makeTemplateBranch(final String gestureName, final TreeItem<String> parent) {
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
