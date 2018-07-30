/**
 *
 */
package jestures.core.recognition.gesturedata;

import java.io.Serializable;

import org.apache.log4j.Logger;

import jestures.core.recognition.UpdateRate;
import smile.math.distance.DynamicTimeWarping;

/**
 * The {@link RecognitionSettingsImpl} class.
 *
 */
public class RecognitionSettingsImpl implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -698249799317303588L;

    private static final Logger LOG = Logger.getLogger(RecognitionSettingsImpl.class);
    private UpdateRate updateRate;
    private double dtwRadius;
    private int minDTWThreashold;
    private int maxDTWThreashold;
    private int minTimeSeparation;
    private int matchNumber;

    /**
     *
     * The constructor for the class.
     */
    public RecognitionSettingsImpl() { // NOPMD
    }

    /**
     *
     * The constructor for the class.
     *
     * @param updateRate
     *            the update rate
     * @param dtwRadius
     *            the radius
     * @param minDTWThreshold
     *            the min threshold
     * @param maxDTWTreshold
     *            the max threshold
     * @param minTimeSeparation
     *            the min time separation
     * @param matchNumber
     *            the match number
     */
    public RecognitionSettingsImpl(final UpdateRate updateRate, final double dtwRadius, final int minDTWThreshold,
            final int maxDTWTreshold, final int minTimeSeparation, final int matchNumber) {
        this.updateRate = updateRate;
        this.dtwRadius = dtwRadius;
        this.minDTWThreashold = minDTWThreshold;
        this.maxDTWThreashold = maxDTWTreshold;
        this.minTimeSeparation = minTimeSeparation;
        this.matchNumber = matchNumber;
    }

    /**
     * Get the {@link DynamicTimeWarping} radius.
     *
     * @return the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    public double getDtwRadius() {
        return this.dtwRadius;
    }

    /**
     * Set the {@link DynamicTimeWarping} radius.
     *
     * @param dtwRadius
     *            the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    public void setDtwRadius(final double dtwRadius) {
        if (dtwRadius < 1 && dtwRadius >= 0) {
            this.dtwRadius = dtwRadius;
        } else {
            throw new IllegalStateException("Radius must be between 0 and 1");
        }
    }

    /**
     * Get the threshold for gesture minimum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) lower than minThreashold, are accepted.
     *
     *
     * @return represents the minimum distance above which a feature vector is accepted
     */
    public double getMinDtwThreashold() {
        return this.minDTWThreashold;
    }

    /**
     * Set the threshold for gesture minimum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) lower than minThreashold, are accepted.
     *
     *
     * @param minDTWThreashold
     *            represents the minimum distance above which a feature vector is accepted
     */
    public void setMinDtwThreashold(final int minDTWThreashold) {
        if (minDTWThreashold >= 0) {
            this.minDTWThreashold = minDTWThreashold;
            RecognitionSettingsImpl.LOG.debug(this.minDTWThreashold);
        } else {
            throw new IllegalStateException("Min threshold must be greater than 0");
        }
    }

    /**
     * Get the threshold for gesture maximum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) greater than minThreashold, are accepted.
     *
     *
     * @return represents the maximum distance above which a feature vector is accepted
     */
    public double getMaxDTWThreashold() {
        return this.maxDTWThreashold;
    }

    /**
     * Set the threshold for gesture maximum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) greater than minThreashold, are accepted.
     *
     *
     * @param maxDtwThreashold
     *            represents the maximum distance above which a feature vector is accepted
     */
    public void setMaxDtwThreashold(final int maxDtwThreashold) {
        if (maxDtwThreashold >= 0) {
            this.maxDTWThreashold = maxDtwThreashold;
            RecognitionSettingsImpl.LOG.debug(this.maxDTWThreashold);
        } else {
            throw new IllegalStateException("Min threshold must be greater than 0");
        }
    }

    /**
     * Get the update rate of the recognizer.
     *
     * @return the frame value
     */
    public UpdateRate getUpdateRate() {
        return this.updateRate;
    }

    /**
     * Set the update rate of the recognizer. The rate must be a value that can be devided by the frame length.
     *
     * @param updateRate
     *            the update rate
     */
    public void setUpdateRate(final UpdateRate updateRate) {
        this.updateRate = updateRate;
    }

    /**
     * Get the minimum time separation between two gestures.
     * <p>
     * If the time is too short a long gesture can be recognized multiple time according to update rate value
     *
     * @return the time separation in milliseconds, a value usually between 0 and 1000.
     */
    public int getMinTimeSeparation() {
        return this.minTimeSeparation;
    }

    /**
     * Set the minimum time separation between two gestures.
     * <p>
     * If the time is too short a long gesture can be recognized multiple time according to update rate value
     *
     * @param minTimeSeparation
     *            the time separation in milliseconds, a value usually between 0 and 1000.
     */
    public void setMinTimeSeparation(final int minTimeSeparation) {
        if (minTimeSeparation >= 0 && minTimeSeparation < 1000) {
            this.minTimeSeparation = minTimeSeparation;
            RecognitionSettingsImpl.LOG.debug(this.minTimeSeparation);
        } else {
            throw new IllegalStateException("Time must be greater than 0 and less than 1000");
        }
    }

    /**
     * Get the minimum number of gesture that have to match the template to get a gesture recognized.
     *
     * @return the number of templates.
     */
    public int getMatchNumber() {
        return this.matchNumber;
    }

    /**
     * Set the minimum number of gesture that have to match the template to get a gesture recognized.
     *
     * @param matchNumber
     *            the number of templates.
     */
    public void setMatchNumber(final int matchNumber) {
        if (matchNumber >= 0) {
            this.matchNumber = matchNumber;
            RecognitionSettingsImpl.LOG.debug(this.matchNumber);
        } else {
            throw new IllegalStateException("Match number must be greater than 0");
        }
    }

}
