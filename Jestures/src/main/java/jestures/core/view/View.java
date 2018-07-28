package jestures.core.view;

import jestures.core.codification.FrameLength;
import jestures.core.tracking.Tracking;

/**
 *
 *
 */
public interface View extends ViewObserver {

    /**
     * Start the {@link Tracking}.
     */
    void startSensor();

    /**
     * Stop the {@link Tracking}.
     */
    void stopSensor();

    /**
     * Load the userProfile.
     *
     * @param name
     *            the String name
     */
    void loadUserProfile(String name);

    /**
     * Set the frame Length.
     *
     * @param length
     *            the {@link FrameLength}
     */
    void setFrameLength(FrameLength length);

}