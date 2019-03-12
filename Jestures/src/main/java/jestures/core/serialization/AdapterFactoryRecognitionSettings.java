package jestures.core.serialization;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import jestures.core.recognition.gesturedata.RecognitionSettings;

/**
 *
 * The {@link AdapterFactoryRecognitionSettings} class.
 *
 */
public class AdapterFactoryRecognitionSettings implements TypeAdapterFactory {

    private final Class<? extends RecognitionSettings> implementationClass;

    /**
     *
     * The constructor for the class.
     *
     * @param impelementationClass
     *            the implmentation class
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
