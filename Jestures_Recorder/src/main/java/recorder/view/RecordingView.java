package recorder.view;

import jestures.core.view.View;

/**
 *
 *
 */
public interface RecordingView extends View, RecordingViewObserver {

    /**
     * Delete the user profile.
     *
     *
     */
    void deleteSelectedUserProfile();

    /**
     * Create the user profile.
     *
     * @param username
     *            the String username
     */
    void createUserProfile(String username);

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
    void addFeatureVectorToDataset(String gesture, int indexClicked);

    /**
     * Add all feature vector elements present in listview.
     */
    void addAllElemInListViewToDataset();

    /**
     * Select the gesture.
     *
     * @param gesture
     *            the gesture
     */
    void selectGesture(String gesture);

    /**
     * Delete the gesture.
     */
    void deleteGesture();

}