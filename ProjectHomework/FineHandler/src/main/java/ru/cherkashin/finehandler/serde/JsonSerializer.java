package ru.cherkashin.finehandler.serde;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

public class JsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new Gson();

    public JsonSerializer() {
    }

    @Override
    public byte[] serialize(String topic, T t) {
        return gson.toJson(t).getBytes(StandardCharsets.UTF_8);
    }
}
