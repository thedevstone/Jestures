package jestures.sensor.kinect;

/**
 *
 * Different value for sensibility. Higher value makes the Kinect more sensible.
 * <p>
 * Moving hands with high sensibility values generate large movement vectors.
 *
 *
 */
public enum KinectSensibility {
    /**
     * Sensibility values.
     */
    HIGH(2000), MID(1000), LOW(500);

    private int sensibility;

    KinectSensibility(final int sensibilityValue) {
        this.sensibility = sensibilityValue;
    }

    /**
     * Get the {@link sensibility].
     *
     * @return the sensibility
     */
    public int getValue() {
        return this.sensibility;
    }

}
