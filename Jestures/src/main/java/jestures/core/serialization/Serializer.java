/*******************************************************************************
 * Copyright (c) 2018 Giulianini Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package jestures.core.serialization;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.gson.JsonIOException;

import jestures.core.recognition.gesturedata.RecognitionSettings;
import jestures.core.recognition.gesturedata.RecognitionSettingsImpl;

/**
 * The {@link Serializer} class.
 *
 */
public interface Serializer {
    /**
     * Get the actual username.
     *
     * @return the String username
     */
    String getUserName();

    /**
     * Get the {@link RecognitionSettings}.
     *
     * @return the settings
     */
    RecognitionSettingsImpl getRecognitionSettings();

    /**
     * Set the settings.
     *
     * @param recognitionSettings
     *            the {@link RecognitionSettings}
     * @throws IOException
     *             the {@link IOException}
     */
    void setRecognitionSettings(RecognitionSettingsImpl recognitionSettings) throws IOException;

    /**
     * Get all user gestures.
     *
     * @return the {@link List} of gestures
     */
    List<String> getAllUserGestures();

    /**
     * Get a copy all feature vectors for all gestures.
     * <p>
     * Useful for template match
     *
     * @return the {@link Map} of all gestures data
     */
    Map<String, List<Vector2D[]>> getDatasetForRecognition();

    /**
     * Get all template (feature vectors) for the selected gesture.
     *
     * @param gestureName
     *            the {@link String} gesture name
     * @return the {@link List} of feature vectors
     */
    List<List<Vector2D>> getGestureDataset(String gestureName);

    /**
     * Delete user profile.
     *
     * @throws IOException
     *             the {@link IOException}
     */
    void deleteUserProfile() throws IOException;

    /**
     * Create user profile.
     *
     * @param name
     *            the {@link String} username
     * @return <code>true</code> if can create the profile
     * @throws IOException
     *             the {@link IOException} if can't create the file
     *
     */
    boolean createUserProfile(String name) throws IOException;

    /**
     * load user profile.
     *
     * @param name
     *            the {@link String} username
     * @return <code>true</code> if can load the profile
     * @throws FileNotFoundException
     *             if File is gone
     * @throws IOException
     *             the {@link IOException} during file creation
     */
    boolean loadOrCreateNewUser(String name) throws FileNotFoundException, IOException;

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
     *             the {@link JsonIOException} if can't serialize
     */
    void addFeatureVectorAndSerialize(String gestureName, List<Vector2D> featureVector) throws IOException;

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
     *             the {@link JsonIOException} if can't serialize
     */
    void addAllFeatureVectorsAndSerialize(String gestureName, List<List<Vector2D>> featureVector) throws IOException;

    /**
     * Delete all the gesture's dataset.
     *
     * @param gestureName
     *            the gesture name
     * @throws IOException
     *             the {@link IOException}
     */
    void deleteGestureDataset(String gestureName) throws IOException;

    /**
     * Delete a single feature vector given a gesture and an index in the dataset.
     *
     * @param gestureName
     *            the String gesture name
     * @param index
     *            the index in the dataset
     */
    void deleteGestureFeatureVector(String gestureName, int index);

}
