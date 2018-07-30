package jestures.core.recognition;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;
import org.apache.log4j.Logger;

import com.google.common.base.Functions;
import com.google.gson.JsonSyntaxException;

import javafx.util.Pair;
import jestures.core.codification.FrameLength;
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
    private final Serializer userManager;
    private static Recognition instance;
    private final Set<ViewObserver> view;
    private Map<String, List<Vector2D[]>> userDataset;
    private long lastGestureTime;

    // RECOGNITION
    private final DynamicTimeWarping<Vector2D> dtw;
    private int updateRate;
    private double dtwRadius;
    private double minDTWThreashold;
    private double maxDTWThreashold;
    private int minTimeSeparation;
    private int matchNumber;
    private boolean gestureRecognized;

    /**
     *
     */
    private Recognizer() {
        this.setUpdateRate(10);
        this.view = new HashSet<>();
        this.userManager = new UserManager();
        this.userDataset = null;
        this.lastGestureTime = 0;
        // RECOGNITION
        this.dtwRadius = 0.5;
        this.updateRate = 5;
        this.minDTWThreashold = 500;
        this.maxDTWThreashold = 800;
        this.minTimeSeparation = 300;
        this.matchNumber = 5;

        this.dtw = new DynamicTimeWarping<Vector2D>((a, b) -> a.distance(b), this.dtwRadius);
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
        if ((frame + 1) % this.updateRate == 0) {
            final Vector2D[] arrayFeatureVector = new Vector2D[featureVector.size()];
            featureVector.toArray(arrayFeatureVector);
            final long currentSec = System.currentTimeMillis();
            if (this.gestureRecognized && currentSec - this.lastGestureTime > this.minTimeSeparation) {
                this.lastGestureTime = currentSec;
                this.recognize(arrayFeatureVector);
            } else if (!this.gestureRecognized) {
                this.lastGestureTime = currentSec;
                this.recognize(arrayFeatureVector);
            }
        }
    }

    @Override
    public void notifyOnFeatureVectorEvent(final List<Vector2D> featureVector) {
        super.notifyOnFeatureVectorEvent(featureVector);
        this.view.forEach(t -> t.notifyOnFeatureVectorEvent());
    }

    // ############################################# TRACKER #########################################
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

    // ############################################# TRACKER #########################################

    private void recognize(final Vector2D... featureVector) {
        final List<Pair<Double, String>> distances = new ArrayList<>();
        for (final String gestureName : this.userDataset.keySet()) {
            for (final Vector2D[] gestureTemplate : this.userDataset.get(gestureName)) {
                final double dtwDist = this.dtw.d(gestureTemplate, featureVector);
                if (dtwDist < this.maxDTWThreashold && dtwDist > this.minDTWThreashold) {
                    distances.add(new Pair<Double, String>(dtwDist, gestureName));
                }
            }
        }

        if (distances.isEmpty()) {
            // Recognizer.LOG.debug("NO GESURES");
            this.gestureRecognized = false;
        } else {
            this.gestureRecognized = true;
        }
        distances.stream()
                 .map(t -> t.getValue())
                 .collect(Collectors.groupingBy(Functions.identity(), Collectors.counting()))
                 .entrySet()
                 .stream()
                 .filter(k -> k.getValue() > this.matchNumber) // MATCH NUMBER
                 .max(Comparator.comparing(Entry::getValue))
                 .ifPresent(t -> Recognizer.LOG.debug(t.getKey()));

    }

    @Override
    public double getDtwRadius() {
        return this.dtwRadius;
    }

    @Override
    public void setDtwRadius(final double dtwRadius) {
        if (dtwRadius < 1 && dtwRadius > 0) {
            this.dtwRadius = dtwRadius;
        }
    }

    @Override
    public double getMinDtwThreashold() {
        return this.minDTWThreashold;
    }

    @Override
    public void setMinDtwThreashold(final double minDTWThreashold) {
        if (minDTWThreashold > 0 && minDTWThreashold < this.maxDTWThreashold) {
            this.minDTWThreashold = minDTWThreashold;
        }
    }

    @Override
    public double getMaxDTWThreashold() {
        return this.maxDTWThreashold;
    }

    @Override
    public void setMaxDtwThreashold(final double maxDtwThreashold) {
        if (maxDtwThreashold > 0 && maxDtwThreashold > this.minDTWThreashold) {
            this.maxDTWThreashold = maxDtwThreashold;
        }
    }

    @Override
    public int getUpdateRate() {
        return this.updateRate;
    }

    @Override
    public void setUpdateRate(final int updateRate) {
        if (updateRate > 0 && updateRate < FrameLength.FPS_30.getFrameNumber()
                && this.getFrameLength().getFrameNumber() % updateRate == 0) {
            this.updateRate = updateRate;
        }
    }

    @Override
    public int getMinTimeSeparation() {
        return this.minTimeSeparation;
    }

    @Override
    public void setMinTimeSeparation(final int minTimeSeparation) {
        if (minTimeSeparation > 0 && minTimeSeparation < 1000) {
            this.minTimeSeparation = minTimeSeparation;
        }
    }

    @Override
    public int getMatchNumber() {
        return this.matchNumber;
    }

    @Override
    public void setMatchNumber(final int matchNumber) {
        if (matchNumber > 0) {
            this.matchNumber = matchNumber;
        }
    }

}
