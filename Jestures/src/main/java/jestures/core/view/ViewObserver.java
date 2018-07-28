/**
 *
 */
package jestures.core.view;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.FrameLength;

/**
 * The {@link ViewObserver} class.
 *
 */
public interface ViewObserver {

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
     *
     *
     */
    void notifyOnFeatureVectorEvent();

    /**
     * Get the {@link FrameLength} for tracking.
     *
     * @return the {@link FrameLength}
     */
    FrameLength getFrameLength();

    /**
     * Load the Users.
     */
    void refreshUsers();
}
