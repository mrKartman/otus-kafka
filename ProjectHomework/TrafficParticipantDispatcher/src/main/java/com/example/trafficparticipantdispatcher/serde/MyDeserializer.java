package com.example.trafficparticipantdispatcher.serde;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;

public class MyDeserializer<T> implements Deserializer<T> {
    private final Gson gson = new Gson();
    private final Class<T> deserializedClass;

    public MyDeserializer(Class<T> deserializedClass) {
        this.deserializedClass = deserializedClass;
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        if(bytes == null){
            return null;
        }

        return gson.fromJson(new String(bytes), deserializedClass);

    }
}
