/**
 *
 */
package jestures.core.view;

import smile.math.distance.DynamicTimeWarping;

/**
 * The {@link RecognitionViewObserver} class.
 *
 */
public interface RecognitionViewObserver extends ViewObserver {

    /**
     * Set the {@link DynamicTimeWarping} radius.
     *
     * @param radius
     *            the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    void updateDtwRadius(double radius);

    /**
     * Set the threshold for gesture minimum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) lower than minThreashold, are accepted.
     *
     *
     * @param minDtwThreashold
     *            represents the minimum distance above which a feature vector is accepted
     */
    void updateMinDtwThreashold(double minDtwThreashold);

    /**
     * Set the threshold for gesture maximum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) greater than minThreashold, are accepted.
     *
     *
     * @param maxDtwThreashold
     *            represents the maximum distance above which a feature vector is accepted
     */
    void updateMaxDtwThreashold(double maxDtwThreashold);

    /**
     * Set the update rate of the recognizer. The rate must be a value that can be devided by the frame length.
     *
     * @param updateRate
     *            the update rate
     */
    void updateUpdateRate(int updateRate);

    /**
     * Set the minimum time separation between two gestures.
     * <p>
     * If the time is too short a long gesture can be recognized multiple time according to update rate value
     *
     * @param minTimeSeparation
     *            the time separation in milliseconds, a value usually between 0 and 1000.
     */
    void updateMinTimeSeparation(int minTimeSeparation);

    /**
     * Set the minimum number of gesture that have to match the template to get a gesture recognized.
     *
     * @param matchNumber
     *            the number of templates.
     */
    void updateMatchNumber(int matchNumber);
}
