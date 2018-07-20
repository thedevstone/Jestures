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
     * Delete the selected elem from the listView.
     *
     * @param indexClicked
     *            the index clicked
     */
    void deleteFeatureVectorInLIstView(int indexClicked);

    /**
     * Clear the view and clear the featurevectors.
     */
    void clearListView();

    /**
     * Add the selected feature vector to database.
     *
     * @param gesture
     *            the String gesture
     * @param indexClicked
     *            the index
     */
    void addFeatureVector(String gesture, int indexClicked);

    /**
     * Add all feature vector elements present in listview.
     */
    void addAllElemInListView();

    /**
     * Select the gesture.
     *
     * @param gesture
     *            the gesture
     */
    void selectGesture(String gesture);

}