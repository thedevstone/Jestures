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

public abstract class AbstractRecorderScreenView implements RecView {
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

    // CANVAS
    private Canvas canvas;
    private GraphicsContext context;

    @FXML
    private JFXButton addGestureButton;
    @FXML
    private JFXButton createUserButton;
    @FXML
    private JFXComboBox<String> selectUserCombo;
    @FXML
    private Canvas userCanvas;
    @FXML
    private JFXTreeView<String> treeView;
    @FXML
    private JFXButton addAllListViewButton;
    @FXML
    private JFXButton clearListViewButton;
    @FXML
    private AnchorPane listViewAnchorPane;
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
    @FXML
    private JFXComboBox<String> gestureComboBox;
    @FXML
    private JFXScrollPane scrollPane;
    @FXML
    private JFXListView<BorderPane> listView;
    @FXML
    private StackPane tabStackPane;

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
        this.initCanvas();
        this.initChart();
        this.initTabPaneListener();
        this.initListView();
        this.initTreeView();
        this.initPopup();

    }

    private void initTreeView() {

    }

    private void initPopup() {
        // CREATE USER POPUP
        this.createUserTextField = new JFXTextField("Insert UserName");
        this.createUserPopup = new JFXPopup(this.createUserTextField);
        this.createUserTextField.setId("createUserTextField");
        this.createUserTextField.setOnAction(t -> {
            this.loadUsers();
            this.createUserProfile(this.createUserTextField.getText());
        });

        // ADD GESTURE POPUP
        this.createUserGesture = new JFXTextField("Insert gesture name");
        this.addGesturePopup = new JFXPopup(this.createUserGesture);
        this.createUserGesture.setId("createUserTextField");
        this.createUserGesture.setOnAction(t -> {
            final String temp = this.createUserGesture.getText().trim().toUpperCase(Locale.ITALIAN);
            if (this.gestureComboBox.getItems().contains(temp)) {
                ViewUtilities.showNotificationPopup("Error adding gesture", temp + " already exists!", Duration.MEDIUM,
                        NotificationType.ERROR, null);
            } else {
                this.gestureComboBox.getItems().add(temp);
                ViewUtilities.showNotificationPopup("Gesture added", temp + " gesture added!", Duration.MEDIUM,
                        NotificationType.SUCCESS, null);
                Collections.sort(this.gestureComboBox.getItems());
            }
        });

    }

    private void initButtons() {
        // DISABLE THE START TILL USER SELECT PROFILE AND GESTURE
        this.startButton.setDisable(true);

        // START AND STOP
        this.startButton.setOnAction(e -> {
            if (this.recorder.state()) {
                this.stopSensor();
                this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY, IconDim.MEDIUM));
                this.selectUserCombo.setDisable(false);
            } else {
                this.startSensor();
                this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY_OFF, IconDim.MEDIUM));
                this.selectUserCombo.setDisable(true);
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
            this.addAllElemInListView();
        });

        // CREATE THE USER PROFILE
        this.createUserButton.setGraphic(ViewUtilities.iconSetter(Material.CREATE, IconDim.MEDIUM));
        this.createUserButton.setTooltip(new Tooltip("Create a user"));
        this.createUserButton.setOnAction(t -> {
            this.createUserPopup.show(this.recorderPane);
        });
        // ADD GESTURE
        this.addGestureButton.setGraphic(ViewUtilities.iconSetter(Material.ADD_CIRCLE, IconDim.MEDIUM));
        this.addGestureButton.setTooltip(new Tooltip("Add a new gesture"));
        this.addGestureButton.setOnAction(t -> {
            this.addGesturePopup.show(this.recorderPane);
        });
        this.addGestureButton.setDisable(true);

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
        this.tabPane.getTabs().get(0).setGraphic(ViewUtilities.iconSetter(Material.PERSON, IconDim.MEDIUM));
        this.tabPane.getTabs().get(1).setGraphic(ViewUtilities.iconSetter(Material.BLUR_ON, IconDim.MEDIUM));
        this.tabPane.getTabs().get(2).setGraphic(ViewUtilities.iconSetter(Material.MULTILINE_CHART, IconDim.MEDIUM));
        this.tabPane.getTabs().get(3).setGraphic(ViewUtilities.iconSetter(Material.VIEW_LIST, IconDim.MEDIUM));
        this.startButton.setGraphic(ViewUtilities.iconSetter(Material.VISIBILITY, IconDim.MEDIUM));

    }

    private void initTabPaneListener() {
        this.tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (this.tabPane.getTabs().get(2).equals(newValue) && !oldValue.equals(newValue)) {
                ViewUtilities.showDialog(this.tabStackPane, "Select Feature Vector",
                        "left-click > select \n right-click > delete", DimDialogs.MEDIUM, null);
            }
        });
    }

    private void initListView() {
        // CHECKSTYLE:OFF Magicnumber AH DI MI TOCCA
        ScrollPaneFactory.wrapListViewOnScrollPane(this.scrollPane, this.listView, "Feature Vectors",
                "headerFeatureVector");
        this.listView.minHeightProperty().bind(this.listViewAnchorPane.heightProperty().subtract(200));
        // CHECKSTYLE:ON Magicnumber AH DI MI TOCCA
    }

    private void initCombos() {
        this.frameLengthCombo.setOnAction(t -> this.setFrameLength(this.frameLengthCombo.getValue()));
        this.frameLengthCombo.getItems().add(FrameLength.ONE_SECOND);
        this.frameLengthCombo.getItems().add(FrameLength.TWO_SECONDS);
        this.frameLengthCombo.getItems().add(FrameLength.THREE_SECONDS);
        this.frameLengthCombo.getSelectionModel().select(this.getFrameLength());
        JFXDepthManager.setDepth(this.frameLengthCombo, 4);

        // GESTURE COMBOBOX
        this.gestureComboBox.setDisable(true);
        JFXDepthManager.setDepth(this.gestureComboBox, 4);
        this.gestureComboBox.setOnAction(t -> {
            final int index = this.gestureComboBox.getSelectionModel().getSelectedIndex();
            final String selected = this.gestureComboBox.getSelectionModel().getSelectedItem();

            if (index != -1) {
                this.selectGesture(selected);
                this.startButton.setDisable(false);
            } else {
                ViewUtilities.showNotificationPopup("Cannot select gesture", "", Duration.MEDIUM,
                        NotificationType.ERROR, null);
            }
        });

        // USER COMBOBOX
        Collections.sort(this.selectUserCombo.getItems());
        this.selectUserCombo.setOnAction(t -> {
            final int index = this.selectUserCombo.getSelectionModel().getSelectedIndex();
            final String selected = this.selectUserCombo.getSelectionModel().getSelectedItem();
            if (index != -1) {
                this.loadUserProfile(selected);
                this.gestureComboBox.setDisable(false);
                this.addGestureButton.setDisable(false);
            } else {
                ViewUtilities.showNotificationPopup("Cannot select user", "", Duration.MEDIUM, NotificationType.ERROR,
                        null);
            }
        });
    }

    /**
     * Fill gesture with default combo.
     *
     */
    public void fillGestureCombo() {
        for (final DefaultGesture gesture : DefaultGesture.values()) {
            this.gestureComboBox.getItems().add(gesture.getGestureName());
        }
        Collections.sort(this.gestureComboBox.getItems());
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
    public Canvas getCanvas() {
        return this.canvas;
    }

    /**
     * Get the {@link GraphicsContext}.
     *
     * @return the {@link GraphicsContext}
     */
    public GraphicsContext getContext() {
        return this.context;
    }

}
