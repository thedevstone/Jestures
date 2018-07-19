/**
 *
 */
package jestures.core.serialization;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import com.google.gson.Gson;

import jestures.core.file.FileManager;
import jestures.core.recognition.gesture.UserData;
import jestures.core.recognition.gesture.UserDataImpl;

/**
 * The {@link UserManager} class.
 *
 */
public class UserManager implements Serializer {
    private final UserData userData;
    private final Gson gson;

    /**
     * The constructor for the {@link UserManager} class.
     */
    public UserManager() {
        this.userData = new UserDataImpl();
        this.gson = new Gson();
    }

    @Override
    public void serializeFeatureVector(final String gestureName, final List<Vector2D> featureVector)
            throws IOException {
        this.userData.addGestureFeatureVector(gestureName, featureVector);
        try (Writer writer = new FileWriter(FileManager.getUserDir(this.userData.getUserName()))) {
            this.gson.toJson(this.userData, writer);
        } catch (final Exception e) {
            System.out.println("Folder does not exist");
            throw e;
        }

    }

    @Override
    public void serializeAllFeatureVectors(final String gestureName, final List<List<Vector2D>> featureVector) {
        this.userData.addAllGestureFeatureVector(gestureName, featureVector);

    }

    @Override
    public boolean createUserProfile(final String name) {
        return FileManager.createUserFolders(name);
    }

    @Override
    public boolean loadAndSetUserProfile(final String name) {
        System.out.println("Loading user:" + name);
        return false;
    }

}
