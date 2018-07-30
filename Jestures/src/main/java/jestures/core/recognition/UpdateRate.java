/**
 *
 */
package jestures.core.recognition;

/**
 * The {@link UpdateRate} class.
 *
 */
public enum UpdateRate {
    /**
     * Gestures duration in frame. 30 FPS base
     */
    FPS_30(30), FPS_10(10), FPS_5(5), FPS_2(2);

    private int frameNumber;

    /**
     * The @link{JestureFrameLenght.java} constructor.
     */
    UpdateRate(final int rate) {
        this.frameNumber = rate;
    }

    /**
     * Get the @link{frameNumber}.
     *
     * @return the @link{frameNumber}
     */
    public int getFrameNumber() {
        return this.frameNumber;
    }
}
