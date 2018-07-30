package jestures.core.view;

import jestures.core.recognition.UpdateRate;
import smile.math.distance.DynamicTimeWarping;

/**
 *
 * The {@link RecognitionView} class.
 *
 */
public interface RecognitionView extends View, RecognitionViewObserver {

    /**
     * Set the {@link DynamicTimeWarping} radius.
     *
     * @param radius
     *            the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    void setDtwRadius(double radius);

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
     * Set the update rate of the recognizer. The rate must be a value that can be devided by the frame length.
     *
     * @param updateRate
     *            the update rate
     */
    void setUpdateRate(UpdateRate updateRate);

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
     * Set the minimum number of gesture that have to match the template to get a gesture recognized.
     *
     * @param matchNumber
     *            the number of templates.
     */
    void setMatchNumber(int matchNumber);
}
