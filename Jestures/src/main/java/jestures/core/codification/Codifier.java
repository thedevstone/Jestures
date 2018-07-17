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
package jestures.core.codification;

import java.util.Queue;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.tracking.Tracker;
import jestures.core.tracking.TrackingObserver;

/**
 * The @link{Codifier} class.
 */
public interface Codifier {
    /**
     * Codify the body joint according to class specification. This performs discretization on skeleton stream.
     * <p>
     * Codification can be derivative or on vectorial space.
     *
     * @param newVector
     *            the primary {@link Vector2D} joint according to sensor settings
     *
     */
    void codifyOnSkeletonChange(Vector2D newVector);

    /**
     * Get the feature vector.
     *
     * @return the {@link Queue} feature vector
     */
    Queue<Vector2D> extractFeatureVector();

    /**
     * Attache the {@link Tracker} for feeback notification.
     *
     * @param recognizer
     *            the {@link Tracker}
     */
    void attacheCoreRecognizer(TrackingObserver recognizer);

    /**
     * Reset the frame for starting a new gesture.
     */
    void resetFrame();

    /**
     * Set the frame length.
     * 
     * @param length
     *            the {@link FrameLenght}.
     */
    void setFrameLength(FrameLenght length);
}
