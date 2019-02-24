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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

import com.google.gson.JsonSyntaxException;

import jestures.core.codification.GestureLength;
import jestures.core.serialization.Serializer;
import jestures.core.serialization.UserManager;
import jestures.core.tracking.Tracker;
import jestures.core.tracking.TrackerImpl;
import jestures.core.view.screens.RecognitionScreenView;
import recorder.view.RecordingViewObserver;

/**
 * Instance of Recorder.
 */

public final class RecorderImpl extends TrackerImpl implements Recorder {
    /**
     * User manager
     */
    private final Serializer userManager;
    /**
     * Instance
     */
    private static Recorder instance;
    /**
     * Set of observable view connected
     */
    private final Set<RecordingViewObserver> view;
    /**
     * Flag for recording
     */
    private boolean isRecording;
    /**
     * Temporary cache for recorded feature vectors
     */
    private final List<List<Vector3D>> listOfRecordedFeatureVector;

    private static final int THREASHOLD = 50;

    private RecorderImpl() {
        this.listOfRecordedFeatureVector = new ArrayList<>();
        this.userManager = new UserManager();
        this.view = new HashSet<>();
        RecognitionScreenView.startFxThread();
    }

    /**
     * Get the instance.
     *
     * @return the {@link Tracker} instance.
     */
    public static Recorder getInstance() {
        synchronized (Tracker.class) {
            if (RecorderImpl.instance == null) {
                RecorderImpl.instance = new RecorderImpl();
            }
        }
        return RecorderImpl.instance;
    }

    // ############################################## OBSERVER ###################################

    @Override
    public void attacheUI(final RecordingViewObserver view) {
        this.view.add(view);
        this.view.forEach(t -> t.refreshUsers());
    }

    // ############################################## FROM SENSOR ###################################
    @Override
    public void notifyOnSkeletonChange(final Vector3D primaryJoint, final Vector3D secondaryJoint) {
        super.notifyOnSkeletonChange(primaryJoint, secondaryJoint);
        // Check if user is recording
        this.secJointTrigger(primaryJoint, secondaryJoint);
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) {
    }

    // ############################################## FROM CODIFIER ###################################
    @Override
    public void notifyOnFrameChange(final int frame, final Queue<Vector3D> featureVector, final Vector3D derivative,
            final Vector3D distanceVector) {
        // Updates view on every frame
        this.view.forEach(t -> t.notifyOnFrameChange(frame, derivative, distanceVector));
    }

    @Override
    public void notifyOnFeatureVectorEvent(final List<Vector3D> featureVector) {
        // If the user is recording and a new feature vector is available that the feature vector is saved in cache
        if (this.isRecording) {
            this.view.forEach(t -> t.notifyOnFeatureVectorEvent());
            this.listOfRecordedFeatureVector.add(Collections.unmodifiableList(featureVector));
        }
    }

    // ############## RECORDED DATASET ##############

    @Override
    public void deleteRecordedFeatureVector(final int index) {
        this.listOfRecordedFeatureVector.remove(index);
    }

    @Override
    public void clearRecordedDataset() {
        this.listOfRecordedFeatureVector.clear();
    }

    // #################### USER MANAGER #####################

    @Override
    public GestureLength getUserGestureLength() {
        return this.userManager.getGestureLength();
    }

    @Override
    public void setUserGestureLength(final GestureLength length) throws IOException {
        this.userManager.setGestureLength(length);
        super.setFrameLength(length);
    }

    @Override
    public String getUserName() {
        return this.userManager.getUserName();
    }

    @Override
    public List<String> getAllUserGesture() {
        return this.userManager.getAllUserGestures();
    }

    @Override
    public List<List<Vector3D>> getGestureDataset(final String gestureName) {
        return this.userManager.getGestureDataset(gestureName);
    }

    @Override
    public void deleteUserProfile() throws IOException {
        this.userManager.deleteUserProfile();
    }

    @Override
    public boolean createUserProfile(final String name) throws IOException {
        final boolean val = this.userManager.createUserProfile(name);
        this.setFrameLength(this.getUserGestureLength());
        this.view.forEach(t -> t.setGuiGestureLenght(this.getUserGestureLength()));
        return val;
    }

    @Override
    public boolean loadUserProfile(final String name) throws FileNotFoundException, IOException, JsonSyntaxException {
        final boolean val = this.userManager.loadOrCreateNewUser(name);
        this.setFrameLength(this.getUserGestureLength());
        this.view.forEach(t -> t.setGuiGestureLenght(this.getUserGestureLength()));
        return val;
    }

    @Override
    public void addFeatureVector(final String gesture, final int index) throws IOException {
        this.userManager.addFeatureVectorAndSerialize(gesture, this.listOfRecordedFeatureVector.get(index));

    }

    @Override
    public void addAllFeatureVectors(final String gesture) throws IOException {
        this.userManager.addAllFeatureVectorsAndSerialize(gesture, this.listOfRecordedFeatureVector);
    }

    @Override
    public void deleteGestureDataset(final String gestureName) throws IOException {
        this.userManager.deleteGestureDataset(gestureName);
    }

    @Override
    public void deleteGestureFeatureVector(final String gestureName, final int index) {
        this.userManager.deleteGestureFeatureVector(gestureName, index);
    }

    // ############################################## INSTANCE METHODS ###################################

    private void secJointTrigger(final Vector3D primaryJoint, final Vector3D secondaryJoint) {
        // If the secondary hand is above the primnary sholder than recording can start
        if (secondaryJoint.getY() > primaryJoint.getY() + RecorderImpl.THREASHOLD && !this.isRecording) {
            this.isRecording = true;
            this.view.forEach(t -> t.setRecording(true));
            this.resetCodificationFrame();
        } else if (primaryJoint.getY() - RecorderImpl.THREASHOLD > secondaryJoint.getY() && this.isRecording) {
            this.isRecording = false;
            this.view.forEach(t -> t.setRecording(false));
        }
    }

}
