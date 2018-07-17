package jestures.core.view;

import org.kordamp.ikonli.material.Material;

import com.jfoenix.controls.JFXButton;
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
     * @param nickname
     *            the {@link String} nickname
     */
    // CHECKSTYLE:OFF AH DI MI TOCCA FARE COSI
    public static void addVectorToListView(final JFXListView<BorderPane> listView, final Image imageProfile,
            final String nickname) {
        final BorderPane pane = new BorderPane();
        final Label label = new Label(nickname);
        final ImageView imageView = new ImageView(imageProfile);
        final JFXButton deletePlayer = new JFXButton();
        deletePlayer.setGraphic(ViewUtilities.iconSetter(Material.DELETE, IconDim.MEDIUM));
        JFXDepthManager.setDepth(deletePlayer, 2);
        JFXDepthManager.setDepth(pane, 1);
        label.setId("player-listView-label");
        pane.setId("player-listView-border");
        imageView.setFitHeight(70);
        imageView.setFitWidth(90);
        BorderPane.setAlignment(imageView, Pos.CENTER_LEFT);
        BorderPane.setAlignment(label, Pos.CENTER_LEFT);
        BorderPane.setAlignment(deletePlayer, Pos.CENTER_RIGHT);
        pane.setRight(deletePlayer);
        pane.setLeft(imageView);
        pane.setCenter(label);
        listView.getItems()
                .add(pane);
    }
    // CHECKSTYLE:ON

}
