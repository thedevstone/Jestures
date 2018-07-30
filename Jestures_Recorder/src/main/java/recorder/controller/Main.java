package recorder.controller;

import jestures.sensor.Joint;
import jestures.sensor.Sensor;
import jestures.sensor.SensorException;
import jestures.sensor.kinect.Kinect;
import jestures.sensor.kinect.KinectSensors;
import jestures.sensor.kinect.KinectVersion;
import recorder.view.RecordingView;
import recorder.view.RecorderScreenView;

/**
 * Main class.
 *
 */
public final class Main {
    private Main() {

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
        final Recording recorder = Recorder.getInstance();
        recorder.attacheSensor(sensor);
        final RecordingView view = new RecorderScreenView(recorder);
        recorder.attacheUI(view);
    }

}
