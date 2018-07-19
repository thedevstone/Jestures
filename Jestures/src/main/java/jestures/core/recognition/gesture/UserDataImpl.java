/**
 *
 */
package jestures.core.recognition.gesture;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.DerivativeCodifier;

/**
 * The {@link UserDataImpl} class that contains all the gestures codified by the {@link DerivativeCodifier}.
 *
 */
public class UserDataImpl implements UserData {
    /**
     * Key ==> String (different gesture name) Value ==> List different feature vectors. A feature vector is a list of
     * {@link Vector2D}
     *
     */
    private final Map<String, List<List<Vector2D>>> gestures;
    private String userId;

    /**
     * The constructor for the {@link UserDataImpl.java} class.
     */
    public UserDataImpl() {
        this.gestures = new HashMap<>();
        this.userId = "null";
    }

    @Override
    public void setUserId(final String id) {
        this.userId = id;
    }

    @Override
    public String setUserId() {
        return this.userId;
    }

    @Override
    public void addGestureFeatureVector(final String gestureName, final List<Vector2D> featureVector) {
        if (this.gestures.containsKey(gestureName) && !this.gestures.get(gestureName).contains(featureVector)) {
            this.gestures.get(gestureName).add(featureVector);
        } else if (!this.gestures.containsKey(gestureName)) {
            final List<List<Vector2D>> newFeatureVector = new ArrayList<>();
            newFeatureVector.add(featureVector);
            this.gestures.put(gestureName, newFeatureVector);
        }
    }

    @Override
    public void addAllGestureFeatureVector(final String gestureName, final List<List<Vector2D>> gestureFeatureVectors) {
        if (!this.gestures.containsKey(gestureName)) {
            this.gestures.get(gestureName).addAll(gestureFeatureVectors);
        }
    }

    @Override
    public List<List<Vector2D>> getGestureFeatureVectors(final String gestureName) {
        return this.gestures.get(gestureName);
    }

    @Override
    public Map<String, List<List<Vector2D>>> getAllGesturesData() {
        return Collections.unmodifiableMap(this.gestures);
    }

}
