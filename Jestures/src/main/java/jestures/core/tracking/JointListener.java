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

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.FrameLength;

/**
 * Interface that generate {@link Vector2D} events.
 *
 */
public interface JointListener {
    /**
     * Simple absolute tracking.
     * <p>
     * Vector values [-1000, 1000]
     *
     * @param primaryJoint
     *            the {@link Vector2D} primary joint
     * @param secondaryJoint
     *            the {@link Vector2D} secondary joint
     */
    void onJointTracked(Vector2D primaryJoint, Vector2D secondaryJoint);

    /**
     * Derivative tracking.
     * <p>
     * Vector values [-100, 100]
     *
     * @param derivativeJoint
     *            {@link Vector2D} derivative
     */
    void onDerivativeJointTracked(Vector2D derivativeJoint);

    /**
     * Distance between starting joint position and actual joint position. Starting position depends on
     * {@link FrameLength}
     * <p>
     * Vector values [-1000, 1000]
     *
     * @param distance
     *            the {@link Vector2D} distance
     */
    void onDistanceFromStartingJoint(Vector2D distance);

    /**
     * Accelerometer tracking.
     * <p>
     *
     * Vector valuse [-1, 1]
     *
     * @param accelerometer
     *            the {@link Vector3D} acceleromter
     */
    void onAccelerometerTracked(Vector3D accelerometer);

}
