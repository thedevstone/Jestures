package jestures.core.recognition.gesture;

import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.Codification;
import jestures.core.codification.DerivativeCodifier;

/**
 *
 * Represents all type of gesture data depending on the type of codification used.
 * <p>
 * {@link Codification} like {@link DerivativeCodifier} o Vectorial Space Codifier.
 *
 */
public interface UserData {
    /**
     * Set the user id.
     *
     * @param id
     *            the user id
     */
    void setUserId(String id);

    /**
     * Get the user id.
     * 
     * @return the String userid
     */
    String setUserId();

    /**
     * The Feature Vector to serialize.
     *
     * @param featureVector
     *            the {@link List} of feature vector
     * @param gestureName
     *            the {@link String} gesture name
     */
    void addGestureFeatureVector(String gestureName, List<Vector2D> featureVector);

    /**
     * The Feature Vector to serialize.
     *
     * @param gestureFeatureVectors
     *            the {@link List} of {@link List} of feature vector
     * @param gestureName
     *            the {@link String} gesture name
     */
    void addAllGestureFeatureVector(String gestureName, List<List<Vector2D>> gestureFeatureVectors);

    /**
     * Get all template (feature vectors) for the selected gesture.
     *
     * @param gestureName
     *            the {@link String} gesture name
     * @return the {@link List} of feature vectors
     */
    List<List<Vector2D>> getGestureFeatureVectors(String gestureName);

    /**
     * Get a copy all feature vectors for all gestures.
     * <p>
     * Useful for template match
     *
     * @return the {@link Map} of all gestures data
     */
    Map<String, List<List<Vector2D>>> getAllGesturesData();
}
