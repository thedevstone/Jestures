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

import com.google.common.collect.EvictingQueue;

import jestures.core.tracking.TrackingObserver;

/**
 * The @link{DerivativeCodifier} class.
 */
public class DerivativeCodifier implements Codifier {
    private final Queue<Vector2D> featureVector;
    private Vector2D oldVector;
    private Vector2D derivative; // NOPMD
    private Vector2D startingVector;
    private int frame;
    private FrameLenght frameLength;
    private TrackingObserver recognizer;

    /**
     * The @link{DerivativeCodifier.java} constructor.
     *
     * @param frames
     *            the gesture's duration in frame
     */
    public DerivativeCodifier(final FrameLenght frames) {
        this.featureVector = EvictingQueue.create(FrameLenght.THREE_SECONDS.getFrameNumber());
        this.oldVector = new Vector2D(0, 0);
        this.frame = 0;
        this.frameLength = frames;
    }

    @Override
    public void codifyOnSkeletonChange(final Vector2D newVector) {
        // CALCOLO DERIVATA
        this.derivative = newVector.subtract(this.oldVector);
        this.featureVector.offer(this.derivative);
        this.oldVector = newVector;
        // SE SONO ALLA FINE RESETTO
        if (this.frame == 0) {
            this.startingVector = newVector;
        }
        if (this.frame > this.frameLength.getFrameNumber() - 1) {
            this.resetFrame();
            this.recognizer.notifyOnFeatureVectorEvent(this.featureVector);
        } else {
            this.recognizer.notifyOnFrameChange(this.frame, this.derivative, this.startingVector.subtract(newVector));
            this.frame++;
        }
    }

    @Override
    public Queue<Vector2D> extractFeatureVector() {
        return this.featureVector;
    }

    @Override
    public void attacheCoreRecognizer(final TrackingObserver recognizer) {
        this.recognizer = recognizer;
    }

    @Override
    public synchronized void resetFrame() {
        this.frame = 0;
        this.featureVector.clear();
    }

    @Override
    public synchronized void setFrameLength(final FrameLenght length) {
        this.frameLength = length;
    }

}
