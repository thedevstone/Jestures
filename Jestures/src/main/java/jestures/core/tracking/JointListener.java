package jestures.core.tracking;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.codification.FrameLength;

/**
 * Interface that generate {@link Vector2D} events.
 *
 */
public interface JointListener {
    /**
     * Simple absolute tracking.
     * <p>
     * Vector values [-1000, 1000]
     *
     * @param primaryJoint
     *            the {@link Vector2D} primary joint
     * @param secondaryJoint
     *            the {@link Vector2D} secondary joint
     */
    void onJointTracked(Vector2D primaryJoint, Vector2D secondaryJoint);

    /**
     * Derivative tracking.
     * <p>
     * Vector values [-100, 100]
     *
     * @param derivativeJoint
     *            {@link Vector2D} derivative
     */
    void onDerivativeJointTracked(Vector2D derivativeJoint);

    /**
     * Distance between starting joint position and actual joint position. Starting position depends on
     * {@link FrameLength}
     * <p>
     * Vector values [-1000, 1000]
     *
     * @param distance
     *            the {@link Vector2D} distance
     */
    void onDistanceFromStartingJoint(Vector2D distance);

    /**
     * Accelerometer tracking.
     * <p>
     *
     * Vector valuse [-1, 1]
     *
     * @param accelerometer
     *            the {@link Vector3D} acceleromter
     */
    void onAccelerometerTracked(Vector3D accelerometer);

}
