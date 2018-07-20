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

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.gson.JsonIOException;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;
import com.sun.javafx.application.PlatformImpl;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import jestures.core.codification.FrameLength;
import jestures.core.file.FileManager;
import jestures.core.view.enums.DialogsType.DimDialogs;
import jestures.core.view.enums.NotificationType;
import jestures.core.view.enums.NotificationType.Duration;
import jestures.core.view.utils.ListViewFactory;
import jestures.core.view.utils.ViewUtilities;
import recorder.controller.Recording;

/**
 *
 *
 */
@SuppressWarnings("restriction")
public class RecorderScreenView extends AbstractRecorderScreenView implements RecView {
    private final Recording recorder;
    private FrameLength frameLength;
    // VIEW
    private Stage stage; // NOPMD
    private Scene scene; // NOPMD

    @FXML
    private JFXComboBox<String> gestureComboBox;
    @FXML
    private JFXComboBox<String> selectUserCombo;
    @FXML
    private BorderPane recorderPane; // NOPMD
    @FXML
    private JFXScrollPane scrollPane;
    @FXML
    private JFXListView<BorderPane> listView;

    /**
     * @param recorder
     *            the {@link RecorderScreenView}
     */
    public RecorderScreenView(final Recording recorder) {
        super(recorder);
        this.recorder = recorder;
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

    @Override
    @FXML
    public void initialize() { // NOPMD
        super.initialize();

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

    private void chargeSceneSheets(final FXMLScreens screen) {
        this.scene.getStylesheets().add(RecorderScreenView.class.getResource(screen.getCssPath()).toString());
    }

    // ############################################## FROM TRACKER (RECORDER) ###################################
    @Override
    public void notifyOnFrameChange(final int frame, final Vector2D derivative, final Vector2D path) {
        Platform.runLater(() -> {
            if (frame == 0) {
                this.getContext().clearRect(0, 0, this.getCanvas().getWidth(), this.getCanvas().getHeight());
                this.getxSeries().getData().clear();
                this.getySeries().getData().clear();
            }
            this.getxSeries().getData().add(new XYChart.Data<Number, Number>(frame, (int) derivative.getX()));
            this.getySeries().getData().add(new XYChart.Data<Number, Number>(frame, (int) derivative.getY()));

            this.getContext().fillOval(-path.getX() + this.getCanvas().getWidth() / 2,
                    path.getY() + this.getCanvas().getHeight() / 2, 10, 10);
        });

    }

    @Override
    public void notifyOnFeatureVectorEvent() {
        Platform.runLater(() -> {
            this.addFeatureVectorToListView(this.listView.getItems().size(),
                    this.getCanvas().snapshot(new SnapshotParameters(), null));
        });
    }

    // ############################################## FROM RECORDER ###################################

    @Override
    public void loadUsers() {
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
    public void setRecording(final boolean isRecording) {
        if (isRecording) {

            Platform.runLater(() -> {
                ViewUtilities.showSnackBar((Pane) this.recorderPane.getCenter(), "Record is started", Duration.MEDIUM,
                        DimDialogs.SMALL, null);
            });
        } else {
            Platform.runLater(() -> {
                ViewUtilities.showSnackBar((Pane) this.recorderPane.getCenter(), "Record is stopped", Duration.MEDIUM,
                        DimDialogs.SMALL, null);
            });
        }
    }

    // ############################################## TO RECORDER ###################################

    // #################################### ALL TABS #############################################
    @Override
    public void selectGesture(final String gesture) {
        System.out.println(gesture);
    }

    @Override
    public void setFrameLength(final FrameLength length) {
        this.frameLength = length;
        this.recorder.setFrameLength(length);
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

    @Override
    public void startFxThread() {
        PlatformImpl.startup(() -> {
        });
    }

    @Override
    public FrameLength getFrameLength() {
        return this.frameLength;
    }

    // ###### TAB 1 ######

    @Override
    public void loadUserProfile(final String name) {
        this.recorder.loadUserProfile(name);
    }

    // ###### TAB 4 ######
    @Override
    public void deleteFeatureVectorInLIstView(final int indexClicked) {
        this.listView.getItems().remove(indexClicked);
        this.recorder.deleteFeatureVector(indexClicked);
        this.scrollPane.setContent(this.listView);

    }

    @Override
    public void clearListView() {
        this.listView.getItems().clear();
        this.recorder.clearFeatureVectors();
        this.scrollPane.setContent(this.listView);
    }

    @Override
    public void addFeatureVector(final String gesture, final int indexClicked) {
        try {
            this.recorder.addFeatureVector(this.getGesture(), indexClicked);
        } catch (final JsonIOException e) {
            ViewUtilities.showNotificationPopup("Json Exception", "Cannot serialize file", Duration.MEDIUM, // NOPMD
                    NotificationType.WARNING, t -> e.printStackTrace());
        } catch (final IOException e2) {
            ViewUtilities.showNotificationPopup("Io Exception", "Cannot serialize file", Duration.MEDIUM,
                    NotificationType.WARNING, t -> e2.printStackTrace());
        }
        this.deleteFeatureVectorInLIstView(indexClicked);
    }

    @Override
    public void addAllElemInListView() {
        this.listView.getItems().clear();
        try {
            this.recorder.addAllFeatureVectors(this.getGesture());
        } catch (final JsonIOException e) {
            ViewUtilities.showNotificationPopup("Json Exception", "Cannot serialize file", Duration.MEDIUM,
                    NotificationType.WARNING, t -> e.printStackTrace());

        } catch (final IOException e2) {
            ViewUtilities.showNotificationPopup("Io Exception", "Cannot serialize file", Duration.MEDIUM,
                    NotificationType.WARNING, t -> e2.printStackTrace());
        }

    }

    // ############################################## INSTANCE METHODS ###################################
    private void addFeatureVectorToListView(final int index, final Image image) {
        ListViewFactory.addVectorToListView(this.listView, image, index);

        // ON CLICK ACTION
        this.listView.setOnMouseClicked(t -> {
            final int indexClicked = this.listView.getSelectionModel().getSelectedIndex();
            if (t.getButton().equals(MouseButton.PRIMARY) && indexClicked != -1) {

                ViewUtilities.showConfirmDialog(this.scrollPane, "Save",
                        "Save the feature vector N: " + indexClicked + "?", DimDialogs.MEDIUM, (final Event event) -> {
                            if (((JFXButton) event.getSource()).getText().equals("YES")) {
                                this.addFeatureVector(this.getGesture(), indexClicked);
                            }
                        });

            } else if (indexClicked != -1) {
                this.deleteFeatureVectorInLIstView(indexClicked);
            }

        });
    }

    private String getGesture() {
        final int i = this.gestureComboBox.getSelectionModel().getSelectedIndex();
        if (i != -1) {
            return this.gestureComboBox.getSelectionModel().getSelectedItem();
        } else {
            throw new IllegalStateException("No gesture selected");
        }
    }

}
