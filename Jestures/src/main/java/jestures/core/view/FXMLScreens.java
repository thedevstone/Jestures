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

package jestures.core.view;

import javax.swing.text.html.CSS;

import javafx.fxml.FXML;

/**
 * Enumerator of the .fxml resources for the view.
 *
 */
enum FXMLScreens {

    /**
     * Menu {@link FXMLScreens} and {@link CSS}.
     */
    HOME("/screens/ScreenRecorder.fxml", "/sheets/ScreenRecorder.css");

    private final String resourcePath;
    private final String cssPath;

    FXMLScreens(final String path, final String styleSheetPath) {
        this.resourcePath = path;
        this.cssPath = styleSheetPath;
    }

    /**
     * Get the path of the {@link FXML}.
     *
     * @return full qualified path of the {@link FXML}
     */
    public String getPath() {
        return this.resourcePath;
    }

    /**
     * Get the path of the {@link CSS}.
     *
     * @return full qualified path of the {@link CSS}
     */
    public String getCssPath() {
        return this.cssPath;
    }
}
