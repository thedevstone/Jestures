/**
 *
 */
package jestures.core.recognition.gesture;

import java.io.Serializable;
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
public class UserDataImpl implements UserData, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -5499772829046871767L;
    /**
     * Key ==> String (different gesture name) Value ==> List different feature vectors. A feature vector is a list of
     * {@link Vector2D}
     *
     */
    private String userName;
    private final Map<String, List<List<Vector2D>>> gestures;

    /**
     * The constructor for the {@link UserDataImpl} class.
     *
     * @param name
     *            the String username
     */
    public UserDataImpl(final String name) {
        this.gestures = new HashMap<>();
        this.userName = "null";
        this.userName = name;
    }

    @Override
    public void setUserName(final String id) {
        this.userName = id;
    }

    @Override
    public String getUserName() {
        return this.userName;
    }

    @Override
    public List<String> getAllUserGestures() {
        return new ArrayList<>(this.gestures.keySet());
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
        if (this.gestures.containsKey(gestureName)) {
            this.gestures.get(gestureName).addAll(gestureFeatureVectors);
        } else {
            this.gestures.put(gestureName, gestureFeatureVectors);
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

    @Override
    public String toString() {
        return "User name: " + this.userName + "\n" + "Gestures: " + this.gestures;
    }

}
