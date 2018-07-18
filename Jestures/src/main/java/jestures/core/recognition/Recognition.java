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

}
