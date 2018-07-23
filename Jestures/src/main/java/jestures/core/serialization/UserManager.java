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
import java.util.List;

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
        this.userData = new UserDataImpl("default user");
        this.gson = new Gson();
    }

    @Override
    public String getUserName() {
        return this.userData.getUserName();
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
    public boolean createUserProfile(final String name) throws IOException {
        final boolean result = FileManager.createUserFolders(name);
        // SE C'E' GIA' NON SERIALIZZARLO
        if (!result) {
            this.userData = new UserDataImpl(name);
            this.serializeUser();
        }
        return result;
    }

    @Override
    public boolean loadOrCreateNewUser(final String name)
            throws FileNotFoundException, IOException, JsonSyntaxException {
        UserManager.LOG.debug("User selected: " + name);
        // IF THE FILE IS GONE RECREATE USER
        try {
            this.deserializeUser(name);
        } catch (final FileNotFoundException e) {
            this.createUserProfile(name);
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
    public void deleteGestureDataset(final String gestureName) {
        this.userData.deleteGestureDataset(gestureName);
    }

    @Override
    public void deleteGestureFeatureVector(final String gestureName, final int index) {
        this.userData.deleteGestureFeatureVector(gestureName, index);

    }

    @Override
    public void addAllFeatureVectorsAndSerialize(final String gestureName, final List<List<Vector2D>> featureVector)
            throws IOException {
        this.userData.addAllGestureFeatureVector(gestureName, featureVector);
        this.serializeUser();

    }

    // ################################ SERIALIZE AND DESERIALIZE ####################
    private void deserializeUser(final String name) throws FileNotFoundException, IOException, JsonSyntaxException {
        // IF USER IS SO STUPID TO DELETE FOLDER WHILE RUNNING
        FileManager.createUserFolders(name);
        final Reader reader = new FileReader(FileManager.getUserDir(name) + "UserData.json");
        this.userData = this.gson.fromJson(reader, UserDataImpl.class);
        if (this.userData == null) {
            this.createUserProfile(name);
        }
        reader.close();
    }

    private void serializeUser() throws IOException {
        // IF USER IS SO STUPID TO DELETE FOLDER WHILE RUNNING, CHECK IF DIRECTORY IS DELETED
        FileManager.createUserFolders(this.userData.getUserName());
        final Writer writer = new FileWriter(FileManager.getUserDir(this.userData.getUserName()) + "UserData.json",
                false);
        this.gson.toJson(this.userData, writer);
        writer.flush();
        writer.close();
    }

}
