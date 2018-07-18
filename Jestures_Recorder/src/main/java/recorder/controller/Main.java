package recorder.controller;

import jestures.sensor.Joint;
import jestures.sensor.Sensor;
import jestures.sensor.SensorException;
import jestures.sensor.kinect.Kinect;
import jestures.sensor.kinect.KinectSensors;
import jestures.sensor.kinect.KinectVersion;
import recorder.view.RecView;
import recorder.view.RecorderScreenView;
import recorder.view.Recording;

public class Main {

    public Main() {
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
        final Recording recorder = Recorder.getInstance();
        recorder.attacheSensor(sensor);
        final RecView view = new RecorderScreenView(recorder);
        recorder.attacheUI(view);
    }

}
