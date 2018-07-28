package jestures.core.recognition;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.tracking.Tracking;
import jestures.core.view.View;
import jestures.core.view.ViewObserver;
import smile.math.distance.DynamicTimeWarping;

/**
 * Interface for recognition.
 *
 */
public interface Recognition extends Tracking {

    /**
     * Attache the view.
     *
     * @param view
     *            the {@link View}
     */
    void attacheUI(ViewObserver view);

    /**
     * Load the user.
     *
     * @param name
     *            the {@link String} username
     * @return <code>true</code> if is loaded
     * @throws FileNotFoundException
     *             if file not found
     * @throws IOException
     *             the {@link IOException} if can't create user folder
     */
    boolean loadUserProfile(String name) throws FileNotFoundException, IOException;

    /**
     * Get all template (feature vectors) for the selected gesture.
     *
     * @param gestureName
     *            the {@link String} gesture name
     * @return the {@link List} of feature vectors
     */
    List<List<Vector2D>> getGestureDataset(String gestureName);

    /**
     * Set the rate of recognition. After the frame number the recognizer is updated with a new feature vector.
     * <p>
     * Fast recognition rate can cause an high cpu load. Slow recognition rate can cause gesture degradation
     *
     * @param rate
     *            the update rate
     */
    void setUpdateRate(int rate);

    /**
     * Set the {@link DynamicTimeWarping} radius.
     *
     * @param radius
     *            the window width of Sakoe-Chiba band in terms of percentage of sequence length.
     */
    void setDTWRadius(double radius);

    /**
     * Set the threshold for gesture minimum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) lower than minThreashold, are accepted.
     *
     *
     * @param minThreashold
     *            represents the minimum distance above which a feature vector is accepted
     */
    void setMinDTWTreshold(double minThreashold);

    /**
     * Set the threshold for gesture maximum acceptance.
     * <p>
     * Only gestures, that have a feature vector distance (by DTW) greater than minThreashold, are accepted.
     *
     *
     * @param maxThreashold
     *            represents the maximum distance above which a feature vector is accepted
     */
    void setMaxDTWThreashold(double maxThreashold);

}
