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
package jestures.core.view.utils;

import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.javafx.FontIcon;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;

import eu.hansolo.enzo.notification.Notification;
import eu.hansolo.enzo.notification.NotificationEvent;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import jestures.core.view.enums.DialogsType.DimDialogs;
import jestures.core.view.enums.IconDim;
import jestures.core.view.enums.NotificationType;

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
     *            the {@link MouseEvent} get text and check if is YES or NO
     */
    public static void showConfirmDialog(final StackPane mainPane, final String title, final String description,
            final DimDialogs size, final EventHandler<? super MouseEvent> ev) {
        String css = "";
        final JFXDialogLayout content = new JFXDialogLayout();
        final Text titolo = new Text(title);
        final Text descrizione = new Text(description);
        final JFXButton buttonYes = new JFXButton("YES");
        final JFXButton buttonNo = new JFXButton("NO");
        switch (size) {
        case SMALL:
            css = "confirmDialogTextSmall";
            break;
        case MEDIUM:
            css = "confirmDialogTextMedium";
            break;
        case BIG:
            css = "confirmDialogTextBig";
            break;
        default:
            break;
        }
        descrizione.getStyleClass().add(css);
        titolo.getStyleClass().add(css);
        content.setHeading(titolo);
        content.setBody(descrizione);
        content.setActions(buttonYes, buttonNo);
        content.getStyleClass().add("confirmDialogContentBackground");
        final JFXDialog dialog = new JFXDialog(mainPane, content, JFXDialog.DialogTransition.CENTER);
        dialog.getStyleClass().add("confirmDialogBackground");
        buttonYes.setOnMouseClicked(ev);
        buttonNo.setOnMouseClicked(ev);
        buttonYes.setOnAction(t -> dialog.close());
        buttonNo.setOnAction(t -> dialog.close());
        dialog.show();

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

    /**
     * Create a {@link JFXSnackbar}.
     *
     * @param snackBarContainer
     *            the {@link Pane} container
     * @param message
     *            the {@link String} message
     * @param secondsDuration
     *            duration
     * @param textSize
     *            the {@link DimDialogs} size
     * @param event
     *            the {@link ActionEvent}
     */
    public static void showSnackBar(final Pane snackBarContainer, final String message,
            final NotificationType.Duration secondsDuration, final DimDialogs textSize,
            final EventHandler<ActionEvent> event) {
        final JFXSnackbar bar = new JFXSnackbar(snackBarContainer);
        final SnackbarEvent eventToast = new SnackbarEvent(message, null, (long) secondsDuration.getValue() * 1000,
                false, event);
        bar.enqueue(eventToast);
    }
}
