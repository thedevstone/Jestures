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
package jestures.core.recognition.gesturedata;

import jestures.core.recognition.UpdateRate;
import smile.math.distance.DynamicTimeWarping;

import java.io.Serializable;

/**
 * The {@link RecognitionSettingsImpl} class.
 */
public class RecognitionSettingsImpl implements Serializable, RecognitionSettings {

    /**
     * Recognition settings that can be serialized.
     */
    private static final long serialVersionUID = -698249799317303588L;

    // private static final Logger LOG = Logger.getLogger(RecognitionSettingsImpl.class);
    private UpdateRate samplingRate;
    private double dtwRadius;
    private double confidenceThresholdDTW;
    private int minTimeSeparation;
    private int kNearestNeighbors;

    /**
     * The constructor for the class.
     */
    public RecognitionSettingsImpl() { // NOPMD
    }

    /**
     * The constructor for the class.
     *
     * @param updateRate             the update rate
     * @param dtwRadius              the radius of the Sakoe and Chiba band
     * @param confidenceThresholdDTW the Threshold of confidence
     * @param minTimeSeparation      the min time separation
     * @param matchNumber            the match number
     */
    public RecognitionSettingsImpl(final UpdateRate updateRate, final double dtwRadius,
                                   final double confidenceThresholdDTW, final int minTimeSeparation, final int matchNumber) {
        this.samplingRate = updateRate;
        this.dtwRadius = dtwRadius;
        this.confidenceThresholdDTW = confidenceThresholdDTW;
        this.minTimeSeparation = minTimeSeparation;
        this.kNearestNeighbors = matchNumber;
    }

    /**
     * Get the {@link DynamicTimeWarping} radius.
     *
     * @return the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    @Override
    public double getDtwRadius() {
        return this.dtwRadius;
    }

    /**
     * Set the {@link DynamicTimeWarping} radius.
     *
     * @param dtwRadius the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    @Override
    public void setDtwRadius(final double dtwRadius) {
        if (dtwRadius < 1 && dtwRadius >= 0) {
            this.dtwRadius = dtwRadius;
        } else {
            throw new IllegalStateException("Radius must be between 0 and 1");
        }
    }

    /**
     * Get the threshold for gesture maximum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) greater than minThreashold, are accepted.
     *
     * @return represents the maximum distance above which a feature vector is accepted
     */
    @Override
    public double getDTWConfidenceThreshold() {
        return this.confidenceThresholdDTW;
    }

    /**
     * Set the threshold for gesture minimum confidence.
     * <p>
     * Only gestures, that have a feature vector confidence (by KNN) greater than Threashold, are accepted.
     *
     * @param confidenceThreshold represents the minimum confidence above which a feature vector is accepted
     */
    @Override
    public void setDTWConfidenceThreshold(final double confidenceThreshold) {
        if (confidenceThreshold >= 0 && confidenceThreshold <= 1) {
            this.confidenceThresholdDTW = confidenceThreshold;
        } else {
            throw new IllegalStateException("Threshold must be greater than 0 and less than 1");
        }
    }

    /**
     * Get the sampling rate of the recognizer.
     *
     * @return the frame value
     */
    @Override
    public UpdateRate getSamplingRate() {
        return this.samplingRate;
    }

    /**
     * Set the sampling rate of the recognizer. The rate must be a value that can be devided by the frame length.
     *
     * @param updateRate the update rate
     */
    @Override
    public void setSamplingRate(final UpdateRate updateRate) {
        this.samplingRate = updateRate;
    }

    /**
     * Get the minimum time separation between two gestures.
     * <p>
     * If the time is too short a long gesture can be recognized multiple time according to update rate value
     *
     * @return the time separation in milliseconds, a value usually between 0 and 1000.
     */
    @Override
    public int getMinTimeSeparation() {
        return this.minTimeSeparation;
    }

    /**
     * Set the minimum time separation between two gestures.
     * <p>
     * If the time is too short a long gesture can be recognized multiple time according to update rate value
     *
     * @param minTimeSeparation the time separation in milliseconds, a value usually between 0 and 1000.
     */
    @Override
    public void setMinTimeSeparation(final int minTimeSeparation) {
        if (minTimeSeparation >= 0 && minTimeSeparation < 10000) {
            this.minTimeSeparation = minTimeSeparation;
        } else {
            throw new IllegalStateException("Time must be greater than 0 and less than 10000");
        }
    }

    /**
     * Get the minimum number of gesture that have to match the template to get a gesture recognized.
     *
     * @return the number of templates.
     */
    @Override
    public int getK() {
        return this.kNearestNeighbors;
    }

    /**
     * Set the minimum number of gesture that have to match the template to get a gesture recognized.
     *
     * @param k the number of templates.
     */
    @Override
    public void setK(final int k) {
        if (k > 0) {
            this.kNearestNeighbors = k;
        } else {
            throw new IllegalStateException("Match number must be greater than 0");
        }
    }

}
