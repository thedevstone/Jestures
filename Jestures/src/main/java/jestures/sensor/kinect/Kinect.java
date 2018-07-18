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

package jestures.sensor.kinect;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.tracking.Tracker;
import jestures.sensor.IllegalSensorStateException;
import jestures.sensor.Joint;
import jestures.sensor.Sensor;
import jestures.sensor.SensorObserver;

/**
 * The @link{Kinect} class.
 */
public class Kinect implements KinectObserver, Sensor {
    private final KinectAdapter kinectAdapter;
    private SensorObserver tracker;
    private boolean state; // NOPMD

    /**
     * The @link{Kinect.java} constructor.
     *
     * @param kinectVersion
     *            the {@link Kinect} version
     */
    public Kinect(final KinectVersion kinectVersion) {
        this.kinectAdapter = new KinectAdapter(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, kinectVersion);
        this.kinectAdapter.attacheKinect(this);
    }

    /**
     * The @link{Kinect.java} constructor.
     *
     * @param primaryJoint
     *            the primary {@link Joint}
     * @param kinectStartingSensors
     *            {@link KinectSensors} the {@link KinectSensors} starting sensors
     * @param kinectVersion
     *            the {@link KinectVersion}
     *
     */
    public Kinect(final Joint primaryJoint, final KinectSensors kinectStartingSensors,
            final KinectVersion kinectVersion) {
        this.kinectAdapter = new KinectAdapter(primaryJoint, kinectStartingSensors, kinectVersion);
        this.kinectAdapter.attacheKinect(this);
    }

    @Override
    public void notifyOnSkeletonChange(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
        this.tracker.notifyOnSkeletonChange(primaryJoint, secondaryJoint);
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) {
        this.tracker.notifyOnAccelerometerChange(acceleration);
    }

    @Override
    public void startSensor() throws IllegalSensorStateException {
        if (!Tracker.checkStarted()) {
            throw new IllegalSensorStateException();
        } else {
            this.kinectAdapter.start();
            this.state = true;
        }
    }

    @Override
    public void stopSensor() throws IllegalSensorStateException {
        if (Tracker.checkStarted()) {
            throw new IllegalSensorStateException();
        } else {
            this.kinectAdapter.stop();
            this.state = false;
        }

    }

    @Override
    public void attacheTracker(final SensorObserver tracker) {
        this.tracker = tracker;
    }

    @Override
    public boolean state() {
        return this.state;
    }

}
