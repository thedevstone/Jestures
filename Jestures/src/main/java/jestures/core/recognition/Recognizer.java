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
import jestures.core.view.ViewObserver;
import jestures.core.view.screens.RecognitionScreenView;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.log4j.Logger;
import smile.classification.KNN;
import smile.math.distance.DynamicTimeWarping;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;


/**
 * A Recognizer is a simple tracker that can also perform a recognition task.
 */
public final class Recognizer extends TrackerImpl implements Recognition {
    private static final Logger LOG = Logger.getLogger(Recognizer.class);
    private static final double POSTERIORI_THRESHOLD = 0.9;
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
    private Pair<int[], Vector2D[][]> userDataset;

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
    private final DynamicTimeWarping<Vector2D> dtw;
    private KNN<Vector2D[]> knn;
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
        this.dtw = new DynamicTimeWarping<>((a, b) -> a.distance(b), this.recognitionSettings.getDtwRadius());

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
        this.view.forEach(ViewObserver::refreshUsers);
    }

    // ############################################## FROM SENSOR ###################################
    @Override
    public void notifyOnSkeletonChange(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
        super.notifyOnSkeletonChange(primaryJoint, secondaryJoint);
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) { // NOPMD
        super.notifyOnAccelerometerChange(acceleration);
    }

    // ############################################## FROM CODIFIER ###################################

    @Override
    public void notifyOnFrameChange(final int frame, final Queue<Vector2D> featureVector, final Vector2D derivative,
                                    final Vector2D distanceVector) {
        // super call for simple hand tracking
        super.notifyOnFrameChange(frame, featureVector, derivative, distanceVector);
        // view update
        this.view.forEach(t -> t.notifyOnFrameChange(frame, derivative, distanceVector));
        // When the actual frame is a multiple of the recognition update time, recognition can be performed
        if ((frame + 1) % this.recognitionSettings.getUpdateRate().getFrameNumber() == 0) {
            // conversion from list to array. Library need arrays
            final Vector2D[] arrayFeatureVector = new Vector2D[featureVector.size()];
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
    public void notifyOnFeatureVectorEvent(final List<Vector2D> featureVector) {
        super.notifyOnFeatureVectorEvent(featureVector);
        this.view.forEach(ViewObserver::notifyOnFeatureVectorEvent);
    }

    // ############################################# TRACKER #########################################
    @Override
    public boolean loadUserProfile(final String name) throws IOException, JsonSyntaxException {
        // Load the user or create a new one
        final boolean userExists = this.userManager.loadOrCreateNewUser(name);
        // Clear old mapping
        this.intToStringGestureMapping.clear();
        // Load gesture dataset
        this.userDataset = this.userManager.getLinearDatasetForRecognition(this.intToStringGestureMapping);
        // Load user settings
        this.recognitionSettings = this.userManager.getRecognitionSettings();
        // Load the new gesture length
        this.setFrameLength(this.getUserGestureLength());
        // Update view with user settings and update label gesture length
        this.view.forEach(t -> {
            t.updateSettings(this.recognitionSettings);
            t.setGestureLengthLabel(this.getUserGestureLength());
        });
        this.knn = new KNN<>(this.userDataset.getValue(), this.userDataset.getKey(), this.dtw, this.recognitionSettings.getMatchNumber());
        Recognizer.LOG.debug("KNN: " + this.knn + " K = " + this.recognitionSettings.getMatchNumber());
        return userExists;
    }

    @Override
    public GestureLength getUserGestureLength() {
        return this.userManager.getGestureLength();
    }

    @Override
    public List<List<Vector2D>> getGestureDataset(final String gestureName) {
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
     * @param featureVector the gesture
     */
    private void recognize(final Vector2D... featureVector) {
        this.gestureRecognized = true;
        final double[] posterioris = new double[this.intToStringGestureMapping.size()];
        this.intToStringGestureMapping.get(this.knn.predict(featureVector, posterioris));
        for (int i = 0; i < posterioris.length; i++) {
            if (posterioris[i] > Recognizer.POSTERIORI_THRESHOLD) {
                final String gesture = this.intToStringGestureMapping.get(i);
                this.gestureListener.forEach(t -> t.onGestureRecognized(gesture));
                this.view.forEach(t -> t.onGestureRecognized(gesture));
            }
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
