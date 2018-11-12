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

package recorder.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.gson.JsonIOException;

import jestures.core.codification.GestureLength;
import jestures.core.tracking.Tracker;
import recorder.view.RecordingView;
import recorder.view.RecordingViewObserver;

/**
 * A recorder is a basic tool that manages all recording tasks.
 *
 */
public interface Recorder extends Tracker {

    /**
     * Attache the view.
     *
     * @param view
     *            the {@link RecordingView}
     */
    void attacheUI(RecordingViewObserver view);

    /**
     * Get all template (feature vectors) for the selected gesture.
     *
     * @param gestureName
     *            the {@link String} gesture name
     * @return the {@link List} of feature vectors
     */
    List<List<Vector2D>> getGestureDataset(String gestureName);

    /**
     * Get the user gesture length.
     *
     * @return the gesture length
     */
    GestureLength getUserGestureLength();

    /**
     * Set the user gesture length.
     *
     * @param length
     *            the gesture length
     * @throws IOException
     *             ioexception
     */
    void setUserGestureLength(GestureLength length) throws IOException;

    /**
     * Delete the user profile.
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
     *             if
     * @throws FileNotFoundException
     *             the {@link FileNotFoundException}
     */
    boolean createUserProfile(String name) throws IOException;

    /**
     * Load the user.
     *
     * @param name
     *            the {@link String} username
     * @return <code>true</code> if is loaded
     * @throws FileNotFoundException
     *             if file not found
     * @throws IOException
     *             the {@link IOException} if can't create user folder
     */
    boolean loadUserProfile(String name) throws FileNotFoundException, IOException;

    /**
     * Select the feature vector.
     *
     * @param gesture
     *            the {@link String} gesture
     * @param index
     *            the index in the list
     * @throws IOException
     *             the exception
     * @throws JsonIOException
     *             the {@link JsonIOException}
     */
    void addFeatureVector(String gesture, int index) throws IOException;

    /**
     * Add all templates.
     *
     * @param gesture
     *            the String gesture
     * @throws IOException
     *             the exception
     * @throws JsonIOException
     *             the {@link JsonIOException}
     */
    void addAllFeatureVectors(String gesture) throws IOException;

    /**
     * Delete the feature vector in the list.
     *
     * @param index
     *            the index in the list.
     */
    void deleteRecordedFeatureVector(int index);

    /**
     * Clear the featureVector.
     */
    void clearRecordedDataset();

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