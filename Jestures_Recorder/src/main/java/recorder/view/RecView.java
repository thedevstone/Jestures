package recorder.view;

import javax.swing.plaf.TreeUI;

import jestures.core.view.View;

/**
 *
 *
 */
public interface RecView extends View {

    /**
     * Set Recording.
     *
     * @param isRecording
     *            {@link TreeUI} if is recording
     */
    void setRecording(boolean isRecording);

    /**
     * Starts the fx thread.
     */
    void startFxThread();

    /**
     * Clear the view and clear the featurevectors.
     */
    void clearListView();

    /**
     * Select the gesture.
     *
     * @param gesture
     *            the gesture
     */
    void selectGesture(String gesture);

    /**
     * Get the actual gesture.
     * 
     * @return the {@link String} gesture
     */
    String getGesture();

}