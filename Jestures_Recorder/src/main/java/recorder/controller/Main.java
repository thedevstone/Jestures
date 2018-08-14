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

import jestures.sensor.Joint;
import jestures.sensor.Sensor;
import jestures.sensor.SensorException;
import jestures.sensor.kinect.Kinect;
import jestures.sensor.kinect.KinectSensors;
import jestures.sensor.kinect.KinectVersion;
import recorder.view.RecorderScreenView;
import recorder.view.RecordingView;

/**
 * Main class.
 *
 */
public final class Main {
    private Main() {

    }

    /**
     * Main.
     *
     * @param args
     *            args
     * @throws SensorException
     *             the sensor exception
     */
    public static void main(final String[] args) throws SensorException {
        final Sensor sensor = new Kinect(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, KinectVersion.KINECT1);
        final Recorder recorder = RecorderImpl.getInstance();
        recorder.attacheSensor(sensor);
        final RecordingView view = new RecorderScreenView(recorder);
        recorder.attacheUI(view);
    }

}
