package jestures.core.recognition.gesture;

/**
 * Default gestures.
 *
 */
public enum DefaultGesture {
    /**
     * Gestures duration in frame. 30 FPS base
     */
    SWIPE_LEFT("SWIPE_LEFT"),
    /**
     * Gestures duration in frame. 30 FPS base
     */
    SWIPE_RIGHT("SWIPE_RIGHT"),
    /**
     * Gestures duration in frame. 30 FPS base
     */
    SWIPE_UP("SWIPE_UP"),
    /**
     * Gestures duration in frame. 30 FPS base
     */
    SWIPE_DOWN("SWIPE_DOWN"),
    /**
     * Gestures duration in frame. 30 FPS base
     */
    CIRCLE("CIRCLE");

    private String name;

    /**
     * The @link{JestureFrameLenght.java} constructor.
     */
    DefaultGesture(final String namez) {
        this.name = namez;
    }

    /**
     * Get the @link{frameNumber}.
     *
     * @return the @link{frameNumber}
     */
    public String getGestureName() {
        return this.name;
    }

}
