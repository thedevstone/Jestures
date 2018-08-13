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

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.recognition.Recognition;
import jestures.core.tracking.Tracker;

/**
 * A simple skeleton view. Extends it to create your own view.
 */
public abstract class AbstractView implements RecognitionView {
    private final Recognition recognizer; // NOPMD

    /**
     * The @link{AbstractView.java} constructor.
     *
     * @param recognizer
     *            the {@link Tracker} recognizer
     */
    public AbstractView(final Recognition recognizer) {
        this.recognizer = recognizer;
        this.recognizer.getClass();
    }

    @Override
    public abstract void notifyOnFrameChange(int frame, Vector2D derivative, Vector2D path);

    @Override
    public abstract void notifyOnFeatureVectorEvent();

}
