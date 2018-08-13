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
package jestures.core.view;

import jestures.core.codification.FrameLength;
import jestures.core.tracking.Tracker;

/**
 * A view that performs all basic tasks.
 */
public interface View extends ViewObserver {

    /**
     * Start the {@link Tracker}.
     */
    void startSensor();

    /**
     * Stop the {@link Tracker}.
     */
    void stopSensor();

    /**
     * Load the userProfile.
     *
     * @param name
     *            the String name
     */
    void loadUserProfile(String name);

    /**
     * Set the frame Length.
     *
     * @param length
     *            the {@link FrameLength}
     */
    void setFrameLength(FrameLength length);

}