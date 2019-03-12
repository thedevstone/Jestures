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

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import jestures.core.codification.Codification;
import jestures.core.codification.Codifier;
import jestures.core.codification.DerivativeCodifier;
import jestures.core.codification.GestureLength;
import jestures.core.view.screens.RecognitionScreenView;
import jestures.sensor.IllegalSensorStateException;
import jestures.sensor.Sensor;
import jestures.sensor.SensorException;
import jestures.sensor.SensorObserver;

/**
 * A general tracker that can be notified by the codifier and the sensor.
 */

public class TrackerImpl implements TrackingObserver, SensorObserver, Tracker {
    private Codifier codifier;
    private final Sensor sensor;
    private final Set<JointListener> jointListener;
    private GestureLength frameLength;
    private static boolean started;

    /**
     * The costructor for the tracker.
     *
     * @param sensor
     *            the sensor
     */
    public TrackerImpl(final Sensor sensor) {
        this(Codification.DERIVATIVE, GestureLength.FRAME_30, sensor);
    }

    /**
     *
     * Construct a sensor tracker.
     *
     * @param codificationType
     *            the {@link Codification} type
     * @param gestureLenght
     *            the {@link GestureLength} for the codification
     * @param sensor
     *            the {@link Sensor}
     */
    public TrackerImpl(final Codification codificationType, final GestureLength gestureLenght, final Sensor sensor) {

        if (codificationType.equals(Codification.DERIVATIVE)) {
            this.codifier = new DerivativeCodifier(gestureLenght);
        }
        this.frameLength = gestureLenght;
        TrackerImpl.started = false;
        this.codifier.attacheTracker(this);

        this.jointListener = new HashSet<>();
        RecognitionScreenView.startFxThread();

        this.sensor = sensor;
        this.sensor.attacheTracker(this);

    }

    // ############################################## FROM SENSOR ###################################
    @Override
    public void notifyOnSkeletonChange(final Vector3D primaryJoint, final Vector3D secondaryJoint) {
        this.codifier.codifyOnSkeletonChange(primaryJoint);
        this.jointListener.forEach(t -> t.onJointTracked(primaryJoint, secondaryJoint));
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) {
        this.jointListener.forEach(t -> t.onAccelerometerTracked(acceleration));
    }

    // ############################################## FROM CODIFIER ###################################
    @Override
    public void notifyOnFrameChange(final int frame, final Queue<Vector3D> featureVector, final Vector3D derivative,
            final Vector3D distanceVector) {
        this.jointListener.forEach(t -> t.onDerivativeJointTracked(derivative));
        this.jointListener.forEach(t -> t.onDistanceFromStartingJoint(distanceVector));

    }

    @Override
    public void notifyOnFeatureVectorEvent(final List<Vector3D> featureVector) {

    }

    // ############################################## INSTANCE METHODS ###################################
    @Override
    public void startSensor() {
        try {
            TrackerImpl.started = true;
            this.sensor.startSensor();
        } catch (final SensorException e) {
            e.printStackTrace();
        } catch (final IllegalSensorStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stopSensor() {
        try {
            TrackerImpl.started = false;
            this.sensor.stopSensor();
        } catch (final SensorException e) {
            e.printStackTrace();
        } catch (final IllegalSensorStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Codification getCodificationType() {
        return this.codifier.getCodificationType();
    }

    @Override
    public void resetCodificationFrame() {
        this.codifier.resetFrame();
    }

    @Override
    public GestureLength getFrameLength() {
        return this.frameLength;
    }

    @Override
    public boolean isStarted() {
        return TrackerImpl.started;
    }

    @Override
    public void setFrameLength(final GestureLength length) throws IOException {
        this.frameLength = length;
        this.codifier.setFrameLength(length);
    }

    @Override
    public void setOnJointTracked(final JointListener listener) {
        this.jointListener.add(listener);
    }

    @Override
    public boolean state() {
        return this.sensor.state();
    }

    @Override
    public void setElevationAngle(final int angle) {
        this.sensor.setElevationAngle(angle);
    }

    @Override
    public int getElevationAngle() {
        return this.sensor.getElevationAngle();
    }
}
