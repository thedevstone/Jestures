package jestures.core.view.utils;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;

import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.NotificationEvent;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import jestures.core.view.enums.IconDim;
import jestures.core.view.enums.NotificationType;
import jestures.core.view.enums.DialogsType.DimDialogs;

/**
 * This class has all the utilities for the view.
 * <p>
 * For example you can set icons in a very simple way and many more options.
 *
 */
public final class ViewUtilities {

    private ViewUtilities() {
    }

    /**
     * A simple method for charging {@link Ikon} in {@link Node}.
     *
     * @param icon
     *            the {@link Ikon}
     * @param fontSize
     *            the font size
     * @return {@link IconDim} the {@link IconDim}
     */
    public static FontIcon iconSetter(final Ikon icon, final IconDim fontSize) {
        final FontIcon tempIcon = new FontIcon(icon);
        tempIcon.setIconSize(fontSize.getDim());
        return tempIcon;
    }

    /**
     * Blur in depth.
     *
     * @param node
     *            the root node
     * @param blur
     *            the {@link GaussianBlur}
     */
    public static void blurDeeply(final Node node, final GaussianBlur blur) {
        node.setEffect(blur);
        if (node instanceof Parent) {
            final Parent parent = (Parent) node;
            final ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (final Node child : children) {
                ViewUtilities.blurDeeply(child, blur);
            }
        }
    }

    /**
     * Cache deeply.
     *
     * @param node
     *            the {@link Node}
     */
    public static void cacheDeeply(final Node node) {
        if (!node.isCache()) {
            node.setCache(true);
            node.setCacheHint(CacheHint.SPEED);
        }
        if (node instanceof Parent) {
            final Parent parent = (Parent) node;
            final ObservableList<Node> children = parent.getChildrenUnmodifiable();
            for (final Node child : children) {
                ViewUtilities.cacheDeeply(child);
            }
        }
    }

    /**
     * Show a {@link JFXDialog} into the main {@link Pane}.
     *
     * @param mainPane
     *            the main {@link StackPane}
     * @param title
     *            the String title dialog
     * @param description
     *            the STring description
     * @param size
     *            the {@link DimDialogs} size
     * @param ev
     *            the {@link MouseEvent}
     */
    public static void showDialog(final StackPane mainPane, final String title, final String description,
            final DimDialogs size, final EventHandler<? super MouseEvent> ev) {
        String css = "";
        final JFXDialogLayout content = new JFXDialogLayout();
        final Text titolo = new Text(title);
        final Text descrizione = new Text(description);
        switch (size) {
        case SMALL:
            css = "dialogTextSmall";
            break;
        case MEDIUM:
            css = "dialogTextMedium";
            break;
        case BIG:
            css = "dialogTextBig";
            break;
        default:
            break;
        }
        descrizione.getStyleClass().add(css);
        titolo.getStyleClass().add(css);
        content.setHeading(titolo);
        content.setBody(descrizione);
        content.getStyleClass().add("dialogContentBackground");
        final JFXDialog dialog = new JFXDialog(mainPane, content, JFXDialog.DialogTransition.CENTER);
        dialog.getStyleClass().add("dialogBackground");
        dialog.show();
        content.setCache(true);
        content.setCacheHint(CacheHint.SPEED);
        dialog.setOnMouseClicked(ev);
    }

    /**
     * Show a {@link Notification} popup into the main windows of the operating system.
     *
     * @param title
     *            the String title of the {@link Notification}
     * @param message
     *            the String text of the {@link Notification}
     * @param secondsDuration
     *            the number of {@link Duration} of the {@link Notification}
     * @param notiType
     *            the {@link NotificationType} of the {@link Notification}
     * @param ev
     *            the {@link EventHandler} ev, lalmba
     */
    public static void showNotificationPopup(final String title, final String message,
            final NotificationType.Duration secondsDuration, final NotificationType notiType,
            final EventHandler<NotificationEvent> ev) { // _____________________________PATTERN STRATEGY

        final Notification.Notifier no = Notification.Notifier.INSTANCE;
        final Notification notification = new Notification(title, message);

        no.setPopupLifetime(Duration.seconds(secondsDuration.getValue()));
        switch (notiType) {
        case ERROR:
            no.notifyError(title, message);
            break;
        case WARNING:
            no.notifyWarning(title, message);
            break;
        case SUCCESS:
            no.notifySuccess(title, message);
            break;
        case INFO:
            no.notifyInfo(title, message);
            break;
        default:
            no.notify(notification);
            break;
        }
        no.setOnNotificationPressed(ev);
    }
}
