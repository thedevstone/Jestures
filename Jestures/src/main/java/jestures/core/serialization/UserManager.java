/*******************************************************************************
 * Copyright (c) 2018 Giulianini Luca Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions and limitations under the
 * License.
 *******************************************************************************/
package jestures.core.serialization;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import jestures.core.codification.GestureLength;
import jestures.core.file.FileManager;
import jestures.core.file.LibPaths;
import jestures.core.recognition.gesturedata.RecognitionSettingsImpl;
import jestures.core.recognition.gesturedata.UserData;
import jestures.core.recognition.gesturedata.UserDataImpl;

/**
 * A User Manager is a serializer that manage all user data and templates.
 */
public class UserManager implements Serializer {
    private static final Logger LOG = Logger.getLogger(UserManager.class);
    /**
     * User data that get serialized in a json file.
     */
    private UserData userData;
    /**
     * Google library for serialization.
     */
    private final Gson gson;

    /**
     * The constructor for the {@link UserManager} class.
     */
    public UserManager() {
        this.userData = null;
        this.gson = new Gson();
        UserManager.LOG.getClass();
    }

    @Override
    public final String getUserName() {
        return this.userData.getUserName();
    }

    @Override
    public final void setGestureLength(final GestureLength length) throws IOException, IllegalStateException {
        this.userData.setGestureLength(length);
        this.serializeUser();
    }

    @Override
    public final GestureLength getGestureLength() {
        return this.userData.getGestureLength();
    }

    @Override
    public final RecognitionSettingsImpl getRecognitionSettings() {
        return this.userData.getRecognitionSettings();
    }

    @Override
    public final void setRecognitionSettings(final RecognitionSettingsImpl recognitionSettings) throws IOException {
        this.userData.setRecognitionSettings(recognitionSettings);
        this.serializeUser();
    }

    @Override
    public final Map<Integer, List<Vector2D[]>> getDatasetForRecognition(
            final Map<Integer, String> gestureKeyToStringMapping) {
        // Craete a map of vectors and another map for gesture Integer-String mapping
        final Map<String, List<List<Vector2D>>> tempMap = this.userData.getAllGesturesData();
        final Map<Integer, List<Vector2D[]>> mappaOut = new HashMap<>();
        int gestureKey = 0;
        for (final String elem : tempMap.keySet()) {
            final List<Vector2D[]> newGestureDataset = new ArrayList<>();
            for (final List<Vector2D> gestureDataset : tempMap.get(elem)) {
                final Vector2D[] newGesture = new Vector2D[gestureDataset.size()];
                gestureDataset.toArray(newGesture);
                newGestureDataset.add(newGesture);
            }
            mappaOut.put(gestureKey, newGestureDataset);
            gestureKeyToStringMapping.put(gestureKey, elem);
            gestureKey++;
        }
        return mappaOut;
    }

    @Override
    public final List<String> getAllUserGestures() {
        return this.userData.getAllUserGestures();
    }

    @Override
    public final List<List<Vector2D>> getGestureDataset(final String gestureName) {
        return this.userData.getGestureDataset(gestureName);
    }

    @Override
    public final void deleteUserProfile() throws IOException {
        FileManager.deleteUserFolder(this.userData.getUserName());
    }

    @Override
    public final boolean createUserProfile(final String name) throws IOException {
        final boolean userNotExists = FileManager.createUserFolder(name);
        if (userNotExists) {
            this.userData = new UserDataImpl(name);
            this.serializeUser();
        }
        return userNotExists;
    }

    @Override
    public final boolean loadOrCreateNewUser(final String name)
            throws FileNotFoundException, IOException, JsonSyntaxException {
        try {
            this.deserializeUser(name);
        } catch (final FileNotFoundException e) {
            if (!this.createUserProfile(name)) { // SE MANCA LA CARTELLA COMPLETA CREA DI NUOVO L'UTENTE
                this.userData = new UserDataImpl(name); // SE MANCA IL FILE MA NON LA CARTELLA CREA SOLO IL FILE
                this.serializeUser();
            }
            throw e;
        }
        return true;
    }

    @Override
    public final void addFeatureVectorAndSerialize(final String gestureName, final List<Vector2D> featureVector)
            throws IOException {
        this.userData.addGestureFeatureVector(gestureName, featureVector);
        this.serializeUser();
    }

    @Override
    public final void addAllFeatureVectorsAndSerialize(final String gestureName,
            final List<List<Vector2D>> featureVector) throws IOException {
        this.userData.addAllGestureFeatureVector(gestureName, featureVector);
        this.serializeUser();
    }

    @Override
    public final void deleteGestureDataset(final String gestureName) throws IOException {
        this.userData.deleteGestureDataset(gestureName);
        this.serializeUser();
    }

    @Override
    public final void deleteGestureFeatureVector(final String gestureName, final int index) {
        this.userData.deleteGestureFeatureVector(gestureName, index);

    }

    // ################################ SERIALIZE AND DESERIALIZE ####################
    private void deserializeUser(final String name) throws FileNotFoundException, IOException, JsonSyntaxException {
        // IF USER IS SO STUPID TO DELETE FOLDER WHILE RUNNING
        final Reader reader = new FileReader(FileManager.getUserDir(name) + LibPaths.USER_DATASET_FILE.getDirName());
        this.userData = this.gson.fromJson(reader, UserDataImpl.class);
        reader.close();
    }

    private void serializeUser() throws IOException {
        // IF USER IS SO STUPID TO DELETE FOLDER WHILE RUNNING, CHECK IF DIRECTORY IS DELETED
        FileManager.createUserFolder(this.userData.getUserName());
        final Writer writer = new FileWriter(
                FileManager.getUserDir(this.userData.getUserName()) + LibPaths.USER_DATASET_FILE.getDirName(), false);
        this.gson.toJson(this.userData, writer);
        writer.flush();
        writer.close();
    }

}
