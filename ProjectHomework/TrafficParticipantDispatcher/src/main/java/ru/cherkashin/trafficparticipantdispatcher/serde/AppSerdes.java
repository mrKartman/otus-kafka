package ru.cherkashin.trafficparticipantdispatcher.serde;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import ru.cherkashin.trafficparticipantdispatcher.dto.TrafficParticipant;

public class AppSerdes {
    private static <T> Serde<T> serde(Class<T> cls) {
        return new Serdes.WrapperSerde<>(new JsonSerializer<>(), new JsonDeserializer<>(cls));
    }

    public static Serde<TrafficParticipant> trafficParticipant() {
        return serde(TrafficParticipant.class);
    }

}
