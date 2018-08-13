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

import jestures.core.codification.FrameLength;

/**
 * The {@link ViewObserver} class.
 *
 */
public interface ViewObserver {

    /**
     * Update view on frame event.
     *
     * @param frame
     *            the frame
     * @param derivative
     *            the {@link Vector2D} derivative
     * @param path
     *            the {@link Vector2D} gesture path
     */
    void notifyOnFrameChange(int frame, Vector2D derivative, Vector2D path);

    /**
     * Update view on feature vector event.
     *
     *
     */
    void notifyOnFeatureVectorEvent();

    /**
     * Get the {@link FrameLength} for tracking.
     *
     * @return the {@link FrameLength}
     */
    FrameLength getFrameLength();

    /**
     * Load the Users.
     */
    void refreshUsers();
}
