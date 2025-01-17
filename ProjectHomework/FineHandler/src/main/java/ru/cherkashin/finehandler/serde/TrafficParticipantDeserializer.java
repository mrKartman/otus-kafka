package ru.cherkashin.finehandler.serde;

import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Deserializer;
import ru.cherkashin.finehandler.dto.TrafficParticipant;

public class TrafficParticipantDeserializer<T> implements Deserializer<T> {
    private final Gson gson = new Gson();

    @Override
    public T deserialize(String s, byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return (T) gson.fromJson(new String(bytes), TrafficParticipant.class);
    }
}
