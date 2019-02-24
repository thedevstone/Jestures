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

package jestures.core.tracking;

import java.util.List;
import java.util.Queue;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 * A Tracking observer observes the codifier and get notified when a feature vector is ready.
 */
public interface TrackingObserver {
    /**
     * Notify the {@link TrackerImpl} when a feature vector {@link Queue} is avaiable.
     *
     * @param list
     *            the {@link List} feature vector.
     */
    void notifyOnFeatureVectorEvent(List<Vector3D> list);

    /**
     * Notify the {@link TrackerImpl} when a frame changes.
     *
     * @param frame
     *            the frame
     * @param featureVector
     *            the actual feature vector
     * @param derivative
     *            the derivative vector
     * @param distanceVector
     *            the distance vector from starting frame
     */
    void notifyOnFrameChange(int frame, Queue<Vector3D> featureVector, Vector3D derivative, Vector3D distanceVector);

}
