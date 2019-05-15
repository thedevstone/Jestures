package jestures.sensor.kinect;

import jestures.core.file.OsUtils;
import jestures.sensor.Joint;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test for KinectAdapter class.
 */
class KinectAdapterTest {
    private static KinectAdapter kinectAdapter;

    @BeforeAll
    static void setUp() {
        KinectAdapterTest.kinectAdapter = new KinectAdapter(Joint.RIGHT_HAND, KinectSensors.ALL_SENSORS, KinectVersion.KINECT1);
    }

    @AfterAll
    static void tearDown() {
    }


    @Test
    @DisplayName("A Kinect on test without SDK should not be initialized")
    void duringTestKinectShouldNotBeInitialized() {
        KinectAdapterTest.kinectAdapter.start();
        if (!OsUtils.getOperatingSystemType().equals(OsUtils.OSType.Windows)) {
            Assertions.assertFalse(KinectAdapterTest.kinectAdapter.isInitialized());
        } else {
            Assertions.assertTrue(KinectAdapterTest.kinectAdapter.isInitialized());
        }
    }
}