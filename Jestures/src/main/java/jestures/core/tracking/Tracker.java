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
import java.util.List;

import jestures.core.codification.Codification;
import jestures.core.codification.GestureLength;
import jestures.sensor.Sensor;

/**
 * A Tracker get all vector positions, codifies them in a new relative form that can be used for more complex tasks
 * (recognition).
 *
 */
public interface Tracker {

    /**
     * Get the {@link Codification} type.
     *
     * @return the {@link Codification} type.
     */
    Codification getCodificationType();

    /**
     * Attache the listener.
     *
     * @param listener
     *            the listener
     */
    void setOnJointTracked(JointListener listener);

    /**
     * Attache the {@link Sensor}.
     *
     * @param sensor
     *            the {@link Sensor}
     */
    void attacheSensor(Sensor sensor);

    /**
     * The sensor is started.
     *
     * @return <code>true</code> if the sensor is started.
     */
    boolean isStarted();

    /**
     * Reset the gesture frame.
     */
    void resetCodificationFrame();

    /**
     * Set the frame length.
     *
     * @param length
     *            the length
     * @throws IOException
     *             IOexception
     */
    void setFrameLength(GestureLength length) throws IOException;

    /**
     * Get all user gestures.
     *
     * @return the {@link List} of gestures
     */
    List<String> getAllUserGesture();

    /**
     * Get the actual user.
     *
     * @return the String username
     */
    String getUserName();

    /**
     * Get the frame length.
     *
     * @return the frame length in frame
     */
    GestureLength getFrameLength();

    /**
     * Start the sensor.
     */
    void startSensor();

    /**
     * Stop the sensor.
     */
    void stopSensor();

    /**
     * Sensor state.
     *
     * @return <code>true</code> if it's on
     */
    boolean state();

    /**
     * Set the elevation angle of the sensor.
     *
     * @param angle
     *            the angle
     */
    void setElevationAngle(int angle);

    /**
     * Get the elevation angle of the sensor.
     *
     * @return the elevation angle
     */
    int getElevationAngle();

}