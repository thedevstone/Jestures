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

}