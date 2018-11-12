/*******************************************************************************
 * Copyright (c) 2018 Giulianini Luca
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package recorder.view;

import jestures.core.codification.FrameLength;
import jestures.core.view.View;

/**
 * A particular view for recording.
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

    /**
     * Get the {@link FrameLength} for tracking.
     *
     * @return the {@link FrameLength}
     */
    FrameLength getFrameLength();

    /**
     * Set the frame Length.
     *
     * @param length
     *            the {@link FrameLength}
     */
    void setFrameLength(FrameLength length);

}