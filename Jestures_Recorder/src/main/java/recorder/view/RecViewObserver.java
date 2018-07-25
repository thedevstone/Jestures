package recorder.view;

import javax.swing.plaf.TreeUI;

import jestures.core.view.ViewObserver;

/**
 *
 * The {@link RecViewObserver} class.
 *
 */
public interface RecViewObserver extends ViewObserver {

    /**
     * Set Recording.
     *
     * @param isRecording
     *            {@link TreeUI} if is recording
     */
    void setRecording(boolean isRecording);
}
