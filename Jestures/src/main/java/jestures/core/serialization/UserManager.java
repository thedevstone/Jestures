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
    public List<String> getAllUserGesture() {
        return this.userData.getAllUserGestures();
    }

    @Override
    public boolean createUserProfile(final String name) throws IOException {
        final boolean result = FileManager.createUserFolders(name);
        this.userData = new UserDataImpl(name);
        this.serializeUser();
        return result;
    }

    @Override
    public boolean loadAndSetUserProfile(final String name) throws FileNotFoundException {
        UserManager.LOG.debug("User selected: " + name);
        this.userData.setUserName(name);
        this.deserializeUser(name);
        if (this.userData == null) {
            this.userData = new UserDataImpl(name);
        }
        return true;
    }

    @Override
    public void serializeFeatureVector(final String gestureName, final List<Vector2D> featureVector)
            throws IOException {
        this.userData.addGestureFeatureVector(gestureName, featureVector);
        this.serializeUser();
    }

    @Override
    public void serializeAllFeatureVectors(final String gestureName, final List<List<Vector2D>> featureVector)
            throws IOException {
        this.userData.addAllGestureFeatureVector(gestureName, featureVector);
        this.serializeUser();

    }

    private void deserializeUser(final String name) throws FileNotFoundException {
        final Reader reader = new FileReader(FileManager.getUserDir(name) + "UserData.json");
        this.userData = this.gson.fromJson(reader, UserDataImpl.class);

    }

    private void serializeUser() throws FileNotFoundException, IOException {
        UserManager.LOG.debug(this.userData.toString());
        final Writer writer = new FileWriter(FileManager.getUserDir(this.userData.getUserName()) + "UserData.json",
                false);
        this.gson.toJson(this.userData, writer);
        writer.flush();
        writer.close();
    }

}
