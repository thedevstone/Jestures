package recorder.view;

import javax.swing.plaf.TreeUI;

import jestures.core.view.ViewObserver;

/**
 *
 * The {@link RecordingViewObserver} class.
 *
 */
public interface RecordingViewObserver extends ViewObserver {

    /**
     * Set Recording.
     *
     * @param isRecording
     *            {@link TreeUI} if is recording
     */
    void setRecording(boolean isRecording);
}
