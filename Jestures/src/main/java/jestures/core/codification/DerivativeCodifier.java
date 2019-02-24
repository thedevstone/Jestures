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

import java.util.ArrayList;
import java.util.Queue;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.google.common.collect.EvictingQueue;

import jestures.core.tracking.TrackingObserver;

/**
 * A derivative codifier takes absolute vectors and create a list of theirs derivatives.
 * <p>
 * For example (2, 0) (3, 5) (3, 6) become (1, 5) (0, 1) and so on.
 */
public class DerivativeCodifier implements Codifier {
    /**
     * The queue that gather all the derivative vectors. This represents a gesture, so a sequence of positions in time.
     */
    private Queue<Vector3D> featureVector;
    /**
     * Old vector for derivative calculus.
     */
    private Vector3D oldVector;
    /**
     * Last derivative vector.
     */
    private Vector3D derivative; // NOPMD
    /**
     * Starting vector for other representations
     */
    private Vector3D startingVector;
    /**
     * Frame count.
     */
    private int frame;
    /**
     * The gesture length.
     */
    private GestureLength gestureLength;
    /**
     * Tracker that gets notified when a feature vector (gesture is ready)
     */
    private TrackingObserver recognizer;
    /**
     * Type of codification
     */
    private static final Codification CODIFICATION = Codification.DERIVATIVE;

    /**
     * The @link{DerivativeCodifier.java} constructor.
     * <p>
     * Create the evicting queue, the first vector to be differentiated and set the frame at zero.
     *
     * @param frames
     *            the gesture's duration in frame
     */
    public DerivativeCodifier(final GestureLength frames) {
        this.featureVector = EvictingQueue.create(frames.getFrameNumber());
        this.oldVector = new Vector3D(0, 0, 0);
        this.frame = 0;
        this.gestureLength = frames;
    }

    @Override
    public Codification getCodificationType() {
        return DerivativeCodifier.CODIFICATION;
    }

    @Override
    public void codifyOnSkeletonChange(final Vector3D newVector) {
        // derivative calculus
        this.derivative = newVector.subtract(this.oldVector);
        // vector is added to the queue
        this.featureVector.offer(this.derivative);
        // swap
        this.oldVector = newVector;
        // if it's the first frame reset the starting vector
        if (this.frame == 0) {
            this.startingVector = newVector;
        }
        // if reached the gesture length notify the recognizer with a new gesture.
        if (this.frame > this.gestureLength.getFrameNumber() - 1) {
            this.recognizer.notifyOnFeatureVectorEvent(new ArrayList<Vector3D>(this.featureVector));
            this.resetFrame();
        } else {
            // notify the recognizer so the view with a new frame
            this.recognizer.notifyOnFrameChange(this.frame, this.featureVector, this.derivative,
                    this.startingVector.subtract(newVector));
            this.frame++;
        }
    }

    @Override
    public Queue<Vector3D> extractFeatureVector() {
        return this.featureVector;
    }

    @Override
    public void attacheTracker(final TrackingObserver recognizer) {
        this.recognizer = recognizer;
    }

    @Override
    public synchronized void resetFrame() {
        this.frame = 0;
    }

    @Override
    public synchronized void setFrameLength(final GestureLength length) {
        this.gestureLength = length;
        this.featureVector = EvictingQueue.create(length.getFrameNumber());
    }

}
