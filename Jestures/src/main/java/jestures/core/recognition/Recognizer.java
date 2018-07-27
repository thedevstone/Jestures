package jestures.core.recognition;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.log4j.Logger;

import com.google.gson.JsonSyntaxException;

import jestures.core.serialization.Serializer;
import jestures.core.serialization.UserManager;
import jestures.core.tracking.Tracker;
import jestures.core.tracking.Tracking;
import jestures.core.view.ViewObserver;
import jestures.core.view.screens.RecognitionScreenView;
import smile.math.distance.DynamicTimeWarping;

/**
 *
 *
 */
public final class Recognizer extends Tracker implements Recognition {
    private static final Logger LOG = Logger.getLogger(Recognizer.class);
    private static final double DTW_RADIUS = 0.5;
    private final Set<ViewObserver> view;
    private static Recognition instance;
    private final Serializer userManager;
    private int updateRate = 0;
    private Map<String, List<Vector2D[]>> userDataset;
    private final DynamicTimeWarping<Vector2D> dtw;

    /**
     *
     */
    private Recognizer() {
        this.setUpdateRate(30);
        this.view = new HashSet<>();
        this.userManager = new UserManager();
        this.userDataset = null;
        this.dtw = new DynamicTimeWarping<Vector2D>((a, b) -> a.distance(b), Recognizer.DTW_RADIUS);
        RecognitionScreenView.startFxThread();
    }

    /**
     * Get the instance.
     *
     * @return the {@link Tracking} instance.
     */
    public static Recognition getInstance() {
        synchronized (Tracking.class) {
            if (Recognizer.instance == null) {
                Recognizer.instance = new Recognizer();
            }
        }
        return Recognizer.instance;
    }

    @Override
    public void attacheUI(final ViewObserver view) {
        this.view.add(view);
        this.view.forEach(t -> t.refreshUsers());
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
        super.notifyOnFrameChange(frame, featureVector, derivative, distanceVector);
        this.view.forEach(t -> t.notifyOnFrameChange(frame, derivative, distanceVector));
        // QUI SI INNESTA IL RICONOSCIMENTO
        if (frame != 0 && frame % this.updateRate == 0) {
            final Vector2D[] arrayFeatureVector = new Vector2D[featureVector.size()];
            featureVector.toArray(arrayFeatureVector);
            this.recognize(arrayFeatureVector);
        }
    }

    private void recognize(final Vector2D[] featureVector) {
        final Map<String, Double> distances = new HashMap<>();
        for (final String gestureName : this.userDataset.keySet()) {
            for (final Vector2D[] gestureTemplate : this.userDataset.get(gestureName)) {
                distances.put(gestureName, this.dtw.d(gestureTemplate, featureVector));
            }
        }
        final Entry<String, Double> min = distances.entrySet()
                                                   .stream()
                                                   .min((a, b) -> Double.compare(a.getValue(), b.getValue()))
                                                   .get();
        if (min.getValue() < 500) {
            Recognizer.LOG.info(min);
        }
    }

    private void printArray(final Vector2D[] array) {
        for (final Vector2D elem : array) {
            Recognizer.LOG.debug(elem);
        }
    }

    @Override
    public void notifyOnFeatureVectorEvent(final List<Vector2D> featureVector) {
        super.notifyOnFeatureVectorEvent(featureVector);
        this.view.forEach(t -> t.notifyOnFeatureVectorEvent());
    }

    @Override
    public boolean loadUserProfile(final String name) throws FileNotFoundException, IOException, JsonSyntaxException {
        final boolean userExists = this.userManager.loadOrCreateNewUser(name);
        this.userDataset = this.userManager.getDatasetForRecognition();
        return userExists;
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

    @Override
    public void setUpdateRate(final int frameNumber) {
        this.updateRate = frameNumber;
    }

}
