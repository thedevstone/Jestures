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
package jestures.core.recognition;

import jestures.core.codification.GestureLength;
import jestures.core.tracking.Tracker;
import jestures.core.view.RecognitionViewObserver;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import smile.math.distance.DynamicTimeWarping;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Interface for recognition. Recognition is based on Dynamic Time Warping algorithm and Knn.
 */
public interface Recognition extends Tracker {

    /**
     * Attache the view.
     *
     * @param view the {@link RecognitionViewObserver}
     */
    void attacheUI(RecognitionViewObserver view);

    /**
     * Load the user.
     *
     * @param name the {@link String} username
     * @return <code>true</code> if is loaded
     * @throws FileNotFoundException if file not found
     * @throws IOException           the {@link IOException} if can't create user folder
     */
    boolean loadUserProfile(String name) throws FileNotFoundException, IOException;

    /**
     * Get the user gesture length.
     *
     * @return the gesture length
     */
    GestureLength getUserGestureLength();

    /**
     * Get all template (feature vectors) for the selected gesture.
     *
     * @param gestureName the {@link String} gesture name
     * @return the {@link List} of feature vectors
     */
    List<List<Vector2D>> getGestureDataset(String gestureName);

    /**
     * Attache the listener.
     *
     * @param listener the listener
     */
    void setOnGestureRecognized(GestureListener listener);

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
    void setConfidenceThreshold(int confidenceThreshold);

    /**
     * Set the update rate of the recognizer. The rate must be a value that can be divided by the frame length.
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
     * <p>
     * Set the k of the Knn algorithm. K represents the nearest neighbor, in other way the nearest feature vector
     *
     * @param k the number of templates.
     */
    void setK(int k);

    /**
     * Save the recognition settings.
     *
     * @throws IOException the {@link IOException}
     */
    void saveSettings() throws IOException;

}
