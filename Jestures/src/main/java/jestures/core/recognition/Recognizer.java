package jestures.core.recognition;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.tracking.Tracker;
import jestures.core.tracking.Tracking;
import jestures.core.view.View;

/**
 *
 *
 */
public final class Recognizer extends Tracker implements Recognition {
    private final Set<View> view;
    private static Recognition instance;

    /**
     *
     */
    private Recognizer() {
        this.view = new HashSet<>();
    }

    /**
     * Get the instance.
     *
     * @return the {@link Tracking} instance.
     */
    public static Recognition getInstance() {
        synchronized (Tracking.class) {
            if (Recognizer.instance == null) {
                Recognizer.instance = new Recognizer();
            }
        }
        return Recognizer.instance;
    }

    @Override
    public void attacheUI(final View view) {
        this.view.add(view);
        this.view.forEach(t -> t.setFrameLength(this.getFrameLength()));
    }

    // ############################################## FROM SENSOR ###################################
    @Override
    public void notifyOnSkeletonChange(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
        super.notifyOnSkeletonChange(primaryJoint, secondaryJoint);
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) { // NOPMD
        super.notifyOnAccelerometerChange(acceleration);
    }

    // ############################################## FROM CODIFIER ###################################
    @Override
    public void notifyOnFrameChange(final int frame, final Vector2D derivative, final Vector2D distanceVector) {
        super.notifyOnFrameChange(frame, derivative, distanceVector);
        this.view.forEach(t -> t.notifyOnFrameChange(frame, derivative, distanceVector));

    }

    @Override
    public void notifyOnFeatureVectorEvent(final List<Vector2D> featureVector) {
        super.notifyOnFeatureVectorEvent(featureVector);
        this.view.forEach(t -> t.notifyOnFeatureVectorEvent());
    }

}
