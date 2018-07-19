package jestures.core.recognition;

import jestures.core.tracking.Tracking;
import jestures.core.view.View;

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
    void attacheUI(View view);

    /**
     * Load the user.
     *
     * @param name
     *            the {@link String} username
     * @return <code>true</code> if is loaded
     */
    boolean loadUserProfile(String name);

}
