package jestures.core.recognition;

import jestures.core.tracking.Tracking;
import jestures.core.view.View;
import jestures.core.view.ViewObserver;

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
     */
    boolean loadUserProfile(String name);

}
