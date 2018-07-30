/**
 *
 */
package jestures.core.view;

import jestures.core.recognition.gesturedata.RecognitionSettings;
import jestures.core.recognition.gesturedata.RecognitionSettingsImpl;

/**
 * The {@link RecognitionViewObserver} class.
 *
 */
public interface RecognitionViewObserver extends ViewObserver {

    /**
     * Update the recognition settings.
     *
     * @param settings
     *            the {@link RecognitionSettings}
     */
    void updateSettings(RecognitionSettingsImpl settings);
}
