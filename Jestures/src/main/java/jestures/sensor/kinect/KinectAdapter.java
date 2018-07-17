/*******************************************************************************
 * Copyright (c) 2018 Giulianini Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

package jestures.sensor.kinect;

import java.util.stream.IntStream;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;
import org.apache.commons.math3.geometry.euclidean.twod.Vector2D;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import jestures.core.file.FileManager;
import jestures.sensor.Joint;

/**
 * The @link{KinectAdapter} class. It adaps kinect J4KSDK to this framework.
 */
class KinectAdapter extends J4KSDK implements KinectInterfaceAdapter {
    static {
        FileManager.createLibSubFolder();
    }
    private KinectObserver kinect;
    private boolean first;
    private final Joint primaryJoint;
    private final KinectSensors kinectStartingSensors;
    private final KinectVersion kinectVersion;
    private static final int MULTIPLIER = 1000;

    /**
     * The @link{KinectAdapter.java} constructor.
     *
     * @param primaryJoint
     *            the main {@link Joint}
     * @param secondaryJoint
     *            the alternative {@link Joint}
     */
    KinectAdapter(final Joint primaryJoint, final KinectSensors kinectStartingSensors,
            final KinectVersion kinectVersion) {
        super(kinectVersion.getVersion());
        this.primaryJoint = primaryJoint;
        this.kinectStartingSensors = kinectStartingSensors;
        this.kinectVersion = kinectVersion;
        this.first = true;
    }

    @Override
    public void onColorFrameEvent(final byte[] arg0) {
        // DO NOTHING
    }

    @Override
    public void onDepthFrameEvent(final short[] arg0, final byte[] arg1, final float[] arg2, final float[] arg3) { // NOPMD
        // DO NOTHING
    }

    @Override
    public void onSkeletonFrameEvent(final boolean[] skeletonTracked, final float[] jointPosition,
            final float[] jointOrientation, final byte[] jointStatus) {

        Skeleton skeleton;
        for (int i = 0; i < this.getMaxNumberOfSkeletons(); i++) {
            if (skeletonTracked[i] && this.first) {
                this.first = false;
                skeleton = Skeleton.getSkeleton(i, skeletonTracked, jointPosition, jointOrientation, jointStatus, this);
                Vector2D joint1;
                Vector2D joint2;
                final Vector3D acceleration;

                // JOINT WITHOUT FOOT
                switch (this.primaryJoint) {
                case RIGHT_HAND:
                    joint1 = new Vector2D(
                            (int) (skeleton.get3DJoint(Skeleton.HAND_RIGHT)[0] * KinectAdapter.MULTIPLIER),
                            (int) (skeleton.get3DJoint(Skeleton.HAND_RIGHT)[1] * KinectAdapter.MULTIPLIER));
                    joint2 = new Vector2D((int) (skeleton.get3DJoint(Skeleton.HAND_LEFT)[0] * KinectAdapter.MULTIPLIER),
                            (int) (skeleton.get3DJoint(Skeleton.HAND_LEFT)[1] * KinectAdapter.MULTIPLIER));
                    break;
                case LEFT_HAND:
                    joint2 = new Vector2D(
                            (int) (skeleton.get3DJoint(Skeleton.HAND_RIGHT)[0] * KinectAdapter.MULTIPLIER),
                            (int) (skeleton.get3DJoint(Skeleton.HAND_RIGHT)[1] * KinectAdapter.MULTIPLIER));
                    joint1 = new Vector2D((int) (skeleton.get3DJoint(Skeleton.HAND_LEFT)[0] * KinectAdapter.MULTIPLIER),
                            (int) (skeleton.get3DJoint(Skeleton.HAND_LEFT)[1] * KinectAdapter.MULTIPLIER));
                    break;
                default:
                    joint1 = new Vector2D(skeleton.get3DJoint(Skeleton.HAND_RIGHT)[0],
                            skeleton.get3DJoint(Skeleton.HAND_RIGHT)[1]);
                    joint2 = new Vector2D(skeleton.get3DJoint(Skeleton.HAND_LEFT)[0],
                            skeleton.get3DJoint(Skeleton.HAND_LEFT)[1]);
                    break;
                }
                // ACCLEROMETER
                final double[] doubleAcceleration = new double[3];
                final float[] floatAccleration = this.getAccelerometerReading();
                IntStream.range(0, floatAccleration.length)
                        .forEach(index -> doubleAcceleration[index] = floatAccleration[index]);
                acceleration = new Vector3D(doubleAcceleration);

                // NOTIFY
                this.kinect.notifyOnSkeletonChange(joint1, joint2);
                this.kinect.notifyOnAccelerometerChange(acceleration);
            }
        }
        this.first = true;
    }

    @Override
    public void attacheKinect(final KinectObserver kinect) {
        this.kinect = kinect;
    }

    @Override
    public void start() {
        this.start(this.kinectStartingSensors.getStartingSensors());
    }

    @Override
    public String printKinectInfo() {
        final String version = "Kinect Version: " + this.kinectVersion;
        final String sensors = "Kinect active sensors: " + this.kinectStartingSensors;
        return version + '\n' + sensors;
    }
}
