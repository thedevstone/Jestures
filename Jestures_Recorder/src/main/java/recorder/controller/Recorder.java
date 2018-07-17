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
package recorder.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.Codification;
import jestures.core.codification.Codifier;
import jestures.core.codification.DerivativeCodifier;
import jestures.core.codification.FrameLenght;
import jestures.core.tracking.Tracking;
import jestures.core.tracking.TrackingObserver;
import jestures.core.view.TrackerView;
import jestures.sensor.IllegalSensorStateException;
import jestures.sensor.Sensor;
import jestures.sensor.SensorException;
import jestures.sensor.SensorObserver;
import recorder.view.RecView;
import recorder.view.Recording;

/**
 * The {@link Recorder} class.
 */

public final class Recorder implements TrackingObserver, SensorObserver, Recording {
    private static Recording instance;
    private Codifier codifier;
    private Sensor sensor;
    private final Set<RecView> view;
    private FrameLenght frameLength;
    private boolean started;
    private boolean isRecording;

    private final List<Queue<Vector2D>> listOfFeatureVector;

    private Recorder() {
        this(Codification.DERIVATIVE, FrameLenght.TWO_SECONDS);
    }

    private Recorder(final Codification codificationType, final FrameLenght gestureLenght) {

        if (codificationType.equals(Codification.DERIVATIVE)) {
            this.codifier = new DerivativeCodifier(gestureLenght);
        }

        this.frameLength = gestureLenght;
        this.started = false;
        this.codifier.attacheCoreRecognizer(this);
        this.listOfFeatureVector = new ArrayList<>();
        this.view = new HashSet<>();

        TrackerView.startFxThread();

    }

    /**
     * Get the instance.
     *
     * @return the {@link Tracking} instance.
     */
    public static Recording getInstance() {
        synchronized (Tracking.class) {
            if (Recorder.instance == null) {
                Recorder.instance = new Recorder();
            }
        }
        return Recorder.instance;
    }

    // ############################################## OBSERVER ###################################
    @Override
    public void attacheSensor(final Sensor sensor) {
        this.sensor = sensor;
        this.sensor.attacheTracker(this);
    }

    @Override
    public void attacheUI(final RecView view) {
        this.view.add(view);
        this.view.forEach(t -> t.setFrameLength(this.frameLength));
    }

    // ############################################## FROM SENSOR ###################################
    @Override
    public void notifyOnSkeletonChange(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
        this.codifier.codifyOnSkeletonChange(primaryJoint);
        this.secJointTrigger(primaryJoint, secondaryJoint);
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) {
    }

    // ############################################## FROM CODIFIER ###################################
    @Override
    public void notifyOnFrameChange(final int frame, final Vector2D derivative, final Vector2D distanceVector) {
        this.view.forEach(t -> t.notifyOnFrameChange(frame, derivative, distanceVector));

    }

    @Override
    public void notifyOnFeatureVectorEvent(final Queue<Vector2D> featureVector) {
        if (this.isRecording) {
            this.view.forEach(t -> t.notifyOnFeatureVectorEvent(featureVector));
            this.listOfFeatureVector.add(featureVector);
            System.out.println(this.listOfFeatureVector.size());
        }
    }

    // ############################################## INSTANCE METHODS ###################################

    private void secJointTrigger(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
        if (secondaryJoint.getY() > primaryJoint.getY() + 50 && !this.isRecording) {
            this.isRecording = true;
            this.view.forEach(t -> t.setRecording(true));
            this.codifier.resetFrame();
        } else if (primaryJoint.getY() - 50 > secondaryJoint.getY() && this.isRecording) {
            this.isRecording = false;
            this.view.forEach(t -> t.setRecording(false));
        }
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
    public boolean state() {
        return this.sensor.state();
    }

    @Override
    public void deleteFeatureVector(final int index) {
        this.listOfFeatureVector.remove(index);
    }

}
