/**
 *
 */
package jestures.core.serialization;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.gson.JsonIOException;

/**
 * The {@link Serializer} class.
 *
 */
public interface Serializer {
    /**
     * Get all user gestures.
     * 
     * @return the {@link List} of gestures
     */
    List<String> getAllUserGesture();

    /**
     * Serialize the feature vector.
     *
     * @param gestureName
     *            the {@link String} gesture name
     * @param featureVector
     *            the {@link List} feature vector
     * @throws IOException
     *             the {@link IOException}
     * @throws JsonIOException
     *             the {@link JsonIOException}
     */
    void serializeFeatureVector(String gestureName, List<Vector2D> featureVector) throws IOException;

    /**
     * Serialize all gesture feature vector.
     *
     * @param gestureName
     *            the {@link String} gesture name
     * @param featureVector
     *            the {@link List} of {@link List} of feature vector.
     * @throws IOException
     *             the {@link IOException}
     * @throws JsonIOException
     *             the {@link JsonIOException}
     */
    void serializeAllFeatureVectors(String gestureName, List<List<Vector2D>> featureVector)
            throws IOException, JsonIOException;

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
     * @throws FileNotFoundException
     *             if File not found
     */
    boolean loadAndSetUserProfile(String name) throws FileNotFoundException;
}
