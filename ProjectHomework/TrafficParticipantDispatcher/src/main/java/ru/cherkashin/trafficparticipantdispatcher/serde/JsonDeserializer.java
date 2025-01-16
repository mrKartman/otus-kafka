package ru.cherkashin.trafficparticipantdispatcher.serde;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Deserializer;

@RequiredArgsConstructor
public class JsonDeserializer<T> implements Deserializer<T> {
    private final Gson gson = new Gson();
    private final Class<T> deserializedClass;

    @Override
    public T deserialize(String s, byte[] bytes) {
        if(bytes == null){
            return null;
        }
        return gson.fromJson(new String(bytes), deserializedClass);
    }
}
