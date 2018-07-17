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

package jestures.core.codification;

/**
 * Frequency in extraction.
 *
 *
 */
public enum FeatureExtractionFrequency {
    /**
     * Gestures duration in frame. 30 FPS base
     */
    TEN_SECOND(10), TWENTY_SECONDS(20), THIRTY_SECONDS(30);

    private int frequencyNumber;

    /**
     * The @link{JestureFrameLenght.java} constructor.
     *
     * @param frequency
     */
    FeatureExtractionFrequency(final int frequency) {
        this.frequencyNumber = frequency;
    }

    /**
     * Get the @link{frameNumber}.
     *
     * @return the @link{frameNumber}
     */
    public int getExtractionFrequency() {
        return this.frequencyNumber;
    }
}
