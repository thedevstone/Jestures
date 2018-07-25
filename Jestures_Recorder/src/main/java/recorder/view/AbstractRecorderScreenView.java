package recorder.view;

import java.util.Collections;
import java.util.Locale;

import org.kordamp.ikonli.material.Material;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXScrollPane;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextField;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import jestures.core.codification.FrameLength;
import jestures.core.recognition.gesture.DefaultGesture;
import jestures.core.view.enums.DialogsType.DimDialogs;
import jestures.core.view.enums.IconDim;
import jestures.core.view.enums.NotificationType;
import jestures.core.view.enums.NotificationType.Duration;
import jestures.core.view.utils.RecordingFactory;
import jestures.core.view.utils.ScrollPaneFactory;
import jestures.core.view.utils.ViewUtilities;
import recorder.controller.Recorder;
import recorder.controller.Recording;

/**
 * Recorder abstact view for initialization.
 *
 */

public abstract class AbstractRecorderScreenView implements RecView, RecViewObserver {
    // private static final Logger LOG = Logger.getLogger(AbstractRecorderScreenView.class);
    private final Recording recorder;

    // CHART
    private LineChart<Number, Number> lineChartX; // NOPMD
    private LineChart<Number, Number> lineChartY; // NOPMD
    private XYChart.Series<Number, Number> xSeries;
    private XYChart.Series<Number, Number> ySeries;
    private JFXTextField createUserTextField;
    private JFXTextField createUserGesture;
    private JFXPopup createUserPopup;
    private JFXPopup addGesturePopup;
    private JFXPopup cnavasPopup;
    private StackPane popupStackPane; // NOPMD

    // LIVE CANVAS
    private Canvas liveCanvas;
    private GraphicsContext liveCanvasContext;

    // USER CANVAS
    private Canvas userCanvas;
    private GraphicsContext userCanvasContext;

    // ########### ALL TABS #############

    @FXML
    private HBox gestureHBox;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private BorderPane recorderPane;
    @FXML
    private JFXButton startButton;
    @FXML
    private StackPane tabStackPane;
    @FXML
    private JFXComboBox<String> gestureComboBox;
    @FXML
    private VBox vbox;
    @FXML
    private ComboBox<FrameLength> frameLengthCombo;
    @FXML
    private JFXButton addGestureButton;
    @FXML
    private JFXButton removeGestureButton;

    // ########### TAB 1 #############
    @FXML
    private HBox userHBox;
    @FXML
    private BorderPane userBorderPane; // NOPMD
    @FXML
    private JFXButton createUserButton;
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
    private JFXScrollPane scrollPane;
    @FXML
    private JFXListView<BorderPane> listView;
    @FXML
    private JFXButton addAllListViewButton;
    @FXML
    private JFXButton clearListViewButton;
    @FXML
    private AnchorPane listViewAnchorPane;

    /**
     * The constructor.
     *
     * @param recorder
     *            the {@link Recorder}
     *
     */
    public AbstractRecorderScreenView(final Recording recorder) {
        this.recorder = recorder;
    }

    /**
     * Initialize method.
     */
    @FXML
    public void initialize() { // NOPMD
        this.initButtons();
        this.fillGestureCombo();
        this.initCombos();
        this.initGraphic();
        this.initLiveCanvas();
        this.initUserCanvas();
        this.initChart();
        this.initTabPaneListener();
        this.initScrollPane();
        this.initTreeView();
        this.initPopup();
        this.setDisabled();
    }

    private void setDisabled() {
        this.startButton.setDisable(true);
        this.gestureHBox.setDisable(true);
    }

    private void initPopup() {
        this.createUserTextField = new JFXTextField();
        // CREATE USER POPUP
        this.popupStackPane = RecordingFactory.createPopupContent(this.createUserTextField);
        this.createUserPopup = new JFXPopup(this.popupStackPane);
        this.createUserTextField.setId("createUserTextField");
        this.createUserTextField.setOnAction(t -> {
            this.createUserProfile(this.createUserTextField.getText());
        });

        // ADD GESTURE POPUP
        this.createUserGesture = new JFXTextField();
        this.popupStackPane = RecordingFactory.createPopupContent(this.createUserGesture);
        this.addGesturePopup = new JFXPopup(this.popupStackPane);
        this.createUserGesture.setId("createUserTextField");
        this.createUserGesture.setOnAction(t -> {
            final String temp = this.createUserGesture.getText().replaceAll("\\s+", "_").toUpperCase(Locale.ITALIAN);
            if (this.gestureComboBox.getItems().contains(temp)) {
                ViewUtilities.showNotificationPopup("Error adding gesture", temp + " already exists!", Duration.MEDIUM,
                        NotificationType.ERROR, null);
            } else {
                this.gestureComboBox.getItems().add(temp);
                this.gestureComboBox.getSelectionModel().select(temp);
                ViewUtilities.showNotificationPopup("Gesture added", temp + " gesture added!", Duration.MEDIUM,
                        NotificationType.SUCCESS, null);
                Collections.sort(this.gestureComboBox.getItems());
            }
        });

        // CANVAS POPUP
        this.cnavasPopup = new JFXPopup(new StackPane(this.getUserCanvas()));
        this.getUserCanvas().setOnMouseClicked(t -> {
            this.cnavasPopup.hide();
        });

    }

    private void initButtons() {

        // START AND STOP
        this.startButton.setOnAction(e -> {
            if (this.recorder.state()) {
                this.stopSensor();
                this.startButton.setTooltip(new Tooltip("Start the sensor"));
                this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY, IconDim.MEDIUM));
            } else {
                this.startSensor();
                this.startButton.setTooltip(new Tooltip("Start the sensor"));
                this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY_OFF, IconDim.MEDIUM));
            }
        });

        // CLEAR THE LISTVIEW
        this.clearListViewButton.setGraphic(ViewUtilities.iconSetter(Material.CLEAR, IconDim.MEDIUM));
        this.clearListViewButton.setTooltip(new Tooltip("Delete all templates"));
        JFXDepthManager.setDepth(this.clearListViewButton, 4);
        this.clearListViewButton.setOnAction(t -> {
            this.clearListView();
        });
        // ADD ALL LIST VIEW
        this.addAllListViewButton.setGraphic(ViewUtilities.iconSetter(Material.ADD, IconDim.MEDIUM));
        this.addAllListViewButton.setTooltip(new Tooltip("Add all templates"));
        JFXDepthManager.setDepth(this.addAllListViewButton, 4);
        this.addAllListViewButton.setOnAction(t -> {
            this.addAllElemInListViewToDataset();
        });

        // CREATE THE USER PROFILE
        this.createUserButton.setGraphic(ViewUtilities.iconSetter(Material.CREATE, IconDim.MEDIUM));
        this.createUserButton.setTooltip(new Tooltip("Create a user"));
        this.createUserButton.setOnAction(t -> {
            this.createUserPopup.show(this.createUserButton);
        });
        this.createUserButton.setOnMouseClicked(t -> {
            if (t.getButton().equals(MouseButton.SECONDARY)
                    && this.selectUserCombo.getSelectionModel().getSelectedIndex() != -1) {
                this.deleteSelectedUserProfile();
            }
        });
        // ADD GESTURE
        this.addGestureButton.setGraphic(ViewUtilities.iconSetter(Material.ADD_CIRCLE, IconDim.MEDIUM));
        this.addGestureButton.setTooltip(new Tooltip("Add a new gesture"));
        JFXDepthManager.setDepth(this.addGestureButton, 4);
        this.addGestureButton.setOnAction(t -> {
            this.addGesturePopup.show(this.addGestureButton);
        });
        // REMOVE GESTURE
        this.removeGestureButton.setGraphic(ViewUtilities.iconSetter(Material.REMOVE_CIRCLE, IconDim.MEDIUM));
        this.removeGestureButton.setTooltip(new Tooltip("Remove selected gesture"));
        JFXDepthManager.setDepth(this.removeGestureButton, 4);
        this.removeGestureButton.setOnAction(t -> {
            this.deleteGesture();
        });

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
        this.tabPane.getTabs().get(0).setGraphic(ViewUtilities.iconSetter(Material.PERSON, IconDim.MEDIUM));
        this.tabPane.getTabs().get(1).setGraphic(ViewUtilities.iconSetter(Material.BLUR_ON, IconDim.MEDIUM));
        this.tabPane.getTabs().get(2).setGraphic(ViewUtilities.iconSetter(Material.MULTILINE_CHART, IconDim.MEDIUM));
        this.tabPane.getTabs().get(3).setGraphic(ViewUtilities.iconSetter(Material.VIEW_LIST, IconDim.MEDIUM));
        this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY, IconDim.MEDIUM));

    }

    private void initTabPaneListener() {
        this.tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (this.tabPane.getTabs().get(3).equals(newValue) && !oldValue.equals(newValue)) {
                ViewUtilities.showDialog(this.tabStackPane, "Select Feature Vector",
                        "left-click > select \n right-click > delete", DimDialogs.MEDIUM, null);
            }
        });
    }

    private void initScrollPane() {
        // CHECKSTYLE:OFF Magicnumber AH DI MI TOCCA
        ScrollPaneFactory.wrapNodeOnScrollPane(this.scrollPane, this.listView, "Feature Vectors",
                "headerFeatureVector");
        this.listView.minHeightProperty().bind(this.listViewAnchorPane.heightProperty().subtract(200));
        ScrollPaneFactory.wrapNodeOnScrollPane(this.userScrollPane, this.userBorderPane, "Select User",
                "headerUserPane");
        this.treeView.maxHeightProperty().bind(this.tabStackPane.heightProperty().subtract(300));
        this.treeView.minHeightProperty().bind(this.tabStackPane.heightProperty().subtract(300));
        JFXDepthManager.setDepth(this.userHBox, 1);
        JFXDepthManager.setDepth(this.createUserButton, 2);
        // CHECKSTYLE:ON Magicnumber AH DI MI TOCCA
    }

    private void initCombos() {
        JFXDepthManager.setDepth(this.gestureHBox, 1);
        this.frameLengthCombo.setOnAction(t -> this.setFrameLength(this.frameLengthCombo.getValue()));
        this.frameLengthCombo.getItems().add(FrameLength.ONE_SECOND);
        this.frameLengthCombo.getItems().add(FrameLength.TWO_SECONDS);
        this.frameLengthCombo.getItems().add(FrameLength.THREE_SECONDS);
        this.frameLengthCombo.getSelectionModel().select(this.getFrameLength());
        JFXDepthManager.setDepth(this.frameLengthCombo, 4);

        // GESTURE COMBOBOX
        JFXDepthManager.setDepth(this.gestureComboBox, 4);
        this.gestureComboBox.getSelectionModel().selectedItemProperty().addListener((v, oldValue, newValue) -> {
            if (newValue != null) {
                this.selectGesture(newValue);
            }
        });
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
     * Draw saved gestures on canvas.
     *
     * @param parent
     *            the {@link TreeItem} parent
     * @param indexOf
     *            the feature vevector index for the selected gesture
     */
    public abstract void drawSavedGestureOnCanvas(TreeItem<String> parent, int indexOf);

    /**
     * Fill gesture with default combo.
     *
     */
    public void fillGestureCombo() {
        this.gestureComboBox.getItems().addAll(DefaultGesture.getAllDefaultGestures());
    }

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
