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
package jestures.core.recognition;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import javafx.util.Pair;
import jestures.core.codification.GestureLength;
import jestures.core.recognition.gesturedata.RecognitionSettings;
import jestures.core.recognition.gesturedata.RecognitionSettingsImpl;
import jestures.core.serialization.Serializer;
import jestures.core.serialization.UserManager;
import jestures.core.tracking.Tracker;
import jestures.core.tracking.TrackerImpl;
import jestures.core.view.RecognitionViewObserver;
import jestures.core.view.screens.RecognitionScreenView;
import smile.math.distance.DynamicTimeWarping;

/**
 * A Recognizer is a simple tracker that can also perform a recognition task.
 *
 */
public final class Recognizer extends TrackerImpl implements Recognition {
    private static final Logger LOG = Logger.getLogger(Recognizer.class);
    /**
     * Access to user serialization and deserialization.
     */
    private final Serializer userManager;
    /**
     * Singleton.
     */
    private static Recognition instance;
    /**
     * Recognition view. Set of observer.
     */
    private final Set<RecognitionViewObserver> view;
    /**
     * Set of gesture observer.
     */
    private final Set<GestureListener> gestureListener;
    /**
     * User dataset of gsetures. Deserialized and loaded in memory. Reduced memory cost.
     */
    private Map<Integer, List<Vector3D[]>> userDataset;
    /**
     * Temporary map that stores the mapping from integer to gesture name
     */
    private final Map<Integer, String> intToStringGestureMapping;
    /**
     * The time passed after the last gesture has been recognized
     */
    private long lastGestureTime;

    /**
     * Dynamic Time Warping algorithm
     */
    private final DynamicTimeWarping<Vector3D> dtw;
    /**
     * User recognition settings
     */
    private RecognitionSettings recognitionSettings;
    /**
     * <code>true</code> if the last gesture has been recognized
     */
    private boolean gestureRecognized;

    private Recognizer() {
        this.view = new HashSet<>();
        this.gestureListener = new HashSet<>();
        this.intToStringGestureMapping = new HashMap<>();
        this.userManager = new UserManager();
        this.userDataset = null;
        this.lastGestureTime = 0;
        this.recognitionSettings = new RecognitionSettingsImpl(UpdateRate.FPS_10, 0, 0, 0, 0, 0);
        this.dtw = new DynamicTimeWarping<Vector3D>((a, b) -> a.distance(b), this.recognitionSettings.getDtwRadius());
        RecognitionScreenView.startFxThread();
    }

    /**
     * Get the instance.
     *
     * @return the {@link Tracker} instance.
     */
    public static Recognition getInstance() {
        synchronized (Tracker.class) {
            if (Recognizer.instance == null) {
                Recognizer.instance = new Recognizer();
            }
        }
        return Recognizer.instance;
    }

    @Override
    public void attacheUI(final RecognitionViewObserver view) {
        this.view.add(view);
        this.view.forEach(t -> t.refreshUsers());
    }

    // ############################################## FROM SENSOR ###################################
    @Override
    public void notifyOnSkeletonChange(final Vector3D primaryJoint, final Vector3D secondaryJoint) {
        super.notifyOnSkeletonChange(primaryJoint, secondaryJoint);
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) { // NOPMD
        super.notifyOnAccelerometerChange(acceleration);
    }

    // ############################################## FROM CODIFIER ###################################

    @Override
    public void notifyOnFrameChange(final int frame, final Queue<Vector3D> featureVector, final Vector3D derivative,
            final Vector3D distanceVector) {
        // super call for simple hand tracking
        super.notifyOnFrameChange(frame, featureVector, derivative, distanceVector);
        // view update
        this.view.forEach(t -> t.notifyOnFrameChange(frame, derivative, distanceVector));
        // When the actual frame is a multiple of the recognition update time, recognition can be performed
        if ((frame + 1) % this.recognitionSettings.getUpdateRate().getFrameNumber() == 0) {
            // conversion from list to array. Library need arrays
            final Vector3D[] arrayFeatureVector = new Vector3D[featureVector.size()];
            featureVector.toArray(arrayFeatureVector);
            // Starting timer
            final long currentSec = System.currentTimeMillis();
            // If the last gesture has been recognized and timer was over then recognition can occur
            if (this.gestureRecognized
                    && currentSec - this.lastGestureTime > this.recognitionSettings.getMinTimeSeparation()) {
                // timer is reset
                this.lastGestureTime = currentSec;
                // start recognition
                this.recognize(arrayFeatureVector);
                // if gesture was not recognized you don't have to wait
            } else if (!this.gestureRecognized) {
                this.lastGestureTime = currentSec;
                this.recognize(arrayFeatureVector);
            }
        }
    }

    @Override
    public void notifyOnFeatureVectorEvent(final List<Vector3D> featureVector) {
        super.notifyOnFeatureVectorEvent(featureVector);
        this.view.forEach(t -> t.notifyOnFeatureVectorEvent());
    }

    // ############################################# TRACKER #########################################
    @Override
    public boolean loadUserProfile(final String name) throws FileNotFoundException, IOException, JsonSyntaxException {
        // Load the user or create a new one
        final boolean userExists = this.userManager.loadOrCreateNewUser(name);
        // Clear old mapping
        this.intToStringGestureMapping.clear();
        // Load gesture dataset
        this.userDataset = this.userManager.getDatasetForRecognition(this.intToStringGestureMapping);
        // Load user settings
        this.recognitionSettings = this.userManager.getRecognitionSettings();
        // Load the new gesture length
        this.setFrameLength(this.getUserGestureLength());
        // Update view with user settings and update label gesture length
        this.view.forEach(t -> {
            t.updateSettings(this.recognitionSettings);
            t.setGestureLengthLabel(this.getUserGestureLength());
        });
        return userExists;
    }

    @Override
    public GestureLength getUserGestureLength() {
        return this.userManager.getGestureLength();
    }

    @Override
    public List<List<Vector3D>> getGestureDataset(final String gestureName) {
        return this.userManager.getGestureDataset(gestureName);
    }

    @Override
    public List<String> getAllUserGesture() {
        return this.userManager.getAllUserGestures();
    }

    @Override
    public String getUserName() {
        return this.userManager.getUserName();
    }

    // ############################################# RECOGNIZER #########################################

    /**
     * The core of recognition.
     *
     * @param featureVector
     *            the gesture
     */
    private void recognize(final Vector3D... featureVector) {
        // Array of distances
        final List<Pair<Double, Integer>> distances = new ArrayList<>();
        // Get the distances from every template and save in the array
        for (final Integer gestureKey : this.userDataset.keySet()) {
            for (final Vector3D[] gestureTemplate : this.userDataset.get(gestureKey)) {
                // Distance calculated with DTW
                final double dtwDist = this.dtw.d(gestureTemplate, featureVector);
                // Threshold prevent unreasonable cpu calc
                if (dtwDist < this.recognitionSettings.getMaxDTWThreashold()
                        && dtwDist > this.recognitionSettings.getMinDtwThreashold()) {
                    distances.add(new Pair<Double, Integer>(dtwDist, gestureKey));
                }
            }
        }

        /**
         * Knn algorithm. Take the nearest k vector from our feature vector and return the associated gesture
         *
         */
        if (distances.size() > this.recognitionSettings.getMatchNumber()) {
            // Shorter distances first
            distances.sort((a, b) -> a.getKey().compareTo(b.getKey()));
            // Array of double with the distances
            final double[] kNearestNeighbor = new double[this.recognitionSettings.getMatchNumber()];
            // Fill the array
            for (int i = 0; i < this.recognitionSettings.getMatchNumber(); i++) {
                kNearestNeighbor[i] = distances.get(i).getValue() + 0.0;
            }
            // Get gseture name
            final String gesture = this.intToStringGestureMapping.get((int) StatUtils.mode(kNearestNeighbor)[0]);
            // Notification
            this.gestureListener.forEach(t -> t.onGestureRecognized(gesture));
            this.view.forEach(t -> t.onGestureRecognized(gesture));

            this.gestureRecognized = true;
        } else {
            // NO GESTURE
            this.gestureRecognized = false;
        }
    }

    @Override
    public void saveSettings() throws IOException {
        this.userManager.setRecognitionSettings(this.recognitionSettings);
    }

    @Override
    public void setDtwRadius(final double radius) {
        this.recognitionSettings.setDtwRadius(radius);

    }

    @Override
    public void setMinDtwThreashold(final int minDtwThreashold) {
        this.recognitionSettings.setMinDtwThreashold(minDtwThreashold);
    }

    @Override
    public void setMaxDtwThreashold(final int maxDtwThreashold) {
        this.recognitionSettings.setMaxDtwThreashold(maxDtwThreashold);
    }

    @Override
    public void setUpdateRate(final UpdateRate updateRate) {
        if (this.getFrameLength().getFrameNumber() % updateRate.getFrameNumber() == 0) {
            this.recognitionSettings.setUpdateRate(updateRate);
        } else {
            Recognizer.LOG.error("Update rate must be a MCD of frame rate");
            throw new IllegalStateException("Update rate must be a MCD of frame rate");
        }
    }

    @Override
    public void setMinTimeSeparation(final int minTimeSeparation) {
        this.recognitionSettings.setMinTimeSeparation(minTimeSeparation);
    }

    @Override
    public void setMatchNumber(final int matchNumber) {
        this.recognitionSettings.setMatchNumber(matchNumber);

    }

    @Override
    public void setOnGestureRecognized(final GestureListener listener) {
        this.gestureListener.add(listener);
    }

}
