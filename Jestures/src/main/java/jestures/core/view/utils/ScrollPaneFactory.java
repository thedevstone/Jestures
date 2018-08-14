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

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXScrollPane;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Static Factory for scroll pane.
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
