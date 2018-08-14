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
 * This exception is thrown when a problem occour during sensor start and stop.
 */
public class SensorException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * The @link{SensorException.java} constructor.
     */
    SensorException() {
        super("A sensor has encountered an error. Please check the configuration or the sensor");
    }

    /**
     * The @link{SensorException.java} constructor.
     *
     * @param message
     *            the exception message.
     */
    SensorException(final String message) {
        super(message);

    }

}
