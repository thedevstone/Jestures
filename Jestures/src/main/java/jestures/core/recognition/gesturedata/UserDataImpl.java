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
package jestures.core.recognition.gesturedata;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.log4j.Logger;

import jestures.core.codification.FrameLength;
import jestures.core.recognition.UpdateRate;

/**
 * The {@link UserDataImpl} class that contains all the gestures codified by the codifier.
 *
 */
public class UserDataImpl implements UserData, Serializable {
    private static final UpdateRate DEFAULT_UPDATE = UpdateRate.FPS_10;
    private static final double DEFAULT_RADIUS = 0.5;
    private static final int DEFAULT_MIN_TRESHOLD = 300;
    private static final int DEFAULT_MAX_TRESHOLD = 700;
    private static final int DEFAULT_TIME_SEP = 500;
    private static final int DEFAULT_MATCH = 3;

    private static final FrameLength DEFAULT_GESTURE_LENGTH = FrameLength.FPS_30;
    /**
     *
     */
    private static final long serialVersionUID = -5499772829046871767L;
    /**
     * Key == String (different gesture name) Value == List different feature vectors. A feature vector is a list of
     * {@link Vector2D}
     *
     */
    private String userName;
    private FrameLength gestureLength;
    private RecognitionSettingsImpl recognitionSettings;
    private final Map<String, List<List<Vector2D>>> gestures;
    private static final Logger LOG = Logger.getLogger(UserDataImpl.class);

    /**
     * The constructor for the {@link UserDataImpl} class.
     *
     * @param name
     *            the String username
     */
    public UserDataImpl(final String name) {
        this.userName = name;
        this.gestureLength = UserDataImpl.DEFAULT_GESTURE_LENGTH;
        this.recognitionSettings = new RecognitionSettingsImpl(UserDataImpl.DEFAULT_UPDATE, UserDataImpl.DEFAULT_RADIUS,
                UserDataImpl.DEFAULT_MIN_TRESHOLD, UserDataImpl.DEFAULT_MAX_TRESHOLD, UserDataImpl.DEFAULT_TIME_SEP,
                UserDataImpl.DEFAULT_MATCH);
        this.gestures = new HashMap<>();
        UserDataImpl.LOG.getClass();
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
    public void setGestureLength(final FrameLength length) {
        if (this.gestures.keySet().isEmpty()) {
            this.gestureLength = length;
        } else {
            throw new IllegalStateException("Cannot have different gesture length");
        }
    }

    @Override
    public FrameLength getGestureLength() {
        return this.gestureLength;
    }

    @Override
    public RecognitionSettingsImpl getRecognitionSettings() {
        return this.recognitionSettings;
    }

    @Override
    public void setRecognitionSettings(final RecognitionSettingsImpl recognitionSettings) {
        this.recognitionSettings = recognitionSettings;
    }

    @Override
    public List<String> getAllUserGestures() {
        return Collections.unmodifiableList(new ArrayList<>(this.gestures.keySet()));
    }

    @Override
    public List<List<Vector2D>> getGestureDataset(final String gestureName) {
        return Collections.unmodifiableList(this.gestures.get(gestureName));
    }

    @Override
    public Map<String, List<List<Vector2D>>> getAllGesturesData() {
        return Collections.unmodifiableMap(this.gestures);
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
            this.gestures.put(gestureName, new ArrayList<List<Vector2D>>(gestureFeatureVectors));
        }
    }

    @Override
    public void deleteGestureDataset(final String gestureName) {
        this.gestures.remove(gestureName);
    }

    @Override
    public void deleteGestureFeatureVector(final String gestureName, final int index) {
        this.gestures.get(gestureName).remove(index);
    }

    @Override
    public String toString() {
        return "User name: " + this.userName + "\n" + "Gestures: " + this.gestures;
    }
}
