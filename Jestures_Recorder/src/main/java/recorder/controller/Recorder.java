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
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.gson.JsonSyntaxException;

import jestures.core.serialization.Serializer;
import jestures.core.serialization.UserManager;
import jestures.core.tracking.Tracker;
import jestures.core.tracking.Tracking;
import jestures.core.view.screens.RecognitionScreenView;
import recorder.view.RecViewObserver;

/**
 * The {@link Recorder} class.
 */

public final class Recorder extends Tracker implements Recording {
    private final Serializer userManager;
    private static Recording instance;
    private final Set<RecViewObserver> view;
    private boolean isRecording;
    private final List<List<Vector2D>> listOfFeatureVector;

    private static final int THREASHOLD = 50;

    private Recorder() {
        this.listOfFeatureVector = new ArrayList<>();
        this.userManager = new UserManager();
        this.view = new HashSet<>();
        RecognitionScreenView.startFxThread();
    }

    /**
     * Get the instance.
     *
     * @return the {@link Tracking} instance.
     */
    public static Recording getInstance() {
        synchronized (Tracking.class) {
            if (Recorder.instance == null) {
                Recorder.instance = new Recorder();
            }
        }
        return Recorder.instance;
    }

    // ############################################## OBSERVER ###################################

    @Override
    public void attacheUI(final RecViewObserver view) {
        this.view.add(view);
        this.view.forEach(t -> t.setFrameLength(this.getFrameLength()));
        this.view.forEach(t -> t.refreshUsers());
    }

    // ############################################## FROM SENSOR ###################################
    @Override
    public void notifyOnSkeletonChange(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
        super.notifyOnSkeletonChange(primaryJoint, secondaryJoint);
        this.secJointTrigger(primaryJoint, secondaryJoint);
    }

    @Override
    public void notifyOnAccelerometerChange(final Vector3D acceleration) {
    }

    // ############################################## FROM CODIFIER ###################################
    @Override
    public void notifyOnFrameChange(final int frame, final Queue<Vector2D> featureVector, final Vector2D derivative,
            final Vector2D distanceVector) {
        this.view.forEach(t -> t.notifyOnFrameChange(frame, derivative, distanceVector));

    }

    @Override
    public void notifyOnFeatureVectorEvent(final List<Vector2D> featureVector) {
        if (this.isRecording) {
            this.view.forEach(t -> t.notifyOnFeatureVectorEvent());
            this.listOfFeatureVector.add(Collections.unmodifiableList(featureVector));
        }
    }

    // ############## RECORDED DATASET ##############

    @Override
    public void deleteRecordedFeatureVector(final int index) {
        this.listOfFeatureVector.remove(index);
    }

    @Override
    public void clearRecordedDataset() {
        this.listOfFeatureVector.clear();
    }

    // #################### USER MANAGER #####################

    @Override
    public String getUserName() {
        return this.userManager.getUserName();
    }

    @Override
    public List<String> getAllUserGesture() {
        return this.userManager.getAllUserGestures();
    }

    @Override
    public List<List<Vector2D>> getGestureDataset(final String gestureName) {
        return this.userManager.getGestureDataset(gestureName);
    }

    @Override
    public void deleteUserProfile() throws IOException {
        this.userManager.deleteUserProfile();
    }

    @Override
    public boolean createUserProfile(final String name) throws IOException {
        return this.userManager.createUserProfile(name);
    }

    @Override
    public boolean loadUserProfile(final String name) throws FileNotFoundException, IOException, JsonSyntaxException {
        return this.userManager.loadOrCreateNewUser(name);
    }

    @Override
    public void addFeatureVector(final String gesture, final int index) throws IOException {
        this.userManager.addFeatureVectorAndSerialize(gesture, this.listOfFeatureVector.get(index));

    }

    @Override
    public void addAllFeatureVectors(final String gesture) throws IOException {
        this.userManager.addAllFeatureVectorsAndSerialize(gesture, this.listOfFeatureVector);
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

    private void secJointTrigger(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
        if (secondaryJoint.getY() > primaryJoint.getY() + Recorder.THREASHOLD && !this.isRecording) {
            this.isRecording = true;
            this.view.forEach(t -> t.setRecording(true));
            this.resetCodificationFrame();
        } else if (primaryJoint.getY() - Recorder.THREASHOLD > secondaryJoint.getY() && this.isRecording) {
            this.isRecording = false;
            this.view.forEach(t -> t.setRecording(false));
        }
    }

}
