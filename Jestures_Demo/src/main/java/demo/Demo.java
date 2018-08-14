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
package demo;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.recognition.Recognition;
import jestures.core.recognition.Recognizer;
import jestures.core.tracking.JointListener;
import jestures.core.view.RecognitionView;
import jestures.core.view.screens.RecognitionScreenView;
import jestures.sensor.IllegalSensorStateException;
import jestures.sensor.Joint;
import jestures.sensor.Sensor;
import jestures.sensor.SensorException;
import jestures.sensor.kinect.Kinect;
import jestures.sensor.kinect.KinectSensors;
import jestures.sensor.kinect.KinectVersion;

/**
 * The @link{Demo} class.
 */
public class Demo {

    /**
     * The @link{Demo.java} constructor.
     */
    public Demo() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Main.
     *
     * @param args
     *            args
     * @throws SensorException
     *             the sensor exception
     * @throws IllegalSensorStateException
     */
    public static void main(final String[] args) throws SensorException, IllegalSensorStateException {
        final Sensor sensor = new Kinect(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, KinectVersion.KINECT1);
        final Recognition recognizer = Recognizer.getInstance();
        recognizer.attacheSensor(sensor);
        final RecognitionView view = new RecognitionScreenView(recognizer);
        recognizer.attacheUI(view);
        recognizer.setOnJointTracked(new JointListener() {

            @Override
            public void onJointTracked(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
                // System.out.println(primaryJoint);

            }

            @Override
            public void onDistanceFromStartingJoint(final Vector2D distance) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDerivativeJointTracked(final Vector2D derivativeJoint) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAccelerometerTracked(final Vector3D accelerometer) {
                // TODO Auto-generated method stub

            }
        });
        recognizer.setOnGestureRecognized(t -> {
            System.out.println(t);
        });

    }

}
