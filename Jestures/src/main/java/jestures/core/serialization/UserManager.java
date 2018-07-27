/**
 *
 */
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

import jestures.core.file.FileManager;
import jestures.core.recognition.gesture.UserData;
import jestures.core.recognition.gesture.UserDataImpl;

/**
 * The {@link UserManager} class.
 *
 */
public class UserManager implements Serializer {
    private static final Logger LOG = Logger.getLogger(UserManager.class);
    private UserData userData;
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
    public String getUserName() {
        return this.userData.getUserName();
    }

    @Override
    public Map<String, List<Vector2D[]>> getDatasetForRecognition() {
        final Map<String, List<List<Vector2D>>> tempMap = this.userData.getAllGesturesData();
        final Map<String, List<Vector2D[]>> mappaOut = new HashMap<>();
        for (final String elem : tempMap.keySet()) {
            final List<Vector2D[]> newGestureDataset = new ArrayList<>();
            for (final List<Vector2D> gestureDataset : tempMap.get(elem)) {
                final Vector2D[] newGesture = new Vector2D[gestureDataset.size()];
                gestureDataset.toArray(newGesture);
                newGestureDataset.add(newGesture);
            }
            mappaOut.put(elem, newGestureDataset);
        }
        return mappaOut;
    }

    @Override
    public List<String> getAllUserGestures() {
        return this.userData.getAllUserGestures();
    }

    @Override
    public List<List<Vector2D>> getGestureDataset(final String gestureName) {
        return this.userData.getGestureDataset(gestureName);
    }

    @Override
    public void deleteUserProfile() throws IOException {
        FileManager.deleteUserFolder(this.userData.getUserName());
    }

    @Override
    public boolean createUserProfile(final String name) throws IOException {
        final boolean userNotExists = FileManager.createUserFolder(name);
        // SE C'E' GIA' NON CREARLO
        if (userNotExists) {
            this.userData = new UserDataImpl(name);
            this.serializeUser();
        }
        return userNotExists;
    }

    @Override
    public boolean loadOrCreateNewUser(final String name)
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
    public void addFeatureVectorAndSerialize(final String gestureName, final List<Vector2D> featureVector)
            throws IOException {
        this.userData.addGestureFeatureVector(gestureName, featureVector);
        this.serializeUser();
    }

    @Override
    public void addAllFeatureVectorsAndSerialize(final String gestureName, final List<List<Vector2D>> featureVector)
            throws IOException {
        this.userData.addAllGestureFeatureVector(gestureName, featureVector);
        this.serializeUser();
    }

    @Override
    public void deleteGestureDataset(final String gestureName) throws IOException {
        this.userData.deleteGestureDataset(gestureName);
        this.serializeUser();
    }

    @Override
    public void deleteGestureFeatureVector(final String gestureName, final int index) {
        this.userData.deleteGestureFeatureVector(gestureName, index);

    }

    // ################################ SERIALIZE AND DESERIALIZE ####################
    private void deserializeUser(final String name) throws FileNotFoundException, IOException, JsonSyntaxException {
        // IF USER IS SO STUPID TO DELETE FOLDER WHILE RUNNING
        final Reader reader = new FileReader(FileManager.getUserDir(name) + "UserData.json");
        this.userData = this.gson.fromJson(reader, UserDataImpl.class);
        reader.close();
    }

    private void serializeUser() throws IOException {
        // IF USER IS SO STUPID TO DELETE FOLDER WHILE RUNNING, CHECK IF DIRECTORY IS DELETED
        FileManager.createUserFolder(this.userData.getUserName());
        final Writer writer = new FileWriter(FileManager.getUserDir(this.userData.getUserName()) + "UserData.json",
                false);
        this.gson.toJson(this.userData, writer);
        writer.flush();
        writer.close();
    }

}
