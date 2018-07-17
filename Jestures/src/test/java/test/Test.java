package test;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import jestures.core.tracking.JointListener;
import jestures.core.tracking.Tracker;
import jestures.core.tracking.Tracking;
import jestures.core.view.TrackerView;
import jestures.core.view.View;
import jestures.sensor.Joint;
import jestures.sensor.Sensor;
import jestures.sensor.SensorException;
import jestures.sensor.kinect.Kinect;
import jestures.sensor.kinect.KinectSensors;
import jestures.sensor.kinect.KinectVersion;

/**
 * Testing class.
 *
 */
public final class Test {

    /**
     * The @link{Test.java} constructor.
     */
    private Test() {
        // TODO Auto-generated constructor stub
    }

    /**
     * Main.
     *
     * @param args
     *            args
     * @throws SensorException
     *             the sensor exception
     */
    public static void main(final String[] args) throws SensorException {
        final Sensor sensor = new Kinect(Joint.RIGHT_HAND, KinectSensors.SKELETON_ONLY, KinectVersion.KINECT1);
        final Tracking recognizer = Tracker.getInstance();
        recognizer.attacheSensor(sensor);
        final View view = new TrackerView(recognizer);
        recognizer.attacheUI(view);
        recognizer.setOnJointTracked(new JointListener() {

            @Override
            public void onJointTracked(final Vector2D primaryJoint, final Vector2D secondaryJoint) {
                System.out.println(primaryJoint);

            }

            @Override
            public void onDistanceFromStartingJoint(final Vector2D distance) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onDerivativeJointTracked(final Vector2D derivativeJoint) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAccelerometerTracked(final Vector3D accelerometer) {
                // TODO Auto-generated method stub

            }
        });
    }

}
