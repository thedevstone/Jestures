package jestures.core.view;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.FrameLenght;
import jestures.core.tracking.Tracking;

/**
 *
 *
 */
public interface View {

    /**
     * Update view on frame event.
     *
     * @param frame
     *            the frame
     * @param derivative
     *            the {@link Vector2D} derivative
     * @param path
     *            the {@link Vector2D} gesture path
     */
    void notifyOnFrameChange(int frame, Vector2D derivative, Vector2D path);

    /**
     * Update view on feature vector event.
     */
    void notifyOnFeatureVectorEvent();

    /**
     * Set the frame Length.
     *
     * @param length
     *            the {@link FrameLenght}
     */
    void setFrameLength(FrameLenght length);

    /**
     * Start the {@link Tracking}.
     */
    void startSensor();

    /**
     * Stop the {@link Tracking}.
     */
    void stopSensor();

    /**
     * Get the tracker.
     *
     * @return the {@link Tracking} tracker
     */
    Tracking getTracker();

    /**
     * Get the {@link FrameLenght} for tracking.
     *
     * @return the {@link FrameLenght}
     */
    FrameLenght getFrameLength();

}