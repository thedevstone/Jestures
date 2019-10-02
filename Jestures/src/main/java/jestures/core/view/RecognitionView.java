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

import jestures.core.recognition.UpdateRate;
import smile.math.distance.DynamicTimeWarping;

/**
 * A simple view with more methods for recognition.
 */
public interface RecognitionView extends View, RecognitionViewObserver {

    /**
     * Set the {@link DynamicTimeWarping} radius.
     *
     * @param radius the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    void setDtwRadius(double radius);

    /**
     * Set the threshold for gesture maximum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) greater than minThreashold, are accepted.
     *
     * @param confidenceThreshold represents the maximum distance above which a feature vector is accepted
     */
    void setConfidenceThreshold(double confidenceThreshold);

    /**
     * Set the update rate of the recognizer. The rate must be a value that can be devided by the frame length.
     *
     * @param samplingRate the update rate
     */
    void setSamplingRate(UpdateRate samplingRate);

    /**
     * Set the minimum time separation between two gestures.
     * <p>
     * If the time is too short a long gesture can be recognized multiple time according to update rate value
     *
     * @param minTimeSeparation the time separation in milliseconds, a value usually between 0 and 1000.
     */
    void setMinTimeSeparation(int minTimeSeparation);

    /**
     * Set the minimum number of gesture that have to match the template to get a gesture recognized.
     *
     * @param k the number of templates.
     */
    void setK(int k);

    /**
     * Save the settings.
     */
    void saveSettings();

    /**
     * Learn Classifier.
     */
    void learnClassifier();
}
