/**
 *
 */
package jestures.core.serialization;

import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

/**
 * The {@link Serializer} class.
 *
 */
public interface Serializer {
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

    /**
     * Create user profile.
     *
     * @param name
     *            the {@link String} username
     * @return <code>true</code> if can create the profile
     */
    boolean createUserProfile(String name);

    /**
     * load user profile.
     *
     * @param name
     *            the {@link String} username
     * @return <code>true</code> if can load the profile
     */
    boolean loadUserProfile(String name);
}
