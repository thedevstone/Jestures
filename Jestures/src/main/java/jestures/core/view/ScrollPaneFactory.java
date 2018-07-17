package jestures.core.view;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
     * @param list
     *            the {@link JFXListView} listView
     * @param titleString
     *            the {@link String} title
     * @param cssId
     *            the cssId
     */
    // CHECKSTYLE:OFF Magicnumber AH DI MI TOCCA
    public static void wrapListViewOnScrollPane(final JFXScrollPane pane, final JFXListView<BorderPane> list,
            final String titleString, final String cssId) {
        list.setMaxHeight(3400);
        final StackPane container = new StackPane(list);
        container.setPadding(new Insets(-1));
        pane.setContent(container);
        final Label title = new Label(titleString);
        pane.getBottomBar()
            .getChildren()
            .add(title);
        title.setStyle("-fx-text-fill:WHITE; -fx-font-size: 40;");
        StackPane.setMargin(title, new Insets(0, 0, 0, 80));
        StackPane.setAlignment(title, Pos.CENTER_LEFT);
        pane.getMainHeader()
            .setId(cssId);
        // CHECKSTYLE:ON Magicnumber
    }

}
