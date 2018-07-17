/**
 *
 */
package jestures.core.view;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.FrameLenght;
import jestures.core.tracking.Tracking;

/**
 * The @link{AbstractView} class.
 */
public abstract class AbstractView implements View {
    private final Tracking tracker;
    private FrameLenght frameLength;

    /**
     * The @link{AbstractView.java} constructor.
     *
     * @param tracker
     *            the {@link Tracking} tracker
     */
    public AbstractView(final Tracking tracker) {
        this.tracker = tracker;
    }

    @Override
    public FrameLenght getFrameLength() {
        return this.frameLength;
    }

    @Override
    public void setFrameLength(final FrameLenght length) {
        this.frameLength = length;
    }

    @Override
    public void startSensor() {
        this.tracker.startSensor();
    }

    @Override
    public void stopSensor() {
        this.tracker.stopSensor();
    }

    @Override
    public Tracking getTracker() {
        return this.tracker;
    }

    @Override
    public abstract void notifyOnFrameChange(int frame, Vector2D derivative, Vector2D path);

    @Override
    public abstract void notifyOnFeatureVectorEvent();

}
