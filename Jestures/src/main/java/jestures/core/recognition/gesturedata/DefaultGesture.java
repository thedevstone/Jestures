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
package jestures.core.recognition.gesturedata;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default gestures.
 *
 */
public enum DefaultGesture {
    /**
     * SWIPE LEFT.
     */
    SWIPE_LEFT("SWIPE_LEFT"),
    /**
     * SWIPE RIGHT.
     */
    SWIPE_RIGHT("SWIPE_RIGHT"),
    /**
     * SWIPE UP.
     */
    SWIPE_UP("SWIPE_UP"),
    /**
     * SWIPE DOWN.
     */
    SWIPE_DOWN("SWIPE_DOWN"),
    /**
     * CIRCLE.
     */
    CIRCLE("CIRCLE");

    private String name;

    /**
     * The @link{JestureFrameLenght.java} constructor.
     */
    DefaultGesture(final String namez) {
        this.name = namez;
    }

    /**
     * Get the @link{frameNumber}.
     *
     * @return the @link{frameNumber}
     */
    public String getGestureName() {
        return this.name;
    }

    /**
     * Fill gesture with default combo.
     *
     * @return {@link List} of {@link String}
     */
    public static List<String> getAllDefaultGestures() {
        final List<String> lista = new ArrayList<>();
        for (final DefaultGesture gesture : DefaultGesture.values()) {
            lista.add(gesture.getGestureName());
        }
        Collections.sort(lista);
        return lista;
    }

}
