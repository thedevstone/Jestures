/**
 *
 */
package recorder.controller.serialization;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The {@link Serialization} class.
 *
 */
public interface Serialization {
    /**
     * Serialize the feature vector.
     *
     * @param gestureName
     *            the {@link String} gesture name
     * @param featureVector
     *            the {@link List} feature vector
     */
    void serializeFeatureVector(String gestureName, List<Vector2D> featureVector);

    /**
     * Serialize all gesture feature vector.
     *
     * @param gestureName
     *            the {@link String} gesture name
     * @param featureVector
     *            the {@link List} of {@link List} of feature vector.
     */
    void serializeAllFeatureVectors(String gestureName, List<List<Vector2D>> featureVector);
}
