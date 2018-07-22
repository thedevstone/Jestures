package jestures.core.view.utils;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Static Factory.
 */
public final class ScrollPaneFactory {

    private ScrollPaneFactory() {
    }

    /**
     * Wrap a {@link JFXListView} on a {@link JFXScrollPane} adding functionality.
     *
     * @param pane
     *            the {@link JFXScrollPane} pane
     * @param node
     *            the {@link Node}
     * @param titleString
     *            the {@link String} title
     * @param cssId
     *            the cssId
     */
    // CHECKSTYLE:OFF Magicnumber AH DI MI TOCCA
    public static void wrapNodeOnScrollPane(final JFXScrollPane pane, final Node node, final String titleString,
            final String cssId) {
        // list.setMinHeight(1000); //ACTIVATE SCROLL
        final StackPane container = new StackPane(node);
        container.setPadding(new Insets(-1));
        pane.setContent(container);
        final Label title = new Label(titleString);
        pane.getBottomBar().getChildren().add(title);
        title.setStyle("-fx-text-fill:WHITE; -fx-font-size: 40;");
        StackPane.setMargin(title, new Insets(0, 0, 0, 80));
        StackPane.setAlignment(title, Pos.CENTER_LEFT);
        pane.getMainHeader().setId(cssId);

        // CHECKSTYLE:ON Magicnumber
    }

}
