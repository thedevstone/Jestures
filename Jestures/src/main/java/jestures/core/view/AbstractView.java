/**
 *
 */
package jestures.core.view;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.recognition.Recognition;
import jestures.core.tracking.Tracking;

/**
 * The @link{AbstractView} class.
 */
public abstract class AbstractView implements RecognitionView {
    private final Recognition recognizer; // NOPMD

    /**
     * The @link{AbstractView.java} constructor.
     *
     * @param recognizer
     *            the {@link Tracking} recognizer
     */
    public AbstractView(final Recognition recognizer) {
        this.recognizer = recognizer;
        this.recognizer.getClass();
    }

    @Override
    public abstract void notifyOnFrameChange(int frame, Vector2D derivative, Vector2D path);

    @Override
    public abstract void notifyOnFeatureVectorEvent();

}
