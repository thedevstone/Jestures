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

package recorder.controller;

import jestures.core.tracking.Tracking;
import recorder.view.RecView;

/**
 * Inteface for tracking.
 *
 *
 */
public interface Recording extends Tracking {

    /**
     * Attache the view.
     *
     * @param view
     *            the {@link RecView}
     */
    void attacheUI(RecView view);

    /**
     * Select the feature vector.
     *
     * @param index
     *            the index in the list
     */
    void selectFeatureVector(int index);

    /**
     * Delete the feature vector in the list.
     *
     * @param index
     *            the index in the list.
     */
    void deleteFeatureVector(int index);

    /**
     * Clear the featureVector.
     */
    void clearFeatureVectors();

}