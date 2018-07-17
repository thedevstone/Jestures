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

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.Codification;
import jestures.core.codification.Codifier;
import jestures.core.codification.DerivativeCodifier;
import jestures.core.codification.FrameLenght;
import jestures.core.view.TrackerView;
import jestures.core.view.View;
import jestures.sensor.IllegalSensorStateException;
import jestures.sensor.Sensor;
import jestures.sensor.SensorException;
import jestures.sensor.SensorObserver;

/**
 * The {@link Tracker} class.
 */

public final class Tracker implements TrackingObserver, SensorObserver, Tracking {
    private static Tracking instance;
    private Codifier codifier;
    private Sensor sensor;
    private final Set<View> view;
    private final Set<JointListener> jointListener;
    private FrameLenght frameLength;
    private boolean started;

    private Tracker() {
        this(Codification.DERIVATIVE, FrameLenght.TWO_SECONDS);
    }

    private Tracker(final Codification codificationType, final FrameLenght gestureLenght) {

        if (codificationType.equals(Codification.DERIVATIVE)) {
            this.codifier = new DerivativeCodifier(gestureLenght);
        }

        this.frameLength = gestureLenght;
        this.started = false;
        this.codifier.attacheCoreRecognizer(this);

        this.view = new HashSet<>();
        this.jointListener = new HashSet<>();
        TrackerView.startFxThread();

    }

    /**
     * Get the instance.
     *
     * @return the {@link Tracking} instance.
     */
    public static Tracking getInstance() {
        synchronized (Tracking.class) {
            if (Tracker.instance == null) {
                Tracker.instance = new Tracker();
            }
        }
        return Tracker.instance;
    }

    @Override
    public void attacheSensor(final Sensor sensor) {
        this.sensor = sensor;
        this.sensor.attacheTracker(this);
    }

    @Override
    public void attacheUI(final View view) {
        this.view.add(view);
        this.view.forEach(t -> t.setFrameLength(this.frameLength));
    }

    @Override
    public void notifyOnSkeletonChange(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
        this.codifier.codifyOnSkeletonChange(primaryJoint);
        this.jointListener.forEach(t -> t.onJointTracked(primaryJoint, secondaryJoint));
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) {
        this.jointListener.forEach(t -> t.onAccelerometerTracked(acceleration));
    }

    @Override
    public void notifyOnFrameChange(final int frame, final Vector2D derivative, final Vector2D distanceVector) {
        this.view.forEach(t -> t.notifyOnFrameChange(frame, derivative, distanceVector));
        this.jointListener.forEach(t -> t.onDerivativeJointTracked(derivative));
        this.jointListener.forEach(t -> t.onDistanceFromStartingJoint(distanceVector));

    }

    @Override
    public void notifyOnFeatureVectorEvent(final Queue<Vector2D> featureVector) {
        this.view.forEach(t -> t.notifyOnFeatureVectorEvent());
    }

    @Override
    public void startSensor() {
        try {
            this.started = true;
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
            this.started = false;
            this.sensor.stopSensor();
        } catch (final SensorException e) {
            e.printStackTrace();
        } catch (final IllegalSensorStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getFrameLength() {
        return this.frameLength.getFrameNumber();
    }

    @Override
    public boolean isStarted() {
        return this.started;
    }

    @Override
    public void setFrameLength(final FrameLenght length) {
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

}
