/**
 *
 */
package jestures.core.recognition.gesturedata;

import jestures.core.recognition.UpdateRate;
import smile.math.distance.DynamicTimeWarping;

/**
 * The {@link RecognitionSettings} class.
 *
 */
public interface RecognitionSettings {

    /**
     * Get the {@link DynamicTimeWarping} radius.
     *
     * @return the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    double getDtwRadius();

    /**
     * Set the {@link DynamicTimeWarping} radius.
     *
     * @param radius
     *            the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    void setDtwRadius(double radius);

    /**
     * Get the threshold for gesture minimum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) lower than minThreashold, are accepted.
     *
     *
     * @return represents the minimum distance above which a feature vector is accepted
     */
    double getMinDtwThreashold();

    /**
     * Set the threshold for gesture minimum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) lower than minThreashold, are accepted.
     *
     *
     * @param minDtwThreashold
     *            represents the minimum distance above which a feature vector is accepted
     */
    void setMinDtwThreashold(int minDtwThreashold);

    /**
     * Get the threshold for gesture maximum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) greater than minThreashold, are accepted.
     *
     *
     * @return represents the maximum distance above which a feature vector is accepted
     */
    double getMaxDTWThreashold();

    /**
     * Set the threshold for gesture maximum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) greater than minThreashold, are accepted.
     *
     *
     * @param maxDtwThreashold
     *            represents the maximum distance above which a feature vector is accepted
     */
    void setMaxDtwThreashold(int maxDtwThreashold);

    /**
     * Get the update rate of the recognizer.
     *
     * @return the frame value
     */
    UpdateRate getUpdateRate();

    /**
     * Set the update rate of the recognizer. The rate must be a value that can be devided by the frame length.
     *
     * @param updateRate
     *            the update rate
     */
    void setUpdateRate(UpdateRate updateRate);

    /**
     * Get the minimum time separation between two gestures.
     * <p>
     * If the time is too short a long gesture can be recognized multiple time according to update rate value
     *
     * @return the time separation in milliseconds, a value usually between 0 and 1000.
     */
    int getMinTimeSeparation();

    /**
     * Set the minimum time separation between two gestures.
     * <p>
     * If the time is too short a long gesture can be recognized multiple time according to update rate value
     *
     * @param minTimeSeparation
     *            the time separation in milliseconds, a value usually between 0 and 1000.
     */
    void setMinTimeSeparation(int minTimeSeparation);

    /**
     * Get the minimum number of gesture that have to match the template to get a gesture recognized.
     *
     * @return the number of templates.
     */
    int getMatchNumber();

    /**
     * Set the minimum number of gesture that have to match the template to get a gesture recognized.
     *
     * @param matchNumber
     *            the number of templates.
     */
    void setMatchNumber(int matchNumber);
}
