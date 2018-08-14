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

/**
 * It represents a general sensor that can be attached to the framework.
 * <p>
 * Sensors may be Kinect, LipMotion ecc. If your sensor is not present please contact me.
 */
public interface Sensor {
    /**
     * Start the sensor with the default configuration setted in the constructor specific sensor.
     *
     * @throws SensorException
     *             if the sensor encurred in problems during starting phase.
     *
     * @throws IllegalSensorStateException
     *             if sensor is started outside recognition
     */
    void startSensor() throws SensorException, IllegalSensorStateException;

    /**
     * Stop the sensor with the default configuration setted in the constructor specific sensor.
     *
     * @throws SensorException
     *             if the sensor encurres in problems during stopping phase.
     *
     * @throws IllegalSensorStateException
     *             if sensor is started outside recognition
     */
    void stopSensor() throws SensorException, IllegalSensorStateException;

    /**
     * Attache the tracker.
     *
     * @param recognizer
     *            the {@link SensorObserver}
     */
    void attacheTracker(SensorObserver recognizer);

    /**
     * Sensor state.
     *
     * @return <code>true</code> if it's on
     */
    boolean state();
}
