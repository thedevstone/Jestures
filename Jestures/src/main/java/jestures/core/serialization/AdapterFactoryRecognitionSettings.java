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
package jestures.core.serialization;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import jestures.core.recognition.gesturedata.RecognitionSettings;

/**
 * The {@link AdapterFactoryRecognitionSettings} class.
 *
 */
public class AdapterFactoryRecognitionSettings implements TypeAdapterFactory {

    private final Class<? extends RecognitionSettings> implementationClass;

    /**
     * Create an adapter factory for RecognitionSettings. So Gson can deserialize the interface.
     * <p>
     * Gson tries to deseialize RecognitionSettings, when i sees that it create a RecognitionSettingsImpl instance. The
     * constructor for the class.
     *
     * @param impelementationClass
     *            the implementationClass
     */
    public AdapterFactoryRecognitionSettings(final Class<? extends RecognitionSettings> impelementationClass) {
        this.implementationClass = impelementationClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
        if (!RecognitionSettings.class.equals(type.getRawType())) {
            return null;

        } else {
            return (TypeAdapter<T>) gson.getAdapter(this.implementationClass);
        }
    }

}
