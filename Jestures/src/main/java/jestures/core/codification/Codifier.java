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

import jestures.core.tracking.TrackerImpl;
import jestures.core.tracking.TrackingObserver;

/**
 * A Codifier take absolute vector in space (vector2D or vector3D) and transforms them in a relative way.
 * <p>
 * This can be done in a derivative way or using other type of codification, for example a starting point codification
 * where every vector is subtracted by the first vector of the list.
 */
public interface Codifier {

    /**
     * Get the {@link Codification}.
     *
     * @return the {@link Codification} type
     */
    Codification getCodificationType();

    /**
     * Codify the body joint according to class specification. This performs discretization on skeleton stream.
     * <p>
     * Codification can be derivative or on starting point.
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
     * Attache the {@link TrackerImpl} for feedback notification.
     *
     * @param recognizer
     *            the {@link TrackerImpl}
     */
    void attacheTracker(TrackingObserver recognizer);

    /**
     * Reset the frame for starting a new gesture.
     */
    void resetFrame();

    /**
     * Set the frame length or FPS.
     *
     * @param length
     *            the {@link FrameLength}.
     */
    void setFrameLength(FrameLength length);
}
