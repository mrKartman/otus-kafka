package ru.cherkashin.trafficgenerator.serde;

import com.google.gson.Gson;
import lombok.NoArgsConstructor;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor
public class JsonSerializer<T> implements Serializer<T> {

    private final Gson gson = new Gson();
    @Override
    public byte[] serialize(String topic, T t) {
        return gson.toJson(t).getBytes(StandardCharsets.UTF_8);
    }
}
