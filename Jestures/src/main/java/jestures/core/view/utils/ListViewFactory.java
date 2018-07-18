package jestures.core.view.utils;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.effects.JFXDepthManager;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * A simil factory class for the creation of gui components ({@link JFXListView}).
 */
public final class ListViewFactory {

    private ListViewFactory() {
    }

    /**
     * Create a {@link JFXListView} for the Players.
     *
     * @param listView
     *            the {@link JFXListView}
     * @param imageProfile
     *            the {@link Image} profile
     * @param index
     *            the {@link Integer} index
     */
    // CHECKSTYLE:OFF AH DI MI TOCCA FARE COSI
    public static void addVectorToListView(final JFXListView<BorderPane> listView, final Image imageProfile,
            final int index) {
        final BorderPane pane = new BorderPane();
        final Label label = new Label(" " + index);
        final ImageView imageView = new ImageView(imageProfile);
        JFXDepthManager.setDepth(pane, 1);
        label.setId("player-listView-label");
        pane.setId("player-listView-border");
        imageView.setFitHeight(150);
        imageView.setFitWidth(200);
        BorderPane.setAlignment(imageView, Pos.CENTER);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        pane.setCenter(imageView);
        pane.setLeft(label);
        listView.getItems().add(pane);
        listView.scrollTo(index);
    }
    // CHECKSTYLE:ON

}
