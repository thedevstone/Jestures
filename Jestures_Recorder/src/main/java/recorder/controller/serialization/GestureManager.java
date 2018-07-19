/**
 *
 */
package recorder.controller.serialization;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.recognition.gesture.UserData;
import jestures.core.recognition.gesture.UserDataImpl;

/**
 * The {@link GestureManager} class.
 *
 */
public class GestureManager implements Serialization {
    private final UserData gestures;
    private boolean modified = false;

    /**
     * The constructor for the {@link GestureManager.java} class.
     */
    public GestureManager() {
        this.gestures = new UserDataImpl();
    }

    @Override
    public void serializeFeatureVector(final String gestureName, final List<Vector2D> featureVector) {
        this.gestures.addGestureFeatureVector(gestureName, featureVector);
        this.modified = true;

    }

    @Override
    public void serializeAllFeatureVectors(final String gestureName, final List<List<Vector2D>> featureVector) {
        this.gestures.addAllGestureFeatureVector(gestureName, featureVector);
        this.modified = true;
    }

}
