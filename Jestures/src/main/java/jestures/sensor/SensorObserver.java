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

package jestures.sensor;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The @link{ObservableRecognizer} class.
 */
public interface SensorObserver {
    /**
     * Notify recognizer when a Joint {@link Vector2D} is available.
     *
     * @param primaryJoint
     *            the primary Joint {@link Vector2D} that generates gestures.
     * @param secondaryJoint
     *            the secondary Joint {@link Vector2D}
     */
    void notifyOnSkeletonChange(Vector2D primaryJoint, Vector2D secondaryJoint);

    /**
     * Notify recognizer when a acceleration {@link Vector3D} is available.
     *
     * @param acceleration
     *            the {@link Vector3D} acceleration
     */
    void notifyOnAccelerometerChange(Vector3D acceleration);
}
