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

/**
 * The @link{KinectSensorType} class.
 */
public enum KinectSensors {
    /**
     * Kinect type of sensor.
     */
    // CHECKSTYLE:OFF
    SKELETON_ONLY(0x20), SKELETON_DEPTH(0x20 | 0x8), ALL_SENSORS(0x01 | 0x02 | 0x08 | 0x20 | 0x100 | 0x1000);

    private final int type;

    /**
     * The @link{KinectSensorType.java} constructor.
     *
     * @param kinectType
     *            the kinect type
     */
    KinectSensors(final int kinectType) {
        this.type = kinectType;
    }

    /**
     * Get the @link{type}.
     *
     * @return the @link{type}
     */
    public int getStartingSensors() {
        return this.type;
    }

}
