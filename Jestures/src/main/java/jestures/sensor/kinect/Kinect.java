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

import jestures.sensor.IllegalSensorStateException;
import jestures.sensor.Joint;
import jestures.sensor.Sensor;
import jestures.sensor.SensorObserver;

/**
 * Kinect sensor from Microsoft. A very basic configuration for the sensor.
 */
public final class Kinect implements KinectAdapterObserver, Sensor {
    private static Kinect instance = null;
    private final KinectAdapterImpl kinectAdapter;
    private SensorObserver tracker;
    private boolean state; // NOPMD

    /**
     * The @link{Kinect.java} constructor.
     *
     * @param kinectVersion
     *            the {@link Kinect} version
     */
    private Kinect(final KinectVersion kinectVersion) {
        this.kinectAdapter = new KinectAdapterImpl(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, kinectVersion,
                KinectSensibility.MID);
        this.kinectAdapter.attacheKinectAdapterObserver(this);
    }

    private Kinect(final Joint primaryJoint, final KinectVersion kinectVersion, final KinectSensibility sensibility) {
        this.kinectAdapter = new KinectAdapterImpl(primaryJoint, KinectSensors.SKELETON_ONLY, kinectVersion,
                sensibility);
        this.kinectAdapter.attacheKinectAdapterObserver(this);
    }

    /**
     * Initialize a Kinect instance.
     *
     * @param primaryJoint
     *            the primary {@link Joint}
     * @param kinectVersion
     *            the {@link KinectVersion}
     * @param sensibility
     *            KinectSensibility
     *            <p>
     *            Higher values sensibility produce higher value vectors along with minimum hand movement
     * @return the Kinect instance
     * @throws IllegalSensorStateException
     *             the {@link IllegalSensorStateException} if Sensor is initialized multiple times
     */
    public static Kinect initialize(final Joint primaryJoint, final KinectVersion kinectVersion,
            final KinectSensibility sensibility) {
        if (Kinect.instance != null) {
            return Kinect.instance;
        }
        Kinect.instance = new Kinect(primaryJoint, kinectVersion, sensibility);
        return Kinect.instance;
    }

    /**
     * Initialize the Kinect default instance.
     * <p>
     * Hand -> right hand <br>
     * Sensibility -> Mid sensibility (1000)
     *
     * @param kinectVersion
     *            the {@link Kinect} version
     * @return a default Kinect instance
     */
    public static Kinect initialize(final KinectVersion kinectVersion) {
        if (Kinect.instance != null) {
            return Kinect.instance;
        }
        Kinect.instance = new Kinect(kinectVersion);
        return Kinect.instance;
    }

    /**
     * Return the Kinect instance.
     *
     * @return the Kinect instance
     * @throws IllegalSensorStateException
     *             thown if the sensor is not initialized
     */
    public static Kinect getInstance() throws IllegalSensorStateException {
        if (Kinect.instance == null) {
            throw new IllegalStateException("Initialize the sensor first");
        }
        return Kinect.instance;
    }

    @Override
    public void notifyOnSkeletonChange(final Vector3D primaryJoint, final Vector3D secondaryJoint) {
        this.tracker.notifyOnSkeletonChange(primaryJoint, secondaryJoint);
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) {
        this.tracker.notifyOnAccelerometerChange(acceleration);
    }

    @Override
    public void startSensor() throws IllegalSensorStateException {
        if (this.tracker == null) {
            throw new IllegalSensorStateException(
                    "Cannot start the sensor outside tracker/recognizer. Please attache it to recognizer and start the recognition");
        } else {
            this.kinectAdapter.start();
            this.state = true;
        }
    }

    @Override
    public void stopSensor() throws IllegalSensorStateException {
        if (this.tracker == null) {
            throw new IllegalSensorStateException(
                    "Cannot start the sensor outside tracker/recognizer. Please attache it to recognizer and start the recognition");
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

    @Override
    public void setElevationAngle(final int angle) {
        this.kinectAdapter.setElevationAngle(angle);
    }

    @Override
    public int getElevationAngle() {
        return this.kinectAdapter.getElevationAngle();
    }

}
